package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesProcess;
import com.liaoyun.mes.domain.vo.MesProcessVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ⑧工艺管理Mapper接口
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
public interface MesProcessMapper extends BaseMapperPlus<MesProcess, MesProcessVo> {
    @Select("SELECT title FROM mes_quality_main WHERE id = #{typeId}")
    String selectTypeTitleById(@Param("typeId") Long typeId);
    @Select("SELECT nick_name FROM sys_user WHERE user_id IN (${teamIds})")
    List<String> selectTitleById(@Param("teamIds") String teamIds);
}
