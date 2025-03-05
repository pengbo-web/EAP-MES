package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesTaskcVo;
import com.liaoyun.mes.domain.bo.MesTaskcBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 组装流转Service接口
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
public interface IMesTaskcService {

    /**
     * 查询组装流转
     *
     * @param id 主键
     * @return 组装流转
     */
    MesTaskcVo queryById(Long id);

    /**
     * 分页查询组装流转列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 组装流转分页列表
     */
    TableDataInfo<MesTaskcVo> queryPageList(MesTaskcBo bo, PageQuery pageQuery);
    TableDataInfo<MesTaskcVo> queryPagecList(MesTaskcBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的组装流转列表
     *
     * @param bo 查询条件
     * @return 组装流转列表
     */
    List<MesTaskcVo> queryList(MesTaskcBo bo);

    /**
     * 新增组装流转
     *
     * @param bo 组装流转
     * @return 是否新增成功
     */
    Boolean insertByBo(MesTaskcBo bo);

    /**
     * 修改组装流转
     *
     * @param bo 组装流转
     * @return 是否修改成功
     */
    Boolean updateByBo(MesTaskcBo bo);

    /**
     * 校验并批量删除组装流转信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
