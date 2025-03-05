package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesToolUse;
import com.liaoyun.mes.domain.vo.MesToolUseVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 夹具使用Mapper接口
 *
 * @author Liao Liao
 * @date 2024-10-11
 */
public interface MesToolUseMapper extends BaseMapperPlus<MesToolUse, MesToolUseVo> {

    @Delete("DELETE FROM mes_tool_use WHERE work_id IS null")
    int deleteall();
    @Select("SELECT nick_name FROM sys_user WHERE user_id = #{typeId}")
    String getusername(@Param("typeId") Long typeId);
}
