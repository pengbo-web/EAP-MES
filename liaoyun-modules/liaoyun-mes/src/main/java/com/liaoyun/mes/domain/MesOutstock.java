package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 出库管理对象 mes_outstock
 *
 * @author Liao Liao
 * @date 2024-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_outstock")
public class MesOutstock extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 物料
     */
    private Long proId;

    /**
     * 出库单位
     */
    private Long unitId;

    /**
     * 来源
     */
    private String fromtype;

    /**
     * 数量
     */
    private BigDecimal count;

    /**
     * 流转码
     */
    private String sn;

    /**
     * 任务关联
     */
    private Long taskId;

    /**
     * 状态（0正常 1停用）
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

    private String remark1;


    private BigDecimal laststock;


}
