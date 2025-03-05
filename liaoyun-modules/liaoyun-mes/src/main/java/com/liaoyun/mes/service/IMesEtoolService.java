package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesEtoolVo;
import com.liaoyun.mes.domain.bo.MesEtoolBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 工装夹具Service接口
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
public interface IMesEtoolService {

    /**
     * 查询工装夹具
     *
     * @param id 主键
     * @return 工装夹具
     */
    MesEtoolVo queryById(Long id);

    /**
     * 分页查询工装夹具列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 工装夹具分页列表
     */
    TableDataInfo<MesEtoolVo> queryPageList(MesEtoolBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的工装夹具列表
     *
     * @param bo 查询条件
     * @return 工装夹具列表
     */
    List<MesEtoolVo> queryList(MesEtoolBo bo);

    /**
     * 新增工装夹具
     *
     * @param bo 工装夹具
     * @return 是否新增成功
     */
    Boolean insertByBo(MesEtoolBo bo);

    /**
     * 修改工装夹具
     *
     * @param bo 工装夹具
     * @return 是否修改成功
     */
    Boolean updateByBo(MesEtoolBo bo);

    /**
     * 校验并批量删除工装夹具信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
