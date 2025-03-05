package com.liaoyun.mes.mapper;

import com.liaoyun.common.mybatis.annotation.DataColumn;
import com.liaoyun.common.mybatis.annotation.DataPermission;
import com.liaoyun.mes.domain.MesTask;
import com.liaoyun.mes.domain.vo.MesTaskVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 任务流转Mapper接口
 *
 * @author Liao Liao
 * @date 2024-07-07
 */
@DataPermission({
    @DataColumn(key = "userName", value = "user_id")
})
public interface MesTaskMapper extends BaseMapperPlus<MesTask, MesTaskVo> {
    @Select("select title from mes_product where id= #{type}")
    String getproname(@Param("type") Long type);
}
