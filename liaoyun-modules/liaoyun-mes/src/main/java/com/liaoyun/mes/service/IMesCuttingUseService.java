package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesCuttingUseVo;
import com.liaoyun.mes.domain.bo.MesCuttingUseBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 刀具使用统计Service接口
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
public interface IMesCuttingUseService {

    /**
     * 查询刀具使用统计
     *
     * @param id 主键
     * @return 刀具使用统计
     */
    MesCuttingUseVo queryById(Long id);

    /**
     * 分页查询刀具使用统计列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 刀具使用统计分页列表
     */
    TableDataInfo<MesCuttingUseVo> queryPageList(MesCuttingUseBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的刀具使用统计列表
     *
     * @param bo 查询条件
     * @return 刀具使用统计列表
     */
    List<MesCuttingUseVo> queryList(MesCuttingUseBo bo);

    /**
     * 新增刀具使用统计
     *
     * @param bo 刀具使用统计
     * @return 是否新增成功
     */
    Boolean insertByBo(MesCuttingUseBo bo);

    /**
     * 修改刀具使用统计
     *
     * @param bo 刀具使用统计
     * @return 是否修改成功
     */
    Boolean updateByBo(MesCuttingUseBo bo);

    /**
     * 校验并批量删除刀具使用统计信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
