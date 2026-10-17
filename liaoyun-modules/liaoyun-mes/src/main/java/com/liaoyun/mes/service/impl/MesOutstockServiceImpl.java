package com.liaoyun.mes.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.controller.ArithHelper;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.bo.MesOurecBo;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOutstockBo;
import com.liaoyun.mes.service.IMesOutstockService;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 出库管理Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-18
 */
@RequiredArgsConstructor
@Service
public class MesOutstockServiceImpl implements IMesOutstockService {

    private final MesOutstockMapper baseMapper;

    private final MesUnitmeasureMapper unitmeasureMapper;

    private final MesProductMapper productMapper;

    private final MesStockMapper stockMapper;

    private final com.liaoyun.mes.fifo.MesFifoService mesFifoService;

    private final MesOurecMapper ourecMapper;

    private final MesAllrecMapper allrecMapper;

    private final MesInventoryLedgerMapper inventoryLedgerMapper;

    /**
     * 查询出库管理
     *
     * @param id 主键
     * @return 出库管理
     */
    @Override
    public MesOutstockVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询出库管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 出库管理分页列表
     */
    @Override
    public TableDataInfo<MesOutstockVo> queryPageList(MesOutstockBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOutstock> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesStock> wrappe=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(bo.getProname())){
            wrappe.like(MesStock::getTilte,bo.getProname());
            List<MesStock> stockList = stockMapper.selectList(wrappe);
            if(stockList.size()>0){
                List<Long> ids=new ArrayList<>();
                for(MesStock stock:stockList){
                    ids.add(stock.getId());
                }
                lqw.in(MesOutstock::getProId,ids);
            }
        }
        Page<MesOutstockVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesOutstockVo vo : result.getRecords()) {
            vo.setStockVo(stockMapper.selectVoById(vo.getProId()));
            vo.setUniname(baseMapper.selectuniTitleById(vo.getUnitId()));
            vo.setUsername(baseMapper.getusername(vo.getCreateBy()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的出库管理列表
     *
     * @param bo 查询条件
     * @return 出库管理列表
     */
    @Override
    public List<MesOutstockVo> queryList(MesOutstockBo bo) {
        LambdaQueryWrapper<MesOutstock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOutstock> buildQueryWrapper(MesOutstockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOutstock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUnitId() != null, MesOutstock::getUnitId, bo.getUnitId());
        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesOutstock::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getFromtype()), MesOutstock::getFromtype, bo.getFromtype());
        lqw.eq(bo.getCount() != null, MesOutstock::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesOutstock::getSn, bo.getSn());
        lqw.eq(bo.getTaskId() != null, MesOutstock::getTaskId, bo.getTaskId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesOutstock::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增出库管理
     *
     * @param bo 出库管理
     * @return 是否新增成功
     */
    @Override
    @DSTransactional
    public Boolean insertByBo(MesOutstockBo bo) {
        MesOutstock add = MapstructUtils.convert(bo, MesOutstock.class);
        validEntityBeforeSave(add);
        MesStock stock=new MesStock();
        int rec=0;
        if(StringUtils.equals(bo.getRemark(),"1")){
            stock=stockMapper.selectById(add.getProId());
//            BigDecimal stocki=stock.getNowstock();
//
//            stocki=ArithHelper.sub(stocki, (add.getCount()));
//            stock.setNowstock(stocki);

//            stockMapper.updateById(stock);
            if(bo.getKwid()==0){
                bo.setKwid(null);
            }
            // 二次开发: 以批次级 FIFO 分配器替代存储过程 sp_inout(原实现按库位汇总FIFO且出库扣减丢失批次号)
            mesFifoService.allocate(stock.getId(), add.getCount().intValue(),
                add.getRemark1() == null ? null : String.valueOf(add.getRemark1()));
            MesAllrec allrec=new MesAllrec();
            allrec.setStockid(add.getProId());
            allrec.setType("出库");
            allrec.setFromtype(bo.getFromtype());
            allrec.setCounts(bo.getCount());
            allrec.setRemark(bo.getRemark());
            allrecMapper.insert(allrec);
        }else{
            stock=stockMapper.selectById(add.getProId());
            //后续开放
//            LambdaQueryWrapper<MesProduct> lqw = Wrappers.lambdaQuery();
//            lqw.eq(MesProduct::getStocks,stock.getId());
//            List<MesProductVo> products = productMapper.selectVoList(lqw);
//            MesProductVo voinfo = products.get(0);
//                BigDecimal stocki=stock.getNowstock();
//                stocki=ArithHelper.sub(stocki, BigDecimal.valueOf(add.getCount()));
//                stock.setNowstock(stocki);
//                stockMapper.updateById(stock);
//
//            MesUnitmeasureVo unitmeasureVo = unitmeasureMapper.selectVoById(add.getUnitId());
//            MesUnitmeasureVo unitmeasureupVo = new MesUnitmeasureVo();
//            String rema=unitmeasureVo.getRemark();
//            BigDecimal gw=new BigDecimal(1);
//            try {
//                gw=BigDecimal.valueOf(unitmeasureVo.getGrow());
//            }catch (Exception e){
//
//            }
//            BigDecimal v = gw;
//            //单位一直
//            if (voinfo.getUnitId().equals(add.getUnitId())) {
//                if (add.getCount() <= voinfo.getStock().intValue()) {
//                    stocki=ArithHelper.sub(stocki, BigDecimal.valueOf(add.getCount()));
//                }
//            } else {//单位需要进率换算
//                do {
//                    unitmeasureupVo = unitmeasureMapper.selectVoById(Long.valueOf(rema));
//                    if(ObjectUtils.isNotNull(unitmeasureupVo.getRemark())){
//                        rema=unitmeasureupVo.getRemark();
//                        v = v.multiply(BigDecimal.valueOf(unitmeasureupVo.getGrow()));
//                    }
//                } while (unitmeasureupVo != null && !unitmeasureupVo.getId().equals(voinfo.getUnitId()));
//                stocki=ArithHelper.mul(stocki,v);
//                if (add.getCount() <= stocki.intValue()) {
//                    stocki=ArithHelper.sub(stocki, BigDecimal.valueOf(add.getCount()));
//                    stocki=ArithHelper.div(stocki,v);
//                }else{
//                    stocki=ArithHelper.div(stocki,v);
//                }
//
//            }
//            voinfo.setStock(stocki);
//            MesProduct update = (MesProduct) BeanMapHelper.Bean2Bean(voinfo, MesProduct.class);
//            productMapper.updateById(update);
        }
        MesOurec ourecBo=new MesOurec();
        ourecBo.setStockid(stock.getId());
        BigDecimal nc=(bo.getCount());
        ourecBo.setCount(nc);
        ourecBo.setRemark("流转出库");
        ourecBo.setStatus("0");
        ourecMapper.insert(ourecBo);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改出库管理
     *
     * @param bo 出库管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesOutstockBo bo) {
        MesOutstock update = MapstructUtils.convert(bo, MesOutstock.class);
        validEntityBeforeSave(update);
        int rec=0;
        if(StringUtils.equals(bo.getStatus(),"1")){
            MesStock stock=new MesStock();
            stock=stockMapper.selectById(update.getProId());
//            BigDecimal stocki=stock.getNowstock();
//            update.setLaststock(stock.getNowstock());
//            stocki=ArithHelper.sub(stocki, (bo.getCount()));
//            stock.setNowstock(stocki);
//            stockMapper.updateById(stock);
            if(bo.getKwid()==0){
                bo.setKwid(null);
            }
            Map<String,Object> map=new HashMap<>();
            map.put("stockid",stock.getId());
            map.put("count",bo.getCount().intValue());
            map.put("kwid",bo.getKwid());
            map.put("tenantid", LoginHelper.getTenantId());
            map.put("msg",0);
            inventoryLedgerMapper.callInOutProcedure(map);

            MesAllrec allrec=new MesAllrec();
            allrec.setStockid(stock.getId());
            allrec.setCounts(bo.getCount());
            allrec.setType("出库");
            allrec.setFromtype("生产领料");
            allrec.setRemark(bo.getRemark());
            allrecMapper.insert(allrec);
        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOutstock entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除出库管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<MesUnitmeasureVo> getUnitList(MesOutstockBo bo) {
        MesStockVo stockVo=stockMapper.selectVoById(bo.getUnitId());

        List<Long> ps = new ArrayList<>();
        List<Long> ids = getAllChild(stockVo.getUnitid(), ps);
        List<Long> ps2 = new ArrayList<>();
        List<Long> ids2 = getAllChildup(stockVo.getUnitid(), ps2);
        ids.addAll(ids2);
        LambdaQueryWrapper<MesUnitmeasure> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MesUnitmeasure::getId, ids);
        List<MesUnitmeasureVo> voList = unitmeasureMapper.selectVoList(wrapper);
        return voList;
    }

    /**
     * 递归查询
     *
     * @param pid
     * @param childIds
     * @return
     */
    public List<Long> getAllChild(Long pid, List<Long> childIds) {
        // 判断当前节点是否已经处理过
        if (!childIds.contains(pid)) {
            childIds.add(pid);
            LambdaQueryWrapper<MesUnitmeasure> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MesUnitmeasure::getRemark, pid);
            List<MesUnitmeasureVo> list = unitmeasureMapper.selectVoList(wrapper);
            if (ObjectUtils.isNotEmpty(list)) {
                for (MesUnitmeasureVo vo : list) {
                    getAllChild(vo.getId(), childIds);
                }
            }
        }
        return childIds;
    }

    public List<Long> getAllChildup(Long pid, List<Long> childIds) {
        // 判断当前节点是否已经处理过
        if (!childIds.contains(pid)) {
            MesUnitmeasureVo vo = unitmeasureMapper.selectVoById(pid);
            if (ObjectUtils.isNotNull(vo.getRemark())) {
                childIds.add(vo.getId());
                getAllChildup(Long.valueOf(vo.getRemark()), childIds);
            }
        }
        return childIds;
    }
}
