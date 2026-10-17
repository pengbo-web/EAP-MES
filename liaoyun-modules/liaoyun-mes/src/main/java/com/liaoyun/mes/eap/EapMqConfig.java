package com.liaoyun.mes.eap;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * EAP 事件总线消费端拓扑(与采集网关声明保持一致, 双端幂等声明)
 * <p>
 * mes.integration 绑定由网关侧声明(routing key eap.#); 此处仅声明队列本体,
 * 保证 MES 先于网关启动时监听容器可正常拉起。
 *
 * @author pengbo
 * @date 2026-10-17
 */
@Configuration
public class EapMqConfig {

    public static final String QUEUE_MES = "mes.integration";
    public static final String QUEUE_DLQ = "eap.events.dlq";

    @Bean
    public Queue mesIntegrationQueue() {
        return QueueBuilder.durable(QUEUE_MES)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", QUEUE_DLQ)
            .build();
    }

    @Bean
    public Queue eapDlq() {
        return QueueBuilder.durable(QUEUE_DLQ).build();
    }
}
