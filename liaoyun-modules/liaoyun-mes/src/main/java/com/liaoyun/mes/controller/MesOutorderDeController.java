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
import com.liaoyun.mes.domain.vo.MesOutorderDeVo;
import com.liaoyun.mes.domain.bo.MesOutorderDeBo;
import com.liaoyun.mes.service.IMesOutorderDeService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 委外详情
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/outorderDe")
public class MesOutorderDeController extends BaseController {

    private final IMesOutorderDeService mesOutorderDeService;

    /**
     * 查询委外详情列表
     */
    @SaCheckPermission("mes:outorderDe:list")
    @GetMapping("/list")
    public TableDataInfo<MesOutorderDeVo> list(MesOutorderDeBo bo, PageQuery pageQuery) {
        return mesOutorderDeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出委外详情列表
     */
    @SaCheckPermission("mes:outorderDe:export")
    @Log(title = "委外详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOutorderDeBo bo, HttpServletResponse response) {
        List<MesOutorderDeVo> list = mesOutorderDeService.queryList(bo);
        ExcelUtil.exportExcel(list, "委外详情", MesOutorderDeVo.class, response);
    }

    /**
     * 获取委外详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:outorderDe:query")
    @GetMapping("/{id}")
    public R<MesOutorderDeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOutorderDeService.queryById(id));
    }

    /**
     * 新增委外详情
     */
    @SaCheckPermission("mes:outorderDe:add")
    @Log(title = "委外详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody MesOutorderDeBo bo) {
        return R.ok(mesOutorderDeService.insertByBo(bo));
    }

    /**
     * 修改委外详情
     */
    @SaCheckPermission("mes:outorderDe:edit")
    @Log(title = "委外详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOutorderDeBo bo) {
        return toAjax(mesOutorderDeService.updateByBo(bo));
    }

    /**
     * 删除委外详情
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:outorderDe:remove")
    @Log(title = "委外详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOutorderDeService.deleteWithValidByIds(List.of(ids), true));
    }
}
