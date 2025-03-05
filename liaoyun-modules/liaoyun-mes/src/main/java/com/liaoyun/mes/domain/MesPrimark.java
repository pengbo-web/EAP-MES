package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 打单列表对象 mes_primark
 *
 * @author Liao Liao
 * @date 2024-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_primark")
public class MesPrimark extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 工作id
     */
    private Long workid;

    /**
     * 订单号
     */
    private Long orderid;

    /**
     * 交货日期
     */
    private String subtime;

    /**
     * 流转码
     */
    private String sn;

    /**
     * 物料码
     */
    private Long stockid;

    /**
     * 版次
     */
    private String banci;

    /**
     * 数量
     */
    private BigDecimal counts;

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



    private Integer wul;

}
