package com.liaoyun.mes.trace;

import cn.dev33.satoken.annotation.SaIgnore;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.common.tenant.helper.TenantHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * SN 双向追溯接口(二次开发)
 *
 * @author pengbo
 * @date 2026-10-16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/mes/trace")
public class MesTraceController {

    private final MesTraceService traceService;

    /**
     * 正向追溯(件级): SN -> 工序/设备/参数/质检/报警 树
     */
    @SaIgnore
    @GetMapping("/forward/{sn}")
    public R<Map<String, Object>> forward(@PathVariable String sn) {
        return R.ok(TenantHelper.ignore(() -> DataPermissionHelper.ignore(() -> traceService.forward(sn))));
    }

    /**
     * 反向追溯(批次级): 入库台账批次 -> 受影响工单 SN 群
     */
    @SaIgnore
    @GetMapping("/backward/{ledgerSn}")
    public R<Map<String, Object>> backward(@PathVariable Long ledgerSn) {
        return R.ok(TenantHelper.ignore(() -> DataPermissionHelper.ignore(() -> traceService.backward(ledgerSn))));
    }
}
