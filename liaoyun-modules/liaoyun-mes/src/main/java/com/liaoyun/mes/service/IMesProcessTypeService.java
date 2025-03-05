package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesProcessTypeVo;
import com.liaoyun.mes.domain.bo.MesProcessTypeBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * ⑦工艺类别Service接口
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
public interface IMesProcessTypeService {

    /**
     * 查询⑦工艺类别
     *
     * @param id 主键
     * @return ⑦工艺类别
     */
    MesProcessTypeVo queryById(Long id);

    /**
     * 分页查询⑦工艺类别列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ⑦工艺类别分页列表
     */
    TableDataInfo<MesProcessTypeVo> queryPageList(MesProcessTypeBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的⑦工艺类别列表
     *
     * @param bo 查询条件
     * @return ⑦工艺类别列表
     */
    List<MesProcessTypeVo> queryList(MesProcessTypeBo bo);

    /**
     * 新增⑦工艺类别
     *
     * @param bo ⑦工艺类别
     * @return 是否新增成功
     */
    Boolean insertByBo(MesProcessTypeBo bo);

    /**
     * 修改⑦工艺类别
     *
     * @param bo ⑦工艺类别
     * @return 是否修改成功
     */
    Boolean updateByBo(MesProcessTypeBo bo);

    /**
     * 校验并批量删除⑦工艺类别信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
