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
import com.liaoyun.mes.domain.vo.MesOutorderVo;
import com.liaoyun.mes.domain.bo.MesOutorderBo;
import com.liaoyun.mes.service.IMesOutorderService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 委外订单
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/outorder")
public class MesOutorderController extends BaseController {

    private final IMesOutorderService mesOutorderService;

    /**
     * 查询委外订单列表
     */
    @SaCheckPermission("mes:outorder:list")
    @GetMapping("/list")
    public TableDataInfo<MesOutorderVo> list(MesOutorderBo bo, PageQuery pageQuery) {
        return mesOutorderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出委外订单列表
     */
    @SaCheckPermission("mes:outorder:export")
    @Log(title = "委外订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOutorderBo bo, HttpServletResponse response) {
        List<MesOutorderVo> list = mesOutorderService.queryList(bo);
        ExcelUtil.exportExcel(list, "委外订单", MesOutorderVo.class, response);
    }

    /**
     * 获取委外订单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:outorder:query")
    @GetMapping("/{id}")
    public R<MesOutorderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOutorderService.queryById(id));
    }
    @GetMapping("/devo/{id}")
    public R<MesOutorderVo> getdevoInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(mesOutorderService.queryById(id));
    }

    /**
     * 新增委外订单
     */
    @SaCheckPermission("mes:outorder:add")
    @Log(title = "委外订单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesOutorderBo bo) {
        return toAjax(mesOutorderService.insertByBo(bo));
    }

    /**
     * 修改委外订单
     */
    @SaCheckPermission("mes:outorder:edit")
    @Log(title = "委外订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOutorderBo bo) {
        return toAjax(mesOutorderService.updateByBo(bo));
    }

    /**
     * 删除委外订单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:outorder:remove")
    @Log(title = "委外订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOutorderService.deleteWithValidByIds(List.of(ids), true));
    }
}
