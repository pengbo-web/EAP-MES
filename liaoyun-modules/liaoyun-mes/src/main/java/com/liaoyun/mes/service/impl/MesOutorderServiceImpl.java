package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.common.translation.annotation.Translation;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOutorderBo;
import com.liaoyun.mes.service.IMesOutorderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.*;

/**
 * 委外订单Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@RequiredArgsConstructor
@Service
public class MesOutorderServiceImpl implements IMesOutorderService {

    private final MesOutorderMapper baseMapper;

    private final MesCustomMapper customMapper;
    private final MesStockMapper stockMapper;
    private  final MesOutorderDeMapper deMapper;
    private final MesUnitmeasureMapper unitmeasureMapper;
    private final MesOolistMapper oolistMapper;
    private final MesAllrecMapper allrecMapper;

    private final MesInventoryLedgerMapper inventoryLedgerMapper;

    /**
     * 查询委外订单
     *
     * @param id 主键
     * @return 委外订单
     */
    @Override
    public MesOutorderVo queryById(Long id){
        MesOutorderVo vo=baseMapper.selectVoById(id);
        List<String> tearms = Arrays.asList(vo.getStockids().split(","));
        List<MesOutorderDeVo> delist=new ArrayList<>();
        if(tearms.size()>0){
                    List<Long> longList = tearms.stream()
                        .map(Long::parseLong)
                        .toList();
                    for(Long l:longList){
                        MesOutorderDeVo deVo=deMapper.selectVoById(l);
                        MesStockVo stockVo=stockMapper.selectVoById(deVo.getStockid());
                        deVo.setStockVo(stockVo);
                        delist.add(deVo);
                    }
            LambdaQueryWrapper<MesOolist> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesOolist::getMainid,vo.getId());
            List<MesOolistVo> oolists=oolistMapper.selectVoList(wrapper);
            for (MesOolistVo oolist : oolists) {
                oolist.setStockVo(stockMapper.selectVoById(oolist.getStockid()));
            }
            vo.setOolistVos(oolists);
            vo.setDevo(delist);
        }
        return vo;
    }
    @Override
    public List<MesOutorderDeVo> queryByDevoId(Long id){
        MesOutorderVo vo=baseMapper.selectVoById(id);
        List<String> tearms = Arrays.asList(vo.getStockids().split(","));
        List<MesOutorderDeVo> delist=new ArrayList<>();
        if(tearms.size()>0){
            List<Long> longList = tearms.stream()
                .map(Long::parseLong)
                .toList();
            for(Long l:longList){
                MesOutorderDeVo deVo=deMapper.selectVoById(l);
                delist.add(deVo);
            }

        }
        return delist;
    }

    /**
     * 分页查询委外订单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 委外订单分页列表
     */
    @Override
    public TableDataInfo<MesOutorderVo> queryPageList(MesOutorderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOutorder> lqw = buildQueryWrapper(bo);
        if(StringUtils.isNotBlank(bo.getCustname())){
            LambdaQueryWrapper<MesCustom> wrapper=new LambdaQueryWrapper<>();
            wrapper.like(MesCustom::getTitle,bo.getCustname());
            List<MesCustomVo> custom=customMapper.selectVoList(wrapper);
            List<Long> ids=new ArrayList<>();
            for (MesCustomVo customVo : custom) {
                ids.add(customVo.getId());
            }
            if(ids.size()>0){
                lqw.in(MesOutorder::getCustid,ids);
            }
        }
        Page<MesOutorderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesOutorderVo vo : result.getRecords()) {
            MesCustomVo customVo=customMapper.selectVoById(vo.getCustid());
            vo.setCname(customVo.getTitle());
            LambdaQueryWrapper<MesOolist> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesOolist::getMainid,vo.getId());
            List<MesOolistVo> oolists=oolistMapper.selectVoList(wrapper);
            for (MesOolistVo oolist : oolists) {
                oolist.setStockVo(stockMapper.selectVoById(oolist.getStockid()));
            }
            vo.setOolistVos(oolists);
            List<String> tearms = Arrays.asList(vo.getStockids().split(","));
            List<MesOutorderDeVo> delist=new ArrayList<>();
            String nc="";
            try{
                if(tearms.size()>0){
                    List<Long> longList = tearms.stream()
                        .map(Long::parseLong)
                        .toList();
                    List<String> names=new ArrayList<>();
                    for(Long id:longList){
                        MesOutorderDeVo de=deMapper.selectVoById(id);
                        delist.add(de);
                        MesStockVo stock=stockMapper.selectVoById(de.getStockid());
                        stock.setUnitname(unitmeasureMapper.selectVoById(stock.getUnitid()).getTitle());
                        de.setStockVo(stock);
                        names.add(stock.getTilte());
                    }
                    if(null!=vo.getStoid()){
                        nc=stockMapper.selectVoById(vo.getStoid()).getTilte();
                    }
                    vo.setStonames(String.join(", ", names));
                }
            }catch (Exception e){

            }
            vo.setStoname(nc);
        vo.setDevo(delist);

        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的委外订单列表
     *
     * @param bo 查询条件
     * @return 委外订单列表
     */
    @Override
    public List<MesOutorderVo> queryList(MesOutorderBo bo) {
        LambdaQueryWrapper<MesOutorder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOutorder> buildQueryWrapper(MesOutorderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOutorder> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesOutorder::getTitle, bo.getTitle());
        lqw.eq(bo.getCustid() != null, MesOutorder::getCustid, bo.getCustid());
        lqw.like(StringUtils.isNotBlank(bo.getOrders()), MesOutorder::getOrders, bo.getOrders());
        lqw.eq(StringUtils.isNotBlank(bo.getStockids()), MesOutorder::getStockids, bo.getStockids());
        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesOutorder::getSn, bo.getSn());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), MesOutorder::getRemark, bo.getRemark());
        lqw.eq(StringUtils.isNotBlank(bo.getFromtype()), MesOutorder::getFromtype, bo.getFromtype());
        lqw.like(StringUtils.isNotBlank(bo.getStocktype()), MesOutorder::getStocktype, bo.getStocktype());
        lqw.eq(bo.getPrices() != null, MesOutorder::getPrices, bo.getPrices());
        lqw.between(params.get("beginSubtime") != null && params.get("endSubtime") != null,
            MesOutorder::getSubtime ,params.get("beginSubtime"), params.get("endSubtime"));
        lqw.between(params.get("beginOuttime") != null && params.get("endOuttime") != null,
            MesOutorder::getOuttime ,params.get("beginOuttime"), params.get("endOuttime"));
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesOutorder::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增委外订单
     *
     * @param bo 委外订单
     * @return 是否新增成功
     */
    @Override
    @Transactional
    public Boolean insertByBo(MesOutorderBo bo) {
        MesOutorder add = MapstructUtils.convert(bo, MesOutorder.class);
        List<String> tearms = Arrays.asList(bo.getStockids().split(","));
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        try{
            if(tearms.size()>0){
                List<Long> longList = tearms.stream()
                    .map(Long::parseLong)
                    .toList();
                List<String> names=new ArrayList<>();
                for(Long id:longList){
                    MesOutorderDeVo de=deMapper.selectVoById(id);
                    de.setMainid(add.getId());
//                    MesStockVo stock=stockMapper.selectVoById(de.getStockid());
//                    BigDecimal newp=BigDecimal.ZERO;
//                    newp=stock.getNowstock().subtract(BigDecimal.valueOf(de.getCounts()));
//                    stock.setNowstock(newp);
//                    MesStock adds = MapstructUtils.convert(stock, MesStock.class);
//
//                    stockMapper.updateById(adds);
                    MesAllrec allrec=new MesAllrec();
                    allrec.setStockid(de.getStockid());
                    allrec.setType("出库");
                    allrec.setFromtype("委外出库-待");
                    allrec.setRemark(de.getRemark());
                    allrec.setPrices(de.getPrices());
                    try{
                        MesCustomVo customVo=customMapper.selectVoById(bo.getCustid());
                        allrec.setCname(customVo.getTitle());
                    }catch (Exception e){

                    }
                    allrec.setCounts(BigDecimal.valueOf(de.getCounts()));
                    allrecMapper.insert(allrec);
                }
            }
        }catch (Exception e){

        }

        return flag;
    }

    /**
     * 修改委外订单
     *
     * @param bo 委外订单
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(MesOutorderBo bo) {
        MesOutorder update = MapstructUtils.convert(bo, MesOutorder.class);
        try{

            if(bo.getCounts()>0&&bo.getStatus().equals("1")){
                MesStock stock=stockMapper.selectById(bo.getStoid());
                BigDecimal nowstock=stock.getNowstock();
                nowstock=nowstock.add(BigDecimal.valueOf(bo.getCounts()));
                stock.setNowstock(nowstock);
                stockMapper.updateById(stock);
                MesAllrec allrec=new MesAllrec();
                allrec.setStockid(stock.getId());
                allrec.setType("入库");
                allrec.setFromtype("委外入库");
                allrec.setRemark(bo.getRemark());
                allrec.setPrices(bo.getPrices());
                allrec.setCounts(BigDecimal.valueOf(bo.getCounts()));
                try{
                    MesCustomVo customVo=customMapper.selectVoById(bo.getCustid());
                    allrec.setCname(customVo.getTitle());
                }catch (Exception e){

                }

                allrecMapper.insert(allrec);
                MesOolist oolist=new MesOolist();
                oolist.setMainid(update.getId());
                oolist.setStockid(bo.getStoid());
                oolist.setCounts(BigDecimal.valueOf(bo.getCounts()));
                oolist.setRemark(bo.getRemark());
                oolist.setOrderid(bo.getOrders());
                oolistMapper.insert(oolist);
            }

        }catch (Exception e){

        }

        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOutorder entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除委外订单信息
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
        for(Long id : ids){
            MesOutorder en=baseMapper.selectById(id);
            en.setRemark2("手动删除");
            baseMapper.updateById(en);
            List<String> tearms = Arrays.asList(en.getStockids().split(","));
            List<MesOutorderDeVo> delist=new ArrayList<>();
            List<Long> longList = tearms.stream()
                        .map(Long::parseLong)
                        .toList();
            for(Long id2:longList){
                MesOutorderDe de=deMapper.selectById(id2);
                MesStock stock=stockMapper.selectById(de.getStockid());
                BigDecimal newp=BigDecimal.ZERO;
                newp=stock.getNowstock().add(BigDecimal.valueOf(de.getCounts()));
                stock.setNowstock(newp);
                stockMapper.updateById(stock);
                de.setRemark2("手动删除");
                deMapper.updateById(de);
                //删除记录
                LambdaQueryWrapper<MesAllrec> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(MesAllrec::getStockid,stock.getId());
                queryWrapper.eq(MesAllrec::getCreateTime,de.getCreateTime());
                queryWrapper.eq(MesAllrec::getCounts,de.getCounts());
                queryWrapper.eq(MesAllrec::getType,"出库");
                List<MesAllrec> list = allrecMapper.selectList(queryWrapper);
                for (MesAllrec mesAllrec : list) {
                    mesAllrec.setRemark2("手动删除");
                    allrecMapper.updateById(mesAllrec);
                    allrecMapper.deleteById(mesAllrec);
                }
                //删除详细记录
                deMapper.deleteById(de);
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
