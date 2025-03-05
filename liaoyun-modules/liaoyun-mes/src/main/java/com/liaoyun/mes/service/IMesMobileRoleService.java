package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesMobileRoleVo;
import com.liaoyun.mes.domain.bo.MesMobileRoleBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 移动端权限Service接口
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
public interface IMesMobileRoleService {

    /**
     * 查询移动端权限
     *
     * @param id 主键
     * @return 移动端权限
     */
    MesMobileRoleVo queryById(Long id);

    /**
     * 分页查询移动端权限列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 移动端权限分页列表
     */
    TableDataInfo<MesMobileRoleVo> queryPageList(MesMobileRoleBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的移动端权限列表
     *
     * @param bo 查询条件
     * @return 移动端权限列表
     */
    List<MesMobileRoleVo> queryList(MesMobileRoleBo bo);

    /**
     * 新增移动端权限
     *
     * @param bo 移动端权限
     * @return 是否新增成功
     */
    Boolean insertByBo(MesMobileRoleBo bo);

    /**
     * 修改移动端权限
     *
     * @param bo 移动端权限
     * @return 是否修改成功
     */
    Boolean updateByBo(MesMobileRoleBo bo);

    /**
     * 校验并批量删除移动端权限信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
