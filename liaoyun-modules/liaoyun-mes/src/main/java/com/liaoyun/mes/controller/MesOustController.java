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
import com.liaoyun.mes.domain.vo.MesOustVo;
import com.liaoyun.mes.domain.bo.MesOustBo;
import com.liaoyun.mes.service.IMesOustService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 其他出库
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/oust")
public class MesOustController extends BaseController {

    private final IMesOustService mesOustService;

    /**
     * 查询其他出库列表
     */
    @SaCheckPermission("mes:oust:list")
    @GetMapping("/list")
    public TableDataInfo<MesOustVo> list(MesOustBo bo, PageQuery pageQuery) {
        return mesOustService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出其他出库列表
     */
    @SaCheckPermission("mes:oust:export")
    @Log(title = "其他出库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOustBo bo, HttpServletResponse response) {
        List<MesOustVo> list = mesOustService.queryList(bo);
        ExcelUtil.exportExcel(list, "其他出库", MesOustVo.class, response);
    }

    /**
     * 获取其他出库详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:oust:query")
    @GetMapping("/{id}")
    public R<MesOustVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOustService.queryById(id));
    }

    /**
     * 新增其他出库
     */
    @SaCheckPermission("mes:oust:add")
    @Log(title = "其他出库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesOustBo bo) {
        return toAjax(mesOustService.insertByBo(bo));
    }

    /**
     * 修改其他出库
     */
    @SaCheckPermission("mes:oust:edit")
    @Log(title = "其他出库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOustBo bo) {
        return toAjax(mesOustService.updateByBo(bo));
    }

    /**
     * 删除其他出库
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:oust:remove")
    @Log(title = "其他出库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOustService.deleteWithValidByIds(List.of(ids), true));
    }
}
