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
import com.liaoyun.mes.domain.bo.MesWuliuBo;
import com.liaoyun.mes.domain.vo.MesWuliuVo;
import com.liaoyun.mes.domain.MesWuliu;
import com.liaoyun.mes.mapper.MesWuliuMapper;
import com.liaoyun.mes.service.IMesWuliuService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 物流信息Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@RequiredArgsConstructor
@Service
public class MesWuliuServiceImpl implements IMesWuliuService {

    private final MesWuliuMapper baseMapper;

    /**
     * 查询物流信息
     *
     * @param id 主键
     * @return 物流信息
     */
    @Override
    public MesWuliuVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询物流信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 物流信息分页列表
     */
    @Override
    public TableDataInfo<MesWuliuVo> queryPageList(MesWuliuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesWuliu> lqw = buildQueryWrapper(bo);
        Page<MesWuliuVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的物流信息列表
     *
     * @param bo 查询条件
     * @return 物流信息列表
     */
    @Override
    public List<MesWuliuVo> queryList(MesWuliuBo bo) {
        LambdaQueryWrapper<MesWuliu> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesWuliu> buildQueryWrapper(MesWuliuBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesWuliu> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesWuliu::getSn, bo.getSn());
        lqw.like(StringUtils.isNotBlank(bo.getCname()), MesWuliu::getCname, bo.getCname());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), MesWuliu::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getConts()), MesWuliu::getConts, bo.getConts());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesWuliu::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), MesWuliu::getRemark, bo.getRemark());
        return lqw;
    }

    /**
     * 新增物流信息
     *
     * @param bo 物流信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesWuliuBo bo) {
        MesWuliu add = MapstructUtils.convert(bo, MesWuliu.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改物流信息
     *
     * @param bo 物流信息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesWuliuBo bo) {
        MesWuliu update = MapstructUtils.convert(bo, MesWuliu.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesWuliu entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除物流信息信息
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
