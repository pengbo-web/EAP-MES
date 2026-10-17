package com.liaoyun.mes.eap;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liaoyun.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * EAP 设备状态对象 mes_eap_device(二次开发新增)
 * <p>
 * 由 EAP 事件驱动维护: 加工完成->运行中, 参数超限->报警; 跨天自动清零当日计数;
 * 离线状态由看板查询时按最后事件时间判定, 不落库。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_eap_device")
public class MesEapDevice extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 状态(0运行中 1报警)
     */
    private String status;

    /**
     * 最后事件时间
     */
    private Date lastEventTime;

    /**
     * 当日完工数量
     */
    private Integer todayCount;

    /**
     * 当日报警次数
     */
    private Integer todayAlarm;

    /**
     * 当日不良数量
     */
    private Integer todayDefective;

    /**
     * 删除标志
     */
    @TableLogic
    private String delFlag;
}
