package com.liaoyun.mes.eap;

import cn.dev33.satoken.annotation.SaIgnore;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.common.tenant.helper.TenantHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

/**
 * OEE 三率接口(二次开发)
 *
 * @author pengbo
 * @date 2026-10-16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/mes/eap")
public class MesOeeController {

    private final MesOeeService oeeService;

    /**
     * 单设备单日 OEE
     */
    @SaIgnore
    @GetMapping("/oee")
    public R<Map<String, Object>> oee(@RequestParam String deviceCode,
                                      @RequestParam(required = false) String date) {
        LocalDate day = parseDate(date);
        return R.ok(TenantHelper.ignore(() -> DataPermissionHelper.ignore(() -> oeeService.oee(deviceCode, day))));
    }

    /**
     * 全设备汇总 + 工厂 OEE(产量加权)
     */
    @SaIgnore
    @GetMapping("/oee/summary")
    public R<Map<String, Object>> summary(@RequestParam(required = false) String date) {
        LocalDate day = parseDate(date);
        return R.ok(TenantHelper.ignore(() -> DataPermissionHelper.ignore(() -> oeeService.summary(day))));
    }

    private LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            return LocalDate.now();
        }
        return LocalDate.parse(date);
    }
}
