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
import com.liaoyun.mes.domain.vo.MesQualityMainVo;
import com.liaoyun.mes.domain.bo.MesQualityMainBo;
import com.liaoyun.mes.service.IMesQualityMainService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 质检主表
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/qualityMain/qualityMain")
public class MesQualityMainController extends BaseController {

    private final IMesQualityMainService mesQualityMainService;

    /**
     * 查询质检主表列表
     */
    @SaCheckPermission("qualityMain:qualityMain:list")
    @GetMapping("/list")
    public TableDataInfo<MesQualityMainVo> list(MesQualityMainBo bo, PageQuery pageQuery) {
        return mesQualityMainService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出质检主表列表
     */
    @SaCheckPermission("qualityMain:qualityMain:export")
    @Log(title = "质检主表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesQualityMainBo bo, HttpServletResponse response) {
        List<MesQualityMainVo> list = mesQualityMainService.queryList(bo);
        ExcelUtil.exportExcel(list, "质检主表", MesQualityMainVo.class, response);
    }

    /**
     * 获取质检主表详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("qualityMain:qualityMain:query")
    @GetMapping("/{id}")
    public R<MesQualityMainVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesQualityMainService.queryById(id));
    }

    /**
     * 新增质检主表
     */
    @SaCheckPermission("qualityMain:qualityMain:add")
    @Log(title = "质检主表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesQualityMainBo bo) {
        return toAjax(mesQualityMainService.insertByBo(bo));
    }

    /**
     * 修改质检主表
     */
    @SaCheckPermission("qualityMain:qualityMain:edit")
    @Log(title = "质检主表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesQualityMainBo bo) {
        return toAjax(mesQualityMainService.updateByBo(bo));
    }

    /**
     * 删除质检主表
     *
     * @param ids 主键串
     */
    @SaCheckPermission("qualityMain:qualityMain:remove")
    @Log(title = "质检主表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesQualityMainService.deleteWithValidByIds(List.of(ids), true));
    }
}
