package com.liaoyun. mes.mapper;

import com.liaoyun. mes.domain.MesInstock;
import com.liaoyun. mes.domain.vo.MesInstockVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 入库信息Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
public interface MesInstockMapper extends BaseMapperPlus<MesInstock, MesInstockVo> {
    @Select("SELECT tilte FROM mes_stock WHERE id = #{proId}")
    String selectproTitleById(@Param("proId") Long proId);

    @Select("SELECT * FROM mes_instock where pro_id = #{proId} ORDER BY create_time DESC LIMIT 1")
    MesInstock selectLatest(@Param("proId") Long proId);
}
