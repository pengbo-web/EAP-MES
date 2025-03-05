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
import com.liaoyun.mes.domain.vo.MesTaskcVo;
import com.liaoyun.mes.domain.bo.MesTaskcBo;
import com.liaoyun.mes.service.IMesTaskcService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 组装流转
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/taskc")
public class MesTaskcController extends BaseController {

    private final IMesTaskcService mesTaskcService;

    /**
     * 查询组装流转列表
     */
    @GetMapping("/list")
    public TableDataInfo<MesTaskcVo> list(MesTaskcBo bo, PageQuery pageQuery) {
        return mesTaskcService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出组装流转列表
     */
    @SaCheckPermission("mes:taskc:export")
    @Log(title = "组装流转", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesTaskcBo bo, HttpServletResponse response) {
        List<MesTaskcVo> list = mesTaskcService.queryList(bo);
        ExcelUtil.exportExcel(list, "组装流转", MesTaskcVo.class, response);
    }

    /**
     * 获取组装流转详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:taskc:query")
    @GetMapping("/{id}")
    public R<MesTaskcVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesTaskcService.queryById(id));
    }

    /**
     * 新增组装流转
     */
    @SaCheckPermission("mes:taskc:add")
    @Log(title = "组装流转", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesTaskcBo bo) {
        return toAjax(mesTaskcService.insertByBo(bo));
    }

    /**
     * 修改组装流转
     */
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesTaskcBo bo) {
        return toAjax(mesTaskcService.updateByBo(bo));
    }

    /**
     * 删除组装流转
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:taskc:remove")
    @Log(title = "组装流转", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesTaskcService.deleteWithValidByIds(List.of(ids), true));
    }
}
