package com.liaoyun.mes.mapper;

import com.liaoyun.common.mybatis.annotation.DataColumn;
import com.liaoyun.common.mybatis.annotation.DataPermission;
import com.liaoyun.mes.domain.MesWorkplaces;
import com.liaoyun.mes.domain.vo.MesWorkplacesVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 工作站Mapper接口
 *
 * @author Liao Liao
 * @date 2024-06-05
 */
public interface MesWorkplacesMapper extends BaseMapperPlus<MesWorkplaces, MesWorkplacesVo> {
    @Select("SELECT title FROM mes_equipment WHERE id IN (${equIds})")
    List<String> selectEquTitleById(@Param("equIds") String equIds);

    @Select("SELECT dept_name FROM sys_dept WHERE dept_id IN (${teamIds})")
    List<String> selectTeamTitleById(@Param("teamIds") String teamIds);


    @Select("SELECT title FROM mes_process WHERE id IN (${processIds})")
    List<String> selectProcessTitleById(@Param("processIds") String processIds);

    @Select("SELECT title FROM mes_workplace WHERE id = ${workIds}")
    String selectWorkplaceTitleById(@Param("workIds") Long workIds);

    @Select("SELECT a.title FROM mes_etool a,mes_tool_use b WHERE b.id IN (${processIds}) and b.toolid=a.id")
    List<String> selecttongsTitleById(@Param("processIds") String processIds);
}
