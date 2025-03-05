package com.liaoyun.system.mapper;

import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.system.domain.vo.MesInventoryLedgerVo;
import com.liaoyun.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.type.JdbcType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 库存流水
Mapper接口
 *
 * @author Liao Liao
 * @date 2025-02-28
 */
public interface MesInventoryLedgerMapper extends BaseMapperPlus<MesInventoryLedger, MesInventoryLedgerVo> {


    @Options(statementType = StatementType.CALLABLE)
    void callInOutProcedure(Map<String,Object> map);

    Integer getStockByProductId(@Param("productId") Long productId);
}
