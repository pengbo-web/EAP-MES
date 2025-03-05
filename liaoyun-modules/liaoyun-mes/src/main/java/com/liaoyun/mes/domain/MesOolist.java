package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 委外入库详情对象 mes_oolist
 *
 * @author Liao Liao
 * @date 2024-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_oolist")
public class MesOolist extends TenantEntity {

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
     * 数量
     */
    private BigDecimal counts;

    /**
     * 主编号
     */
    private Long mainid;

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

    private Long kwid;
    private String orderid;
}
