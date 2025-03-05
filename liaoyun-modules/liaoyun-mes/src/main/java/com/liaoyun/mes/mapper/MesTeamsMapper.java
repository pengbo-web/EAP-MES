package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesTeams;
import com.liaoyun.mes.domain.vo.MesTeamsVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 班组管理Mapper接口
 *
 * @author Liao Liao
 * @date 2024-10-25
 */
public interface MesTeamsMapper extends BaseMapperPlus<MesTeams, MesTeamsVo> {
    @Select("SELECT nick_name FROM sys_user WHERE user_id IN (${teamIds})")
    List<String> selectTitleById(@Param("teamIds") String teamIds);
}
