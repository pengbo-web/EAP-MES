package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesInrecVo;
import com.liaoyun.mes.domain.bo.MesInrecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 入库记录Service接口
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
public interface IMesInrecService {

    /**
     * 查询入库记录
     *
     * @param id 主键
     * @return 入库记录
     */
    MesInrecVo queryById(Long id);

    /**
     * 分页查询入库记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 入库记录分页列表
     */
    TableDataInfo<MesInrecVo> queryPageList(MesInrecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的入库记录列表
     *
     * @param bo 查询条件
     * @return 入库记录列表
     */
    List<MesInrecVo> queryList(MesInrecBo bo);

    /**
     * 新增入库记录
     *
     * @param bo 入库记录
     * @return 是否新增成功
     */
    Long insertByBo(MesInrecBo bo);

    /**
     * 修改入库记录
     *
     * @param bo 入库记录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesInrecBo bo);

    /**
     * 校验并批量删除入库记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
