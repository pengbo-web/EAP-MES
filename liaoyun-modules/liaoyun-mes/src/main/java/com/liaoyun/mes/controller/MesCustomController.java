package com.liaoyun.mes.controller;

import java.util.List;

import com.liaoyun.mes.domain.MesCustom;
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
import com.liaoyun.mes.domain.vo.MesCustomVo;
import com.liaoyun.mes.domain.bo.MesCustomBo;
import com.liaoyun.mes.service.IMesCustomService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 客户供应商
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/custom")
public class MesCustomController extends BaseController {

    private final IMesCustomService mesCustomService;

    /**
     * 查询客户供应商列表
     */
    @SaCheckPermission("mes:custom:list")
    @GetMapping("/list")
    public TableDataInfo<MesCustomVo> list(MesCustomBo bo, PageQuery pageQuery) {
        return mesCustomService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出客户供应商列表
     */
    @SaCheckPermission("mes:custom:export")
    @Log(title = "客户供应商", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesCustomBo bo, HttpServletResponse response) {
        List<MesCustomVo> list = mesCustomService.queryList(bo);
        ExcelUtil.exportExcel(list, "客户供应商", MesCustomVo.class, response);
    }

    /**
     * 获取客户供应商详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:custom:query")
    @GetMapping("/{id}")
    public R<MesCustomVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesCustomService.queryById(id));
    }

    /**
     * 新增客户供应商
     */
    @SaCheckPermission("mes:custom:add")
    @Log(title = "客户供应商", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesCustomBo bo) {
        return toAjax(mesCustomService.insertByBo(bo));
    }
    /**
     * 新增客户供应商
     */
    @SaCheckPermission("mes:custom:add")
    @Log(title = "客户供应商", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/vo")
    public R<MesCustom> add2(@Validated(AddGroup.class) @RequestBody MesCustomBo bo) {
        MesCustom custom=mesCustomService.insertByBo2(bo);
        return R.ok(custom);
    }

    /**
     * 修改客户供应商
     */
    @SaCheckPermission("mes:custom:edit")
    @Log(title = "客户供应商", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesCustomBo bo) {
        return toAjax(mesCustomService.updateByBo(bo));
    }

    /**
     * 删除客户供应商
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:custom:remove")
    @Log(title = "客户供应商", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesCustomService.deleteWithValidByIds(List.of(ids), true));
    }
}
