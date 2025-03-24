package com.liaoyun.mes.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.common.sse.core.SseEmitterManager;
import com.liaoyun.common.sse.dto.SseMessageDto;
import com.liaoyun.common.web.core.BaseController;
import com.liaoyun.common.websocket.dto.WebSocketMessageDto;
import com.liaoyun.common.websocket.holder.WebSocketSessionHolder;
import com.liaoyun.common.websocket.utils.WebSocketUtils;
import com.liaoyun.mes.domain.MesWork;
import com.liaoyun.mes.domain.bo.MesQuaRecBo;
import com.liaoyun.mes.domain.bo.MesWorkRecBo;
import com.liaoyun.mes.mapper.MesWorkMapper;
import com.liaoyun.mes.service.IMesQuaRecService;
import com.liaoyun.mes.service.IMesWorkRecService;
import com.liaoyun.mes.service.impl.MesQuaRecServiceImpl;
import com.liaoyun.mes.service.impl.MesWorkRecServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/index")

public class MesIndexController extends BaseController {
    private final IMesQuaRecService quaRecService;
    private final IMesWorkRecService workRecService;
    private final MesWorkMapper mesWorkMapper;
    private final SseEmitterManager sseEmitterManager;
    @GetMapping("/tolk")
    public R<Map<String,Integer>> tolk(){

        Map<String,Integer> map = new HashMap<>();
        SseMessageDto dto = new SseMessageDto();
        dto.setUserIds(List.of(LoginHelper.getUserId()));
        dto.setMessage("123");
        sseEmitterManager.publishMessage(dto);
        return R.ok(map);
    }

    @GetMapping("/list")
    public R<Map<String,Integer>> info(){
        WebSocketSessionHolder.getSessionsAll().forEach(key -> {
            WebSocketUtils.sendMessage(key, "123");
        });
        //获取当天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dates=sdf.format(System.currentTimeMillis());
        String beg=dates+" 00:00:00";
        String end=dates+" 23:59:59";
        //获取本月
            // 获取当前日期
        LocalDate currentDate = LocalDate.now();

            // 获取本月的月初日期
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
            // 创建日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 获取本月的月末日期
        LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
        // 格式化日期为 "yyyy-MM-dd" 格式
        String formattedFirstDayOfMonth = firstDayOfMonth.format(formatter);
        String formattedLastDayOfMonth = lastDayOfMonth.format(formatter);
        Map<String,Object> oldmap=new HashMap<>();
        oldmap.put("beginEndtime",beg);
        oldmap.put("endEndtime",end);
        MesQuaRecBo quaRecBo=new MesQuaRecBo();
        quaRecBo.setParams(oldmap);
        int quacount=quaRecService.queryList(quaRecBo).size();
        MesWorkRecBo workRecBo=new MesWorkRecBo();
        workRecBo.setParams(oldmap);
        int workcount=workRecService.queryList(workRecBo).size();
        int warning=0;
        Map<String,Integer> map=new HashMap<>();
        map.put("work",workcount);
        map.put("qua",quacount);
        map.put("warn",warning);
        //流转中订单
        int workdone=0;
        int markingdone=0;
        LambdaQueryWrapper<MesWork> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(MesWork::getStatus,0);
        wrapper.between(MesWork::getCreateTime,firstDayOfMonth,lastDayOfMonth);
        List<MesWork> list=mesWorkMapper.selectList(wrapper);
        if(!list.isEmpty()){
            for(MesWork mesWork:list){
                if (!StringUtils.equals(mesWork.getStatus(),"3")){
                    workdone+=mesWork.getNeednum().intValue();
                }else{
                    markingdone+=mesWork.getNeednum().intValue();
                }
            }
        }
        map.put("workdone",workdone);
        map.put("markingdone",markingdone);
        return R.ok(map);
    }
}
