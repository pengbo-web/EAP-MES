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
import com.liaoyun.mes.domain.vo.MesPreVo;
import com.liaoyun.mes.domain.bo.MesPreBo;
import com.liaoyun.mes.service.IMesPreService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 采购审核
 *
 * @author Liao Liao
 * @date 2025-03-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/pre")
public class MesPreController extends BaseController {

    private final IMesPreService mesPreService;

    /**
     * 查询采购审核列表
     */
    @SaCheckPermission("mes:pre:list")
    @GetMapping("/list")
    public TableDataInfo<MesPreVo> list(MesPreBo bo, PageQuery pageQuery) {
        return mesPreService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出采购审核列表
     */
    @SaCheckPermission("mes:pre:export")
    @Log(title = "采购审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesPreBo bo, HttpServletResponse response) {
        List<MesPreVo> list = mesPreService.queryList(bo);
        ExcelUtil.exportExcel(list, "采购审核", MesPreVo.class, response);
    }

    /**
     * 获取采购审核详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:pre:query")
    @GetMapping("/{id}")
    public R<MesPreVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesPreService.queryById(id));
    }

    /**
     * 新增采购审核
     */
    @SaCheckPermission("mes:pre:add")
    @Log(title = "采购审核", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<MesPreVo> add(@Validated(AddGroup.class) @RequestBody MesPreBo bo) {
        return R.ok(mesPreService.insertByBo(bo));
    }

    /**
     * 修改采购审核
     */
    @SaCheckPermission("mes:pre:edit")
    @Log(title = "采购审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesPreBo bo) {
        return toAjax(mesPreService.updateByBo(bo));
    }

    /**
     * 删除采购审核
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:pre:remove")
    @Log(title = "采购审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesPreService.deleteWithValidByIds(List.of(ids), true));
    }
}
