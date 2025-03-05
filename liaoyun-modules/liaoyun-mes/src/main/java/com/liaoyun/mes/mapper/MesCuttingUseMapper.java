package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesCuttingUse;
import com.liaoyun.mes.domain.vo.MesCuttingUseVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 刀具使用统计Mapper接口
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
public interface MesCuttingUseMapper extends BaseMapperPlus<MesCuttingUse, MesCuttingUseVo> {
    @Select("SELECT title FROM mes_cutting WHERE id = #{typeId}")
    String selectmainTitleById(@Param("typeId") Long typeId);
    @Select("SELECT nick_name FROM sys_user WHERE user_id = #{typeId}")
    String getusername(@Param("typeId") Long typeId);
}
