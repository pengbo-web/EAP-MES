package com.liaoyun.mes.dispatch;

import cn.dev33.satoken.annotation.SaIgnore;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.common.tenant.helper.TenantHelper;
import com.liaoyun.mes.domain.MesTask;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 工序级自动派工接口(二次开发)
 *
 * @author pengbo
 * @date 2026-10-16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/mes/dispatch")
public class MesDispatchController {

    private final MesDispatchService dispatchService;

    /**
     * 工单下达派工(首工序)
     */
    @SaIgnore
    @PostMapping("/release/{workId}")
    public R<MesTask> release(@PathVariable Long workId) {
        return R.ok(TenantHelper.ignore(() -> DataPermissionHelper.ignore(() -> dispatchService.release(workId))));
    }

    /**
     * 派工计划透明化(各工序候选站得分)
     */
    @SaIgnore
    @GetMapping("/plan/{workId}")
    public R<List<Map<String, Object>>> plan(@PathVariable Long workId) {
        return R.ok(TenantHelper.ignore(() -> DataPermissionHelper.ignore(() -> dispatchService.plan(workId))));
    }

    /**
     * 手动暂停工作站(运维)
     */
    @SaIgnore
    @PostMapping("/suspend/{workplaceId}")
    public R<Void> suspend(@PathVariable Long workplaceId,
                           @RequestParam(defaultValue = "30") int minutes,
                           @RequestParam(defaultValue = "运维手动暂停") String reason) {
        TenantHelper.ignore(() -> DataPermissionHelper.ignore(() -> {
            dispatchService.suspend(workplaceId, minutes, reason);
            return null;
        }));
        return R.ok();
    }
}
