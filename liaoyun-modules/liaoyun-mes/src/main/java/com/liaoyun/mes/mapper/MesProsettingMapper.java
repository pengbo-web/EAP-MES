package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesProsetting;
import com.liaoyun.mes.domain.vo.MesProsettingVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 排程设置Mapper接口
 *
 * @author Liao Liao
 * @date 2024-06-09
 */
public interface MesProsettingMapper extends BaseMapperPlus<MesProsetting, MesProsettingVo> {
    @Select("SELECT title FROM mes_workplaces WHERE id = #{workshopId}")
    String selectwpTitleById(@Param("workshopId") Long workshopId);
    @Select("SELECT title FROM mes_process WHERE id = #{typeId}")
    String selectpreTitleById(@Param("typeId") Long typeId);

}
