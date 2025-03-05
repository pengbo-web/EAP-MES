package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.MesCustom;
import com.liaoyun.mes.domain.vo.MesCustomVo;
import com.liaoyun.mes.domain.bo.MesCustomBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 客户供应商Service接口
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
public interface IMesCustomService {

    /**
     * 查询客户供应商
     *
     * @param id 主键
     * @return 客户供应商
     */
    MesCustomVo queryById(Long id);

    /**
     * 分页查询客户供应商列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客户供应商分页列表
     */
    TableDataInfo<MesCustomVo> queryPageList(MesCustomBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的客户供应商列表
     *
     * @param bo 查询条件
     * @return 客户供应商列表
     */
    List<MesCustomVo> queryList(MesCustomBo bo);

    /**
     * 新增客户供应商
     *
     * @param bo 客户供应商
     * @return 是否新增成功
     */
    Boolean insertByBo(MesCustomBo bo);
    MesCustom insertByBo2(MesCustomBo bo);
    /**
     * 修改客户供应商
     *
     * @param bo 客户供应商
     * @return 是否修改成功
     */
    Boolean updateByBo(MesCustomBo bo);

    /**
     * 校验并批量删除客户供应商信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
