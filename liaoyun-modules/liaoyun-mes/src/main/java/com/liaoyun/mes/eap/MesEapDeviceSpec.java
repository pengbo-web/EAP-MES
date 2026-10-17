package com.liaoyun.mes.eap;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liaoyun.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 设备 OEE 基准参数对象 mes_eap_device_spec(二次开发新增)
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_eap_device_spec")
public class MesEapDeviceSpec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 理论周期(秒/件)
     */
    private BigDecimal idealCycleSec;

    /**
     * 日负荷时间(分钟)
     */
    private Integer plannedMinutes;
}
