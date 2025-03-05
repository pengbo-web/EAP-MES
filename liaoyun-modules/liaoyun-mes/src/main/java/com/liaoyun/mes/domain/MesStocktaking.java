package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 库位盘点对象 mes_stocktaking
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_stocktaking")
public class MesStocktaking extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 物料
     */
    private Long stockid;

    /**
     * 库位
     */
    private Long stocktypeid;

    /**
     * 盘查数量
     */
    private BigDecimal counts;

    /**
     * 异常数量
     */
    private BigDecimal yccounts;

    /**
     * 异常原因
     */
    private String ycrec;

    /**
     * 结存量
     */
    private BigDecimal jc;

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


}
