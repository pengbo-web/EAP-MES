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
import com.liaoyun.mes.domain.vo.MesEquCheckVo;
import com.liaoyun.mes.domain.bo.MesEquCheckBo;
import com.liaoyun.mes.service.IMesEquCheckService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 设备点检
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/equCheck")
public class MesEquCheckController extends BaseController {

    private final IMesEquCheckService mesEquCheckService;

    /**
     * 查询设备点检列表
     */
    @SaCheckPermission("mes:equCheck:list")
    @GetMapping("/list")
    public TableDataInfo<MesEquCheckVo> list(MesEquCheckBo bo, PageQuery pageQuery) {
        return mesEquCheckService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备点检列表
     */
    @SaCheckPermission("mes:equCheck:export")
    @Log(title = "设备点检", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesEquCheckBo bo, HttpServletResponse response) {
        List<MesEquCheckVo> list = mesEquCheckService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备点检", MesEquCheckVo.class, response);
    }

    /**
     * 获取设备点检详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:equCheck:query")
    @GetMapping("/{id}")
    public R<MesEquCheckVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesEquCheckService.queryById(id));
    }

    /**
     * 新增设备点检
     */
    @SaCheckPermission("mes:equCheck:add")
    @Log(title = "设备点检", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesEquCheckBo bo) {
        return toAjax(mesEquCheckService.insertByBo(bo));
    }

    /**
     * 修改设备点检
     */
    @SaCheckPermission("mes:equCheck:edit")
    @Log(title = "设备点检", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesEquCheckBo bo) {
        return toAjax(mesEquCheckService.updateByBo(bo));
    }

    /**
     * 删除设备点检
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:equCheck:remove")
    @Log(title = "设备点检", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesEquCheckService.deleteWithValidByIds(List.of(ids), true));
    }
}
