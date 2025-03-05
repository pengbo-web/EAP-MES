package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesOutRec;
import com.liaoyun.mes.domain.vo.MesOutRecVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 外协记录Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-26
 */
public interface MesOutRecMapper extends BaseMapperPlus<MesOutRec, MesOutRecVo> {
    @Select("SELECT title FROM mes_product WHERE id = #{typeId}")
    String selectProTitleById(@Param("typeId") Long typeId);
    @Select("SELECT title FROM mes_outsourcing WHERE id = #{typeId}")
    String selectOutnameById(@Param("typeId") Long typeId);
}
