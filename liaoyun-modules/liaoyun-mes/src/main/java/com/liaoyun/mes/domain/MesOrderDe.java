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
 * 订单详情对象 mes_order_de
 *
 * @author Liao Liao
 * @date 2024-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_order_de")
public class MesOrderDe extends TenantEntity {

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


    private Long orderid;

    private Integer pcount;


    private BigDecimal ckcounts;
}
