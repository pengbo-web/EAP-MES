package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOolistVo;
import com.liaoyun.mes.domain.bo.MesOolistBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 委外入库详情Service接口
 *
 * @author Liao Liao
 * @date 2024-12-07
 */
public interface IMesOolistService {

    /**
     * 查询委外入库详情
     *
     * @param id 主键
     * @return 委外入库详情
     */
    MesOolistVo queryById(Long id);

    /**
     * 分页查询委外入库详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 委外入库详情分页列表
     */
    TableDataInfo<MesOolistVo> queryPageList(MesOolistBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的委外入库详情列表
     *
     * @param bo 查询条件
     * @return 委外入库详情列表
     */
    List<MesOolistVo> queryList(MesOolistBo bo);

    /**
     * 新增委外入库详情
     *
     * @param bo 委外入库详情
     * @return 是否新增成功
     */
    Boolean insertByBo(MesOolistBo bo);

    /**
     * 修改委外入库详情
     *
     * @param bo 委外入库详情
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOolistBo bo);

    /**
     * 校验并批量删除委外入库详情信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
