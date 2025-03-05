package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOustVo;
import com.liaoyun.mes.domain.bo.MesOustBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 其他出库Service接口
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
public interface IMesOustService {

    /**
     * 查询其他出库
     *
     * @param id 主键
     * @return 其他出库
     */
    MesOustVo queryById(Long id);

    /**
     * 分页查询其他出库列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 其他出库分页列表
     */
    TableDataInfo<MesOustVo> queryPageList(MesOustBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的其他出库列表
     *
     * @param bo 查询条件
     * @return 其他出库列表
     */
    List<MesOustVo> queryList(MesOustBo bo);

    /**
     * 新增其他出库
     *
     * @param bo 其他出库
     * @return 是否新增成功
     */
    Boolean insertByBo(MesOustBo bo);

    /**
     * 修改其他出库
     *
     * @param bo 其他出库
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOustBo bo);

    /**
     * 校验并批量删除其他出库信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
