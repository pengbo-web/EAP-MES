package com.liaoyun.mes.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liaoyun.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * FIFO 批次级出库分配明细对象 mes_fifo_alloc(二次开发新增)
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_fifo_alloc")
public class MesFifoAlloc extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 入库台账批次sn(收货批次标识)
     */
    private Long ledgerSn;

    /**
     * 库存档案id
     */
    private Long stockid;

    /**
     * 本批次分配数量
     */
    private Integer qty;

    /**
     * 出库单号/订单号
     */
    private String outNo;

    /**
     * 删除标志
     */
    @TableLogic
    private String delFlag;
}
