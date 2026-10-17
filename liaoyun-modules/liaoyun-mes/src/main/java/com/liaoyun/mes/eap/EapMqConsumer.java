package com.liaoyun.mes.eap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liaoyun.common.core.exception.ServiceException;
import com.liaoyun.common.redis.utils.RedisUtils;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * EAP 设备事件 MQ 消费者(二次开发)
 * <p>
 * 消费采集网关发布到 eap.events 交换机的统一事件(JSON), 复用 EapIntegrationService
 * 自动报工/异常联动, 与 HTTP 通道 /mes/eap/event 并存互为备份。
 * <p>
 * 可靠性设计:
 * 1. 手动 ack, 处理成功才确认; 未知异常 nack 不重回队列, 经死信路由进入 eap.events.dlq;
 * 2. 消费侧幂等: 设备 mid 以 Redis SETNX 去重(与网关发布侧/时序落库侧各守一段, 三跳防重);
 * 3. 网关 __TypeId__ 指向网关侧类, MES 无此类, 故以原始报文手工反序列化, 不依赖类型头;
 * 4. 业务性跳过(批次无进行中任务等 ServiceException)直接 ack 放行, 避免设备空转刷爆死信。
 *
 * @author pengbo
 * @date 2026-10-17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EapMqConsumer {

    private final EapIntegrationService integrationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = EapMqConfig.QUEUE_MES)
    public void onEvent(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            JsonNode node = objectMapper.readTree(body);
            String eventType = node.path("eventType").asText("");
            // 在线/离线等监控事件不入报工链路, 直接确认放行
            if (!EapIntegrationService.EVENT_PROCESS_COMPLETE.equals(eventType)
                && !EapIntegrationService.EVENT_PARAM_ALARM.equals(eventType)) {
                channel.basicAck(tag, false);
                return;
            }
            // 未映射 MES 流转批次的设备为纯遥测事件: 放行且不触发去重/查库, 避免压测流量空耗数据库
            String sn = node.path("sn").asText("");
            if (sn.isBlank()) {
                channel.basicAck(tag, false);
                return;
            }
            String deviceCode = node.path("deviceCode").asText("");
            String mid = node.path("mid").asText("");
            if (!mid.isBlank()) {
                boolean first = RedisUtils.setObjectIfAbsent(
                    "eap:mes:mid:" + deviceCode + ":" + mid, "1", Duration.ofMinutes(10));
                if (!first) {
                    log.debug("MQ重复消息丢弃: device={} mid={}", deviceCode, mid);
                    channel.basicAck(tag, false);
                    return;
                }
            }
            integrationService.handleEvent(toBo(node, deviceCode, eventType, mid));
            channel.basicAck(tag, false);
        } catch (ServiceException e) {
            // 业务性跳过: 如批次无进行中任务, 属正常生产场景, ack 防止空转事件堆积死信
            log.debug("EAP事件业务跳过: {} body={}", e.getMessage(), abbreviate(body));
            ackQuietly(channel, tag);
        } catch (Exception e) {
            log.warn("EAP事件消费失败, 入死信: err={} body={}", e.getMessage(), abbreviate(body));
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception ignored) {
                // 通道异常忽略
            }
        }
    }

    private EapDeviceEventBo toBo(JsonNode node, String deviceCode, String eventType, String mid) throws Exception {
        EapDeviceEventBo bo = new EapDeviceEventBo();
        bo.setDeviceCode(deviceCode);
        bo.setEventType(eventType);
        bo.setSn(node.path("sn").asText(null));
        if (node.hasNonNull("count")) {
            bo.setCount(node.get("count").asLong());
        }
        if (node.hasNonNull("defective")) {
            bo.setDefective(node.get("defective").asInt());
        }
        if (node.hasNonNull("eventTime")) {
            bo.setEventTime(node.get("eventTime").asLong());
        }
        JsonNode params = node.get("params");
        if (params != null && !params.isNull()) {
            bo.setParams(objectMapper.writeValueAsString(params));
        }
        return bo;
    }

    private void ackQuietly(Channel channel, long tag) {
        try {
            channel.basicAck(tag, false);
        } catch (Exception ignored) {
            // 通道异常忽略
        }
    }

    private String abbreviate(String body) {
        return body.length() <= 200 ? body : body.substring(0, 200);
    }
}
