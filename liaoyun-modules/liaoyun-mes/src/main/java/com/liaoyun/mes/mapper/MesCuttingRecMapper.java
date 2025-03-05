package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesCuttingRec;
import com.liaoyun.mes.domain.vo.MesCuttingRecVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 刀具记录审核Mapper接口
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
public interface MesCuttingRecMapper extends BaseMapperPlus<MesCuttingRec, MesCuttingRecVo> {
    @Select("SELECT title FROM mes_cutting WHERE id = #{typeId}")
    String selectmainTitleById(@Param("typeId") Long typeId);
    @Select("SELECT nick_name FROM sys_user WHERE user_id = #{typeId}")
    String getusername(@Param("typeId") Long typeId);

}
