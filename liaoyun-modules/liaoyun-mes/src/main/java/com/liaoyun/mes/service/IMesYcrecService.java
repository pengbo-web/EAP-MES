package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesYcrecVo;
import com.liaoyun.mes.domain.bo.MesYcrecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 异常报工审核Service接口
 *
 * @author Liao Liao
 * @date 2024-11-08
 */
public interface IMesYcrecService {

    /**
     * 查询异常报工审核
     *
     * @param id 主键
     * @return 异常报工审核
     */
    MesYcrecVo queryById(Long id);

    /**
     * 分页查询异常报工审核列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 异常报工审核分页列表
     */
    TableDataInfo<MesYcrecVo> queryPageList(MesYcrecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的异常报工审核列表
     *
     * @param bo 查询条件
     * @return 异常报工审核列表
     */
    List<MesYcrecVo> queryList(MesYcrecBo bo);

    /**
     * 新增异常报工审核
     *
     * @param bo 异常报工审核
     * @return 是否新增成功
     */
    Boolean insertByBo(MesYcrecBo bo);

    /**
     * 修改异常报工审核
     *
     * @param bo 异常报工审核
     * @return 是否修改成功
     */
    Boolean updateByBo(MesYcrecBo bo);

    /**
     * 校验并批量删除异常报工审核信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
