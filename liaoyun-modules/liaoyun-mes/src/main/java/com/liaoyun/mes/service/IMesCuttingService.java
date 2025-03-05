package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesCuttingVo;
import com.liaoyun.mes.domain.bo.MesCuttingBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 刀具管理Service接口
 *
 * @author Liao Liao
 * @date 2024-10-09
 */
public interface IMesCuttingService {

    /**
     * 查询刀具管理
     *
     * @param id 主键
     * @return 刀具管理
     */
    MesCuttingVo queryById(Long id);

    /**
     * 分页查询刀具管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 刀具管理分页列表
     */
    TableDataInfo<MesCuttingVo> queryPageList(MesCuttingBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的刀具管理列表
     *
     * @param bo 查询条件
     * @return 刀具管理列表
     */
    List<MesCuttingVo> queryList(MesCuttingBo bo);

    /**
     * 新增刀具管理
     *
     * @param bo 刀具管理
     * @return 是否新增成功
     */
    Boolean insertByBo(MesCuttingBo bo);

    /**
     * 修改刀具管理
     *
     * @param bo 刀具管理
     * @return 是否修改成功
     */
    Boolean updateByBo(MesCuttingBo bo);

    /**
     * 校验并批量删除刀具管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
