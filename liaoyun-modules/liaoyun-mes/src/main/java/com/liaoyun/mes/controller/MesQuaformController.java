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
import com.liaoyun.mes.domain.vo.MesQuaformVo;
import com.liaoyun.mes.domain.bo.MesQuaformBo;
import com.liaoyun.mes.service.IMesQuaformService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 来料检
 *
 * @author Liao Liao
 * @date 2024-10-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/quaform")
public class MesQuaformController extends BaseController {

    private final IMesQuaformService mesQuaformService;

    /**
     * 查询来料检列表
     */
    @SaCheckPermission("mes:quaform:list")
    @GetMapping("/list")
    public TableDataInfo<MesQuaformVo> list(MesQuaformBo bo, PageQuery pageQuery) {
        return mesQuaformService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出来料检列表
     */
    @SaCheckPermission("mes:quaform:export")
    @Log(title = "来料检", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesQuaformBo bo, HttpServletResponse response) {
        List<MesQuaformVo> list = mesQuaformService.queryList(bo);
        ExcelUtil.exportExcel(list, "来料检", MesQuaformVo.class, response);
    }

    /**
     * 获取来料检详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:quaform:query")
    @GetMapping("/{id}")
    public R<MesQuaformVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesQuaformService.queryById(id));
    }

    /**
     * 新增来料检
     */
    @SaCheckPermission("mes:quaform:add")
    @Log(title = "来料检", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesQuaformBo bo) {
        return toAjax(mesQuaformService.insertByBo(bo));
    }

    /**
     * 修改来料检
     */
    @SaCheckPermission("mes:quaform:edit")
    @Log(title = "来料检", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesQuaformBo bo) {
        return toAjax(mesQuaformService.updateByBo(bo));
    }

    /**
     * 删除来料检
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:quaform:remove")
    @Log(title = "来料检", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesQuaformService.deleteWithValidByIds(List.of(ids), true));
    }
}
