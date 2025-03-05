package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesEquRecVo;
import com.liaoyun.mes.domain.bo.MesEquRecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 设备点检记录Service接口
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
public interface IMesEquRecService {

    /**
     * 查询设备点检记录
     *
     * @param id 主键
     * @return 设备点检记录
     */
    MesEquRecVo queryById(Long id);

    /**
     * 分页查询设备点检记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 设备点检记录分页列表
     */
    TableDataInfo<MesEquRecVo> queryPageList(MesEquRecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的设备点检记录列表
     *
     * @param bo 查询条件
     * @return 设备点检记录列表
     */
    List<MesEquRecVo> queryList(MesEquRecBo bo);

    /**
     * 新增设备点检记录
     *
     * @param bo 设备点检记录
     * @return 是否新增成功
     */
    Boolean insertByBo(MesEquRecBo bo);

    /**
     * 修改设备点检记录
     *
     * @param bo 设备点检记录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesEquRecBo bo);

    /**
     * 校验并批量删除设备点检记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
