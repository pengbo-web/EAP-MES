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
import com.liaoyun.mes.domain.vo.MesCuttingUseVo;
import com.liaoyun.mes.domain.bo.MesCuttingUseBo;
import com.liaoyun.mes.service.IMesCuttingUseService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 刀具使用统计
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/cuttingUse")
public class MesCuttingUseController extends BaseController {

    private final IMesCuttingUseService mesCuttingUseService;

    /**
     * 查询刀具使用统计列表
     */
    @SaCheckPermission("mes:cuttingUse:list")
    @GetMapping("/list")
    public TableDataInfo<MesCuttingUseVo> list(MesCuttingUseBo bo, PageQuery pageQuery) {
        return mesCuttingUseService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出刀具使用统计列表
     */
    @SaCheckPermission("mes:cuttingUse:export")
    @Log(title = "刀具使用统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesCuttingUseBo bo, HttpServletResponse response) {
        List<MesCuttingUseVo> list = mesCuttingUseService.queryList(bo);
        ExcelUtil.exportExcel(list, "刀具使用统计", MesCuttingUseVo.class, response);
    }

    /**
     * 获取刀具使用统计详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:cuttingUse:query")
    @GetMapping("/{id}")
    public R<MesCuttingUseVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesCuttingUseService.queryById(id));
    }

    /**
     * 新增刀具使用统计
     */
    @SaCheckPermission("mes:cuttingUse:add")
    @Log(title = "刀具使用统计", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesCuttingUseBo bo) {
        return toAjax(mesCuttingUseService.insertByBo(bo));
    }

    /**
     * 修改刀具使用统计
     */
    @SaCheckPermission("mes:cuttingUse:edit")
    @Log(title = "刀具使用统计", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesCuttingUseBo bo) {
        return toAjax(mesCuttingUseService.updateByBo(bo));
    }

    /**
     * 删除刀具使用统计
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:cuttingUse:remove")
    @Log(title = "刀具使用统计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesCuttingUseService.deleteWithValidByIds(List.of(ids), true));
    }
}
