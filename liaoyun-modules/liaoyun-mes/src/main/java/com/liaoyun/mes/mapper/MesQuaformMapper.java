package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesQuaform;
import com.liaoyun.mes.domain.vo.MesQuaformVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 来料检Mapper接口
 *
 * @author Liao Liao
 * @date 2024-10-12
 */
public interface MesQuaformMapper extends BaseMapperPlus<MesQuaform, MesQuaformVo> {
    @Select("SELECT nick_name FROM sys_user WHERE user_id = #{typeId}")
    String getusername(@Param("typeId") Long typeId);
}
