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
import com.liaoyun.mes.domain.vo.MesMobileMenuVo;
import com.liaoyun.mes.domain.bo.MesMobileMenuBo;
import com.liaoyun.mes.service.IMesMobileMenuService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 移动端目录
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/mobileMenu")
public class MesMobileMenuController extends BaseController {

    private final IMesMobileMenuService mesMobileMenuService;

    /**
     * 查询移动端目录列表
     */
    @SaCheckPermission("mes:mobileMenu:list")
    @GetMapping("/list")
    public TableDataInfo<MesMobileMenuVo> list(MesMobileMenuBo bo, PageQuery pageQuery) {
        return mesMobileMenuService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出移动端目录列表
     */
    @SaCheckPermission("mes:mobileMenu:export")
    @Log(title = "移动端目录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesMobileMenuBo bo, HttpServletResponse response) {
        List<MesMobileMenuVo> list = mesMobileMenuService.queryList(bo);
        ExcelUtil.exportExcel(list, "移动端目录", MesMobileMenuVo.class, response);
    }

    /**
     * 获取移动端目录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:mobileMenu:query")
    @GetMapping("/{id}")
    public R<MesMobileMenuVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesMobileMenuService.queryById(id));
    }

    /**
     * 新增移动端目录
     */
    @SaCheckPermission("mes:mobileMenu:add")
    @Log(title = "移动端目录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesMobileMenuBo bo) {
        return toAjax(mesMobileMenuService.insertByBo(bo));
    }

    /**
     * 修改移动端目录
     */
    @SaCheckPermission("mes:mobileMenu:edit")
    @Log(title = "移动端目录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesMobileMenuBo bo) {
        return toAjax(mesMobileMenuService.updateByBo(bo));
    }

    /**
     * 删除移动端目录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:mobileMenu:remove")
    @Log(title = "移动端目录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesMobileMenuService.deleteWithValidByIds(List.of(ids), true));
    }
}
