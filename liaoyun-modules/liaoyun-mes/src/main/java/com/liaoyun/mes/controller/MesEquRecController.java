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
import com.liaoyun.mes.domain.vo.MesEquRecVo;
import com.liaoyun.mes.domain.bo.MesEquRecBo;
import com.liaoyun.mes.service.IMesEquRecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 设备点检记录
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/equRec")
public class MesEquRecController extends BaseController {

    private final IMesEquRecService mesEquRecService;

    /**
     * 查询设备点检记录列表
     */
    @SaCheckPermission("mes:equRec:list")
    @GetMapping("/list")
    public TableDataInfo<MesEquRecVo> list(MesEquRecBo bo, PageQuery pageQuery) {
        return mesEquRecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备点检记录列表
     */
    @SaCheckPermission("mes:equRec:export")
    @Log(title = "设备点检记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesEquRecBo bo, HttpServletResponse response) {
        List<MesEquRecVo> list = mesEquRecService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备点检记录", MesEquRecVo.class, response);
    }

    /**
     * 获取设备点检记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:equRec:query")
    @GetMapping("/{id}")
    public R<MesEquRecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesEquRecService.queryById(id));
    }

    /**
     * 新增设备点检记录
     */
    @SaCheckPermission("mes:equRec:add")
    @Log(title = "设备点检记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesEquRecBo bo) {
        return toAjax(mesEquRecService.insertByBo(bo));
    }

    /**
     * 修改设备点检记录
     */
    @SaCheckPermission("mes:equRec:edit")
    @Log(title = "设备点检记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesEquRecBo bo) {
        return toAjax(mesEquRecService.updateByBo(bo));
    }

    /**
     * 删除设备点检记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:equRec:remove")
    @Log(title = "设备点检记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesEquRecService.deleteWithValidByIds(List.of(ids), true));
    }
}
