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
import com.liaoyun.mes.domain.vo.MesYcrecVo;
import com.liaoyun.mes.domain.bo.MesYcrecBo;
import com.liaoyun.mes.service.IMesYcrecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 异常报工审核
 *
 * @author Liao Liao
 * @date 2024-11-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/ycrec")
public class MesYcrecController extends BaseController {

    private final IMesYcrecService mesYcrecService;

    /**
     * 查询异常报工审核列表
     */
    @SaCheckPermission("mes:ycrec:list")
    @GetMapping("/list")
    public TableDataInfo<MesYcrecVo> list(MesYcrecBo bo, PageQuery pageQuery) {
        return mesYcrecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出异常报工审核列表
     */
    @SaCheckPermission("mes:ycrec:export")
    @Log(title = "异常报工审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesYcrecBo bo, HttpServletResponse response) {
        List<MesYcrecVo> list = mesYcrecService.queryList(bo);
        ExcelUtil.exportExcel(list, "异常报工审核", MesYcrecVo.class, response);
    }

    /**
     * 获取异常报工审核详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:ycrec:query")
    @GetMapping("/{id}")
    public R<MesYcrecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesYcrecService.queryById(id));
    }

    /**
     * 新增异常报工审核
     */
    @SaCheckPermission("mes:ycrec:add")
    @Log(title = "异常报工审核", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesYcrecBo bo) {
        return toAjax(mesYcrecService.insertByBo(bo));
    }

    /**
     * 修改异常报工审核
     */
    @SaCheckPermission("mes:ycrec:edit")
    @Log(title = "异常报工审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesYcrecBo bo) {
        return toAjax(mesYcrecService.updateByBo(bo));
    }

    /**
     * 删除异常报工审核
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:ycrec:remove")
    @Log(title = "异常报工审核", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesYcrecService.deleteWithValidByIds(List.of(ids), true));
    }
}
