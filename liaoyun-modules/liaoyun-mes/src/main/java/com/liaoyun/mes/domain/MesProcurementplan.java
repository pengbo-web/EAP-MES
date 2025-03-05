package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 采购计划对象 mes_procurementplan
 *
 * @author Liao Liao
 * @date 2024-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_procurementplan")
public class MesProcurementplan extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 流转号
     */
    private String sn;
    private BigDecimal prices;
    /**
     * 产品
     */
    private Long proId;

    /**
     * 数量
     */
    private Long count;

    /**
     * 任务主表
     */
    private Long workId;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    private String remark2;

    private String fromtype;
}
