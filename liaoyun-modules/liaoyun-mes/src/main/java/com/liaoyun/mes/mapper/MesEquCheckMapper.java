package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesEquCheck;
import com.liaoyun.mes.domain.vo.MesEquCheckVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设备点检Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
public interface MesEquCheckMapper extends BaseMapperPlus<MesEquCheck, MesEquCheckVo> {
    @Select("SELECT title FROM mes_equipment WHERE id IN (${equIds})")
    List<String> selectEquTitleById(@Param("equIds") String equIds);
}
