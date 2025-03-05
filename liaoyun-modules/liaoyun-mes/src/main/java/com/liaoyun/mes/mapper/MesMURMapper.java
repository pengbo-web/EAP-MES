package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesMUR;
import com.liaoyun.mes.domain.vo.MesMURVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 移动套餐Mapper接口
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
public interface MesMURMapper extends BaseMapperPlus<MesMUR, MesMURVo> {
    @Select("SELECT title FROM mes_mobile_menu WHERE id IN (${teamIds})")
    List<String> selectTitleById(@Param("teamIds") String teamIds);
}
