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
import com.liaoyun.mes.domain.vo.MesEquipmentVo;
import com.liaoyun.mes.domain.bo.MesEquipmentBo;
import com.liaoyun.mes.service.IMesEquipmentService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 设备列表
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/equipment")
public class MesEquipmentController extends BaseController {

    private final IMesEquipmentService mesEquipmentService;

    /**
     * 查询设备列表列表
     */
    @SaCheckPermission("mes:equipment:list")
    @GetMapping("/list")
    public TableDataInfo<MesEquipmentVo> list(MesEquipmentBo bo, PageQuery pageQuery) {
        return mesEquipmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备列表列表
     */
    @SaCheckPermission("mes:equipment:export")
    @Log(title = "设备列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesEquipmentBo bo, HttpServletResponse response) {
        List<MesEquipmentVo> list = mesEquipmentService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备列表", MesEquipmentVo.class, response);
    }

    /**
     * 获取设备列表详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:equipment:query")
    @GetMapping("/{id}")
    public R<MesEquipmentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesEquipmentService.queryById(id));
    }

    /**
     * 新增设备列表
     */
    @SaCheckPermission("mes:equipment:add")
    @Log(title = "设备列表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesEquipmentBo bo) {
        return toAjax(mesEquipmentService.insertByBo(bo));
    }

    /**
     * 修改设备列表
     */
    @SaCheckPermission("mes:equipment:edit")
    @Log(title = "设备列表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesEquipmentBo bo) {
        return toAjax(mesEquipmentService.updateByBo(bo));
    }

    /**
     * 删除设备列表
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:equipment:remove")
    @Log(title = "设备列表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesEquipmentService.deleteWithValidByIds(List.of(ids), true));
    }
}
