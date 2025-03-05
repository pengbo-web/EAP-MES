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
import com.liaoyun.system.domain.vo.MesConfigVo;
import com.liaoyun.system.domain.bo.MesConfigBo;
import com.liaoyun.system.service.IMesConfigService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * MES配置
 *
 * @author Liao Liao
 * @date 2025-02-25
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/mesconfig")
public class MesConfigController extends BaseController {

    private final IMesConfigService mesConfigService;

    /**
     * 查询MES配置列表
     */
    @SaCheckPermission("system:mesconfig:list")
    @GetMapping("/list")
    public TableDataInfo<MesConfigVo> list(MesConfigBo bo, PageQuery pageQuery) {
        return mesConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出MES配置列表
     */
    @SaCheckPermission("system:mesconfig:export")
    @Log(title = "MES配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesConfigBo bo, HttpServletResponse response) {
        List<MesConfigVo> list = mesConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "MES配置", MesConfigVo.class, response);
    }

    /**
     * 获取MES配置详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:mesconfig:query")
    @GetMapping("/{id}")
    public R<MesConfigVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesConfigService.queryById(id));
    }

    /**
     * 新增MES配置
     */
    @SaCheckPermission("system:mesconfig:add")
    @Log(title = "MES配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesConfigBo bo) {
        return toAjax(mesConfigService.insertByBo(bo));
    }

    /**
     * 修改MES配置
     */
    @SaCheckPermission("system:mesconfig:edit")
    @Log(title = "MES配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesConfigBo bo) {
        return toAjax(mesConfigService.updateByBo(bo));
    }

    /**
     * 删除MES配置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:mesconfig:remove")
    @Log(title = "MES配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesConfigService.deleteWithValidByIds(List.of(ids), true));
    }
}
