package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOutsourcingVo;
import com.liaoyun.mes.domain.bo.MesOutsourcingBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 外协列Service接口
 *
 * @author Liao Liao
 * @date 2024-07-16
 */
public interface IMesOutsourcingService {

    /**
     * 查询外协列
     *
     * @param id 主键
     * @return 外协列
     */
    MesOutsourcingVo queryById(Long id);

    /**
     * 分页查询外协列列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 外协列分页列表
     */
    TableDataInfo<MesOutsourcingVo> queryPageList(MesOutsourcingBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的外协列列表
     *
     * @param bo 查询条件
     * @return 外协列列表
     */
    List<MesOutsourcingVo> queryList(MesOutsourcingBo bo);

    /**
     * 新增外协列
     *
     * @param bo 外协列
     * @return 是否新增成功
     */
    Boolean insertByBo(MesOutsourcingBo bo);

    /**
     * 修改外协列
     *
     * @param bo 外协列
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOutsourcingBo bo);

    /**
     * 校验并批量删除外协列信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
