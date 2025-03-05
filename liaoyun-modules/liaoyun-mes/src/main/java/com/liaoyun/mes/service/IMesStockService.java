package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.domain.bo.MesStockBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 仓库信息Service接口
 *
 * @author Liao Liao
 * @date 2024-11-10
 */
public interface IMesStockService {

    /**
     * 查询仓库信息
     *
     * @param id 主键
     * @return 仓库信息
     */
    MesStockVo queryById(Long id);



    List<Map<String, Object>> queryMap(Long id);

    /**
     * 分页查询仓库信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 仓库信息分页列表
     */
    TableDataInfo<MesStockVo> queryPageList(MesStockBo bo, PageQuery pageQuery);
    public List<MesStockVo> Listw(MesStockBo bo);


    /**
     * 入库信息（一废除）
     * @param bo
     * @return
     */
    List<MesStockVo> rukulist(Long bo);

    /**
     * 出库信息（已废除）
     * @param bo
     * @return
     */
    List<MesStockVo> chukulist(Long bo);

    /**
     * 查询符合条件的仓库信息列表
     *
     * @param bo 查询条件
     * @return 仓库信息列表
     */
    List<MesStockVo> queryList(MesStockBo bo);

    /**
     * 新增仓库信息
     *
     * @param bo 仓库信息
     * @return 是否新增成功
     */
    Boolean insertByBo(MesStockBo bo);

    /**
     * 修改仓库信息
     *
     * @param bo 仓库信息
     * @return 是否修改成功
     */
    Boolean updateByBo(MesStockBo bo);

    /**
     * 校验并批量删除仓库信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
