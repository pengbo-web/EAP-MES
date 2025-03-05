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
import com.liaoyun.mes.domain.vo.MesEqucheckListVo;
import com.liaoyun.mes.domain.bo.MesEqucheckListBo;
import com.liaoyun.mes.service.IMesEqucheckListService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 设备点检列
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/equcheckList")
public class MesEqucheckListController extends BaseController {

    private final IMesEqucheckListService mesEqucheckListService;

    /**
     * 查询设备点检列列表
     */
    @SaCheckPermission("mes:equcheckList:list")
    @GetMapping("/list")
    public TableDataInfo<MesEqucheckListVo> list(MesEqucheckListBo bo, PageQuery pageQuery) {
        return mesEqucheckListService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备点检列列表
     */
    @SaCheckPermission("mes:equcheckList:export")
    @Log(title = "设备点检列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesEqucheckListBo bo, HttpServletResponse response) {
        List<MesEqucheckListVo> list = mesEqucheckListService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备点检列", MesEqucheckListVo.class, response);
    }

    /**
     * 获取设备点检列详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:equcheckList:query")
    @GetMapping("/{id}")
    public R<MesEqucheckListVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesEqucheckListService.queryById(id));
    }

    /**
     * 新增设备点检列
     */
    @SaCheckPermission("mes:equcheckList:add")
    @Log(title = "设备点检列", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesEqucheckListBo bo) {
        return toAjax(mesEqucheckListService.insertByBo(bo));
    }

    /**
     * 修改设备点检列
     */
    @SaCheckPermission("mes:equcheckList:edit")
    @Log(title = "设备点检列", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesEqucheckListBo bo) {
        return toAjax(mesEqucheckListService.updateByBo(bo));
    }

    /**
     * 删除设备点检列
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:equcheckList:remove")
    @Log(title = "设备点检列", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesEqucheckListService.deleteWithValidByIds(List.of(ids), true));
    }
}
