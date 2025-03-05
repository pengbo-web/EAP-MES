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
import com.liaoyun.mes.domain.vo.MesCuttingRecVo;
import com.liaoyun.mes.domain.bo.MesCuttingRecBo;
import com.liaoyun.mes.service.IMesCuttingRecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 刀具记录审核
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/cuttingRec")
public class MesCuttingRecController extends BaseController {

    private final IMesCuttingRecService mesCuttingRecService;

    /**
     * 查询刀具记录审核列表
     */
    @SaCheckPermission("mes:cuttingRec:list")
    @GetMapping("/list")
    public TableDataInfo<MesCuttingRecVo> list(MesCuttingRecBo bo, PageQuery pageQuery) {
        return mesCuttingRecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出刀具记录审核列表
     */
    @SaCheckPermission("mes:cuttingRec:export")
    @Log(title = "刀具记录审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesCuttingRecBo bo, HttpServletResponse response) {
        List<MesCuttingRecVo> list = mesCuttingRecService.queryList(bo);
        ExcelUtil.exportExcel(list, "刀具记录审核", MesCuttingRecVo.class, response);
    }

    /**
     * 获取刀具记录审核详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:cuttingRec:query")
    @GetMapping("/{id}")
    public R<MesCuttingRecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesCuttingRecService.queryById(id));
    }

    /**
     * 新增刀具记录审核
     */
    @SaCheckPermission("mes:cuttingRec:add")
    @Log(title = "刀具记录审核", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesCuttingRecBo bo) {
        return toAjax(mesCuttingRecService.insertByBo(bo));
    }

    /**
     * 修改刀具记录审核
     */
    @SaCheckPermission("mes:cuttingRec:edit")
    @Log(title = "刀具记录审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesCuttingRecBo bo) {
        return toAjax(mesCuttingRecService.updateByBo(bo));
    }

    /**
     * 删除刀具记录审核
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:cuttingRec:remove")
    @Log(title = "刀具记录审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesCuttingRecService.deleteWithValidByIds(List.of(ids), true));
    }
}
