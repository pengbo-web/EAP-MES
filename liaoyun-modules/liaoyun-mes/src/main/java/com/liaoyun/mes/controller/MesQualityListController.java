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
import com.liaoyun.mes.domain.vo.MesQualityListVo;
import com.liaoyun.mes.domain.bo.MesQualityListBo;
import com.liaoyun.mes.service.IMesQualityListService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 质检内容
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/qualityList")
public class MesQualityListController extends BaseController {

    private final IMesQualityListService mesQualityListService;

    /**
     * 查询质检内容列表
     */
    @SaCheckPermission("mes:qualityList:list")
    @GetMapping("/list")
    public TableDataInfo<MesQualityListVo> list(MesQualityListBo bo, PageQuery pageQuery) {
        return mesQualityListService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出质检内容列表
     */
    @SaCheckPermission("mes:qualityList:export")
    @Log(title = "质检内容", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesQualityListBo bo, HttpServletResponse response) {
        List<MesQualityListVo> list = mesQualityListService.queryList(bo);
        ExcelUtil.exportExcel(list, "质检内容", MesQualityListVo.class, response);
    }

    /**
     * 获取质检内容详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:qualityList:query")
    @GetMapping("/{id}")
    public R<MesQualityListVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesQualityListService.queryById(id));
    }

    /**
     * 新增质检内容
     */
    @SaCheckPermission("mes:qualityList:add")
    @Log(title = "质检内容", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesQualityListBo bo) {
        return toAjax(mesQualityListService.insertByBo(bo));
    }

    /**
     * 修改质检内容
     */
    @SaCheckPermission("mes:qualityList:edit")
    @Log(title = "质检内容", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesQualityListBo bo) {
        return toAjax(mesQualityListService.updateByBo(bo));
    }

    /**
     * 删除质检内容
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:qualityList:remove")
    @Log(title = "质检内容", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesQualityListService.deleteWithValidByIds(List.of(ids), true));
    }
}
