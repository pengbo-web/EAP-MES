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
import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;
import com.liaoyun.mes.domain.bo.MesUnitmeasureBo;
import com.liaoyun.mes.service.IMesUnitmeasureService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 计量单位
 *
 * @author Kylin
 * @date 2024-05-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/unitmeasure")
public class MesUnitmeasureController extends BaseController {

    private final IMesUnitmeasureService mesUnitmeasureService;

    /**
     * 查询计量单位列表
     */
    @SaCheckPermission("mes:unitmeasure:list")
    @GetMapping("/list")
    public TableDataInfo<MesUnitmeasureVo> list(MesUnitmeasureBo bo, PageQuery pageQuery) {
        return mesUnitmeasureService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出计量单位列表
     */
    @SaCheckPermission("mes:unitmeasure:export")
    @Log(title = "计量单位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesUnitmeasureBo bo, HttpServletResponse response) {
        List<MesUnitmeasureVo> list = mesUnitmeasureService.queryList(bo);
        ExcelUtil.exportExcel(list, "计量单位", MesUnitmeasureVo.class, response);
    }

    /**
     * 获取计量单位详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:unitmeasure:query")
    @GetMapping("/{id}")
    public R<MesUnitmeasureVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesUnitmeasureService.queryById(id));
    }

    /**
     * 新增计量单位
     */
    @SaCheckPermission("mes:unitmeasure:add")
    @Log(title = "计量单位", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesUnitmeasureBo bo) {
        return toAjax(mesUnitmeasureService.insertByBo(bo));
    }

    /**
     * 修改计量单位
     */
    @SaCheckPermission("mes:unitmeasure:edit")
    @Log(title = "计量单位", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesUnitmeasureBo bo) {
        return toAjax(mesUnitmeasureService.updateByBo(bo));
    }

    /**
     * 删除计量单位
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:unitmeasure:remove")
    @Log(title = "计量单位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesUnitmeasureService.deleteWithValidByIds(List.of(ids), true));
    }
}
