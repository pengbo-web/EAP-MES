package com.liaoyun.system.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 库存流水
对象 mes_inventory_ledger
 *
 * @author Liao Liao
 * @date 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_inventory_ledger")
public class MesInventoryLedger extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     *
     */

    private Long sn;

    /**
     * 物料
     */
    private Long stockid;

    /**
     * 库位ID（FK
     */
    private Long locationId;

    /**
     * 操作数量（正数入库，负数出库）
     */
    private Integer quantity;

    /**
     * 操作类型（1入库/2出库）
     */
    private Integer operation;

    /**
     * 业务批次号
     */
    private String batchNo;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
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
