package com.liaoyun.mes.mapper;

import com.liaoyun.common.mybatis.annotation.DataColumn;
import com.liaoyun.common.mybatis.annotation.DataPermission;
import com.liaoyun.mes.domain.MesTaskc;
import com.liaoyun.mes.domain.vo.MesTaskcVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 组装流转Mapper接口
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@DataPermission({
    @DataColumn(key = "userName", value = "user_id")
})
public interface MesTaskcMapper extends BaseMapperPlus<MesTaskc, MesTaskcVo> {

    @Select("select title from mes_product where id = #{typeId}")
    String gettitle(@Param("typeId") Long typeId);
    @Select("SELECT user_name FROM sys_user WHERE user_id = #{typeId}")
    String getusername(@Param("typeId") Long typeId);
    @Select("SELECT dept_name FROM sys_dept WHERE dept_id = #{typeId}")
    String getdeptname(@Param("typeId") Long typeId);
}
