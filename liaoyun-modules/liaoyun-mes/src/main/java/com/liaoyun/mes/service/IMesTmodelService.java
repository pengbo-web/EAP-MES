package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesTmodelVo;
import com.liaoyun.mes.domain.bo.MesTmodelBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 模具管理Service接口
 *
 * @author Liao Liao
 * @date 2024-12-08
 */
public interface IMesTmodelService {

    /**
     * 查询模具管理
     *
     * @param id 主键
     * @return 模具管理
     */
    MesTmodelVo queryById(Long id);

    /**
     * 分页查询模具管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 模具管理分页列表
     */
    TableDataInfo<MesTmodelVo> queryPageList(MesTmodelBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的模具管理列表
     *
     * @param bo 查询条件
     * @return 模具管理列表
     */
    List<MesTmodelVo> queryList(MesTmodelBo bo);

    /**
     * 新增模具管理
     *
     * @param bo 模具管理
     * @return 是否新增成功
     */
    Boolean insertByBo(MesTmodelBo bo);

    /**
     * 修改模具管理
     *
     * @param bo 模具管理
     * @return 是否修改成功
     */
    Boolean updateByBo(MesTmodelBo bo);

    /**
     * 校验并批量删除模具管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
