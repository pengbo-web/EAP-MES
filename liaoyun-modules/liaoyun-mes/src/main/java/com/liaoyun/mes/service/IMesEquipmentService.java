package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesEquipmentVo;
import com.liaoyun.mes.domain.bo.MesEquipmentBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 设备列表Service接口
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
public interface IMesEquipmentService {

    /**
     * 查询设备列表
     *
     * @param id 主键
     * @return 设备列表
     */
    MesEquipmentVo queryById(Long id);

    /**
     * 分页查询设备列表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 设备列表分页列表
     */
    TableDataInfo<MesEquipmentVo> queryPageList(MesEquipmentBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的设备列表列表
     *
     * @param bo 查询条件
     * @return 设备列表列表
     */
    List<MesEquipmentVo> queryList(MesEquipmentBo bo);

    /**
     * 新增设备列表
     *
     * @param bo 设备列表
     * @return 是否新增成功
     */
    Boolean insertByBo(MesEquipmentBo bo);

    /**
     * 修改设备列表
     *
     * @param bo 设备列表
     * @return 是否修改成功
     */
    Boolean updateByBo(MesEquipmentBo bo);

    /**
     * 校验并批量删除设备列表信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
