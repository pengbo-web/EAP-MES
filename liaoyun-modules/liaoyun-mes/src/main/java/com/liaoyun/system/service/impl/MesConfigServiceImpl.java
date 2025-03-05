package com.liaoyun.system.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.system.domain.bo.MesConfigBo;
import com.liaoyun.system.domain.vo.MesConfigVo;
import com.liaoyun.system.domain.MesConfig;
import com.liaoyun.system.mapper.MesConfigMapper;
import com.liaoyun.system.service.IMesConfigService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * MES配置Service业务层处理
 *
 * @author Liao Liao
 * @date 2025-02-25
 */
@RequiredArgsConstructor
@Service
public class MesConfigServiceImpl implements IMesConfigService {

    private final MesConfigMapper baseMapper;

    /**
     * 查询MES配置
     *
     * @param id 主键
     * @return MES配置
     */
    @Override
    public MesConfigVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询MES配置列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return MES配置分页列表
     */
    @Override
    public TableDataInfo<MesConfigVo> queryPageList(MesConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesConfig> lqw = buildQueryWrapper(bo);
        Page<MesConfigVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的MES配置列表
     *
     * @param bo 查询条件
     * @return MES配置列表
     */
    @Override
    public List<MesConfigVo> queryList(MesConfigBo bo) {
        LambdaQueryWrapper<MesConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesConfig> buildQueryWrapper(MesConfigBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesConfig> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(MesConfig::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getTimes()), MesConfig::getTimes, bo.getTimes());
        lqw.eq(StringUtils.isNotBlank(bo.getChetime()), MesConfig::getChetime, bo.getChetime());
        return lqw;
    }

    /**
     * 新增MES配置
     *
     * @param bo MES配置
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesConfigBo bo) {
        MesConfig add = MapstructUtils.convert(bo, MesConfig.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改MES配置
     *
     * @param bo MES配置
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesConfigBo bo) {
        MesConfig update = MapstructUtils.convert(bo, MesConfig.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesConfig entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除MES配置信息
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
        return baseMapper.deleteByIds(ids) > 0;
    }
}
