package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesStockTypeVo;
import com.liaoyun.mes.domain.bo.MesStockTypeBo;

import java.util.Collection;
import java.util.List;

/**
 * 库存分类Service接口
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
public interface IMesStockTypeService {

    /**
     * 查询库存分类
     *
     * @param id 主键
     * @return 库存分类
     */
    MesStockTypeVo queryById(Long id);


    /**
     * 查询符合条件的库存分类列表
     *
     * @param bo 查询条件
     * @return 库存分类列表
     */
    List<MesStockTypeVo> queryList(MesStockTypeBo bo);

    /**
     * 新增库存分类
     *
     * @param bo 库存分类
     * @return 是否新增成功
     */
    Boolean insertByBo(MesStockTypeBo bo);

    /**
     * 修改库存分类
     *
     * @param bo 库存分类
     * @return 是否修改成功
     */
    Boolean updateByBo(MesStockTypeBo bo);

    /**
     * 校验并批量删除库存分类信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
