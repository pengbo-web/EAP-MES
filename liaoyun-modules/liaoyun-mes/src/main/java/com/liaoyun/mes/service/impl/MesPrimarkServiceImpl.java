package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.MesOrderVo;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.domain.vo.MesWorkVo;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesPrimarkBo;
import com.liaoyun.mes.domain.vo.MesPrimarkVo;
import com.liaoyun.mes.service.IMesPrimarkService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 打单列表Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-16
 */
@RequiredArgsConstructor
@Service
public class MesPrimarkServiceImpl implements IMesPrimarkService {

    private final MesPrimarkMapper baseMapper;

    private final MesWorkMapper workMapper;

    private final MesOrderMapper orderMapper;

    private final MesStockMapper stockMapper;

    private final MesAllrecMapper allrecMapper;

    private final MesProductMapper productMapper;

    private final MesInventoryLedgerMapper inventoryLedgerMapper;


    /**
     * 查询打单列表
     *
     * @param id 主键
     * @return 打单列表
     */
    @Override
    public MesPrimarkVo queryById(Long id){
        MesPrimarkVo vo=baseMapper.selectVoById(id);
        MesStockVo vo1=stockMapper.selectVoById(vo.getStockid());
        MesOrderVo orderVo=orderMapper.selectVoById(vo.getOrderid());
        MesWorkVo workVo=workMapper.selectVoById(vo.getWorkid());
        vo.setOrderVo(orderVo);
        vo.setWorkVo(workVo);
        vo.setStockVo(vo1);
        return vo;
    }

    /**
     * 分页查询打单列表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 打单列表分页列表
     */
    @Override
    public TableDataInfo<MesPrimarkVo> queryPageList(MesPrimarkBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesPrimark> lqw = buildQueryWrapper(bo);
        Page<MesPrimarkVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesPrimarkVo vo : result.getRecords()) {
            MesStockVo vo1=stockMapper.selectVoById(vo.getStockid());
            MesOrderVo orderVo=orderMapper.selectVoById(vo.getOrderid());
            MesWorkVo workVo=workMapper.selectVoById(vo.getWorkid());
            vo.setOrderVo(orderVo);
            vo.setWorkVo(workVo);
            vo.setStockVo(vo1);
            //上级入库单位
            LambdaQueryWrapper<MesProduct> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesProduct::getStocks, vo.getStockid());
            vo.setTc(1);
            try{
                MesProduct mesProduct=productMapper.selectList(wrapper).get(0);
                if(null!=mesProduct.getPid()){
                    MesProduct product=productMapper.selectById(mesProduct.getPid());
                    vo.setUptitle(product.getTitle());
                    vo.setProduct(product);
                }

            }catch (Exception e){

            }

        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的打单列表列表
     *
     * @param bo 查询条件
     * @return 打单列表列表
     */
    @Override
    public List<MesPrimarkVo> queryList(MesPrimarkBo bo) {
        LambdaQueryWrapper<MesPrimark> lqw = buildQueryWrapper(bo);
        List<MesPrimarkVo> list = baseMapper.selectVoList(lqw);
        for (MesPrimarkVo vo : list) {
            MesStockVo vo1=stockMapper.selectVoById(vo.getStockid());
            MesOrderVo orderVo=orderMapper.selectVoById(vo.getOrderid());
            MesWorkVo workVo=workMapper.selectVoById(vo.getWorkid());
            vo.setOrderVo(orderVo);
            vo.setWorkVo(workVo);
            vo.setStockVo(vo1);

        }
        return list;
    }

