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
import com.liaoyun.mes.domain.vo.MesProcessTypeVo;
import com.liaoyun.mes.domain.bo.MesProcessTypeBo;
import com.liaoyun.mes.service.IMesProcessTypeService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * ⑦工艺类别
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/processType")
public class MesProcessTypeController extends BaseController {

    private final IMesProcessTypeService mesProcessTypeService;

    /**
     * 查询⑦工艺类别列表
     */
    @SaCheckPermission("mes:processType:list")
    @GetMapping("/list")
    public TableDataInfo<MesProcessTypeVo> list(MesProcessTypeBo bo, PageQuery pageQuery) {
        return mesProcessTypeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出⑦工艺类别列表
     */
    @SaCheckPermission("mes:processType:export")
    @Log(title = "⑦工艺类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesProcessTypeBo bo, HttpServletResponse response) {
        List<MesProcessTypeVo> list = mesProcessTypeService.queryList(bo);
        ExcelUtil.exportExcel(list, "⑦工艺类别", MesProcessTypeVo.class, response);
    }

    /**
     * 获取⑦工艺类别详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:processType:query")
    @GetMapping("/{id}")
    public R<MesProcessTypeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesProcessTypeService.queryById(id));
    }

    /**
     * 新增⑦工艺类别
     */
    @SaCheckPermission("mes:processType:add")
    @Log(title = "⑦工艺类别", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesProcessTypeBo bo) {
        return toAjax(mesProcessTypeService.insertByBo(bo));
    }

    /**
     * 修改⑦工艺类别
     */
    @SaCheckPermission("mes:processType:edit")
    @Log(title = "⑦工艺类别", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesProcessTypeBo bo) {
        return toAjax(mesProcessTypeService.updateByBo(bo));
    }

    /**
     * 删除⑦工艺类别
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:processType:remove")
    @Log(title = "⑦工艺类别", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesProcessTypeService.deleteWithValidByIds(List.of(ids), true));
    }
}
