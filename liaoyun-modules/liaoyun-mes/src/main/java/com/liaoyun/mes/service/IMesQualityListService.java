package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesQualityListVo;
import com.liaoyun.mes.domain.bo.MesQualityListBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 质检内容Service接口
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
public interface IMesQualityListService {

    /**
     * 查询质检内容
     *
     * @param id 主键
     * @return 质检内容
     */
    MesQualityListVo queryById(Long id);

    /**
     * 分页查询质检内容列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 质检内容分页列表
     */
    TableDataInfo<MesQualityListVo> queryPageList(MesQualityListBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的质检内容列表
     *
     * @param bo 查询条件
     * @return 质检内容列表
     */
    List<MesQualityListVo> queryList(MesQualityListBo bo);

    /**
     * 新增质检内容
     *
     * @param bo 质检内容
     * @return 是否新增成功
     */
    Boolean insertByBo(MesQualityListBo bo);

    /**
     * 修改质检内容
     *
     * @param bo 质检内容
     * @return 是否修改成功
     */
    Boolean updateByBo(MesQualityListBo bo);

    /**
     * 校验并批量删除质检内容信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
