package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesEquipment;
import com.liaoyun.mes.domain.vo.MesEquipmentVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 设备列表Mapper接口
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
public interface MesEquipmentMapper extends BaseMapperPlus<MesEquipment, MesEquipmentVo> {
    @Select("SELECT title FROM mes_equipment_type WHERE id = #{typeId}")
    String selectTypeTitleById(@Param("typeId") Long typeId);
}
