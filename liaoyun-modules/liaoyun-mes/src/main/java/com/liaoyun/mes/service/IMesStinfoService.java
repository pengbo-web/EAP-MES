package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesStinfoVo;
import com.liaoyun.mes.domain.bo.MesStinfoBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 库位调拨Service接口
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
public interface IMesStinfoService {

    /**
     * 查询库位调拨
     *
     * @param id 主键
     * @return 库位调拨
     */
    MesStinfoVo queryById(Long id);

    /**
     * 分页查询库位调拨列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 库位调拨分页列表
     */
    TableDataInfo<MesStinfoVo> queryPageList(MesStinfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的库位调拨列表
     *
     * @param bo 查询条件
     * @return 库位调拨列表
     */
    List<MesStinfoVo> queryList(MesStinfoBo bo);

    /**
     * 新增库位调拨
     *
     * @param bo 库位调拨
     * @return 是否新增成功
     */
    Boolean insertByBo(MesStinfoBo bo);

    /**
     * 修改库位调拨
     *
     * @param bo 库位调拨
     * @return 是否修改成功
     */
    Boolean updateByBo(MesStinfoBo bo);

    /**
     * 校验并批量删除库位调拨信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
