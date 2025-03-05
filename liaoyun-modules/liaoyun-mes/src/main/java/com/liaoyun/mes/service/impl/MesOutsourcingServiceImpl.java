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
import com.liaoyun.mes.domain.bo.MesOutsourcingBo;
import com.liaoyun.mes.domain.vo.MesOutsourcingVo;
import com.liaoyun.mes.domain.MesOutsourcing;
import com.liaoyun.mes.mapper.MesOutsourcingMapper;
import com.liaoyun.mes.service.IMesOutsourcingService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 外协列Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-16
 */
@RequiredArgsConstructor
@Service
public class MesOutsourcingServiceImpl implements IMesOutsourcingService {

    private final MesOutsourcingMapper baseMapper;

    /**
     * 查询外协列
     *
     * @param id 主键
     * @return 外协列
     */
    @Override
    public MesOutsourcingVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询外协列列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 外协列分页列表
     */
    @Override
    public TableDataInfo<MesOutsourcingVo> queryPageList(MesOutsourcingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOutsourcing> lqw = buildQueryWrapper(bo);
        Page<MesOutsourcingVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的外协列列表
     *
     * @param bo 查询条件
     * @return 外协列列表
     */
    @Override
    public List<MesOutsourcingVo> queryList(MesOutsourcingBo bo) {
        LambdaQueryWrapper<MesOutsourcing> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOutsourcing> buildQueryWrapper(MesOutsourcingBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOutsourcing> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesOutsourcing::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getProcess()), MesOutsourcing::getProcess, bo.getProcess());
        lqw.like(StringUtils.isNotBlank(bo.getMobile()), MesOutsourcing::getMobile, bo.getMobile());
        lqw.like(StringUtils.isNotBlank(bo.getAddress()), MesOutsourcing::getAddress, bo.getAddress());
        return lqw;
    }

    /**
     * 新增外协列
     *
     * @param bo 外协列
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesOutsourcingBo bo) {
        MesOutsourcing add = MapstructUtils.convert(bo, MesOutsourcing.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改外协列
     *
     * @param bo 外协列
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesOutsourcingBo bo) {
        MesOutsourcing update = MapstructUtils.convert(bo, MesOutsourcing.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOutsourcing entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除外协列信息
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
