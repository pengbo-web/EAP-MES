package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 入库记录对象 mes_inrec
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_inrec")
public class MesInrec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 物料
     */
    private Long stockid;

    private String sn;

    private Long stocktypeid;

    /**
     * 供应商
     */
    private Long custId;

    /**
     * 进货价
     */
    private BigDecimal nprice;

    /**
     * 税点
     */
    private Long shuidian;

    /**
     * 结算方式
     */
    private String met;

    /**
     * 数量
     */
    private BigDecimal count;

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


    private String fromtype;


    private String remark2;


    private String orderid;
}
