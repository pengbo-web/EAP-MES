package com.liaoyun.mes.eap;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.common.tenant.helper.TenantHelper;
import com.liaoyun.mes.domain.MesWork;
import com.liaoyun.mes.domain.MesWorkRec;
import com.liaoyun.mes.mapper.MesEapDeviceMapper;
import com.liaoyun.mes.mapper.MesWorkMapper;
import com.liaoyun.mes.mapper.MesWorkRecMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * EAP 生产看板(二次开发)
 * <p>
 * 独立 FreeMarker 大屏页 + 数据接口, 不依赖管理端前端框架:
 * 设备状态分布与稼动率 / 工单工序进度 / 当日良品率 / 当日报工小时趋势。
 * 设备离线态按"最后事件时间超过阈值"实时判定, 不落库。
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/eap")
public class MesEapDashboardController {

    private final MesEapDeviceMapper deviceMapper;
    private final MesWorkMapper workMapper;
    private final MesWorkRecMapper workRecMapper;

    /**
     * 设备离线判定阈值(分钟)
     */
    @Value("${eap.dashboard.offline-minutes:5}")
    private int offlineMinutes;

    /**
     * 大屏页面(车间看板终端免登录访问)
     * <p>
     * RuoYi-Plus 为纯 REST 后端无视图解析器, 故以静态资源字符串直出
     */
    @SaIgnore
    @GetMapping(value = "/dashboard-ui", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String dashboardUi() throws java.io.IOException {
        return new String(new org.springframework.core.io.ClassPathResource("static/eap/dashboard.html")
            .getInputStream().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * 看板数据
     */
    @SaIgnore
    @ResponseBody
    @GetMapping("/dashboard/data")
    public R<Map<String, Object>> dashboardData() {
        return R.ok(TenantHelper.ignore(() -> DataPermissionHelper.ignore(this::buildData)));
    }

    private Map<String, Object> buildData() {
        Map<String, Object> result = new LinkedHashMap<>();
        long offlineMillis = offlineMinutes * 60_000L;
        long now = System.currentTimeMillis();

        // 设备状态
        List<MesEapDevice> devices = deviceMapper.selectList(new LambdaQueryWrapper<MesEapDevice>());
        List<Map<String, Object>> deviceList = new ArrayList<>();
        int running = 0, alarm = 0, offline = 0;
        for (MesEapDevice dev : devices) {
            String status = dev.getStatus();
            if (dev.getLastEventTime() == null || now - dev.getLastEventTime().getTime() > offlineMillis) {
                status = "2";
            }
            if ("0".equals(status)) {
                running++;
            } else if ("1".equals(status)) {
                alarm++;
            } else {
                offline++;
            }
            Map<String, Object> item = new HashMap<>();
            item.put("code", dev.getDeviceCode());
            item.put("name", dev.getDeviceName());
            item.put("status", status);
            item.put("lastEventTime", dev.getLastEventTime());
            item.put("todayCount", dev.getTodayCount());
            item.put("todayAlarm", dev.getTodayAlarm());
            item.put("todayDefective", dev.getTodayDefective());
            deviceList.add(item);
        }
        int total = devices.size();
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total", total);
        summary.put("running", running);
        summary.put("alarm", alarm);
        summary.put("offline", offline);
        // 稼动率 = 运行中设备 / 设备总数
        summary.put("utilization", total == 0 ? 0 : Math.round(running * 1000.0 / total) / 10.0);
        result.put("summary", summary);
        result.put("devices", deviceList);

        // 工单工序进度
        List<MesWork> works = workMapper.selectList(new LambdaQueryWrapper<MesWork>()
            .gt(MesWork::getProcesscount, 0)
            .orderByDesc(MesWork::getId)
            .last("limit 10"));
        List<Map<String, Object>> progress = new ArrayList<>();
        for (MesWork work : works) {
            Map<String, Object> item = new HashMap<>();
            item.put("sn", work.getSn());
            item.put("title", work.getTitle());
            item.put("okprocess", work.getOkprocess());
            item.put("processcount", work.getProcesscount());
            long pc = work.getProcesscount() == null ? 0 : work.getProcesscount();
            long ok = work.getOkprocess() == null ? 0 : work.getOkprocess();
            item.put("pct", pc == 0 ? 0 : Math.min(100.0, Math.round(ok * 1000.0 / pc) / 10.0));
            item.put("status", work.getStatus());
            progress.add(item);
        }
        result.put("workProgress", progress);

        // 当日质量: 报工数量 vs 不良数量
        List<MesWorkRec> recs = workRecMapper.selectList(new LambdaQueryWrapper<MesWorkRec>()
            .ge(MesWorkRec::getCreateTime, dayStart()));
        long qty = 0, defective = 0;
        Map<String, Long> hourly = new LinkedHashMap<>();
        for (int h = 0; h < 24; h++) {
            hourly.put(String.format("%02d", h), 0L);
        }
        for (MesWorkRec rec : recs) {
            qty += rec.getCount() == null ? 0 : rec.getCount();
            defective += rec.getDefective() == null ? 0 : rec.getDefective();
            if (rec.getCreateTime() != null) {
                String hh = String.format("%02d", rec.getCreateTime().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).getHour());
                hourly.merge(hh, rec.getCount() == null ? 0L : rec.getCount(), Long::sum);
            }
        }
        Map<String, Object> quality = new LinkedHashMap<>();
        quality.put("qty", qty);
        quality.put("defective", defective);
        quality.put("rate", qty == 0 ? 100.0 : Math.round((qty - defective) * 1000.0 / qty) / 10.0);
        result.put("quality", quality);
        result.put("hourlyTrend", hourly);
        return result;
    }

    private Date dayStart() {
        java.time.LocalDate today = java.time.LocalDate.now();
        return Date.from(today.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
    }
}
