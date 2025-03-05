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
import com.liaoyun.mes.domain.vo.MesMURVo;
import com.liaoyun.mes.domain.bo.MesMURBo;
import com.liaoyun.mes.service.IMesMURService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 移动套餐
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/mUR")
public class MesMURController extends BaseController {

    private final IMesMURService mesMURService;

    /**
     * 查询移动套餐列表
     */
    @SaCheckPermission("mes:mUR:list")
    @GetMapping("/list")
    public TableDataInfo<MesMURVo> list(MesMURBo bo, PageQuery pageQuery) {
        return mesMURService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出移动套餐列表
     */
    @SaCheckPermission("mes:mUR:export")
    @Log(title = "移动套餐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesMURBo bo, HttpServletResponse response) {
        List<MesMURVo> list = mesMURService.queryList(bo);
        ExcelUtil.exportExcel(list, "移动套餐", MesMURVo.class, response);
    }

    /**
     * 获取移动套餐详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:mUR:query")
    @GetMapping("/{id}")
    public R<MesMURVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesMURService.queryById(id));
    }

    /**
     * 新增移动套餐
     */
    @SaCheckPermission("mes:mUR:add")
    @Log(title = "移动套餐", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesMURBo bo) {
        return toAjax(mesMURService.insertByBo(bo));
    }

    /**
     * 修改移动套餐
     */
    @SaCheckPermission("mes:mUR:edit")
    @Log(title = "移动套餐", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesMURBo bo) {
        return toAjax(mesMURService.updateByBo(bo));
    }

    /**
     * 删除移动套餐
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:mUR:remove")
    @Log(title = "移动套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesMURService.deleteWithValidByIds(List.of(ids), true));
    }
}
