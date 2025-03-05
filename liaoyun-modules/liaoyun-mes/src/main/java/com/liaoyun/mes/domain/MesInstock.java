package com.liaoyun. mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;
import java.util.Date;

/**
 * 入库信息对象 mes_instock
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_instock")
public class MesInstock extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 产品
     */
    private Long proId;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 来源
     */
    private String fromtype;

    /**
     * 价格
     */
    private BigDecimal nowprice;

    /**
     * 总价
     */
    private BigDecimal allprice;

    private BigDecimal oldst;

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

    private String kwname;

    private Date subtime;

    private Integer st;

    private Long stockid;
    private Long kwid;
    private String kwsn;
}
