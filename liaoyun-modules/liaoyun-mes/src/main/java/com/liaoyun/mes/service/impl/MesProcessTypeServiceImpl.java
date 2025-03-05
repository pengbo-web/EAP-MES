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
import com.liaoyun.mes.domain.bo.MesProcessTypeBo;
import com.liaoyun.mes.domain.vo.MesProcessTypeVo;
import com.liaoyun.mes.domain.MesProcessType;
import com.liaoyun.mes.mapper.MesProcessTypeMapper;
import com.liaoyun.mes.service.IMesProcessTypeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * ⑦工艺类别Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@RequiredArgsConstructor
@Service
public class MesProcessTypeServiceImpl implements IMesProcessTypeService {

    private final MesProcessTypeMapper baseMapper;

    /**
     * 查询⑦工艺类别
     *
     * @param id 主键
     * @return ⑦工艺类别
     */
    @Override
    public MesProcessTypeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询⑦工艺类别列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ⑦工艺类别分页列表
     */
    @Override
    public TableDataInfo<MesProcessTypeVo> queryPageList(MesProcessTypeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesProcessType> lqw = buildQueryWrapper(bo);
        Page<MesProcessTypeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的⑦工艺类别列表
     *
     * @param bo 查询条件
     * @return ⑦工艺类别列表
     */
    @Override
    public List<MesProcessTypeVo> queryList(MesProcessTypeBo bo) {
        LambdaQueryWrapper<MesProcessType> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesProcessType> buildQueryWrapper(MesProcessTypeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesProcessType> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesProcessType::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesProcessType::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesProcessType::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增⑦工艺类别
     *
     * @param bo ⑦工艺类别
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesProcessTypeBo bo) {
        MesProcessType add = MapstructUtils.convert(bo, MesProcessType.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改⑦工艺类别
     *
     * @param bo ⑦工艺类别
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesProcessTypeBo bo) {
        MesProcessType update = MapstructUtils.convert(bo, MesProcessType.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesProcessType entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除⑦工艺类别信息
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
