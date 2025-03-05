package com.liaoyun.system.domain.bo;

import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 库存流水
业务对象 mes_inventory_ledger
 *
 * @author Liao Liao
 * @date 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesInventoryLedger.class, reverseConvertGenerate = false)
public class MesInventoryLedgerBo extends BaseEntity {

    /**
     * 编号
     */
    private Long id;

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long sn;

    /**
     * 物料
     */
    @NotNull(message = "物料不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long stockid;

    /**
     * 库位ID（FK
     */
    @NotNull(message = "库位ID（FK不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long locationId;

    /**
     * 操作数量（正数入库，负数出库）
     */
    @NotNull(message = "操作数量（正数入库，负数出库）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long quantity;

    /**
     * 操作类型（1入库/2出库）
     */
    @NotNull(message = "操作类型（1入库/2出库）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long operation;

    /**
     * 业务批次号
     */
    private String batchNo;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
