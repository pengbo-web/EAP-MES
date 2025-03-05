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
import com.liaoyun.mes.domain.bo.MesMobileRoleBo;
import com.liaoyun.mes.domain.vo.MesMobileRoleVo;
import com.liaoyun.mes.domain.MesMobileRole;
import com.liaoyun.mes.mapper.MesMobileRoleMapper;
import com.liaoyun.mes.service.IMesMobileRoleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 移动端权限Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@RequiredArgsConstructor
@Service
public class MesMobileRoleServiceImpl implements IMesMobileRoleService {

    private final MesMobileRoleMapper baseMapper;

    /**
     * 查询移动端权限
     *
     * @param id 主键
     * @return 移动端权限
     */
    @Override
    public MesMobileRoleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询移动端权限列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 移动端权限分页列表
     */
    @Override
    public TableDataInfo<MesMobileRoleVo> queryPageList(MesMobileRoleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesMobileRole> lqw = buildQueryWrapper(bo);
        Page<MesMobileRoleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesMobileRoleVo vo : result.getRecords()) {
            String[] equlist= baseMapper.selectTitleById(vo.getUserId()).toArray(new String[0]);
            vo.setIdaname(String.join(StringUtils.SEPARATOR, equlist));
            vo.setRolename(baseMapper.selectTitlenvById(vo.getMenuids()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的移动端权限列表
     *
     * @param bo 查询条件
     * @return 移动端权限列表
     */
    @Override
    public List<MesMobileRoleVo> queryList(MesMobileRoleBo bo) {
        LambdaQueryWrapper<MesMobileRole> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesMobileRole> buildQueryWrapper(MesMobileRoleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesMobileRole> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, MesMobileRole::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getMenuids()), MesMobileRole::getMenuids, bo.getMenuids());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesMobileRole::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增移动端权限
     *
     * @param bo 移动端权限
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesMobileRoleBo bo) {
        MesMobileRole add = MapstructUtils.convert(bo, MesMobileRole.class);
        add.setUserId(String.join(",", bo.getUserIdarray()));
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改移动端权限
     *
     * @param bo 移动端权限
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesMobileRoleBo bo) {
        MesMobileRole update = MapstructUtils.convert(bo, MesMobileRole.class);
        update.setUserId(String.join(",", bo.getUserIdarray()));
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesMobileRole entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除移动端权限信息
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
