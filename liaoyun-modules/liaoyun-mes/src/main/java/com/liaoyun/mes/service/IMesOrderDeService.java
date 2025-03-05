package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOrderDeVo;
import com.liaoyun.mes.domain.bo.MesOrderDeBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 订单详情Service接口
 *
 * @author Liao Liao
 * @date 2024-11-17
 */
public interface IMesOrderDeService {

    /**
     * 查询订单详情
     *
     * @param id 主键
     * @return 订单详情
     */
    MesOrderDeVo queryById(Long id);

    /**
     * 分页查询订单详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 订单详情分页列表
     */
    TableDataInfo<MesOrderDeVo> queryPageList(MesOrderDeBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的订单详情列表
     *
     * @param bo 查询条件
     * @return 订单详情列表
     */
    List<MesOrderDeVo> queryList(MesOrderDeBo bo);

    /**
     * 新增订单详情
     *
     * @param bo 订单详情
     * @return 是否新增成功
     */
    Long insertByBo(MesOrderDeBo bo);

    /**
     * 修改订单详情
     *
     * @param bo 订单详情
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOrderDeBo bo);

    /**
     * 校验并批量删除订单详情信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
