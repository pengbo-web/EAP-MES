package com.liaoyun.mes.controller;

import java.util.List;

import com.liaoyun.mes.domain.MesOutorder;
import com.liaoyun.mes.domain.MesOutstock;
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
import com.liaoyun.mes.domain.vo.MesOrderVo;
import com.liaoyun.mes.domain.bo.MesOrderBo;
import com.liaoyun.mes.service.IMesOrderService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 销售订单
 *
 * @author Liao Liao
 * @date 2024-11-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/order")
public class MesOrderController extends BaseController {

    private final IMesOrderService mesOrderService;

    /**
     * 查询销售订单列表
     */
    @SaCheckPermission("mes:order:list")
    @GetMapping("/list")
    public TableDataInfo<MesOrderVo> list(MesOrderBo bo, PageQuery pageQuery) {
        return mesOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出销售订单列表
     */
    @SaCheckPermission("mes:order:export")
    @Log(title = "销售订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOrderBo bo, HttpServletResponse response) {
        List<MesOrderVo> list = mesOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "销售订单", MesOrderVo.class, response);
    }

    @GetMapping("/bequick/{id}")
    public R<String> bequick(@PathVariable Long id) {
        mesOrderService.bequick(id);
       return R.ok("成功");
    }

    /**
     * 废除
     * @param id
     */
    @GetMapping("/getlc/{id}")
    public void getsor(@PathVariable Long id){
        //获取订单成本
        //一、委外与原材料出库
        MesOrderVo vo = mesOrderService.queryById(id);
        MesOutstock outstock = new MesOutstock();
        MesOutorder outorder=new MesOutorder();

    }
    /**
     * 获取销售订单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:order:query")
    @GetMapping("/{id}")
    public R<MesOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOrderService.queryById(id));
    }

    /**
     * 新增销售订单
     */
    @SaCheckPermission("mes:order:add")
    @Log(title = "销售订单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesOrderBo bo) {
        return toAjax(mesOrderService.insertByBo(bo));
    }

    /**
     * 修改销售订单
     */
    @SaCheckPermission("mes:order:edit")
    @Log(title = "销售订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOrderBo bo) {
        return toAjax(mesOrderService.updateByBo(bo));
    }

    /**
     * 删除销售订单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:order:remove")
    @Log(title = "销售订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOrderService.deleteWithValidByIds(List.of(ids), true));
    }
}
