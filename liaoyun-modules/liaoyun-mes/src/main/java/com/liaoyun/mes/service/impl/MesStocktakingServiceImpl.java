package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesOutstock;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.mapper.MesStockMapper;
import com.liaoyun.mes.mapper.MesStockTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesStocktakingBo;
import com.liaoyun.mes.domain.vo.MesStocktakingVo;
import com.liaoyun.mes.domain.MesStocktaking;
import com.liaoyun.mes.mapper.MesStocktakingMapper;
import com.liaoyun.mes.service.IMesStocktakingService;

import java.util.*;

/**
 * 库位盘点Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@RequiredArgsConstructor
@Service
public class MesStocktakingServiceImpl implements IMesStocktakingService {

    private final MesStocktakingMapper baseMapper;

    private final MesStockMapper stockMapper;

    private final MesStockTypeMapper stockTypeMapper;
    /**
     * 查询库位盘点
     *
     * @param id 主键
     * @return 库位盘点
     */
    @Override
    public MesStocktakingVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询库位盘点列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 库位盘点分页列表
     */
    @Override
    public TableDataInfo<MesStocktakingVo> queryPageList(MesStocktakingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesStocktaking> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesStock> wrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(bo.getStockname())){
            wrapper.like(MesStock::getTilte,bo.getStockname());
            List<MesStock> stockList = stockMapper.selectList(wrapper);
            if(stockList.size()>0){
                List<Long> ids=new ArrayList<>();
                for(MesStock stock:stockList){
                    ids.add(stock.getId());
                }
                lqw.in(MesStocktaking::getStockid,ids);
            }
        }
        Page<MesStocktakingVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for(MesStocktakingVo stocktakingVo : result.getRecords()){
            stocktakingVo.setStockVo(stockMapper.selectVoById(stocktakingVo.getStockid()));
            stocktakingVo.setStypeinfo(stockTypeMapper.selectVoById(stocktakingVo.getStocktypeid()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的库位盘点列表
     *
     * @param bo 查询条件
     * @return 库位盘点列表
     */
    @Override
    public List<MesStocktakingVo> queryList(MesStocktakingBo bo) {
        LambdaQueryWrapper<MesStocktaking> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesStocktaking> buildQueryWrapper(MesStocktakingBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesStocktaking> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStockid() != null, MesStocktaking::getStockid, bo.getStockid());
        lqw.eq(bo.getStocktypeid() != null, MesStocktaking::getStocktypeid, bo.getStocktypeid());
        lqw.eq(bo.getCounts() != null, MesStocktaking::getCounts, bo.getCounts());
        lqw.eq(bo.getYccounts() != null, MesStocktaking::getYccounts, bo.getYccounts());
        lqw.eq(StringUtils.isNotBlank(bo.getYcrec()), MesStocktaking::getYcrec, bo.getYcrec());
        lqw.eq(bo.getJc() != null, MesStocktaking::getJc, bo.getJc());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesStocktaking::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增库位盘点
     *
     * @param bo 库位盘点
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesStocktakingBo bo) {
        MesStocktaking add = MapstructUtils.convert(bo, MesStocktaking.class);
        MesStockVo stockVo=stockMapper.selectVoById(bo.getStockid());
        if(Objects.equals(stockVo.getNowstock(), bo.getCounts())){
            add.setStatus("1");
        }
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改库位盘点
     *
     * @param bo 库位盘点
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesStocktakingBo bo) {
        MesStocktaking update = MapstructUtils.convert(bo, MesStocktaking.class);
        validEntityBeforeSave(update);
        if(StringUtils.equals(bo.getStatus(),"1")){
            MesStock stock=stockMapper.selectById(bo.getStockid());
            update.setRemark(stock.getNowstock().toString());
            stock.setNowstock(bo.getCounts());
            stockMapper.updateById(stock);
        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesStocktaking entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除库位盘点信息
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
