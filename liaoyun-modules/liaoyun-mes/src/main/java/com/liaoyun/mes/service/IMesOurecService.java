package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOurecVo;
import com.liaoyun.mes.domain.bo.MesOurecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 出库记录Service接口
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
public interface IMesOurecService {

    /**
     * 查询出库记录
     *
     * @param id 主键
     * @return 出库记录
     */
    MesOurecVo queryById(Long id);

    /**
     * 分页查询出库记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 出库记录分页列表
     */
    TableDataInfo<MesOurecVo> queryPageList(MesOurecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的出库记录列表
     *
     * @param bo 查询条件
     * @return 出库记录列表
     */
    List<MesOurecVo> queryList(MesOurecBo bo);

    /**
     * 新增出库记录
     *
     * @param bo 出库记录
     * @return 是否新增成功
     */
    Boolean insertByBo(MesOurecBo bo);

    /**
     * 修改出库记录
     *
     * @param bo 出库记录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOurecBo bo);

    /**
     * 校验并批量删除出库记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
