package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesEquRec;
import com.liaoyun.mes.domain.vo.MesEquRecVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设备点检记录Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
public interface MesEquRecMapper extends BaseMapperPlus<MesEquRec, MesEquRecVo> {
    @Select("SELECT title FROM mes_equipment WHERE id = (${equIds})")
    String selectequmian(@Param("equIds") Long equIds);

    @Select("SELECT title FROM mes_equ_check WHERE id = (${equIds})")
    String selecttilte(@Param("equIds") Long equIds);
    @Select("SELECT nick_name FROM sys_user WHERE user_id = #{typeId}")
    String getusername(@Param("typeId") Long typeId);
}
