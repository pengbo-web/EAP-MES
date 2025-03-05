package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOutorderDeVo;
import com.liaoyun.mes.domain.vo.MesOutorderVo;
import com.liaoyun.mes.domain.bo.MesOutorderBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 委外订单Service接口
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
public interface IMesOutorderService {

    /**
     * 查询委外订单
     *
     * @param id 主键
     * @return 委外订单
     */
    MesOutorderVo queryById(Long id);

    List<MesOutorderDeVo> queryByDevoId(Long id);

    /**
     * 分页查询委外订单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 委外订单分页列表
     */
    TableDataInfo<MesOutorderVo> queryPageList(MesOutorderBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的委外订单列表
     *
     * @param bo 查询条件
     * @return 委外订单列表
     */
    List<MesOutorderVo> queryList(MesOutorderBo bo);

    /**
     * 新增委外订单
     *
     * @param bo 委外订单
     * @return 是否新增成功
     */
    Boolean insertByBo(MesOutorderBo bo);

    /**
     * 修改委外订单
     *
     * @param bo 委外订单
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOutorderBo bo);

    /**
     * 校验并批量删除委外订单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
