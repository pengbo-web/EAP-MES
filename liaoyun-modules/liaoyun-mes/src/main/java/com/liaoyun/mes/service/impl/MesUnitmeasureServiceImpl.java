package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesUnitmeasureBo;
import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;
import com.liaoyun.mes.domain.MesUnitmeasure;
import com.liaoyun.mes.mapper.MesUnitmeasureMapper;
import com.liaoyun.mes.service.IMesUnitmeasureService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 计量单位Service业务层处理
 *
 * @author Kylin
 * @date 2024-05-10
 */
@RequiredArgsConstructor
@Service
public class MesUnitmeasureServiceImpl implements IMesUnitmeasureService {

    private final MesUnitmeasureMapper baseMapper;

    /**
     * 查询计量单位
     */
    @Override
    public MesUnitmeasureVo queryById(Long id){

        return baseMapper.selectVoById(id);
    }

    /**
     * 查询计量单位列表
     */
    @Override
    public TableDataInfo<MesUnitmeasureVo> queryPageList(MesUnitmeasureBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesUnitmeasure> lqw = buildQueryWrapper(bo);
        Page<MesUnitmeasureVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesUnitmeasureVo vo:result.getRecords()){
            vo.setUpname("无");
            if(null!=vo.getRemark()){
                MesUnitmeasureVo upvo=baseMapper.selectVoById(vo.getRemark());
                vo.setVo(upvo);
                vo.setUpname(upvo.getTitle());
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询计量单位列表
     */
    @Override
    public List<MesUnitmeasureVo> queryList(MesUnitmeasureBo bo) {
        LambdaQueryWrapper<MesUnitmeasure> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesUnitmeasure> buildQueryWrapper(MesUnitmeasureBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesUnitmeasure> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getUnitsn()), MesUnitmeasure::getUnitsn, bo.getUnitsn());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesUnitmeasure::getTitle, bo.getTitle());
        lqw.eq(bo.getIsMain() != null, MesUnitmeasure::getIsMain, bo.getIsMain());
        lqw.eq(bo.getGrow() != null, MesUnitmeasure::getGrow, bo.getGrow());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesUnitmeasure::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增计量单位
     */
    @Override
    public Boolean insertByBo(MesUnitmeasureBo bo) {
        MesUnitmeasure add = MapstructUtils.convert(bo, MesUnitmeasure.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改计量单位
     */
    @Override
    public Boolean updateByBo(MesUnitmeasureBo bo) {
        MesUnitmeasure update = MapstructUtils.convert(bo, MesUnitmeasure.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesUnitmeasure entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除计量单位
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }

        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
