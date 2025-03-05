package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesProcessVo;
import com.liaoyun.mes.domain.bo.MesProcessBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * ⑧工艺管理Service接口
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
public interface IMesProcessService {

    /**
     * 查询⑧工艺管理
     *
     * @param id 主键
     * @return ⑧工艺管理
     */
    MesProcessVo queryById(Long id);

    /**
     * 分页查询⑧工艺管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ⑧工艺管理分页列表
     */
    TableDataInfo<MesProcessVo> queryPageList(MesProcessBo bo, PageQuery pageQuery);


    /**
     * 分页查询⑧工艺管理列表质检合并
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ⑧工艺管理分页列表
     */
    TableDataInfo<MesProcessVo> queryPageListby(MesProcessBo bo, PageQuery pageQuery);


    List<MesProcessVo> queryListBystring(List<Long> bo);
    /**
     * 查询符合条件的⑧工艺管理列表
     *
     * @param bo 查询条件
     * @return ⑧工艺管理列表
     */
    List<MesProcessVo> queryList(MesProcessBo bo);

    /**
     * 新增⑧工艺管理
     *
     * @param bo ⑧工艺管理
     * @return 是否新增成功
     */
    Boolean insertByBo(MesProcessBo bo);

    /**
     * 修改⑧工艺管理
     *
     * @param bo ⑧工艺管理
     * @return 是否修改成功
     */
    Boolean updateByBo(MesProcessBo bo);

    /**
     * 校验并批量删除⑧工艺管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
