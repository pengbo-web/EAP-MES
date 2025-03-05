package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesMobileMenuVo;
import com.liaoyun.mes.domain.bo.MesMobileMenuBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 移动端目录Service接口
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
public interface IMesMobileMenuService {

    /**
     * 查询移动端目录
     *
     * @param id 主键
     * @return 移动端目录
     */
    MesMobileMenuVo queryById(Long id);

    /**
     * 分页查询移动端目录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 移动端目录分页列表
     */
    TableDataInfo<MesMobileMenuVo> queryPageList(MesMobileMenuBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的移动端目录列表
     *
     * @param bo 查询条件
     * @return 移动端目录列表
     */
    List<MesMobileMenuVo> queryList(MesMobileMenuBo bo);


    List<MesMobileMenuVo> getmylist();

    /**
     * 新增移动端目录
     *
     * @param bo 移动端目录
     * @return 是否新增成功
     */
    Boolean insertByBo(MesMobileMenuBo bo);

    /**
     * 修改移动端目录
     *
     * @param bo 移动端目录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesMobileMenuBo bo);

    /**
     * 校验并批量删除移动端目录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
