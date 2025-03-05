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
import com.liaoyun.mes.domain.vo.MesQuaRecVo;
import com.liaoyun.mes.domain.bo.MesQuaRecBo;
import com.liaoyun.mes.service.IMesQuaRecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 质检列表
 *
 * @author Liao Liao
 * @date 2024-07-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/quaRec")
public class MesQuaRecController extends BaseController {

    private final IMesQuaRecService mesQuaRecService;

    /**
     * 查询质检列表列表
     */
    @SaCheckPermission("mes:quaRec:list")
    @GetMapping("/list")
    public TableDataInfo<MesQuaRecVo> list(MesQuaRecBo bo, PageQuery pageQuery) {
        return mesQuaRecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出质检列表列表
     */
    @SaCheckPermission("mes:quaRec:export")
    @Log(title = "质检列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesQuaRecBo bo, HttpServletResponse response) {
        List<MesQuaRecVo> list = mesQuaRecService.queryList(bo);
        ExcelUtil.exportExcel(list, "质检列表", MesQuaRecVo.class, response);
    }

    /**
     * 获取质检列表详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:quaRec:query")
    @GetMapping("/{id}")
    public R<MesQuaRecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesQuaRecService.queryById(id));
    }

    /**
     * 新增质检列表
     */
    @SaCheckPermission("mes:quaRec:add")
    @Log(title = "质检列表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesQuaRecBo bo) {
        return toAjax(mesQuaRecService.insertByBo(bo));
    }

    /**
     * 修改质检列表
     */
    @SaCheckPermission("mes:quaRec:edit")
    @Log(title = "质检列表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesQuaRecBo bo) {
        return toAjax(mesQuaRecService.updateByBo(bo));
    }

    /**
     * 删除质检列表
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:quaRec:remove")
    @Log(title = "质检列表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesQuaRecService.deleteWithValidByIds(List.of(ids), true));
    }
}
