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
 * 委外订单对象 mes_outorder
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_outorder")
public class MesOutorder extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 委外名称
     */
    private String title;

    /**
     * 委外商家
     */
    private Long custid;

    /**
     * 订单号
     */
    private String orders;

    /**
     * 外部编号
     */
    private String sn;

    /**
     * 委外物料清单
     */
    private String stockids;

    /**
     * 出库库位
     */
    private String stocktype;

    /**
     * 价格
     */
    private BigDecimal prices;

    /**
     * 要求完成日期
     */
    private Date subtime;

    /**
     * 出库时间
     */
    private Date outtime;

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

    private Long stoid;

    private Integer counts;

    private String fromtype;

    private String remark2;
}
