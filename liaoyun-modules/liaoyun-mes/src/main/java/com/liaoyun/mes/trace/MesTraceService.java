package com.liaoyun.mes.trace;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.mes.domain.MesFifoAlloc;
import com.liaoyun.mes.domain.MesQuaRec;
import com.liaoyun.mes.domain.MesTask;
import com.liaoyun.mes.domain.MesWork;
import com.liaoyun.mes.domain.MesWorkRec;
import com.liaoyun.mes.domain.MesYcrec;
import com.liaoyun.mes.domain.vo.MesProcessVo;
import com.liaoyun.mes.mapper.MesFifoAllocMapper;
import com.liaoyun.mes.mapper.MesProcessMapper;
import com.liaoyun.mes.mapper.MesQuaRecMapper;
import com.liaoyun.mes.mapper.MesTaskMapper;
import com.liaoyun.mes.mapper.MesWorkMapper;
import com.liaoyun.mes.mapper.MesWorkRecMapper;
import com.liaoyun.mes.mapper.MesYcrecMapper;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SN 双向追溯服务(二次开发)
 * <p>
 * 正向(件级): SN -> 任务 -> 报工行(工序/设备/参数快照/操作者/数量/时间) + 质检结果 + 报警单, 组装追溯树;
 * 反向(批次级): 入库台账批次sn -> FIFO分配明细(out_no=订单号) -> 工单 -> 工单SN群;
 * 边界声明: 反向为批次级(影响面清单), 件级反向需 SN-批次绑定表, 不在本期范围。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MesTraceService {

    private final MesTaskMapper taskMapper;
    private final MesWorkMapper workMapper;
    private final MesWorkRecMapper workRecMapper;
    private final MesQuaRecMapper quaRecMapper;
    private final MesYcrecMapper ycrecMapper;
    private final MesProcessMapper processMapper;
    private final MesFifoAllocMapper fifoAllocMapper;
    private final SysUserMapper sysUserMapper;

    /**
     * 正向追溯: 按流转批次号 SN
     */
    public Map<String, Object> forward(String sn) {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("sn", sn);
        List<MesTask> tasks = taskMapper.selectList(new LambdaQueryWrapper<MesTask>()
            .eq(MesTask::getSn, sn).orderByAsc(MesTask::getId));
        List<Map<String, Object>> taskNodes = new ArrayList<>();
        for (MesTask task : tasks) {
            Map<String, Object> taskNode = new LinkedHashMap<>();
            taskNode.put("taskId", task.getId());
            taskNode.put("status", task.getStatus());
            MesWork work = task.getWorkId() == null ? null : workMapper.selectById(task.getWorkId());
            if (work != null) {
                Map<String, Object> workInfo = new LinkedHashMap<>();
                workInfo.put("workSn", work.getSn());
                workInfo.put("title", work.getTitle());
                taskNode.put("work", workInfo);
            }
            // 报工工序链(含设备与参数快照)
            List<MesWorkRec> recs = workRecMapper.selectList(new LambdaQueryWrapper<MesWorkRec>()
                .eq(MesWorkRec::getTaskId, task.getId()).orderByAsc(MesWorkRec::getCreateTime));
            List<Map<String, Object>> processNodes = new ArrayList<>();
            for (MesWorkRec rec : recs) {
                Map<String, Object> pNode = new LinkedHashMap<>();
                MesProcessVo process = rec.getProid() == null ? null : processMapper.selectVoById(rec.getProid());
                pNode.put("process", process == null ? null : process.getTitle());
                pNode.put("deviceCode", rec.getDeviceCode());
                pNode.put("params", rec.getEapParams());
                pNode.put("count", rec.getCount());
                pNode.put("defective", rec.getDefective());
                pNode.put("operator", userName(rec.getUserId()));
                pNode.put("time", rec.getCreateTime());
                processNodes.add(pNode);
            }
            taskNode.put("processes", processNodes);
            // 质检结果
            List<MesQuaRec> quas = quaRecMapper.selectList(new LambdaQueryWrapper<MesQuaRec>()
                .eq(MesQuaRec::getTaskId, task.getId()));
            List<Map<String, Object>> quaNodes = new ArrayList<>();
            for (MesQuaRec qua : quas) {
                Map<String, Object> qNode = new LinkedHashMap<>();
                qNode.put("title", qua.getTitle());
                qNode.put("standard", qua.getValuest());
                qNode.put("result", qua.getRes());
                quaNodes.add(qNode);
            }
            taskNode.put("quality", quaNodes);
            taskNodes.add(taskNode);
        }
        root.put("tasks", taskNodes);
        // 报警单(按 SN)
        List<MesYcrec> ycrecs = ycrecMapper.selectList(new LambdaQueryWrapper<MesYcrec>()
            .eq(MesYcrec::getSn, sn));
        List<Map<String, Object>> alarmNodes = new ArrayList<>();
        for (MesYcrec yc : ycrecs) {
            Map<String, Object> aNode = new LinkedHashMap<>();
            aNode.put("desc", yc.getYcdesc());
            aNode.put("status", yc.getStatus());
            aNode.put("time", yc.getCreateTime());
            alarmNodes.add(aNode);
        }
        root.put("alarms", alarmNodes);
        return root;
    }

    /**
     * 反向追溯: 按入库台账批次 sn, 返回受影响工单 SN 群(批次级)
     */
    public Map<String, Object> backward(Long ledgerSn) {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("ledgerSn", ledgerSn);
        List<MesFifoAlloc> allocs = fifoAllocMapper.selectList(new LambdaQueryWrapper<MesFifoAlloc>()
            .eq(MesFifoAlloc::getLedgerSn, ledgerSn));
        List<Map<String, Object>> affected = new ArrayList<>();
        for (MesFifoAlloc alloc : allocs) {
            if (alloc.getOutNo() == null || alloc.getOutNo().isBlank()) {
                continue;
            }
            long orderId;
            try {
                orderId = Long.parseLong(alloc.getOutNo().trim());
            } catch (NumberFormatException e) {
                log.warn("分配明细 out_no 非订单号, 跳过: {}", alloc.getOutNo());
                continue;
            }
            List<MesWork> works = workMapper.selectList(new LambdaQueryWrapper<MesWork>()
                .eq(MesWork::getOrderid, orderId));
            for (MesWork work : works) {
                Map<String, Object> wNode = new LinkedHashMap<>();
                wNode.put("workSn", work.getSn());
                wNode.put("title", work.getTitle());
                wNode.put("allocQty", alloc.getQty());
                affected.add(wNode);
            }
        }
        root.put("affectedWorks", affected);
        return root;
    }

    private String userName(Long userId) {
        if (userId == null) {
            return null;
        }
        SysUser user = sysUserMapper.selectById(userId);
        return user == null ? String.valueOf(userId) : user.getNickName();
    }
}
