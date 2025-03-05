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
import com.liaoyun.mes.domain.vo.MesToolRecVo;
import com.liaoyun.mes.domain.bo.MesToolRecBo;
import com.liaoyun.mes.service.IMesToolRecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 夹具记录
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/toolRec")
public class MesToolRecController extends BaseController {

    private final IMesToolRecService mesToolRecService;

    /**
     * 查询夹具记录列表
     */
    @SaCheckPermission("mes:toolRec:list")
    @GetMapping("/list")
    public TableDataInfo<MesToolRecVo> list(MesToolRecBo bo, PageQuery pageQuery) {
        return mesToolRecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出夹具记录列表
     */
    @SaCheckPermission("mes:toolRec:export")
    @Log(title = "夹具记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesToolRecBo bo, HttpServletResponse response) {
        List<MesToolRecVo> list = mesToolRecService.queryList(bo);
        ExcelUtil.exportExcel(list, "夹具记录", MesToolRecVo.class, response);
    }

    /**
     * 获取夹具记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:toolRec:query")
    @GetMapping("/{id}")
    public R<MesToolRecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesToolRecService.queryById(id));
    }

    /**
     * 新增夹具记录
     */
    @SaCheckPermission("mes:toolRec:add")
    @Log(title = "夹具记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesToolRecBo bo) {
        return toAjax(mesToolRecService.insertByBo(bo));
    }

    /**
     * 修改夹具记录
     */
    @SaCheckPermission("mes:toolRec:edit")
    @Log(title = "夹具记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesToolRecBo bo) {
        return toAjax(mesToolRecService.updateByBo(bo));
    }

    /**
     * 删除夹具记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:toolRec:remove")
    @Log(title = "夹具记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesToolRecService.deleteWithValidByIds(List.of(ids), true));
    }
}
