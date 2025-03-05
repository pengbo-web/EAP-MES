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
import com.liaoyun.mes.domain.vo.MesStockTypeVo;
import com.liaoyun.mes.domain.bo.MesStockTypeBo;
import com.liaoyun.mes.service.IMesStockTypeService;

/**
 * 库存分类
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/stockType")
public class MesStockTypeController extends BaseController {

    private final IMesStockTypeService mesStockTypeService;

    /**
     * 查询库存分类列表
     */
    @SaCheckPermission("mes:stockType:list")
    @GetMapping("/list")
    public R<List<MesStockTypeVo>> list(MesStockTypeBo bo) {
        List<MesStockTypeVo> list = mesStockTypeService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 导出库存分类列表
     */
    @SaCheckPermission("mes:stockType:export")
    @Log(title = "库存分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesStockTypeBo bo, HttpServletResponse response) {
        List<MesStockTypeVo> list = mesStockTypeService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存分类", MesStockTypeVo.class, response);
    }

    /**
     * 获取库存分类详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:stockType:query")
    @GetMapping("/{id}")
    public R<MesStockTypeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesStockTypeService.queryById(id));
    }

    /**
     * 新增库存分类
     */
    @SaCheckPermission("mes:stockType:add")
    @Log(title = "库存分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesStockTypeBo bo) {
        return toAjax(mesStockTypeService.insertByBo(bo));
    }

    /**
     * 修改库存分类
     */
    @SaCheckPermission("mes:stockType:edit")
    @Log(title = "库存分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesStockTypeBo bo) {
        return toAjax(mesStockTypeService.updateByBo(bo));
    }

    /**
     * 删除库存分类
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:stockType:remove")
    @Log(title = "库存分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesStockTypeService.deleteWithValidByIds(List.of(ids), true));
    }
}
