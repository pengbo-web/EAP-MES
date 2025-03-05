package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesProsettingzp;
import com.liaoyun.mes.domain.vo.MesProsettingzpVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 组装排程Mapper接口
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
public interface MesProsettingzpMapper extends BaseMapperPlus<MesProsettingzp, MesProsettingzpVo> {
    @Select("select dept_name from sys_dept where dept_id=#{typeId}")
    String selectTypeTitleById(@Param("typeId") Long typeId);
}
