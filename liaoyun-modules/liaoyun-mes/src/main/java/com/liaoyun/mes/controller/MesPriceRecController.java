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
import com.liaoyun.mes.domain.vo.MesPriceRecVo;
import com.liaoyun.mes.domain.bo.MesPriceRecBo;
import com.liaoyun.mes.service.IMesPriceRecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 价格波动
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/priceRec")
public class MesPriceRecController extends BaseController {

    private final IMesPriceRecService mesPriceRecService;

    /**
     * 查询价格波动列表
     */
    @SaCheckPermission("mes:priceRec:list")
    @GetMapping("/list")
    public TableDataInfo<MesPriceRecVo> list(MesPriceRecBo bo, PageQuery pageQuery) {
        return mesPriceRecService.queryPageList(bo, pageQuery);
    }


    /**
     * 导出价格波动列表
     */
    @SaCheckPermission("mes:priceRec:export")
    @Log(title = "价格波动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesPriceRecBo bo, HttpServletResponse response) {
        List<MesPriceRecVo> list = mesPriceRecService.queryList(bo);
        ExcelUtil.exportExcel(list, "价格波动", MesPriceRecVo.class, response);
    }

    /**
     * 获取价格波动详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:priceRec:query")
    @GetMapping("/{id}")
    public R<MesPriceRecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesPriceRecService.queryById(id));
    }

    /**
     * 新增价格波动
     */
    @SaCheckPermission("mes:priceRec:add")
    @Log(title = "价格波动", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesPriceRecBo bo) {
        return toAjax(mesPriceRecService.insertByBo(bo));
    }

    /**
     * 修改价格波动
     */
    @SaCheckPermission("mes:priceRec:edit")
    @Log(title = "价格波动", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesPriceRecBo bo) {
        return toAjax(mesPriceRecService.updateByBo(bo));
    }

    /**
     * 删除价格波动
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:priceRec:remove")
    @Log(title = "价格波动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesPriceRecService.deleteWithValidByIds(List.of(ids), true));
    }
}
