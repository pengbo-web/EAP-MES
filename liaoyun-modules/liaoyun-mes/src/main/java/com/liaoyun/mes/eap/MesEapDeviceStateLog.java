package com.liaoyun.mes.eap;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liaoyun.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 设备状态变更流水对象 mes_eap_device_state_log(二次开发新增)
 * <p>
 * OEE 时间稼动率的时长积分依据: 仅状态迁移时写入一行, to_status 生效至下一条流水。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_eap_device_state_log")
public class MesEapDeviceStateLog extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 变更前状态(0运行 1报警 2离线, 首条为null)
     */
    private String fromStatus;

    /**
     * 变更后状态
     */
    private String toStatus;

    /**
     * 变更时间
     */
    private Date eventTime;
}
