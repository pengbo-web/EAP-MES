package com.liaoyun.mes.controller;

import java.util.List;

import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;
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
import com.liaoyun.mes.domain.vo.MesOutstockVo;
import com.liaoyun.mes.domain.bo.MesOutstockBo;
import com.liaoyun.mes.service.IMesOutstockService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 出库管理
 *
 * @author Liao Liao
 * @date 2024-07-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/outstock")
public class MesOutstockController extends BaseController {

    private final IMesOutstockService mesOutstockService;

    /**
     * 查询出库管理列表
     */
    @SaCheckPermission("mes:outstock:list")
    @GetMapping("/list")
    public TableDataInfo<MesOutstockVo> list(MesOutstockBo bo, PageQuery pageQuery) {
        return mesOutstockService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询出库管理列表
     */
    @SaCheckPermission("mes:outstock:list")
    @GetMapping("/unitlist/{id}")
    public List<MesUnitmeasureVo> unitlist(@PathVariable Long id) {
        MesOutstockBo bo=new MesOutstockBo();
        bo.setUnitId(id);
        return mesOutstockService.getUnitList(bo);
    }

    /**
     * 导出出库管理列表
     */
    @SaCheckPermission("mes:outstock:export")
    @Log(title = "出库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOutstockBo bo, HttpServletResponse response) {
        List<MesOutstockVo> list = mesOutstockService.queryList(bo);
        ExcelUtil.exportExcel(list, "出库管理", MesOutstockVo.class, response);
    }

    /**
     * 获取出库管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:outstock:query")
    @GetMapping("/{id}")
    public R<MesOutstockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOutstockService.queryById(id));
    }

    /**
     * 新增出库管理
     */
    @SaCheckPermission("mes:outstock:add")
    @Log(title = "出库管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesOutstockBo bo) {
        return toAjax(mesOutstockService.insertByBo(bo));
    }

    /**
     * 修改出库管理
     */
    @SaCheckPermission("mes:outstock:edit")
    @Log(title = "出库管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOutstockBo bo) {
        return toAjax(mesOutstockService.updateByBo(bo));
    }

    /**
     * 删除出库管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:outstock:remove")
    @Log(title = "出库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOutstockService.deleteWithValidByIds(List.of(ids), true));
    }
}
