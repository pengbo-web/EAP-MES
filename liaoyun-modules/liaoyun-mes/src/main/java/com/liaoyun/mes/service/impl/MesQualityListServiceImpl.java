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
import com.liaoyun.mes.domain.bo.MesQualityListBo;
import com.liaoyun.mes.domain.vo.MesQualityListVo;
import com.liaoyun.mes.domain.MesQualityList;
import com.liaoyun.mes.mapper.MesQualityListMapper;
import com.liaoyun.mes.service.IMesQualityListService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 质检内容Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@RequiredArgsConstructor
@Service
public class MesQualityListServiceImpl implements IMesQualityListService {

    private final MesQualityListMapper baseMapper;

    /**
     * 查询质检内容
     *
     * @param id 主键
     * @return 质检内容
     */
    @Override
    public MesQualityListVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询质检内容列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 质检内容分页列表
     */
    @Override
    public TableDataInfo<MesQualityListVo> queryPageList(MesQualityListBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesQualityList> lqw = buildQueryWrapper(bo);
        Page<MesQualityListVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的质检内容列表
     *
     * @param bo 查询条件
     * @return 质检内容列表
     */
    @Override
    public List<MesQualityListVo> queryList(MesQualityListBo bo) {
        LambdaQueryWrapper<MesQualityList> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesQualityList> buildQueryWrapper(MesQualityListBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesQualityList> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getQualitymainId() != null, MesQualityList::getQualitymainId, bo.getQualitymainId());
        lqw.eq(StringUtils.isNotBlank(bo.getStandards()), MesQualityList::getStandards, bo.getStandards());
        lqw.eq(StringUtils.isNotBlank(bo.getRefvalue()), MesQualityList::getRefvalue, bo.getRefvalue());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesQualityList::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增质检内容
     *
     * @param bo 质检内容
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesQualityListBo bo) {
        MesQualityList add = MapstructUtils.convert(bo, MesQualityList.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改质检内容
     *
     * @param bo 质检内容
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesQualityListBo bo) {
        MesQualityList update = MapstructUtils.convert(bo, MesQualityList.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesQualityList entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除质检内容信息
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
