package com.liaoyun.system.service;

import com.liaoyun.system.domain.vo.MesInventoryLedgerVo;
import com.liaoyun.system.domain.bo.MesInventoryLedgerBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 库存流水
Service接口
 *
 * @author Liao Liao
 * @date 2025-02-28
 */
public interface IMesInventoryLedgerService {

    /**
     * 查询库存流水

     *
     * @param sn 主键
     * @return 库存流水

     */
    MesInventoryLedgerVo queryById(Long sn);

    /**
     * 分页查询库存流水
列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 库存流水
分页列表
     */
    TableDataInfo<MesInventoryLedgerVo> queryPageList(MesInventoryLedgerBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的库存流水
列表
     *
     * @param bo 查询条件
     * @return 库存流水
列表
     */
    List<MesInventoryLedgerVo> queryList(MesInventoryLedgerBo bo);

    /**
     * 新增库存流水

     *
     * @param bo 库存流水

     * @return 是否新增成功
     */

    Boolean insertByBo(MesInventoryLedgerBo bo);
    Boolean outByBo(MesInventoryLedgerBo bo);
    /**
     * 修改库存流水

     *
     * @param bo 库存流水

     * @return 是否修改成功
     */
    Boolean updateByBo(MesInventoryLedgerBo bo);

    /**
     * 校验并批量删除库存流水
信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
