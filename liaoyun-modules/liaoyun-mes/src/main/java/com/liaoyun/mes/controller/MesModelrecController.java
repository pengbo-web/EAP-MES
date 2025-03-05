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
import com.liaoyun.mes.domain.vo.MesModelrecVo;
import com.liaoyun.mes.domain.bo.MesModelrecBo;
import com.liaoyun.mes.service.IMesModelrecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 模具使用记录
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/modelrec")
public class MesModelrecController extends BaseController {

    private final IMesModelrecService mesModelrecService;

    /**
     * 查询模具使用记录列表
     */
    @SaCheckPermission("mes:modelrec:list")
    @GetMapping("/list")
    public TableDataInfo<MesModelrecVo> list(MesModelrecBo bo, PageQuery pageQuery) {
        return mesModelrecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出模具使用记录列表
     */
    @SaCheckPermission("mes:modelrec:export")
    @Log(title = "模具使用记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesModelrecBo bo, HttpServletResponse response) {
        List<MesModelrecVo> list = mesModelrecService.queryList(bo);
        ExcelUtil.exportExcel(list, "模具使用记录", MesModelrecVo.class, response);
    }

    /**
     * 获取模具使用记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:modelrec:query")
    @GetMapping("/{id}")
    public R<MesModelrecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesModelrecService.queryById(id));
    }

    /**
     * 新增模具使用记录
     */
    @SaCheckPermission("mes:modelrec:add")
    @Log(title = "模具使用记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesModelrecBo bo) {
        return toAjax(mesModelrecService.insertByBo(bo));
    }

    /**
     * 修改模具使用记录
     */
    @SaCheckPermission("mes:modelrec:edit")
    @Log(title = "模具使用记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesModelrecBo bo) {
        return toAjax(mesModelrecService.updateByBo(bo));
    }

    /**
     * 删除模具使用记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:modelrec:remove")
    @Log(title = "模具使用记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesModelrecService.deleteWithValidByIds(List.of(ids), true));
    }
}