    private LambdaQueryWrapper<MesPrimark> buildQueryWrapper(MesPrimarkBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesPrimark> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getWorkid() != null, MesPrimark::getWorkid, bo.getWorkid());
        lqw.eq(bo.getOrderid() != null, MesPrimark::getOrderid, bo.getOrderid());
        lqw.eq(StringUtils.isNotBlank(bo.getSubtime()), MesPrimark::getSubtime, bo.getSubtime());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesPrimark::getSn, bo.getSn());
        lqw.eq(bo.getStockid() != null, MesPrimark::getStockid, bo.getStockid());
        lqw.eq(StringUtils.isNotBlank(bo.getBanci()), MesPrimark::getBanci, bo.getBanci());
        lqw.eq(bo.getCounts() != null, MesPrimark::getCounts, bo.getCounts());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesPrimark::getStatus, bo.getStatus());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            MesPrimark::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增打单列表
     *
     * @param bo 打单列表
     * @return 是否新增成功
     */
    @Override
    public Long insertByBo(MesPrimarkBo bo) {
        MesPrimark add = MapstructUtils.convert(bo, MesPrimark.class);
        validEntityBeforeSave(add);
        MesWork workVo=workMapper.selectById(bo.getWorkid());

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            workVo.setWul(workVo.getWul()+1);
            workMapper.updateById(workVo);
            return add.getId();
        }
        return 0L;
    }

    /**
     * 修改打单列表
     *
     * @param bo 打单列表
     * @return 是否修改成功
     */
    @Override
    @Transactional
    public Boolean updateByBo(MesPrimarkBo bo) {
        MesPrimark update = MapstructUtils.convert(bo, MesPrimark.class);

        validEntityBeforeSave(update);
        if(StringUtils.equals(bo.getStatus(),"1")){
            MesStock stock=stockMapper.selectById(bo.getStockid());
            MesInventoryLedger inventoryLedger=new MesInventoryLedger();
            inventoryLedger.setStockid(stock.getId());
            inventoryLedger.setQuantity(bo.getCounts().intValue());
            inventoryLedger.setLocationId(0L);
            inventoryLedger.setOperation(1);
            inventoryLedgerMapper.insert(inventoryLedger);
//            BigDecimal newstock=stock.getNowstock().add(bo.getCounts());
//            stock.setNowstock(newstock);
//            stockMapper.updateById(stock);
            MesAllrec allrec=new MesAllrec();
            allrec.setType("入库");
            allrec.setFromtype("半成品入库");
            allrec.setStockid(stock.getId());
            allrec.setUnitid(stock.getUnitid());
            allrec.setCname("车间入库");
            allrec.setCounts(bo.getCounts());
            allrecMapper.insert(allrec);
        }
        if(StringUtils.equals(bo.getStatus(),"3")){
            MesStock stock=stockMapper.selectById(bo.getStockid());
//            BigDecimal newstock=stock.getNowstock().subtract(bo.getCounts());
//            stock.setNowstock(newstock);
//            stockMapper.updateById(stock);
            Map<String,Object> map=new HashMap<>();
            map.put("stockid",stock.getId());
            map.put("count",bo.getCounts().intValue());
            map.put("kwid",null);
            map.put("tenantid", LoginHelper.getTenantId());
            map.put("msg",0);
            inventoryLedgerMapper.callInOutProcedure(map);
            MesAllrec allrec=new MesAllrec();
            allrec.setType("出库");
            allrec.setFromtype("半成品出库");
            allrec.setStockid(stock.getId());
            allrec.setUnitid(stock.getUnitid());
            allrec.setCname("车间出库");
            allrec.setCounts(bo.getCounts());
            allrec.setStocktype(1873337162020016129L);
            allrecMapper.insert(allrec);
        }
        //tc为成品入库
        if(null!=bo.getTc()){
            if(bo.getTc()==1){
                /*MesStock stock=stockMapper.selectById(bo.getStockid());
                BigDecimal newstock=stock.getNowstock().subtract(bo.getCounts());
                stock.setNowstock(newstock);
                stockMapper.updateById(stock);*/
//                MesAllrec allrec=new MesAllrec();
//                allrec.setType("出库");
//                allrec.setFromtype("半成品出库");
//                allrec.setStockid(stock.getId());
//                allrec.setUnitid(stock.getUnitid());
//                allrec.setCname("车间出库");
//                allrec.setCounts(bo.getCounts());
//                allrecMapper.insert(allrec);
                MesStock stock1=stockMapper.selectById(bo.getUpid());
//                BigDecimal newstock1=stock1.getNowstock().add(bo.getCounts1());
//                stock1.setNowstock(newstock1);
//                stockMapper.updateById(stock);
                MesInventoryLedger inventoryLedger=new MesInventoryLedger();
                inventoryLedger.setStockid(stock1.getId());
                inventoryLedger.setQuantity(bo.getCounts1().intValue());
                inventoryLedger.setLocationId(0L);
                inventoryLedger.setOperation(1);
                inventoryLedgerMapper.insert(inventoryLedger);
                MesAllrec allrec1=new MesAllrec();
                allrec1.setType("入库");
                allrec1.setFromtype("成品入库");
                allrec1.setStockid(stock1.getId());
                allrec1.setUnitid(stock1.getUnitid());
                allrec1.setCname("车间入库");
                allrec1.setCounts(bo.getCounts1());
                allrecMapper.insert(allrec1);
            }
        }

        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesPrimark entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除打单列表信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        for (Long id : ids) {
            MesPrimark entity = baseMapper.selectById(id);
            MesWork work = workMapper.selectById(entity.getWorkid());
            work.setWul(work.getWul()-1);

            workMapper.updateById(work);
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
