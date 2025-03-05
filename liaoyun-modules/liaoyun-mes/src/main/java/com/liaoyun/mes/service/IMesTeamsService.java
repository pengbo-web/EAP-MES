package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesTeamsVo;
import com.liaoyun.mes.domain.bo.MesTeamsBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 班组管理Service接口
 *
 * @author Liao Liao
 * @date 2024-10-25
 */
public interface IMesTeamsService {

    /**
     * 查询班组管理
     *
     * @param id 主键
     * @return 班组管理
     */
    MesTeamsVo queryById(Long id);

    /**
     * 分页查询班组管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 班组管理分页列表
     */
    TableDataInfo<MesTeamsVo> queryPageList(MesTeamsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的班组管理列表
     *
     * @param bo 查询条件
     * @return 班组管理列表
     */
    List<MesTeamsVo> queryList(MesTeamsBo bo);

    /**
     * 新增班组管理
     *
     * @param bo 班组管理
     * @return 是否新增成功
     */
    Boolean insertByBo(MesTeamsBo bo);

    /**
     * 修改班组管理
     *
     * @param bo 班组管理
     * @return 是否修改成功
     */
    Boolean updateByBo(MesTeamsBo bo);

    /**
     * 校验并批量删除班组管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
