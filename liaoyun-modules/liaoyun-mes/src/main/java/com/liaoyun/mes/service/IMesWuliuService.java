package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesWuliuVo;
import com.liaoyun.mes.domain.bo.MesWuliuBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 物流信息Service接口
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
public interface IMesWuliuService {

    /**
     * 查询物流信息
     *
     * @param id 主键
     * @return 物流信息
     */
    MesWuliuVo queryById(Long id);

    /**
     * 分页查询物流信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 物流信息分页列表
     */
    TableDataInfo<MesWuliuVo> queryPageList(MesWuliuBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的物流信息列表
     *
     * @param bo 查询条件
     * @return 物流信息列表
     */
    List<MesWuliuVo> queryList(MesWuliuBo bo);

    /**
     * 新增物流信息
     *
     * @param bo 物流信息
     * @return 是否新增成功
     */
    Boolean insertByBo(MesWuliuBo bo);

    /**
     * 修改物流信息
     *
     * @param bo 物流信息
     * @return 是否修改成功
     */
    Boolean updateByBo(MesWuliuBo bo);

    /**
     * 校验并批量删除物流信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
