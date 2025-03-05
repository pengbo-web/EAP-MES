package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesModelrecVo;
import com.liaoyun.mes.domain.bo.MesModelrecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 模具使用记录Service接口
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
public interface IMesModelrecService {

    /**
     * 查询模具使用记录
     *
     * @param id 主键
     * @return 模具使用记录
     */
    MesModelrecVo queryById(Long id);

    /**
     * 分页查询模具使用记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 模具使用记录分页列表
     */
    TableDataInfo<MesModelrecVo> queryPageList(MesModelrecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的模具使用记录列表
     *
     * @param bo 查询条件
     * @return 模具使用记录列表
     */
    List<MesModelrecVo> queryList(MesModelrecBo bo);

    /**
     * 新增模具使用记录
     *
     * @param bo 模具使用记录
     * @return 是否新增成功
     */
    Boolean insertByBo(MesModelrecBo bo);

    /**
     * 修改模具使用记录
     *
     * @param bo 模具使用记录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesModelrecBo bo);

    /**
     * 校验并批量删除模具使用记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
