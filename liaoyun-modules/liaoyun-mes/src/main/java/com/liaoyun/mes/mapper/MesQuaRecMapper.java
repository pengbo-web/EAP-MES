package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesQuaRec;
import com.liaoyun.mes.domain.vo.MesQuaRecVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 质检列表Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-24
 */
public interface MesQuaRecMapper extends BaseMapperPlus<MesQuaRec, MesQuaRecVo> {
    @Select("SELECT title FROM mes_quality_main WHERE id = (${equIds})")
    String selectEquTitleById(@Param("equIds") Long equIds);
}
