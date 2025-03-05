package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesCuttingRec;
import com.liaoyun.mes.domain.MesCuttingUse;
import com.liaoyun.mes.domain.vo.MesCuttingRecVo;
import com.liaoyun.mes.domain.vo.MesCuttingUseVo;
import com.liaoyun.mes.mapper.MesCuttingRecMapper;
import com.liaoyun.mes.mapper.MesCuttingUseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesCuttingBo;
import com.liaoyun.mes.domain.vo.MesCuttingVo;
import com.liaoyun.mes.domain.MesCutting;
import com.liaoyun.mes.mapper.MesCuttingMapper;
import com.liaoyun.mes.service.IMesCuttingService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 刀具管理Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-10-09
 */
@RequiredArgsConstructor
@Service
public class MesCuttingServiceImpl implements IMesCuttingService {

    private final MesCuttingMapper baseMapper;

    private final MesCuttingRecMapper cuttingRecMapper;

    private final MesCuttingUseMapper cuttingUseMapper;

    /**
     * 查询刀具管理
     *
     * @param id 主键
     * @return 刀具管理
     */
    @Override
    public MesCuttingVo queryById(Long id){
        MesCuttingVo vo=baseMapper.selectVoById(id);
        LambdaQueryWrapper<MesCuttingRec> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MesCuttingRec::getToolid, id);
        wrapper.orderByDesc(MesCuttingRec::getCreateTime);
        List<MesCuttingRecVo> list=cuttingRecMapper.selectVoList(wrapper);
        for(MesCuttingRecVo vo1:list){
            vo1.setUsername(cuttingRecMapper.getusername(vo1.getUserId()));
        }
        LambdaQueryWrapper<MesCuttingUse> wrapper2 = Wrappers.lambdaQuery();
        wrapper2.eq(MesCuttingUse::getToolid, id);
        List<MesCuttingUseVo> list1=cuttingUseMapper.selectVoList(wrapper2);
        for(MesCuttingUseVo vo2:list1){
            vo2.setUsername(cuttingUseMapper.getusername(vo2.getUserId()));
        }
        try{
            vo.setRecVoList(list);
            vo.setUseVoList(list1);
        }catch (Exception e){

        }

        return vo;
    }

    /**
     * 分页查询刀具管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 刀具管理分页列表
     */
    @Override
    public TableDataInfo<MesCuttingVo> queryPageList(MesCuttingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesCutting> lqw = buildQueryWrapper(bo);
        Page<MesCuttingVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的刀具管理列表
     *
     * @param bo 查询条件
     * @return 刀具管理列表
     */
    @Override
    public List<MesCuttingVo> queryList(MesCuttingBo bo) {
        LambdaQueryWrapper<MesCutting> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesCutting> buildQueryWrapper(MesCuttingBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesCutting> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesCutting::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesCutting::getSn, bo.getSn());
        lqw.like(StringUtils.isNotBlank(bo.getBrand()), MesCutting::getBrand, bo.getBrand());
        lqw.eq(StringUtils.isNotBlank(bo.getOtype()), MesCutting::getOtype, bo.getOtype());
        lqw.eq(bo.getStocknum() != null, MesCutting::getStocknum, bo.getStocknum());
        lqw.eq(bo.getStockrec() != null, MesCutting::getStockrec, bo.getStockrec());
        lqw.eq(bo.getOtstock() != null, MesCutting::getOtstock, bo.getOtstock());
        lqw.eq(bo.getUsestock() != null, MesCutting::getUsestock, bo.getUsestock());
        lqw.between(params.get("beginOtime") != null && params.get("endOtime") != null,
            MesCutting::getOtime ,params.get("beginOtime"), params.get("endOtime"));
        lqw.between(params.get("beginNexttime") != null && params.get("endNexttime") != null,
            MesCutting::getNexttime ,params.get("beginNexttime"), params.get("endNexttime"));
        lqw.eq(bo.getInstock() != null, MesCutting::getInstock, bo.getInstock());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesCutting::getStatus, bo.getStatus());
        lqw.eq(bo.getMarkcount() != null, MesCutting::getMarkcount, bo.getMarkcount());
        lqw.eq(StringUtils.isNotBlank(bo.getProids()), MesCutting::getProids, bo.getProids());
        return lqw;
    }

    /**
     * 新增刀具管理
     *
     * @param bo 刀具管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesCuttingBo bo) {
        MesCutting add = MapstructUtils.convert(bo, MesCutting.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改刀具管理
     *
     * @param bo 刀具管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesCuttingBo bo) {
        MesCutting update = MapstructUtils.convert(bo, MesCutting.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesCutting entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除刀具管理信息
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
