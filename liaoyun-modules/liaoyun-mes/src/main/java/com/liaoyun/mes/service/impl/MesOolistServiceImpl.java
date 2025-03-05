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
import com.liaoyun.mes.domain.vo.MesOutorderVo;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOolistBo;
import com.liaoyun.mes.domain.vo.MesOolistVo;
import com.liaoyun.mes.service.IMesOolistService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 委外入库详情Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-07
 */
@RequiredArgsConstructor
@Service
public class MesOolistServiceImpl implements IMesOolistService {

    private final MesOolistMapper baseMapper;
    private final MesStockMapper stockMapper;
    private final MesAllrecMapper allrecMapper;
    private final MesInventoryLedgerMapper inventoryLedgerMapper;
    private final MesOutorderMapper outorderMapper;

    private final MesCustomMapper customMapper;
    /**
     * 查询委外入库详情
     *
     * @param id 主键
     * @return 委外入库详情
     */
    @Override
    public MesOolistVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询委外入库详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 委外入库详情分页列表
     */
    @Override
    public TableDataInfo<MesOolistVo> queryPageList(MesOolistBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOolist> lqw = buildQueryWrapper(bo);
        Page<MesOolistVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesOolistVo vo : result.getRecords()) {
            vo.setStockVo(stockMapper.selectVoById(vo.getStockid()));
            MesOutorderVo outorder=outorderMapper.selectVoById(vo.getMainid());
            try{
                outorder.setCname(customMapper.selectVoById(outorder.getCustid()).getTitle());
            }catch (Exception e){

            }

            vo.setOutorderVo(outorder);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的委外入库详情列表
     *
     * @param bo 查询条件
     * @return 委外入库详情列表
     */
    @Override
    public List<MesOolistVo> queryList(MesOolistBo bo) {
        LambdaQueryWrapper<MesOolist> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOolist> buildQueryWrapper(MesOolistBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOolist> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStockid() != null, MesOolist::getStockid, bo.getStockid());
//        lqw.eq(StringUtils.isNotBlank(bo.getCounts()), MesOolist::getCounts, bo.getCounts());
        lqw.eq(bo.getMainid() != null, MesOolist::getMainid, bo.getMainid());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesOolist::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增委外入库详情
     *
     * @param bo 委外入库详情
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesOolistBo bo) {
        MesOolist add = MapstructUtils.convert(bo, MesOolist.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改委外入库详情
     *
     * @param bo 委外入库详情
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesOolistBo bo) {
        MesOolist update = MapstructUtils.convert(bo, MesOolist.class);
        validEntityBeforeSave(update);
        Map<String,Object> map=new HashMap<>();
        if(bo.getStatus().equals("2")){
            if(bo.getKwid()==0){
                bo.setKwid(null);
            }
            map.put("stockid",bo.getStockid());
            map.put("count",bo.getCounts().intValue());
            map.put("kwid",bo.getKwid());
            map.put("tenantid", LoginHelper.getTenantId());
            map.put("msg",0);
            inventoryLedgerMapper.callInOutProcedure(map);
        }
        if(map.get("msg").equals(1)){
            baseMapper.updateById(update);
            return true;
        }
        return false;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOolist entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除委外入库详情信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        for(Long id : ids){
            MesOolist o = baseMapper.selectById(id);
            MesStock stock = stockMapper.selectById(o.getStockid());
            o.setRemark2("手动删除");
            baseMapper.updateById(o);
            if(stock != null){
                BigDecimal nows=stock.getNowstock().subtract(o.getCounts());
                stock.setNowstock(nows);
                stockMapper.updateById(stock);
            }
            LambdaQueryWrapper<MesAllrec> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesAllrec::getStockid,stock.getId());
            wrapper.eq(MesAllrec::getCreateTime,o.getCreateTime());
            wrapper.eq(MesAllrec::getType,"入库");
            wrapper.eq(MesAllrec::getCounts,o.getCounts());
            for (MesAllrec allrec : allrecMapper.selectList(wrapper)) {
                allrec.setRemark2("手动删除");
                allrecMapper.updateById(allrec);
                allrecMapper.deleteById(allrec);
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
