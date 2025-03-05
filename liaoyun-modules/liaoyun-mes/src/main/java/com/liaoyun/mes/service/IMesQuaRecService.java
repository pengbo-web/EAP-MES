package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesQuaRecVo;
import com.liaoyun.mes.domain.bo.MesQuaRecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 质检列表Service接口
 *
 * @author Liao Liao
 * @date 2024-07-24
 */
public interface IMesQuaRecService {

    /**
     * 查询质检列表
     *
     * @param id 主键
     * @return 质检列表
     */
    MesQuaRecVo queryById(Long id);

    /**
     * 分页查询质检列表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 质检列表分页列表
     */
    TableDataInfo<MesQuaRecVo> queryPageList(MesQuaRecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的质检列表列表
     *
     * @param bo 查询条件
     * @return 质检列表列表
     */
    List<MesQuaRecVo> queryList(MesQuaRecBo bo);

    /**
     * 新增质检列表
     *
     * @param bo 质检列表
     * @return 是否新增成功
     */
    Boolean insertByBo(MesQuaRecBo bo);

    /**
     * 修改质检列表
     *
     * @param bo 质检列表
     * @return 是否修改成功
     */
    Boolean updateByBo(MesQuaRecBo bo);

    /**
     * 校验并批量删除质检列表信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
