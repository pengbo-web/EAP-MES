package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesPrimarkVo;
import com.liaoyun.mes.domain.bo.MesPrimarkBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 打单列表Service接口
 *
 * @author Liao Liao
 * @date 2024-12-16
 */
public interface IMesPrimarkService {

    /**
     * 查询打单列表
     *
     * @param id 主键
     * @return 打单列表
     */
    MesPrimarkVo queryById(Long id);

    /**
     * 分页查询打单列表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 打单列表分页列表
     */
    TableDataInfo<MesPrimarkVo> queryPageList(MesPrimarkBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的打单列表列表
     *
     * @param bo 查询条件
     * @return 打单列表列表
     */
    List<MesPrimarkVo> queryList(MesPrimarkBo bo);

    /**
     * 新增打单列表
     *
     * @param bo 打单列表
     * @return 是否新增成功
     */
    Long insertByBo(MesPrimarkBo bo);

    /**
     * 修改打单列表
     *
     * @param bo 打单列表
     * @return 是否修改成功
     */
    Boolean updateByBo(MesPrimarkBo bo);

    /**
     * 校验并批量删除打单列表信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
