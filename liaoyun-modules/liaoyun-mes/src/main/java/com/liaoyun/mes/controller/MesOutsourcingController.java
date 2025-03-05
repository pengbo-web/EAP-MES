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
import com.liaoyun.mes.domain.vo.MesOutsourcingVo;
import com.liaoyun.mes.domain.bo.MesOutsourcingBo;
import com.liaoyun.mes.service.IMesOutsourcingService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 外协列
 *
 * @author Liao Liao
 * @date 2024-07-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/outsourcing")
public class MesOutsourcingController extends BaseController {

    private final IMesOutsourcingService mesOutsourcingService;

    /**
     * 查询外协列列表
     */
    @SaCheckPermission("mes:outsourcing:list")
    @GetMapping("/list")
    public TableDataInfo<MesOutsourcingVo> list(MesOutsourcingBo bo, PageQuery pageQuery) {
        return mesOutsourcingService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出外协列列表
     */
    @SaCheckPermission("mes:outsourcing:export")
    @Log(title = "外协列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOutsourcingBo bo, HttpServletResponse response) {
        List<MesOutsourcingVo> list = mesOutsourcingService.queryList(bo);
        ExcelUtil.exportExcel(list, "外协列", MesOutsourcingVo.class, response);
    }

    /**
     * 获取外协列详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:outsourcing:query")
    @GetMapping("/{id}")
    public R<MesOutsourcingVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOutsourcingService.queryById(id));
    }

    /**
     * 新增外协列
     */
    @SaCheckPermission("mes:outsourcing:add")
    @Log(title = "外协列", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesOutsourcingBo bo) {
        return toAjax(mesOutsourcingService.insertByBo(bo));
    }

    /**
     * 修改外协列
     */
    @SaCheckPermission("mes:outsourcing:edit")
    @Log(title = "外协列", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOutsourcingBo bo) {
        return toAjax(mesOutsourcingService.updateByBo(bo));
    }

    /**
     * 删除外协列
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:outsourcing:remove")
    @Log(title = "外协列", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOutsourcingService.deleteWithValidByIds(List.of(ids), true));
    }
}
