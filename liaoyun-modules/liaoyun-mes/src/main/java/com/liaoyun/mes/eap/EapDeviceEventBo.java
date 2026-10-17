package com.liaoyun.mes.eap;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * EAP 设备事件对象
 * <p>
 * 由自研 EAP 采集网关推送至 MES,驱动自动报工与异常触发。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Data
public class EapDeviceEventBo {

    /**
     * 设备编码(对应 mes_workplaces 机台)
     */
    @NotBlank(message = "设备编码不能为空")
    private String deviceCode;

    /**
     * 事件类型:PROCESS_COMPLETE-加工完成 PARAM_ALARM-参数超限报警
     */
    @NotBlank(message = "事件类型不能为空")
    private String eventType;

    /**
     * 流转批次号(mes_task.sn)
     */
    @NotBlank(message = "流转批次号不能为空")
    private String sn;

    /**
     * 本次完成数量
     */
    private Long count;

    /**
     * 不良品数量
     */
    private Integer defective;

    /**
     * 工艺参数 JSON(报警时携带超限参数)
     */
    private String params;

    /**
     * 租户编号,默认主租户
     */
    private String tenantId = "000000";

    /**
     * 事件发生时间(设备侧时间戳,毫秒)
     */
    private Long eventTime;
}
