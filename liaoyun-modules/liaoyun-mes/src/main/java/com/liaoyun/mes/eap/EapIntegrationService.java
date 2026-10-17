package com.liaoyun.mes.eap;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.exception.ServiceException;
import com.liaoyun.common.core.utils.DateUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.common.tenant.helper.TenantHelper;
import com.liaoyun.mes.domain.MesTask;
import com.liaoyun.mes.domain.MesWorkRec;
import com.liaoyun.mes.domain.MesYcrec;
import com.liaoyun.mes.domain.bo.MesWorkBo;
import com.liaoyun.mes.domain.vo.MesProcessVo;
import com.liaoyun.mes.domain.vo.MesWorkVo;
import com.liaoyun.mes.mapper.MesEapDeviceMapper;
import com.liaoyun.mes.mapper.MesEapDeviceStateLogMapper;
import com.liaoyun.mes.mapper.MesProcessMapper;
import com.liaoyun.mes.mapper.MesTaskMapper;
import com.liaoyun.mes.mapper.MesWorkRecMapper;
import com.liaoyun.mes.mapper.MesYcrecMapper;
import com.liaoyun.mes.service.IMesWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * EAP-MES 集成服务
 * <p>
 * 消费 EAP 采集网关推送的设备事件:
 * 1. PROCESS_COMPLETE 加工完成 -> 自动生成报工记录(mes_work_rec)并推进工单工序;
 * 2. PARAM_ALARM 参数超限 -> 自动生成异常报工单(mes_ycrec)并将任务置为异常完工。
 * <p>
 * 网关通道无登录态,租户上下文通过 TenantHelper.dynamic 显式切换。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EapIntegrationService {

    /**
     * 事件类型:加工完成
     */
    public static final String EVENT_PROCESS_COMPLETE = "PROCESS_COMPLETE";
    /**
     * 事件类型:参数超限报警
     */
    public static final String EVENT_PARAM_ALARM = "PARAM_ALARM";

    private final MesTaskMapper taskMapper;
    private final MesWorkRecMapper workRecMapper;
    private final MesYcrecMapper ycrecMapper;
    private final MesProcessMapper processMapper;
    private final MesEapDeviceMapper deviceMapper;
    private final MesEapDeviceStateLogMapper stateLogMapper;
    private final com.liaoyun.mes.dispatch.MesDispatchService dispatchService;
    private final IMesWorkService workService;

    /**
     * 派工引擎开关(关闭时回退一期"完工即关闭"语义)
     */
    @org.springframework.beans.factory.annotation.Value("${eap.dispatch.enabled:true}")
    private boolean dispatchEnabled;

    /**
     * 报警联动暂停时长(分钟)
     */
    @org.springframework.beans.factory.annotation.Value("${eap.dispatch.suspend-minutes:30}")
    private int suspendMinutes;

    /**
     * 处理 EAP 设备事件(入口,负责租户上下文切换)
     */
    @DSTransactional
    public String handleEvent(EapDeviceEventBo event) {
        // 网关通道无登录态:显式切换租户上下文 + 忽略数据权限拦截
        return TenantHelper.dynamic(event.getTenantId(),
            () -> DataPermissionHelper.ignore(() -> doHandle(event)));
    }

    private String doHandle(EapDeviceEventBo event) {
        MesTask task = queryRunningTask(event.getSn());
        if (EVENT_PROCESS_COMPLETE.equals(event.getEventType())) {
            return autoReport(task, event);
        }
        if (EVENT_PARAM_ALARM.equals(event.getEventType())) {
            return autoAlarm(task, event);
        }
        throw new ServiceException("未知事件类型: " + event.getEventType());
    }

    /**
     * 按流转批次号查询进行中任务
     */
    private MesTask queryRunningTask(String sn) {
        LambdaQueryWrapper<MesTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesTask::getSn, sn);
        wrapper.eq(MesTask::getStatus, "1");
        wrapper.last("limit 1");
        MesTask task = taskMapper.selectOne(wrapper);
        if (task == null) {
            throw new ServiceException("未找到进行中的任务, sn: " + sn);
        }
        return task;
    }

    /**
     * 加工完成 -> 自动报工 + 工单工序推进(推进逻辑与移动报工 scandorep 保持一致)
     */
    private String autoReport(MesTask task, EapDeviceEventBo event) {
        long count = event.getCount() == null ? 1L : event.getCount();
        // 1. 生成报工记录
        MesWorkRec rec = new MesWorkRec();
        rec.setUserId(task.getUserId());
        rec.setTaskId(task.getId());
        rec.setCount(count);
        rec.setLastcount(count);
        rec.setDefective(event.getDefective() == null ? 0 : event.getDefective());
        rec.setStatus("2");
        rec.setProid(task.getCurrproid());
        rec.setRemark("EAP自动报工: 设备" + event.getDeviceCode());
        // 追溯快照: 报工行自包含设备与工艺参数, 追溯不依赖设备表当前值
        rec.setDeviceCode(event.getDeviceCode());
        rec.setEapParams(event.getParams());
        workRecMapper.insert(rec);
        // 2. 任务累加生产数量, 够量才完工(未完成保持进行中, 持续接收设备事件)
        long newCount = (task.getMarkcount() == null ? 0L : task.getMarkcount()) + count;
        task.setMarkcount(newCount);
        boolean completed = task.getNeedcount() != null && newCount >= task.getNeedcount();
        if (completed) {
            task.setStatus("2");
        }
        taskMapper.updateById(task);
        if (completed) {
        // 3. 工单工序推进(步骤完工时推进)
        MesWorkVo workVo = workService.queryById(task.getWorkId());
        if (workVo != null) {
            MesWorkBo workBo = new MesWorkBo();
            workBo.setId(workVo.getId());
            // updateByBo 会按 proId 重算工序数, 必须回传 proId
            workBo.setProId(workVo.getProId());
            int okProcess = (workVo.getOkprocess() == null ? 0 : workVo.getOkprocess()) + 1;
            workBo.setOkprocess(okProcess);
            MesProcessVo processVo = processMapper.selectVoById(task.getCurrproid());
            String title = processVo == null ? "未知工序" : processVo.getTitle();
            workBo.setCurrprocess((workVo.getCurrprocess() == null ? "" : workVo.getCurrprocess() + ",") + title + ":EAP自动完工");
            if (workVo.getProcesscount() != null && okProcess >= workVo.getProcesscount()) {
                workBo.setStatus("3");
                workBo.setEndtime(DateUtils.getTime());
            }
            workService.updateByBo(workBo);
        }
        // 4. 工序流转: 本步骤完工后自动派工下一工序任务(派工引擎, 配置可关)
        if (dispatchEnabled) {
            MesTask next = dispatchService.releaseNextStep(task);
            if (next != null) {
                log.info("工序流转: 任务{}完工, 下工序任务{}派至工作站{}", task.getId(), next.getId(), next.getWorkplaceId());
            }
        }
        }
        log.info("EAP自动报工成功, 设备: {}, sn: {}, 任务: {}, 数量: {}", event.getDeviceCode(), event.getSn(), task.getId(), count);
        touchDevice(event, false, event.getDefective() == null ? 0 : event.getDefective(), (int) count);
        return "自动报工成功, 任务" + task.getId() + "完工数量" + count;
    }

    /**
     * 参数超限 -> 生成异常报工单(待人工审核) + 任务备注留痕
     * <p>
     * 不自动终止任务: mes_ycrec 为"异常报工审核"表, 停线与否应由人工审核决定
     */
    private String autoAlarm(MesTask task, EapDeviceEventBo event) {
        MesYcrec ycrec = new MesYcrec();
        ycrec.setSn(event.getSn());
        ycrec.setCount(event.getCount() == null ? 0L : event.getCount());
        ycrec.setYccount(event.getDefective() == null ? 1L : event.getDefective().longValue());
        ycrec.setYcdesc("EAP参数超限报警: 设备" + event.getDeviceCode() + ", 参数" + event.getParams());
        ycrec.setUserId(task.getUserId());
        ycrec.setStatus("0");
        ycrec.setRemark("EAP集成模块自动生成, 待审核");
        ycrecMapper.insert(ycrec);
        task.setRemark((task.getRemark() == null ? "" : task.getRemark() + ",") + "EAP报警: " + event.getParams());
        taskMapper.updateById(task);
        // 报警联动: 暂停设备所在工作站并改派在制任务
        if (dispatchEnabled) {
            dispatchService.suspendByDevice(event.getDeviceCode(), suspendMinutes, "EAP参数超限: " + event.getParams());
        }
        log.warn("EAP参数超限转异常报工(待审核), 设备: {}, sn: {}, 异常单: {}", event.getDeviceCode(), event.getSn(), ycrec.getId());
        touchDevice(event, true, event.getDefective() == null ? 1 : event.getDefective(), 0);
        return "报警已转异常报工单(待审核): " + ycrec.getId();
    }

    /**
     * 维护设备状态: 完工->运行中, 报警->报警态; 跨天清零当日计数
     */
    private void touchDevice(EapDeviceEventBo event, boolean alarm, int defective, int finished) {
        MesEapDevice dev = deviceMapper.selectOne(new LambdaQueryWrapper<MesEapDevice>()
            .eq(MesEapDevice::getDeviceCode, event.getDeviceCode()).last("limit 1"));
        java.time.LocalDate today = java.time.LocalDate.now();
        boolean isNew = dev == null;
        if (isNew) {
            dev = new MesEapDevice();
            dev.setDeviceCode(event.getDeviceCode());
            dev.setDeviceName(event.getDeviceCode());
            dev.setTodayCount(0);
            dev.setTodayAlarm(0);
            dev.setTodayDefective(0);
        } else if (dev.getLastEventTime() == null
            || !dev.getLastEventTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().equals(today)) {
            dev.setTodayCount(0);
            dev.setTodayAlarm(0);
            dev.setTodayDefective(0);
        }
        String newStatus = alarm ? "1" : "0";
        String oldStatus = dev.getStatus();
        dev.setStatus(newStatus);
        dev.setLastEventTime(new java.util.Date());
        // 状态迁移才写流水(OEE 时长积分依据), 同状态心跳不写
        if (oldStatus == null || !oldStatus.equals(newStatus)) {
            MesEapDeviceStateLog logRow = new MesEapDeviceStateLog();
            logRow.setDeviceCode(event.getDeviceCode());
            logRow.setFromStatus(oldStatus);
            logRow.setToStatus(newStatus);
            logRow.setEventTime(new java.util.Date());
            stateLogMapper.insert(logRow);
        }
        dev.setTodayCount((dev.getTodayCount() == null ? 0 : dev.getTodayCount()) + finished);
        dev.setTodayAlarm((dev.getTodayAlarm() == null ? 0 : dev.getTodayAlarm()) + (alarm ? 1 : 0));
        dev.setTodayDefective((dev.getTodayDefective() == null ? 0 : dev.getTodayDefective()) + defective);
        if (isNew) {
            deviceMapper.insert(dev);
        } else {
            deviceMapper.updateById(dev);
        }
    }
}
