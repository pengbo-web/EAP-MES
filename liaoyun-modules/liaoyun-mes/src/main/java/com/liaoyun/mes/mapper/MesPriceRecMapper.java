package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesPriceRec;
import com.liaoyun.mes.domain.vo.MesPriceRecVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 价格波动Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
public interface MesPriceRecMapper extends BaseMapperPlus<MesPriceRec, MesPriceRecVo> {
    @Select("SELECT title FROM mes_product WHERE id = #{proId}")
    String selectproTitleById(@Param("proId") Long proId);

}
