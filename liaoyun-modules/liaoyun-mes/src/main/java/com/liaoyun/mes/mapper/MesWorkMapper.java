package com.liaoyun.mes.mapper;

import com.liaoyun.mes.domain.MesWork;
import com.liaoyun.mes.domain.vo.MesWorkVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 生产派单Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-03
 */
public interface MesWorkMapper extends BaseMapperPlus<MesWork, MesWorkVo> {


    @Select("select sum(counts) from mes_primark where status='1' and workid= #{id} and del_flag='0'")
    Integer getincount(@Param("id") Long id);

    @Select("select sum(counts) from mes_primark where  workid= #{id} and del_flag='0'")
    Integer getpriCount(@Param("id") Long id);

    @Update("update mes_work set del_flag='2' where id=#{id} and del_flag='0'")
    Integer updateFlag(@Param("id") Long id);
}
