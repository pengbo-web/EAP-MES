package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesProsettingzpVo;
import com.liaoyun.mes.domain.bo.MesProsettingzpBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 组装排程Service接口
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
public interface IMesProsettingzpService {

    /**
     * 查询组装排程
     *
     * @param id 主键
     * @return 组装排程
     */
    MesProsettingzpVo queryById(Long id);

    /**
     * 分页查询组装排程列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 组装排程分页列表
     */
    TableDataInfo<MesProsettingzpVo> queryPageList(MesProsettingzpBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的组装排程列表
     *
     * @param bo 查询条件
     * @return 组装排程列表
     */
    List<MesProsettingzpVo> queryList(MesProsettingzpBo bo);

    /**
     * 新增组装排程
     *
     * @param bo 组装排程
     * @return 是否新增成功
     */
    Boolean insertByBo(MesProsettingzpBo bo);

    /**
     * 修改组装排程
     *
     * @param bo 组装排程
     * @return 是否修改成功
     */
    Boolean updateByBo(MesProsettingzpBo bo);

    /**
     * 校验并批量删除组装排程信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
