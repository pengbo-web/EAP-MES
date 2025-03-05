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
import com.liaoyun.mes.domain.vo.MesOolistVo;
import com.liaoyun.mes.domain.bo.MesOolistBo;
import com.liaoyun.mes.service.IMesOolistService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 委外入库详情
 *
 * @author Liao Liao
 * @date 2024-12-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/oolist")
public class MesOolistController extends BaseController {

    private final IMesOolistService mesOolistService;

    /**
     * 查询委外入库详情列表
     */
    @GetMapping("/list")
    public TableDataInfo<MesOolistVo> list(MesOolistBo bo, PageQuery pageQuery) {
        return mesOolistService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出委外入库详情列表
     */
    @Log(title = "委外入库详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOolistBo bo, HttpServletResponse response) {
        List<MesOolistVo> list = mesOolistService.queryList(bo);
        ExcelUtil.exportExcel(list, "委外入库详情", MesOolistVo.class, response);
    }

    /**
     * 获取委外入库详情详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<MesOolistVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOolistService.queryById(id));
    }

    /**
     * 新增委外入库详情
     */
    @Log(title = "委外入库详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesOolistBo bo) {
        return toAjax(mesOolistService.insertByBo(bo));
    }

    /**
     * 修改委外入库详情
     */
    @Log(title = "委外入库详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOolistBo bo) {
        return toAjax(mesOolistService.updateByBo(bo));
    }

    /**
     * 删除委外入库详情
     *
     * @param ids 主键串
     */
    @Log(title = "委外入库详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOolistService.deleteWithValidByIds(List.of(ids), true));
    }
}
