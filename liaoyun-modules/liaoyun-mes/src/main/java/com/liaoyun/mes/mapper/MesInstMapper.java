package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesInst;
import com.liaoyun.mes.domain.vo.MesInstVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 其他入库Mapper接口
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
public interface MesInstMapper extends BaseMapperPlus<MesInst, MesInstVo> {
    @Select("SELECT stockid FROM mes_inrec WHERE id IN (${teamIds})")
    List<Long> selectTeamTitleById(@Param("teamIds") String teamIds);
}
