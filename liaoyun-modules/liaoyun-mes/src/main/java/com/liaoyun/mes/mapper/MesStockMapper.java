package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 仓库信息Mapper接口
 *
 * @author Liao Liao
 * @date 2024-11-10
 */
public interface MesStockMapper extends BaseMapperPlus<MesStock, MesStockVo> {
    @Select("SELECT title FROM mes_custom WHERE id IN (${teamIds})")
    List<String> selectTeamTitleById(@Param("teamIds") String teamIds);

    @Select("SELECT * FROM mes_stock WHERE nowstock<=warnnum and type='${type}'")
    List<MesStockVo> selectlist(@Param("type") String type);
}
