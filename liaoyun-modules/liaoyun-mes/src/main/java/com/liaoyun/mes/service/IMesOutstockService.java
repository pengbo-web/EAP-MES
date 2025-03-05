package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOutstockVo;
import com.liaoyun.mes.domain.bo.MesOutstockBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;

import java.util.Collection;
import java.util.List;

/**
 * 出库管理Service接口
 *
 * @author Liao Liao
 * @date 2024-07-18
 */
public interface IMesOutstockService {

    /**
     * 查询出库管理
     *
     * @param id 主键
     * @return 出库管理
     */
    MesOutstockVo queryById(Long id);

    /**
     * 分页查询出库管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 出库管理分页列表
     */
    TableDataInfo<MesOutstockVo> queryPageList(MesOutstockBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的出库管理列表
     *
     * @param bo 查询条件
     * @return 出库管理列表
     */
    List<MesOutstockVo> queryList(MesOutstockBo bo);

    /**
     * 新增出库管理
     *
     * @param bo 出库管理
     * @return 是否新增成功
     */
    Boolean insertByBo(MesOutstockBo bo);

    /**
     * 修改出库管理
     *
     * @param bo 出库管理
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOutstockBo bo);

    /**
     * 校验并批量删除出库管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<MesUnitmeasureVo> getUnitList(MesOutstockBo bo);
}
