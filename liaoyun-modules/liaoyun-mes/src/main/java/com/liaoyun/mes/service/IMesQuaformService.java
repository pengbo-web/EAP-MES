package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesQuaformVo;
import com.liaoyun.mes.domain.bo.MesQuaformBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 来料检Service接口
 *
 * @author Liao Liao
 * @date 2024-10-12
 */
public interface IMesQuaformService {

    /**
     * 查询来料检
     *
     * @param id 主键
     * @return 来料检
     */
    MesQuaformVo queryById(Long id);

    /**
     * 分页查询来料检列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 来料检分页列表
     */
    TableDataInfo<MesQuaformVo> queryPageList(MesQuaformBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的来料检列表
     *
     * @param bo 查询条件
     * @return 来料检列表
     */
    List<MesQuaformVo> queryList(MesQuaformBo bo);

    /**
     * 新增来料检
     *
     * @param bo 来料检
     * @return 是否新增成功
     */
    Boolean insertByBo(MesQuaformBo bo);

    /**
     * 修改来料检
     *
     * @param bo 来料检
     * @return 是否修改成功
     */
    Boolean updateByBo(MesQuaformBo bo);

    /**
     * 校验并批量删除来料检信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
