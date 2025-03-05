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
import com.liaoyun.mes.domain.vo.MesProtypeVo;
import com.liaoyun.mes.domain.bo.MesProtypeBo;
import com.liaoyun.mes.service.IMesProtypeService;

/**
 * ②产品类别
 *
 * @author Kylin W
 * @date 2024-05-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/protype")
public class MesProtypeController extends BaseController {

    private final IMesProtypeService mesProtypeService;

    /**
     * 查询②产品类别列表
     */
    @SaCheckPermission("mes:protype:list")
    @GetMapping("/list")
    public R<List<MesProtypeVo>> list(MesProtypeBo bo) {
        List<MesProtypeVo> list = mesProtypeService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 导出②产品类别列表
     */
    @SaCheckPermission("mes:protype:export")
    @Log(title = "②产品类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesProtypeBo bo, HttpServletResponse response) {
        List<MesProtypeVo> list = mesProtypeService.queryList(bo);
        ExcelUtil.exportExcel(list, "②产品类别", MesProtypeVo.class, response);
    }

    /**
     * 获取②产品类别详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:protype:query")
    @GetMapping("/{id}")
    public R<MesProtypeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesProtypeService.queryById(id));
    }

    /**
     * 新增②产品类别
     */
    @SaCheckPermission("mes:protype:add")
    @Log(title = "②产品类别", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesProtypeBo bo) {
        return toAjax(mesProtypeService.insertByBo(bo));
    }

    /**
     * 修改②产品类别
     */
    @SaCheckPermission("mes:protype:edit")
    @Log(title = "②产品类别", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesProtypeBo bo) {
        return toAjax(mesProtypeService.updateByBo(bo));
    }

    /**
     * 删除②产品类别
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:protype:remove")
    @Log(title = "②产品类别", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesProtypeService.deleteWithValidByIds(List.of(ids), true));
    }
}
