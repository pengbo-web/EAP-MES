package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOrderVo;
import com.liaoyun.mes.domain.bo.MesOrderBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 销售订单Service接口
 *
 * @author Liao Liao
 * @date 2024-11-16
 */
public interface IMesOrderService {

    /**
     * 查询销售订单
     *
     * @param id 主键
     * @return 销售订单
     */
    MesOrderVo queryById(Long id);

    /**
     * 分页查询销售订单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 销售订单分页列表
     */
    TableDataInfo<MesOrderVo> queryPageList(MesOrderBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的销售订单列表
     *
     * @param bo 查询条件
     * @return 销售订单列表
     */
    List<MesOrderVo> queryList(MesOrderBo bo);

    /**
     * 新增销售订单
     *
     * @param bo 销售订单
     * @return 是否新增成功
     */
    Boolean insertByBo(MesOrderBo bo);
    Boolean bequick(Long id);
    /**
     * 修改销售订单
     *
     * @param bo 销售订单
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOrderBo bo);

    /**
     * 校验并批量删除销售订单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
