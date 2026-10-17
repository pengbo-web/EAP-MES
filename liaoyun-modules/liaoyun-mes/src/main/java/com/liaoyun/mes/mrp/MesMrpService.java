package com.liaoyun.mes.mrp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.mes.controller.UnitConverter;
import com.liaoyun.mes.domain.MesOutstock;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.MesUnitmeasure;
import com.liaoyun.mes.domain.vo.MesProductVo;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.mapper.MesOutstockMapper;
import com.liaoyun.mes.mapper.MesProductMapper;
import com.liaoyun.mes.mapper.MesStockMapper;
import com.liaoyun.mes.mapper.MesUnitmeasureMapper;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MRP 物料需求递归展开服务(二次开发)
 * <p>
 * 替代原 getbom 的单层展开与订单详情的硬编码三层展开:
 * 1. 递归展开至可配置深度(mes.mrp.max-depth, 默认5层), 带循环引用保护;
 * 2. 全链路 BigDecimal 运算(4位小数), 替代原 float 精度损失;
 * 3. 子件缺失库存档案/单位档案时跳过并告警, 替代原空 catch 静默吞异常;
 * 4. 新增净需求计算: 净需求 = max(0, 毛需求 - 现有库存);
 * 5. 保留作者原有业务规则: 色粉按25kg/袋换算, 包材按个数反算。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MesMrpService {

    private static final BigDecimal COLOR_POWDER_PACK = BigDecimal.valueOf(25);
    private static final int SCALE = 4;

    private final MesProductMapper productMapper;
    private final MesStockMapper stockMapper;
    private final MesUnitmeasureMapper unitmeasureMapper;
    private final MesOutstockMapper outstockMapper;
    private final MesInventoryLedgerMapper inventoryLedgerMapper;

    @Value("${mes.mrp.max-depth:5}")
    private int maxDepth;

    /**
     * 从产品节点递归展开物料需求树
     *
     * @param productId 父产品id
     * @param orderQty  父层需求数量(已换算到父层单位)
     * @param orderId   订单id(用于统计出库占用)
     */
    public List<MesStockVo> expand(Long productId, BigDecimal orderQty, Long orderId) {
        return expand(productId, orderQty, orderId, 1, new HashSet<>());
    }

    private List<MesStockVo> expand(Long productId, BigDecimal qty, Long orderId, int depth, Set<Long> visited) {
        List<MesStockVo> result = new ArrayList<>();
        if (productId == null || depth > maxDepth) {
            if (depth > maxDepth) {
                log.warn("BOM展开达到最大深度{}层, 停止向下展开, productId={}", maxDepth, productId);
            }
            return result;
        }
        List<MesProductVo> children = productMapper.selectVoList(
            new LambdaQueryWrapper<MesProduct>().eq(MesProduct::getPid, productId));
        if (children.isEmpty()) {
            return result;
        }
        List<MesUnitmeasure> units = unitmeasureMapper.selectList();
        for (MesProductVo child : children) {
            if (child.getId() == null || !visited.add(child.getId())) {
                log.warn("检测到BOM循环引用或重复节点, 跳过: productId={}", child.getId());
                continue;
            }
            MesStockVo vo = child.getStocks() == null ? null : stockMapper.selectVoById(child.getStocks());
            if (vo == null) {
                log.warn("子件缺少库存档案, 跳过: productId={}", child.getId());
                visited.remove(child.getId());
                continue;
            }
            MesUnitmeasure sourceUnit = unitmeasureMapper.selectById(child.getUnitId());
            MesUnitmeasure targetUnit = unitmeasureMapper.selectById(vo.getUnitid());
            // 单件用量 = 需求数量 * 权重
            BigDecimal per = BigDecimal.valueOf(child.getNeedCount() == null ? 0 : child.getNeedCount())
                .multiply(BigDecimal.valueOf(child.getWeight()));
            // 毛需求 = 单件用量 * 父层数量, 再做单位换算
            BigDecimal gross = per.multiply(qty == null ? BigDecimal.ZERO : qty).setScale(SCALE, RoundingMode.HALF_UP);
            if (sourceUnit != null && targetUnit != null) {
                gross = UnitConverter.convertUnit(sourceUnit.getTitle(), targetUnit.getTitle(), gross, units)
                    .setScale(SCALE, RoundingMode.HALF_UP);
            }
            // 作者原有业务规则保留
            if ("色粉".equals(vo.getType())) {
                gross = gross.divide(COLOR_POWDER_PACK, SCALE, RoundingMode.HALF_UP);
            } else if ("包材".equals(vo.getType()) && per.compareTo(BigDecimal.ZERO) != 0) {
                gross = (qty == null ? BigDecimal.ZERO : qty).divide(per, SCALE, RoundingMode.HALF_UP);
            }
            vo.setNcout(gross.floatValue());
            vo.setNcname(sourceUnit == null ? null : sourceUnit.getTitle());
            vo.setUnitname(targetUnit == null ? null : targetUnit.getTitle());
            vo.setProid(child.getId());
            // 现有库存: 查询失败记警告并按0计, 不再静默吞异常
            BigDecimal onHand = BigDecimal.ZERO;
            try {
                onHand = new BigDecimal(inventoryLedgerMapper.getStockByProductId(vo.getId()));
            } catch (Exception e) {
                log.warn("现有库存查询失败, 按0计: stockId={}, 原因={}", vo.getId(), e.getMessage());
            }
            vo.setNowstock(onHand);
            // 出库占用: 该订单已出库数量
            float occupied = 0;
            if (orderId != null) {
                List<MesOutstock> outs = outstockMapper.selectList(new LambdaQueryWrapper<MesOutstock>()
                    .eq(MesOutstock::getRemark1, orderId)
                    .eq(MesOutstock::getProId, vo.getId())
                    .eq(MesOutstock::getStatus, '1'));
                for (MesOutstock out : outs) {
                    if (out.getCount() != null) {
                        occupied += out.getCount().floatValue();
                    }
                }
            }
            vo.setCkcount(occupied);
            // 净需求 = max(0, 毛需求 - 现有库存)
            vo.setNetcount(gross.subtract(onHand).max(BigDecimal.ZERO).setScale(SCALE, RoundingMode.HALF_UP));
            // 递归展开子层(毛需求作为子层父量)
            vo.setBomlist(expand(child.getId(), gross, orderId, depth + 1, visited));
            visited.remove(child.getId());
            result.add(vo);
        }
        return result;
    }
}
