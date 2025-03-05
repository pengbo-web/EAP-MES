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
import com.liaoyun.mes.domain.vo.MesProsettingVo;
import com.liaoyun.mes.domain.bo.MesProsettingBo;
import com.liaoyun.mes.service.IMesProsettingService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 排程设置
 *
 * @author Liao Liao
 * @date 2024-06-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/prosetting")
public class MesProsettingController extends BaseController {

    private final IMesProsettingService mesProsettingService;

    /**
     * 查询排程设置列表
     */
    @GetMapping("/list")
    public TableDataInfo<MesProsettingVo> list(MesProsettingBo bo, PageQuery pageQuery) {
        return mesProsettingService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出排程设置列表
     */
    @Log(title = "排程设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesProsettingBo bo, HttpServletResponse response) {
        List<MesProsettingVo> list = mesProsettingService.queryList(bo);
        ExcelUtil.exportExcel(list, "排程设置", MesProsettingVo.class, response);
    }

    /**
     * 获取排程设置详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:prosetting:query")
    @GetMapping("/{id}")
    public R<MesProsettingVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesProsettingService.queryById(id));
    }

    /**
     * 新增排程设置
     */
    @SaCheckPermission("mes:prosetting:add")
    @Log(title = "排程设置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesProsettingBo bo) {
        return toAjax(mesProsettingService.insertByBo(bo));
    }

    @PostMapping("/ins")
    public R<Void> adds(@RequestBody  MesProsettingBo bo) {
        return toAjax(mesProsettingService.insertByBos(bo));
    }

    /**
     * 修改排程设置
     */
    @SaCheckPermission("mes:prosetting:edit")
    @Log(title = "排程设置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesProsettingBo bo) {
        return toAjax(mesProsettingService.updateByBo(bo));
    }


    /**
     * 删除排程设置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:prosetting:remove")
    @Log(title = "排程设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesProsettingService.deleteWithValidByIds(List.of(ids), true));
    }
}
