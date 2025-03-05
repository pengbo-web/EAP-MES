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
import com.liaoyun.mes.domain.vo.MesStocktakingVo;
import com.liaoyun.mes.domain.bo.MesStocktakingBo;
import com.liaoyun.mes.service.IMesStocktakingService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 库位盘点
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/stocktaking")
public class MesStocktakingController extends BaseController {

    private final IMesStocktakingService mesStocktakingService;

    /**
     * 查询库位盘点列表
     */
    @SaCheckPermission("mes:stocktaking:list")
    @GetMapping("/list")
    public TableDataInfo<MesStocktakingVo> list(MesStocktakingBo bo, PageQuery pageQuery) {
        return mesStocktakingService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出库位盘点列表
     */
    @SaCheckPermission("mes:stocktaking:export")
    @Log(title = "库位盘点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesStocktakingBo bo, HttpServletResponse response) {
        List<MesStocktakingVo> list = mesStocktakingService.queryList(bo);
        ExcelUtil.exportExcel(list, "库位盘点", MesStocktakingVo.class, response);
    }

    /**
     * 获取库位盘点详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:stocktaking:query")
    @GetMapping("/{id}")
    public R<MesStocktakingVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesStocktakingService.queryById(id));
    }

    /**
     * 新增库位盘点
     */
    @SaCheckPermission("mes:stocktaking:add")
    @Log(title = "库位盘点", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesStocktakingBo bo) {
        return toAjax(mesStocktakingService.insertByBo(bo));
    }

    /**
     * 修改库位盘点
     */
    @SaCheckPermission("mes:stocktaking:edit")
    @Log(title = "库位盘点", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesStocktakingBo bo) {
        return toAjax(mesStocktakingService.updateByBo(bo));
    }

    /**
     * 删除库位盘点
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:stocktaking:remove")
    @Log(title = "库位盘点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesStocktakingService.deleteWithValidByIds(List.of(ids), true));
    }
}
