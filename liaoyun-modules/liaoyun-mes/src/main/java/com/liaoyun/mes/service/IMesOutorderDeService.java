package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOutorderDeVo;
import com.liaoyun.mes.domain.bo.MesOutorderDeBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 委外详情Service接口
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
public interface IMesOutorderDeService {

    /**
     * 查询委外详情
     *
     * @param id 主键
     * @return 委外详情
     */
    MesOutorderDeVo queryById(Long id);

    /**
     * 分页查询委外详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 委外详情分页列表
     */
    TableDataInfo<MesOutorderDeVo> queryPageList(MesOutorderDeBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的委外详情列表
     *
     * @param bo 查询条件
     * @return 委外详情列表
     */
    List<MesOutorderDeVo> queryList(MesOutorderDeBo bo);

    /**
     * 新增委外详情
     *
     * @param bo 委外详情
     * @return 是否新增成功
     */
    Long insertByBo(MesOutorderDeBo bo);

    /**
     * 修改委外详情
     *
     * @param bo 委外详情
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOutorderDeBo bo);

    /**
     * 校验并批量删除委外详情信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
