package com.liaoyun.mes.eap;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.mes.domain.MesWorkRec;
import com.liaoyun.mes.mapper.MesEapDeviceMapper;
import com.liaoyun.mes.mapper.MesEapDeviceSpecMapper;
import com.liaoyun.mes.mapper.MesEapDeviceStateLogMapper;
import com.liaoyun.mes.mapper.MesWorkRecMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OEE 三率计算引擎(二次开发)
 * <p>
 * 时间稼动率 A = 运行时长 / 负荷时间(状态流水区间积分, 跨天切片);
 * 性能稼动率 P = 理论周期 × 产量 / 运行时长;
 * 良率       Q = (产量 - 不良) / 产量;
 * OEE = A × P × Q, 各率封顶 1.0。
 * <p>
 * 离线推断: 最后流水之后超过 offline-minutes 未上报即视为离线, 运行/报警区间在该点截断。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MesOeeService {

    private final MesEapDeviceStateLogMapper stateLogMapper;
    private final MesEapDeviceSpecMapper specMapper;
    private final MesEapDeviceMapper deviceMapper;
    private final MesWorkRecMapper workRecMapper;

    @Value("${eap.dashboard.offline-minutes:5}")
    private int offlineMinutes;

    /**
     * 单设备单日 OEE
     */
    public Map<String, Object> oee(String deviceCode, LocalDate date) {
        long[] window = windowOf(date);
        long windowStart = window[0];
        long windowEnd = window[1];

        List<MesEapDeviceStateLog> logs = stateLogMapper.selectList(
            new LambdaQueryWrapper<MesEapDeviceStateLog>()
                .eq(MesEapDeviceStateLog::getDeviceCode, deviceCode)
                .lt(MesEapDeviceStateLog::getEventTime, new Date(windowEnd))
                .orderByAsc(MesEapDeviceStateLog::getEventTime));

        // 窗口起点状态 = 窗口前最后一条流水的 to_status
        String currentStatus = null;
        for (MesEapDeviceStateLog row : logs) {
            if (row.getEventTime().getTime() < windowStart) {
                currentStatus = row.getToStatus();
            } else {
                break;
            }
        }
        long currentStart = windowStart;
        long runMs = 0, alarmMs = 0;
        Long lastEventMs = null;
        for (MesEapDeviceStateLog row : logs) {
            long t = row.getEventTime().getTime();
            if (t < windowStart) {
                continue;
            }
            if (currentStatus != null && t > currentStart) {
                long[] cut = cutInterval(currentStatus, currentStart, t, lastEventMs, windowEnd);
                runMs += cut[0];
                alarmMs += cut[1];
            }
            currentStatus = row.getToStatus();
            currentStart = Math.max(t, windowStart);
            lastEventMs = t;
        }
        if (currentStatus != null && windowEnd > currentStart) {
            long[] cut = cutInterval(currentStatus, currentStart, windowEnd, lastEventMs, windowEnd);
            runMs += cut[0];
            alarmMs += cut[1];
        }

        MesEapDeviceSpec spec = specMapper.selectOne(new LambdaQueryWrapper<MesEapDeviceSpec>()
            .eq(MesEapDeviceSpec::getDeviceCode, deviceCode).last("limit 1"));
        long loadMin = spec == null || spec.getPlannedMinutes() == null ? 480 : spec.getPlannedMinutes();
        BigDecimal idealCycle = spec == null || spec.getIdealCycleSec() == null ? BigDecimal.ZERO : spec.getIdealCycleSec();

        // 当日产量/不良(按报工快照设备编码)
        List<MesWorkRec> recs = workRecMapper.selectList(new LambdaQueryWrapper<MesWorkRec>()
            .eq(MesWorkRec::getDeviceCode, deviceCode)
            .ge(MesWorkRec::getCreateTime, new Date(windowStart))
            .lt(MesWorkRec::getCreateTime, new Date(windowEnd)));
        long qty = 0, def = 0;
        for (MesWorkRec rec : recs) {
            qty += rec.getCount() == null ? 0 : rec.getCount();
            def += rec.getDefective() == null ? 0 : rec.getDefective();
        }

        double runMin = runMs / 60000.0;
        double alarmMin = alarmMs / 60000.0;
        double a = loadMin <= 0 ? 0 : Math.min(1.0, runMin / loadMin);
        double p = runMin <= 0 ? 0 : Math.min(1.0,
            idealCycle.multiply(BigDecimal.valueOf(qty))
                .divide(BigDecimal.valueOf(runMin * 60), 6, RoundingMode.HALF_UP).doubleValue());
        double q = qty <= 0 ? 1.0 : (qty - def) * 1.0 / qty;
        double oee = a * p * q;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("deviceCode", deviceCode);
        result.put("date", date.toString());
        result.put("runMinutes", round(runMin));
        result.put("alarmMinutes", round(alarmMin));
        result.put("loadMinutes", loadMin);
        result.put("availability", pct(a));
        result.put("performance", pct(p));
        result.put("quality", pct(q));
        result.put("oee", pct(oee));
        result.put("qty", qty);
        result.put("defective", def);
        return result;
    }

    /**
     * 全设备汇总 + 产量加权工厂 OEE
     */
    public Map<String, Object> summary(LocalDate date) {
        List<MesEapDevice> devices = deviceMapper.selectList(new LambdaQueryWrapper<MesEapDevice>());
        List<Map<String, Object>> list = new ArrayList<>();
        double weighted = 0;
        long totalQty = 0;
        for (MesEapDevice dev : devices) {
            Map<String, Object> item = oee(dev.getDeviceCode(), date);
            list.add(item);
            long qty = (Long) item.get("qty");
            totalQty += qty;
            weighted += qty * ((Double) item.get("oee"));
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("date", date.toString());
        result.put("devices", list);
        result.put("factoryOee", totalQty == 0 ? 0.0 : round(weighted / totalQty));
        return result;
    }

    /**
     * 区间截断: 运行/报警区间在最后事件 + offlineMinutes 处截断(之后视为离线)
     *
     * @return [runMs增量, alarmMs增量]
     */
    private long[] cutInterval(String status, long start, long end, Long lastEventMs, long windowEnd) {
        long effectiveEnd = end;
        if (lastEventMs != null && ("0".equals(status) || "1".equals(status))) {
            long offlineCut = lastEventMs + offlineMinutes * 60_000L;
            if (end > offlineCut && end >= windowEnd) {
                effectiveEnd = Math.max(start, Math.min(end, offlineCut));
            }
        }
        long span = Math.max(0, effectiveEnd - start);
        if ("0".equals(status)) {
            return new long[]{span, 0};
        }
        if ("1".equals(status)) {
            return new long[]{0, span};
        }
        return new long[]{0, 0};
    }

    private long[] windowOf(LocalDate date) {
        ZoneId zone = ZoneId.systemDefault();
        long start = date.atStartOfDay(zone).toInstant().toEpochMilli();
        long end = Math.min(System.currentTimeMillis(), date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli());
        return new long[]{start, end};
    }

    private double pct(double rate) {
        return Math.round(rate * 1000.0) / 10.0;
    }

    private double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
