package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesPreVo;
import com.liaoyun.mes.domain.bo.MesPreBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 采购审核Service接口
 *
 * @author Liao Liao
 * @date 2025-03-13
 */
public interface IMesPreService {

    /**
     * 查询采购审核
     *
     * @param id 主键
     * @return 采购审核
     */
    MesPreVo queryById(Long id);

    /**
     * 分页查询采购审核列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 采购审核分页列表
     */
    TableDataInfo<MesPreVo> queryPageList(MesPreBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的采购审核列表
     *
     * @param bo 查询条件
     * @return 采购审核列表
     */
    List<MesPreVo> queryList(MesPreBo bo);

    /**
     * 新增采购审核
     *
     * @param bo 采购审核
     * @return 是否新增成功
     */
    MesPreVo insertByBo(MesPreBo bo);

    /**
     * 修改采购审核
     *
     * @param bo 采购审核
     * @return 是否修改成功
     */
    Boolean updateByBo(MesPreBo bo);

    /**
     * 校验并批量删除采购审核信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
