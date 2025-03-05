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
import com.liaoyun.mes.domain.vo.MesEquipmentTypeVo;
import com.liaoyun.mes.domain.bo.MesEquipmentTypeBo;
import com.liaoyun.mes.service.IMesEquipmentTypeService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * ⑤设备类别
 *
 * @author Kylin
 * @date 2024-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/equipmentType")
public class MesEquipmentTypeController extends BaseController {

    private final IMesEquipmentTypeService mesEquipmentTypeService;

    /**
     * 查询⑤设备类别列表
     */
    @SaCheckPermission("mes:equipmentType:list")
    @GetMapping("/list")
    public TableDataInfo<MesEquipmentTypeVo> list(MesEquipmentTypeBo bo, PageQuery pageQuery) {
        return mesEquipmentTypeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出⑤设备类别列表
     */
    @SaCheckPermission("mes:equipmentType:export")
    @Log(title = "⑤设备类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesEquipmentTypeBo bo, HttpServletResponse response) {
        List<MesEquipmentTypeVo> list = mesEquipmentTypeService.queryList(bo);
        ExcelUtil.exportExcel(list, "⑤设备类别", MesEquipmentTypeVo.class, response);
    }

    /**
     * 获取⑤设备类别详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:equipmentType:query")
    @GetMapping("/{id}")
    public R<MesEquipmentTypeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesEquipmentTypeService.queryById(id));
    }

    /**
     * 新增⑤设备类别
     */
    @SaCheckPermission("mes:equipmentType:add")
    @Log(title = "⑤设备类别", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesEquipmentTypeBo bo) {
        return toAjax(mesEquipmentTypeService.insertByBo(bo));
    }

    /**
     * 修改⑤设备类别
     */
    @SaCheckPermission("mes:equipmentType:edit")
    @Log(title = "⑤设备类别", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesEquipmentTypeBo bo) {
        return toAjax(mesEquipmentTypeService.updateByBo(bo));
    }

    /**
     * 删除⑤设备类别
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:equipmentType:remove")
    @Log(title = "⑤设备类别", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesEquipmentTypeService.deleteWithValidByIds(List.of(ids), true));
    }
}
