package com.liaoyun.system.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.liaoyun.common.idempotent.annotation.RepeatSubmit;
import com.liaoyun.common.log.annotation.Log;
import com.liaoyun.common.web.core.BaseController;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import com.liaoyun.common.log.enums.BusinessType;
import com.liaoyun.common.excel.utils.ExcelUtil;
import com.liaoyun.system.domain.vo.MesInventoryLedgerVo;
import com.liaoyun.system.domain.bo.MesInventoryLedgerBo;
import com.liaoyun.system.service.IMesInventoryLedgerService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 库存流水

 *
 * @author Liao Liao
 * @date 2025-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/inventoryLedger")
public class MesInventoryLedgerController extends BaseController {

    private final IMesInventoryLedgerService mesInventoryLedgerService;

    /**
     * 查询库存流水
列表
     */
    @SaCheckPermission("mes:inventoryLedger:list")
    @GetMapping("/list")
    public TableDataInfo<MesInventoryLedgerVo> list(MesInventoryLedgerBo bo, PageQuery pageQuery) {
        return mesInventoryLedgerService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出库存流水
列表
     */
    @SaCheckPermission("mes:inventoryLedger:export")
    @Log(title = "库存流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesInventoryLedgerBo bo, HttpServletResponse response) {
        List<MesInventoryLedgerVo> list = mesInventoryLedgerService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存流水", MesInventoryLedgerVo.class, response);
    }

    /**
     * 获取库存流水
详细信息
     *
     * @param sn 主键
     */
    @SaCheckPermission("mes:inventoryLedger:query")
    @GetMapping("/{sn}")
    public R<MesInventoryLedgerVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long sn) {
        return R.ok(mesInventoryLedgerService.queryById(sn));
    }

    /**
     * 新增库存流水

     */
    @SaCheckPermission("mes:inventoryLedger:add")
    @Log(title = "库存流水", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesInventoryLedgerBo bo) {
        return toAjax(mesInventoryLedgerService.insertByBo(bo));
    }

    /**
     * 修改库存流水

     */
    @SaCheckPermission("mes:inventoryLedger:edit")
    @Log(title = "库存流水", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesInventoryLedgerBo bo) {
        return toAjax(mesInventoryLedgerService.updateByBo(bo));
    }

    /**
     * 删除库存流水

     *
     * @param sns 主键串
     */
    @SaCheckPermission("mes:inventoryLedger:remove")
    @Log(title = "库存流水", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sns}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] sns) {
        return toAjax(mesInventoryLedgerService.deleteWithValidByIds(List.of(sns), true));
    }
}
