package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesOutstock;
import com.liaoyun.mes.domain.vo.MesOutstockVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 出库管理Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-18
 */
public interface MesOutstockMapper extends BaseMapperPlus<MesOutstock, MesOutstockVo> {

    @Select("SELECT title FROM mes_unitmeasure WHERE id = #{typeId}")
    String selectuniTitleById(@Param("typeId") Long typeId);
    @Select("SELECT title FROM mes_stock WHERE id = #{typeId}")
    String selectproTitleById(@Param("typeId") Long typeId);
    @Select("SELECT nick_name FROM sys_user WHERE user_id = #{typeId}")
    String getusername(@Param("typeId") Long typeId);
}
