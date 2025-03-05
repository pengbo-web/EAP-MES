package com.liaoyun.job.snailjob;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.client.model.ExecuteResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.mes.domain.MesOrder;
import com.liaoyun.mes.domain.vo.MesOrderDeVo;
import com.liaoyun.mes.mapper.MesOrderDeMapper;
import com.liaoyun.mes.mapper.MesOrderMapper;
import com.liaoyun.mes.mapper.MesSaleoutMapper;
import com.liaoyun.mes.service.impl.MesOrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@JobExecutor(name = "warming")
@RequiredArgsConstructor
public class WarmingExecutor {
    private final MesOrderMapper orderMapper;
    private final MesOrderDeMapper orderDeMapper;
    private final MesSaleoutMapper saleoutMapper;
    private final MesOrderServiceImpl mesOrderService;
    public ExecuteResult jobExecute(JobArgs jobArgs) {
        LambdaQueryWrapper<MesOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MesOrder::getTsta,3);
        List<MesOrder> orders = orderMapper.selectList(queryWrapper);
        for (MesOrder order : orders) {

            // 定义日期时间格式化器
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String formattedDbTime = formatter1.format(order.getSubtime());
            // 将字符串解析为 LocalDateTime
            LocalDateTime dbTime = LocalDateTime.parse(formattedDbTime, formatter);

            // 假设需要减去的天数
            int daysToSubtract = order.getWarmdate();
            // 计算减去指定天数后的时间
            LocalDateTime subtractedTime = dbTime.minusDays(daysToSubtract);

            // 假设需要对比的时间
            LocalDateTime compareTime = LocalDateTime.now(); // 当前时间

            // 比较减去天数后的时间是否大于对比时间
            boolean isGreaterThanCompareTime = subtractedTime.isAfter(compareTime);
            if (isGreaterThanCompareTime) {
                order.setIstop(1);
                orderMapper.updateById(order);
                mesOrderService.bequick(order.getId());
            }
        }
        return ExecuteResult.success("加急成功");
    }
}
