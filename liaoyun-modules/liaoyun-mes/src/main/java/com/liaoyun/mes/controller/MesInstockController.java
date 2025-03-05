package com.liaoyun. mes.controller;

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
import com.liaoyun. mes.domain.vo.MesInstockVo;
import com.liaoyun. mes.domain.bo.MesInstockBo;
import com.liaoyun. mes.service.IMesInstockService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 入库信息
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/instock")
public class MesInstockController extends BaseController {

    private final IMesInstockService mesInstockService;

    /**
     * 查询入库信息列表
     */
    @SaCheckPermission("mes:instock:list")
    @GetMapping("/list")
    public TableDataInfo<MesInstockVo> list(MesInstockBo bo, PageQuery pageQuery) {
        return mesInstockService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出入库信息列表
     */
    @SaCheckPermission("mes:instock:export")
    @Log(title = "入库信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesInstockBo bo, HttpServletResponse response) {
        List<MesInstockVo> list = mesInstockService.queryList(bo);
        ExcelUtil.exportExcel(list, "入库信息", MesInstockVo.class, response);
    }

    /**
     * 获取入库信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:instock:query")
    @GetMapping("/{id}")
    public R<MesInstockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesInstockService.queryById(id));
    }

    /**
     * 新增入库信息
     */
    @SaCheckPermission("mes:instock:add")
    @Log(title = "入库信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesInstockBo bo) {
        return toAjax(mesInstockService.insertByBo(bo,0));
    }

    /**
     * 修改入库信息
     */
    @SaCheckPermission("mes:instock:edit")
    @Log(title = "入库信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesInstockBo bo) {
        return toAjax(mesInstockService.updateByBo(bo));
    }

    /**
     * 删除入库信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:instock:remove")
    @Log(title = "入库信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesInstockService.deleteWithValidByIds(List.of(ids), true));
    }
}
