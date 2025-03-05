package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesToolUseVo;
import com.liaoyun.mes.domain.bo.MesToolUseBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 夹具使用Service接口
 *
 * @author Liao Liao
 * @date 2024-10-11
 */
public interface IMesToolUseService {

    /**
     * 查询夹具使用
     *
     * @param id 主键
     * @return 夹具使用
     */
    MesToolUseVo queryById(Long id);

    /**
     * 分页查询夹具使用列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 夹具使用分页列表
     */
    TableDataInfo<MesToolUseVo> queryPageList(MesToolUseBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的夹具使用列表
     *
     * @param bo 查询条件
     * @return 夹具使用列表
     */
    List<MesToolUseVo> queryList(MesToolUseBo bo);

    /**
     * 新增夹具使用
     *
     * @param bo 夹具使用
     * @return 是否新增成功
     */
    Long insertByBo(MesToolUseBo bo);

    /**
     * 修改夹具使用
     *
     * @param bo 夹具使用
     * @return 是否修改成功
     */
    Boolean updateByBo(MesToolUseBo bo);

    /**
     * 校验并批量删除夹具使用信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
