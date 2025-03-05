package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 委外详情对象 mes_outorder_de
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_outorder_de")
public class MesOutorderDe extends TenantEntity {

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
     * 单位
     */
    private Long unid;

    /**
     * 单价
     */
    private BigDecimal prices;

    /**
     * 数量
     */
    private Long counts;

    /**
     * 总价
     */
    private BigDecimal allprice;

    /**
     * 交付时间
     */
    private Date ctime;

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

    /**
     * 已计划数量
     */
    private Long pcount;

    private String fromtype;

    private String remark2;

    private String orderid;

    private Long mainid;

}
