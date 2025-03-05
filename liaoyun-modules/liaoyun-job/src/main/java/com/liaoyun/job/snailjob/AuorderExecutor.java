package com.liaoyun.job.snailjob;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.client.model.ExecuteResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.mes.domain.MesOrder;
import com.liaoyun.mes.domain.MesSaleout;
import com.liaoyun.mes.domain.vo.MesOrderDeVo;
import com.liaoyun.mes.mapper.MesOrderDeMapper;
import com.liaoyun.mes.mapper.MesOrderMapper;
import com.liaoyun.mes.mapper.MesProductMapper;
import com.liaoyun.mes.mapper.MesSaleoutMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@JobExecutor(name = "auorder")
@RequiredArgsConstructor
public class AuorderExecutor {
    private final MesOrderMapper orderMapper;
    private final MesOrderDeMapper orderDeMapper;
    private final MesSaleoutMapper saleoutMapper;
    public ExecuteResult jobExecute(JobArgs jobArgs) {
        List<MesOrder> orders = orderMapper.selectList(null);
        for (MesOrder order : orders) {
            List<String> tearms = Arrays.asList(order.getStids().split(","));
            List<Long> longList = tearms.stream()
                .map(Long::parseLong)
                .toList();
            int allplan=tearms.size();
            int curr=0;
            int curr2=longList.size();
            int curr3=0;
            for (Long id : longList) {
                MesOrderDeVo vo=orderDeMapper.selectVoById(id);
                if(StringUtils.equals(vo.getStatus(),"1")){
                    curr++;
                }
                //出库是否否完成
                if(vo.getCkcounts().intValue()>=vo.getCounts().intValue()){
                    curr3++;
                }
            }
            if(curr==allplan){
                order.setStatus("1");
                orderMapper.updateById(order);
            }
            if(curr2==curr3){
                order.setTsta(3);
                orderMapper.updateById(order);
            }

        }
        return ExecuteResult.success("MRP计算完成");
    }
}
