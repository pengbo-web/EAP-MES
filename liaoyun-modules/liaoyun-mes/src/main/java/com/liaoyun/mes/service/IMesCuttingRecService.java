package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesCuttingRecVo;
import com.liaoyun.mes.domain.bo.MesCuttingRecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 刀具记录审核Service接口
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
public interface IMesCuttingRecService {

    /**
     * 查询刀具记录审核
     *
     * @param id 主键
     * @return 刀具记录审核
     */
    MesCuttingRecVo queryById(Long id);

    /**
     * 分页查询刀具记录审核列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 刀具记录审核分页列表
     */
    TableDataInfo<MesCuttingRecVo> queryPageList(MesCuttingRecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的刀具记录审核列表
     *
     * @param bo 查询条件
     * @return 刀具记录审核列表
     */
    List<MesCuttingRecVo> queryList(MesCuttingRecBo bo);

    /**
     * 新增刀具记录审核
     *
     * @param bo 刀具记录审核
     * @return 是否新增成功
     */
    Boolean insertByBo(MesCuttingRecBo bo);

    /**
     * 修改刀具记录审核
     *
     * @param bo 刀具记录审核
     * @return 是否修改成功
     */
    Boolean updateByBo(MesCuttingRecBo bo);

    /**
     * 校验并批量删除刀具记录审核信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
