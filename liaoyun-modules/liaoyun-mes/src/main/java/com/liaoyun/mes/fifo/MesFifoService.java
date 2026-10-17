package com.liaoyun.mes.fifo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.exception.ServiceException;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.mes.domain.MesFifoAlloc;
import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.mes.mapper.MesFifoAllocMapper;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批次级 FIFO 出库分配器(二次开发)
 * <p>
 * 替代存储过程 sp_inout 的"库位级 FIFO":
 * 1. 以入库台账流水 sn 作为收货批次标识, 按入库先后( sn 升序 )贪心分配;
 * 2. 分配明细落 mes_fifo_alloc, 出库台账扣减行回填来源批次号 batch_no, 修复批次追溯断链;
 * 3. 批次余量 = 入库数量 - 已分配数量, 避免原实现按库位汇总导致的批次粒度丢失;
 * 4. 库存不足时抛出业务异常, 由调用方事务回滚。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MesFifoService {

    private final MesInventoryLedgerMapper ledgerMapper;
    private final MesFifoAllocMapper allocMapper;

    /**
     * 按 FIFO 为出库需求分配收货批次
     *
     * @param stockId 库存档案id
     * @param qty     出库数量
     * @param outNo   出库单号/订单号
     * @return 分配明细
     */
    @Transactional(rollbackFor = Exception.class)
    public List<MesFifoAlloc> allocate(Long stockId, int qty, String outNo) {
        if (stockId == null || qty <= 0) {
            throw new ServiceException("FIFO分配参数非法: stockId=" + stockId + ", qty=" + qty);
        }
        // 入库批次按入库先后排序( sn 为台账自增流水 )
        List<MesInventoryLedger> receipts = ledgerMapper.selectList(
            new LambdaQueryWrapper<MesInventoryLedger>()
                .eq(MesInventoryLedger::getStockid, stockId)
                .eq(MesInventoryLedger::getOperation, 1)
                .gt(MesInventoryLedger::getQuantity, 0)
                .orderByAsc(MesInventoryLedger::getSn));
        int need = qty;
        List<MesFifoAlloc> result = new java.util.ArrayList<>();
        for (MesInventoryLedger receipt : receipts) {
            if (need <= 0) {
                break;
            }
            Integer allocated = allocMapper.selectList(new LambdaQueryWrapper<MesFifoAlloc>()
                    .eq(MesFifoAlloc::getLedgerSn, receipt.getSn()))
                .stream().mapToInt(MesFifoAlloc::getQty).sum();
            int remaining = receipt.getQuantity() - allocated;
            if (remaining <= 0) {
                continue;
            }
            int take = Math.min(remaining, need);
            MesFifoAlloc alloc = new MesFifoAlloc();
            alloc.setLedgerSn(receipt.getSn());
            alloc.setStockid(stockId);
            alloc.setQty(take);
            alloc.setOutNo(outNo);
            allocMapper.insert(alloc);
            // 出库扣减行回填来源批次号, 修复批次追溯断链
            MesInventoryLedger deduct = new MesInventoryLedger();
            deduct.setStockid(stockId);
            deduct.setLocationId(receipt.getLocationId());
            deduct.setQuantity(-take);
            deduct.setOperation(2);
            deduct.setBatchNo(String.valueOf(receipt.getSn()));
            deduct.setRemark("FIFO分配 出库单:" + (StringUtils.isBlank(outNo) ? "-" : outNo)
                + " 来源批次:" + receipt.getSn());
            ledgerMapper.insert(deduct);
            need -= take;
            result.add(alloc);
        }
        if (need > 0) {
            throw new ServiceException("FIFO批次库存不足, 缺口: " + need + ", stockId: " + stockId);
        }
        log.info("FIFO分配完成: stockId={}, qty={}, 批次数={}, 出库单={}", stockId, qty, result.size(), outNo);
        return result;
    }
}
