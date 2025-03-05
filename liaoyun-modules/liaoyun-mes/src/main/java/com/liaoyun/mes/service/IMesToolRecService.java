package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesToolRecVo;
import com.liaoyun.mes.domain.bo.MesToolRecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 夹具记录Service接口
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
public interface IMesToolRecService {

    /**
     * 查询夹具记录
     *
     * @param id 主键
     * @return 夹具记录
     */
    MesToolRecVo queryById(Long id);

    /**
     * 分页查询夹具记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 夹具记录分页列表
     */
    TableDataInfo<MesToolRecVo> queryPageList(MesToolRecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的夹具记录列表
     *
     * @param bo 查询条件
     * @return 夹具记录列表
     */
    List<MesToolRecVo> queryList(MesToolRecBo bo);

    /**
     * 新增夹具记录
     *
     * @param bo 夹具记录
     * @return 是否新增成功
     */
    Boolean insertByBo(MesToolRecBo bo);

    /**
     * 修改夹具记录
     *
     * @param bo 夹具记录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesToolRecBo bo);

    /**
     * 校验并批量删除夹具记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
