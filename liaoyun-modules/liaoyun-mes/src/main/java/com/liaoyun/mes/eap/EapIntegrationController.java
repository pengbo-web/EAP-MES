package com.liaoyun.mes.eap;

import cn.dev33.satoken.annotation.SaIgnore;
import com.liaoyun.common.core.constant.HttpStatus;
import com.liaoyun.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * EAP 设备事件接入接口
 * <p>
 * 供自研 EAP 采集网关推送设备事件(加工完成/参数超限),
 * 网关通道使用独立网关密钥校验,不走用户登录态。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/eap")
public class EapIntegrationController {

    private final EapIntegrationService eapIntegrationService;

    /**
     * 网关密钥,生产环境应放置于配置中心并按环境隔离
     */
    @Value("${eap.gateway-key:eap-demo-key}")
    private String gatewayKey;

    /**
     * 接收 EAP 设备事件
     *
     * @param key   网关密钥头 X-EAP-Key
     * @param event 设备事件
     */
    @SaIgnore
    @PostMapping("/event")
    public R<String> receiveEvent(@RequestHeader("X-EAP-Key") String key,
                                  @Validated @RequestBody EapDeviceEventBo event) {
        if (!gatewayKey.equals(key)) {
            return R.fail(HttpStatus.FORBIDDEN, "网关密钥校验失败");
        }
        return R.ok(eapIntegrationService.handleEvent(event));
    }
}
