package com.liaoyun.mes.controller;

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
import com.liaoyun.mes.domain.vo.MesProcurementplanVo;
import com.liaoyun.mes.domain.bo.MesProcurementplanBo;
import com.liaoyun.mes.service.IMesProcurementplanService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 采购计划
 *
 * @author Liao Liao
 * @date 2024-07-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/procurementplan")
public class MesProcurementplanController extends BaseController {

    private final IMesProcurementplanService mesProcurementplanService;

    /**
     * 查询采购计划列表
     */
    @SaCheckPermission("mes:procurementplan:list")
    @GetMapping("/list")
    public TableDataInfo<MesProcurementplanVo> list(MesProcurementplanBo bo, PageQuery pageQuery) {
        return mesProcurementplanService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出采购计划列表
     */
    @SaCheckPermission("mes:procurementplan:export")
    @Log(title = "采购计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesProcurementplanBo bo, HttpServletResponse response) {
        List<MesProcurementplanVo> list = mesProcurementplanService.queryList(bo);
        ExcelUtil.exportExcel(list, "采购计划", MesProcurementplanVo.class, response);
    }

    /**
     * 获取采购计划详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:procurementplan:query")
    @GetMapping("/{id}")
    public R<MesProcurementplanVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesProcurementplanService.queryById(id));
    }

    /**
     * 新增采购计划
     */
    @SaCheckPermission("mes:procurementplan:add")
    @Log(title = "采购计划", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesProcurementplanBo bo) {
        return toAjax(mesProcurementplanService.insertByBo(bo));
    }

    /**
     * 修改采购计划
     */
    @SaCheckPermission("mes:procurementplan:edit")
    @Log(title = "采购计划", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesProcurementplanBo bo) {
        return toAjax(mesProcurementplanService.updateByBo(bo));
    }

    /**
     * 删除采购计划
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:procurementplan:remove")
    @Log(title = "采购计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesProcurementplanService.deleteWithValidByIds(List.of(ids), true));
    }
}
