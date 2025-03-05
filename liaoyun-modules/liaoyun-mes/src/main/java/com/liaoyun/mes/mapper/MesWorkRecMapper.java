package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesWorkRec;
import com.liaoyun.mes.domain.vo.MesWorkRecVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 工作记录Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-09
 */
public interface MesWorkRecMapper extends BaseMapperPlus<MesWorkRec, MesWorkRecVo> {
    @Select("SELECT nick_name FROM sys_user WHERE user_id = #{typeId}")
    String selecttteById(@Param("typeId") Long typeId);
    @Select("SELECT title FROM mes_product WHERE id = #{typeId}")
    String selecproname(@Param("typeId") Long typeId);
}
