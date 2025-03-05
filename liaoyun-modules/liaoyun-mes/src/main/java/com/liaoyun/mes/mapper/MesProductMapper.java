package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.vo.MesProductVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * ③产品管理Mapper接口
 *
 * @author Kylin W
 * @date 2024-05-11
 */
public interface MesProductMapper extends BaseMapperPlus<MesProduct, MesProductVo> {

    @Select("SELECT title FROM mes_unitmeasure WHERE id = #{typeId}")
    String selectTypeTitleById(@Param("typeId") Long typeId);

}
