package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesProsettingVo;
import com.liaoyun.mes.domain.bo.MesProsettingBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 排程设置Service接口
 *
 * @author Liao Liao
 * @date 2024-06-09
 */
public interface IMesProsettingService {

    /**
     * 查询排程设置
     *
     * @param id 主键
     * @return 排程设置
     */
    MesProsettingVo queryById(Long id);

    /**
     * 分页查询排程设置列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 排程设置分页列表
     */
    TableDataInfo<MesProsettingVo> queryPageList(MesProsettingBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的排程设置列表
     *
     * @param bo 查询条件
     * @return 排程设置列表
     */
    List<MesProsettingVo> queryList(MesProsettingBo bo);

    /**
     * 新增排程设置
     *
     * @param bo 排程设置
     * @return 是否新增成功
     */
    Boolean insertByBo(MesProsettingBo bo);
    Boolean insertByBos(MesProsettingBo bo);
    /**
     * 修改排程设置
     *
     * @param bo 排程设置
     * @return 是否修改成功
     */
    Boolean updateByBo(MesProsettingBo bo);

    /**
     * 校验并批量删除排程设置信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
