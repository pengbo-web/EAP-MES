package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesProcurementplanVo;
import com.liaoyun.mes.domain.bo.MesProcurementplanBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 采购计划Service接口
 *
 * @author Liao Liao
 * @date 2024-07-22
 */
public interface IMesProcurementplanService {

    /**
     * 查询采购计划
     *
     * @param id 主键
     * @return 采购计划
     */
    MesProcurementplanVo queryById(Long id);

    /**
     * 分页查询采购计划列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 采购计划分页列表
     */
    TableDataInfo<MesProcurementplanVo> queryPageList(MesProcurementplanBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的采购计划列表
     *
     * @param bo 查询条件
     * @return 采购计划列表
     */
    List<MesProcurementplanVo> queryList(MesProcurementplanBo bo);

    /**
     * 新增采购计划
     *
     * @param bo 采购计划
     * @return 是否新增成功
     */
    Boolean insertByBo(MesProcurementplanBo bo);

    /**
     * 修改采购计划
     *
     * @param bo 采购计划
     * @return 是否修改成功
     */
    Boolean updateByBo(MesProcurementplanBo bo);

    /**
     * 校验并批量删除采购计划信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
