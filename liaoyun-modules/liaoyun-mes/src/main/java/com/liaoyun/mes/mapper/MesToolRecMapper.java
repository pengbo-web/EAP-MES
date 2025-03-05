package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesToolRec;
import com.liaoyun.mes.domain.vo.MesToolRecVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 夹具记录Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
public interface MesToolRecMapper extends BaseMapperPlus<MesToolRec, MesToolRecVo> {
    @Select("SELECT title FROM mes_etool WHERE id = #{typeId}")
    String selectmainTitleById(@Param("typeId") Long typeId);
    @Select("SELECT nick_name FROM sys_user WHERE user_id = #{typeId}")
    String getusername(@Param("typeId") Long typeId);
}
