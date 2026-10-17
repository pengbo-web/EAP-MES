package com.liaoyun.mes.dispatch;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.exception.ServiceException;
import com.liaoyun.mes.domain.MesProsetting;
import com.liaoyun.mes.domain.MesTask;
import com.liaoyun.mes.domain.MesWork;
import com.liaoyun.mes.domain.MesWorkplaces;
import com.liaoyun.mes.mapper.MesProsettingMapper;
import com.liaoyun.mes.mapper.MesTaskMapper;
import com.liaoyun.mes.mapper.MesWorkMapper;
import com.liaoyun.mes.mapper.MesWorkplaceSuspendMapper;
import com.liaoyun.mes.mapper.MesWorkplacesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工序级自动派工规则引擎(二次开发)
 * <p>
 * 规则: 候选 = 工作站能力(processIds 包含工序) 且未暂停; 得分 = 在制负载 / 容量因子(counts);
 * 取最低分, 平局取 id 升序; 无候选回退工艺路线默认工作站, 仍无则抛业务异常。
 * 流转: 步骤任务够量完工后自动创建下一工序任务(同 SN), 末工序完工即工单完工。
 * 报警联动: 设备报警 -> 其工作站暂停 -> 在制任务改派次优候选。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MesDispatchService {

    private final MesWorkMapper workMapper;
    private final MesTaskMapper taskMapper;
    private final MesProsettingMapper prosettingMapper;
    private final MesWorkplacesMapper workplacesMapper;
    private final MesWorkplaceSuspendMapper suspendMapper;

    /**
     * 工单下达: 按工艺路线首工序创建任务并规则派工
     */
    @Transactional(rollbackFor = Exception.class)
    public MesTask release(Long workId) {
        MesWork work = workMapper.selectById(workId);
        if (work == null) {
            throw new ServiceException("工单不存在: " + workId);
        }
        MesProsetting first = firstStep(work.getProId());
        if (first == null) {
            throw new ServiceException("产品未配置工艺路线, proId: " + work.getProId());
        }
        return createStepTask(work, first, null);
    }

    /**
     * 工序流转: 当前步骤任务完工后创建下一工序任务, 无下一工序返回 null(工单完工)
     */
    @Transactional(rollbackFor = Exception.class)
    public MesTask releaseNextStep(MesTask finishedTask) {
        MesProsetting current = prosettingMapper.selectOne(new LambdaQueryWrapper<MesProsetting>()
            .eq(MesProsetting::getProId, finishedTask.getProId())
            .eq(MesProsetting::getProcessId, String.valueOf(finishedTask.getCurrproid()))
            .last("limit 1"));
        int currentSort = current == null || current.getSort() == null ? 0 : current.getSort();
        MesProsetting next = prosettingMapper.selectOne(new LambdaQueryWrapper<MesProsetting>()
            .eq(MesProsetting::getProId, finishedTask.getProId())
            .gt(MesProsetting::getSort, currentSort)
            .orderByAsc(MesProsetting::getSort)
            .last("limit 1"));
        if (next == null) {
            return null;
        }
        MesWork work = workMapper.selectById(finishedTask.getWorkId());
        return createStepTask(work, next, finishedTask);
    }

    /**
     * 派工计划透明化: 各工序候选工作站得分明细
     */
    public List<Map<String, Object>> plan(Long workId) {
        MesWork work = workMapper.selectById(workId);
        if (work == null) {
            throw new ServiceException("工单不存在: " + workId);
        }
        List<MesProsetting> steps = prosettingMapper.selectList(new LambdaQueryWrapper<MesProsetting>()
            .eq(MesProsetting::getProId, work.getProId()).orderByAsc(MesProsetting::getSort));
        List<Map<String, Object>> result = new ArrayList<>();
        for (MesProsetting step : steps) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("sort", step.getSort());
            item.put("processId", parseLong(step.getProcessId()));
            item.put("candidates", candidatesWithScore(parseLong(step.getProcessId()), null));
            result.add(item);
        }
        return result;
    }

    /**
     * 报警联动: 暂停设备所在工作站并在制任务改派
     */
    @Transactional(rollbackFor = Exception.class)
    public int suspendByDevice(String deviceCode, int minutes, String reason) {
        List<MesWorkplaces> hits = workplacesMapper.selectList(new LambdaQueryWrapper<MesWorkplaces>()
            .eq(MesWorkplaces::getStatus, "0"));
        List<Long> suspended = new ArrayList<>();
        Date until = new Date(System.currentTimeMillis() + minutes * 60_000L);
        for (MesWorkplaces wp : hits) {
            if (wp.getEquIds() != null && Arrays.stream(wp.getEquIds().split(","))
                .map(String::trim).anyMatch(deviceCode::equals)) {
                MesWorkplaceSuspend sus = new MesWorkplaceSuspend();
                sus.setWorkplaceId(wp.getId());
                sus.setReason(reason);
                sus.setSuspendUntil(until);
                suspendMapper.insert(sus);
                suspended.add(wp.getId());
            }
        }
        int redispatched = 0;
        for (Long wpId : suspended) {
            List<MesTask> tasks = taskMapper.selectList(new LambdaQueryWrapper<MesTask>()
                .eq(MesTask::getWorkplaceId, wpId).eq(MesTask::getStatus, "1"));
            for (MesTask task : tasks) {
                Long target = pickWorkplace(task.getCurrproid(), wpId);
                if (target != null && !target.equals(wpId)) {
                    task.setWorkplaceId(target);
                    task.setWorkplaceDispatch("RULE");
                    task.setRemark((task.getRemark() == null ? "" : task.getRemark() + ",")
                        + "报警改派: " + wpId + "->" + target);
                    taskMapper.updateById(task);
                    redispatched++;
                }
            }
        }
        log.warn("报警联动暂停工作站 {} 个, 改派任务 {} 个, 设备: {}", suspended.size(), redispatched, deviceCode);
        return redispatched;
    }

    /**
     * 手动暂停工作站(运维场景)
     */
    @Transactional(rollbackFor = Exception.class)
    public void suspend(Long workplaceId, int minutes, String reason) {
        MesWorkplaceSuspend sus = new MesWorkplaceSuspend();
        sus.setWorkplaceId(workplaceId);
        sus.setReason(reason);
        sus.setSuspendUntil(new Date(System.currentTimeMillis() + minutes * 60_000L));
        suspendMapper.insert(sus);
    }

    private MesTask createStepTask(MesWork work, MesProsetting step, MesTask previous) {
        Long processId = parseLong(step.getProcessId());
        Long workplaceId = pickWorkplace(processId, null);
        if (workplaceId == null) {
            // 回退工艺路线默认工作站
            if (step.getWorkshopId() != null && !step.getWorkshopId().isBlank()) {
                try {
                    workplaceId = Long.parseLong(step.getWorkshopId().trim());
                } catch (NumberFormatException ignored) {
                    // 默认工作站非法则继续抛异常
                }
            }
            if (workplaceId == null) {
                throw new ServiceException("工序无可用工作站, processId: " + processId);
            }
        }
        MesTask task = new MesTask();
        task.setSn(work.getSn());
        task.setWorkId(work.getId());
        task.setProId(work.getProId());
        task.setCurrproid(processId);
        task.setCurrprocess(step.getSort());
        task.setWorkplaceId(workplaceId);
        task.setNeedcount(previous == null
            ? (work.getNeednum() == null ? 1L : work.getNeednum().longValue())
            : previous.getNeedcount());
        task.setMarkcount(0L);
        task.setStatus("1");
        task.setWorkplaceDispatch("RULE");
        task.setAllprocess(prosettingMapper.selectCount(new LambdaQueryWrapper<MesProsetting>()
            .eq(MesProsetting::getProId, work.getProId())).intValue());
        task.setOkprocess(0);
        taskMapper.insert(task);
        log.info("派工: 工单{} 工序sort{} -> 工作站{} (RULE)", work.getSn(), step.getSort(), workplaceId);
        return task;
    }

    private MesProsetting firstStep(Long proId) {
        return prosettingMapper.selectOne(new LambdaQueryWrapper<MesProsetting>()
            .eq(MesProsetting::getProId, proId).orderByAsc(MesProsetting::getSort).last("limit 1"));
    }

    /**
     * 规则选站: 能力匹配 + 未暂停 + 负载/容量 得分最低
     */
    public Long pickWorkplace(Long processId, Long excludeWorkplaceId) {
        List<Map<String, Object>> scored = candidatesWithScore(processId, excludeWorkplaceId);
        return scored.isEmpty() ? null : (Long) scored.get(0).get("workplaceId");
    }

    private List<Map<String, Object>> candidatesWithScore(Long processId, Long excludeWorkplaceId) {
        Date now = new Date();
        List<MesWorkplaces> all = workplacesMapper.selectList(new LambdaQueryWrapper<MesWorkplaces>()
            .eq(MesWorkplaces::getStatus, "0"));
        List<Map<String, Object>> scored = new ArrayList<>();
        for (MesWorkplaces wp : all) {
            if (excludeWorkplaceId != null && excludeWorkplaceId.equals(wp.getId())) {
                continue;
            }
            if (wp.getProcessIds() == null || Arrays.stream(wp.getProcessIds().split(","))
                .map(String::trim).noneMatch(s -> s.equals(String.valueOf(processId)))) {
                continue;
            }
            boolean suspended = suspendMapper.selectCount(new LambdaQueryWrapper<MesWorkplaceSuspend>()
                .eq(MesWorkplaceSuspend::getWorkplaceId, wp.getId())
                .gt(MesWorkplaceSuspend::getSuspendUntil, now)) > 0;
            if (suspended) {
                continue;
            }
            long load = taskMapper.selectCount(new LambdaQueryWrapper<MesTask>()
                .eq(MesTask::getWorkplaceId, wp.getId()).eq(MesTask::getStatus, "1"));
            double capacity = wp.getCounts() == null || wp.getCounts() <= 0 ? 1 : wp.getCounts();
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("workplaceId", wp.getId());
            item.put("title", wp.getTitle());
            item.put("load", load);
            item.put("capacity", capacity);
            item.put("score", Math.round(load / capacity * 1000.0) / 1000.0);
            scored.add(item);
        }
        scored.sort((x, y) -> {
            int c = Double.compare((Double) x.get("score"), (Double) y.get("score"));
            return c != 0 ? c : Long.compare((Long) x.get("workplaceId"), (Long) y.get("workplaceId"));
        });
        return scored;
    }

    private Long parseLong(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
