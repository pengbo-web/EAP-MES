package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesMobileRole;
import com.liaoyun.mes.domain.vo.MesMobileRoleVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 移动端权限Mapper接口
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
public interface MesMobileRoleMapper extends BaseMapperPlus<MesMobileRole, MesMobileRoleVo> {
    @Select("SELECT user_name FROM sys_user WHERE user_id IN (${teamIds})")
    List<String> selectTitleById(@Param("teamIds") String teamIds);

    @Select("SELECT title FROM mes_m_u_r WHERE id = (${teamIds})")
    String selectTitlenvById(@Param("teamIds") String teamIds);

    @Select("SELECT * FROM mes_mobile_role where user_id like '%${id}%'")
    List<MesMobileRole> selectByUserIdt(@Param("id") String id);
}
