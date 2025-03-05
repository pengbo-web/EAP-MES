package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.bo.MesOrderBo;
import com.liaoyun.mes.domain.bo.MesWorkBo;
import com.liaoyun.mes.domain.vo.MesOrderVo;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.domain.vo.MesWorkVo;
import com.liaoyun.mes.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesAllrecBo;
import com.liaoyun.mes.domain.vo.MesAllrecVo;
import com.liaoyun.mes.service.IMesAllrecService;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 出入库记录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@RequiredArgsConstructor
@Service
public class MesAllrecServiceImpl implements IMesAllrecService {

    private final MesAllrecMapper baseMapper;
    private final MesStockMapper stockMapper;
    private final MesUnitmeasureMapper unitmeasureMapper;
    private final MesOrderMapper orderMapper;
    private final MesWorkMapper workMapper;
    private final MesSaleoutMapper saleoutMapper;
    private final MesCustomMapper customMapper;
    private final MesOutorderDeMapper outorderdeMapper;
    private final MesOutorderMapper outorderMapper;
    /**
     * 查询出入库记录
     *
     * @param id 主键
     * @return 出入库记录
     */
    @Override
    public MesAllrecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询出入库记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 出入库记录分页列表
     */
    @Override
    public TableDataInfo<MesAllrecVo> queryPageList(MesAllrecBo bo, PageQuery pageQuery) {

        LambdaQueryWrapper<MesAllrec> lq = new LambdaQueryWrapper<>();

        lq.eq(MesAllrec::getFromtype, "销售出库");
        lq.isNull(MesAllrec::getCname);
        List<MesAllrec> list11=baseMapper.selectList(lq);
        for (MesAllrec mesAllrec : list11) {
            LambdaQueryWrapper<MesSaleout> mne=new LambdaQueryWrapper<>();
            mne.eq(MesSaleout::getCreateTime, mesAllrec.getCreateTime());
            mne.eq(MesSaleout::getCounts, mesAllrec.getCounts());
            mne.eq(MesSaleout::getStockid, mesAllrec.getStockid());
            List<MesSaleout> tlist=saleoutMapper.selectList(mne);
            try{
                for (MesSaleout mesSaleout : tlist) {
                    MesOrder order=orderMapper.selectById(mesSaleout.getOrderid());
                    MesCustom custom=customMapper.selectById(order.getCustid());
                    mesAllrec.setCname(custom.getTitle());
                    baseMapper.updateById(mesAllrec);
                }
            }catch (Exception e){

            }
        }
        LambdaQueryWrapper<MesAllrec> lqc = new LambdaQueryWrapper<>();
        lqc.eq(MesAllrec::getFromtype, "委外出库");
        lqc.isNull(MesAllrec::getCname);
        List<MesAllrec> list11c=baseMapper.selectList(lqc);
        for (MesAllrec mesAllrec : list11c) {
            LambdaQueryWrapper<MesOutorderDe> mne1=new LambdaQueryWrapper<>();
            mne1.eq(MesOutorderDe::getCreateTime, mesAllrec.getCreateTime());
            mne1.eq(MesOutorderDe::getCounts, mesAllrec.getCounts());
            mne1.eq(MesOutorderDe::getStockid, mesAllrec.getStockid());
            List<MesOutorderDe> tlist=outorderdeMapper.selectList(mne1);
            try{
                for (MesOutorderDe mesSaleout : tlist) {
                    LambdaQueryWrapper<MesOutorder> outorderLambdaQueryWrapper=new LambdaQueryWrapper<>();
                    outorderLambdaQueryWrapper.like(MesOutorder::getStockids,mesSaleout.getId());

                    MesOutorder order=outorderMapper.selectList(outorderLambdaQueryWrapper).get(0);
                    MesCustom custom=customMapper.selectById(order.getCustid());
                    mesAllrec.setCname(custom.getTitle());
                    baseMapper.updateById(mesAllrec);
                }
            }catch (Exception e){

            }
        }



        LambdaQueryWrapper<MesAllrec> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesStock> stockwra=new LambdaQueryWrapper<>();
        List<Long> ids=new ArrayList<>();
        if(StringUtils.isNotBlank(bo.getStockcode())){
            stockwra.like(MesStock::getSn,bo.getStockcode());
            List<MesStock> list = stockMapper.selectList(stockwra);
            if(!list.isEmpty()){
                ids=list.stream().map(MesStock::getId).toList();
            }
        }
        if(StringUtils.isNotBlank(bo.getStockname())){
            stockwra.like(MesStock::getTilte,bo.getStockname());
            List<MesStock> list2 = stockMapper.selectList(stockwra);
            if(!list2.isEmpty()){
                ids=list2.stream().map(MesStock::getId).toList();
            }
        }
        if(!ids.isEmpty()){
            lqw.in(MesAllrec::getStockid, ids);
        }
        Page<MesAllrecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        float ins=0;
        float ous=0;
        for (MesAllrecVo vo : result.getRecords()) {
            vo.setStockVo(stockMapper.selectVoById(vo.getStockid()));
            vo.setUnitVo(unitmeasureMapper.selectVoById(vo.getUnitid()));
            if(vo.getType().equals("出库")){
                ous+=vo.getCounts().floatValue();
            }
            if(vo.getType().equals("入库")){
                ins+=vo.getCounts().floatValue();
            }
        }
        String msg="本列汇总，出库数量:"+ous+",入库数量："+ins;
        TableDataInfo<MesAllrecVo> ca=TableDataInfo.build(result);
        ca.setMsg(msg);
        return ca;
    }
    @Override
    public List<Map<String,String>> queryPageLists(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dates=sdf.format(System.currentTimeMillis());
        String beg=dates+" 00:00:00";
        String end=dates+" 23:59:59";
//        bo.setParams(params);
//        进入出库总量
        float chu=0;
        float ru=0;
        LambdaQueryWrapper<MesAllrec> lqw = new LambdaQueryWrapper<>();
        lqw.between(MesAllrec::getCreateTime, beg, end);
        List<MesAllrecVo> list=baseMapper.selectVoList(lqw);
        if (!list.isEmpty()){
            for (MesAllrecVo mesAllrecVo : list) {
                if(mesAllrecVo.getType().equals("出库")){
                    chu+=mesAllrecVo.getCounts().floatValue();
                }
                if(mesAllrecVo.getType().equals("入库")){
                    ru+=mesAllrecVo.getCounts().floatValue();
                }
            }
        }
        LambdaQueryWrapper<MesOrder> lqw2 = new LambdaQueryWrapper<>();
        lqw2.between(MesOrder::getCreateTime, beg, end);
        List<MesOrderVo> vo=orderMapper.selectVoList(lqw2);
        int orsize=vo.size();
        LambdaQueryWrapper<MesWork> lqw3=new LambdaQueryWrapper<>();
        lqw3.between(MesWork::getCreateTime, beg, end);
        List<MesWorkVo> vo2=workMapper.selectVoList(lqw3);
        int worksize=vo2.size();
        List<Map<String,String>> maps=new ArrayList<>();

        Map<String,String> map=new HashMap<>();
        map.put("name","今日入库");
        map.put("value", String.valueOf(ru));
        maps.add(map);
        map=new HashMap<>();
        map.put("name","进入出库");
        map.put("value", String.valueOf(chu));
        maps.add(map);
        map=new HashMap<>();
        map.put("name","今日订单");
        map.put("value", String.valueOf(orsize));
        maps.add(map);
        map=new HashMap<>();
        map.put("name","今日排产");
        map.put("value", String.valueOf(worksize));
        maps.add(map);

        return maps;
    }
    /**
     * 查询符合条件的出入库记录列表
     *
     * @param bo 查询条件
     * @return 出入库记录列表
     */
    @Override
    public List<MesAllrecVo> queryList(MesAllrecBo bo) {
        LambdaQueryWrapper<MesAllrec> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesStock> stockwra=new LambdaQueryWrapper<>();
        List<Long> ids=new ArrayList<>();
        if(StringUtils.isNotBlank(bo.getStockcode())){
            stockwra.eq(MesStock::getSn,bo.getStockcode());
            List<MesStock> list = stockMapper.selectList(stockwra);
            if(!list.isEmpty()){
                ids=list.stream().map(MesStock::getId).toList();
            }
        }
        if(StringUtils.isNotBlank(bo.getStockname())){
            stockwra.eq(MesStock::getTilte,bo.getStockname());
            List<MesStock> list2 = stockMapper.selectList(stockwra);
            if(!list2.isEmpty()){
                ids=list2.stream().map(MesStock::getId).toList();
            }
        }
        if(!ids.isEmpty()){
            lqw.in(MesAllrec::getStockid, ids);
        }
        List<MesAllrecVo> list = baseMapper.selectVoList(lqw);
        for (MesAllrecVo vo : list) {
            MesStockVo stock = stockMapper.selectVoById(vo.getStockid());
            vo.setStockVo(stock);
            vo.setStockname(stock.getTilte());
            vo.setStockcode(stock.getSn());
            vo.setStockgk(stock.getGk());
            vo.setUnitVo(unitmeasureMapper.selectVoById(vo.getUnitid()));
        }
        return list;
    }

    private LambdaQueryWrapper<MesAllrec> buildQueryWrapper(MesAllrecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesAllrec> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getType()), MesAllrec::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getFromtype()), MesAllrec::getFromtype, bo.getFromtype());
        lqw.eq(bo.getCounts() != null, MesAllrec::getCounts, bo.getCounts());
        lqw.like(bo.getPrices() != null, MesAllrec::getPrices, bo.getPrices());
        lqw.eq(bo.getStockid() != null, MesAllrec::getStockid, bo.getStockid());
        lqw.like(StringUtils.isNotBlank(bo.getCname()), MesAllrec::getCname, bo.getCname());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesAllrec::getStatus, bo.getStatus());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            MesAllrec::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), MesAllrec::getRemark, bo.getRemark());
        return lqw;
    }

    /**
     * 新增出入库记录
     *
     * @param bo 出入库记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesAllrecBo bo) {
        MesAllrec add = MapstructUtils.convert(bo, MesAllrec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改出入库记录
     *
     * @param bo 出入库记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesAllrecBo bo) {
        MesAllrec update = MapstructUtils.convert(bo, MesAllrec.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesAllrec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除出入库记录信息
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
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
