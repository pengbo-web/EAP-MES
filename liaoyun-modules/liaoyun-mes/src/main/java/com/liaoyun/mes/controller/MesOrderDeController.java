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
import com.liaoyun.mes.domain.vo.MesOrderDeVo;
import com.liaoyun.mes.domain.bo.MesOrderDeBo;
import com.liaoyun.mes.service.IMesOrderDeService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 订单详情
 *
 * @author Liao Liao
 * @date 2024-11-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/orderDe")
public class MesOrderDeController extends BaseController {

    private final IMesOrderDeService mesOrderDeService;

    /**
     * 查询订单详情列表
     */
    @SaCheckPermission("mes:orderDe:list")
    @GetMapping("/list")
    public TableDataInfo<MesOrderDeVo> list(MesOrderDeBo bo, PageQuery pageQuery) {
        return mesOrderDeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单详情列表
     */
    @SaCheckPermission("mes:orderDe:export")
    @Log(title = "订单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOrderDeBo bo, HttpServletResponse response) {
        List<MesOrderDeVo> list = mesOrderDeService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单详情", MesOrderDeVo.class, response);
    }

    /**
     * 获取订单详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:orderDe:query")
    @GetMapping("/{id}")
    public R<MesOrderDeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOrderDeService.queryById(id));
    }

    /**
     * 新增订单详情
     */
    @SaCheckPermission("mes:orderDe:add")
    @Log(title = "订单详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody MesOrderDeBo bo) {
        Long id=mesOrderDeService.insertByBo(bo);
        return R.ok(id);
    }

    /**
     * 修改订单详情
     */
    @SaCheckPermission("mes:orderDe:edit")
    @Log(title = "订单详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOrderDeBo bo) {
        return toAjax(mesOrderDeService.updateByBo(bo));
    }

    /**
     * 删除订单详情
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:orderDe:remove")
    @Log(title = "订单详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOrderDeService.deleteWithValidByIds(List.of(ids), true));
    }
}
