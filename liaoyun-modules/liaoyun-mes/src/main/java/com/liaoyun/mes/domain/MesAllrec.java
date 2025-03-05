package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 出入库记录对象 mes_allrec
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_allrec")
public class MesAllrec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 来源
     */
    private String fromtype;

    /**
     * 数量
     */
    private BigDecimal counts;

    /**
     * 单位
     */
    private Long unitid;

    /**
     * 价格
     */
    private BigDecimal prices;

    /**
     * 物料
     */
    private Long stockid;

    /**
     * 供应商
     */
    private String cname;

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

    private String remark2;

    private Long stocktype;
    /**
     * 订单号
     */
    private String orderid;
}
