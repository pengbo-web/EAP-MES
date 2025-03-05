package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesStocktakingVo;
import com.liaoyun.mes.domain.bo.MesStocktakingBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 库位盘点Service接口
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
public interface IMesStocktakingService {

    /**
     * 查询库位盘点
     *
     * @param id 主键
     * @return 库位盘点
     */
    MesStocktakingVo queryById(Long id);

    /**
     * 分页查询库位盘点列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 库位盘点分页列表
     */
    TableDataInfo<MesStocktakingVo> queryPageList(MesStocktakingBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的库位盘点列表
     *
     * @param bo 查询条件
     * @return 库位盘点列表
     */
    List<MesStocktakingVo> queryList(MesStocktakingBo bo);

    /**
     * 新增库位盘点
     *
     * @param bo 库位盘点
     * @return 是否新增成功
     */
    Boolean insertByBo(MesStocktakingBo bo);

    /**
     * 修改库位盘点
     *
     * @param bo 库位盘点
     * @return 是否修改成功
     */
    Boolean updateByBo(MesStocktakingBo bo);

    /**
     * 校验并批量删除库位盘点信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
