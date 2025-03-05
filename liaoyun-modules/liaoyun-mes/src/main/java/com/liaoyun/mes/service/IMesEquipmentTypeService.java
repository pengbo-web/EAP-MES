package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesEquipmentTypeVo;
import com.liaoyun.mes.domain.bo.MesEquipmentTypeBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * ⑤设备类别Service接口
 *
 * @author Kylin
 * @date 2024-06-03
 */
public interface IMesEquipmentTypeService {

    /**
     * 查询⑤设备类别
     *
     * @param id 主键
     * @return ⑤设备类别
     */
    MesEquipmentTypeVo queryById(Long id);

    /**
     * 分页查询⑤设备类别列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ⑤设备类别分页列表
     */
    TableDataInfo<MesEquipmentTypeVo> queryPageList(MesEquipmentTypeBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的⑤设备类别列表
     *
     * @param bo 查询条件
     * @return ⑤设备类别列表
     */
    List<MesEquipmentTypeVo> queryList(MesEquipmentTypeBo bo);

    /**
     * 新增⑤设备类别
     *
     * @param bo ⑤设备类别
     * @return 是否新增成功
     */
    Boolean insertByBo(MesEquipmentTypeBo bo);

    /**
     * 修改⑤设备类别
     *
     * @param bo ⑤设备类别
     * @return 是否修改成功
     */
    Boolean updateByBo(MesEquipmentTypeBo bo);

    /**
     * 校验并批量删除⑤设备类别信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
