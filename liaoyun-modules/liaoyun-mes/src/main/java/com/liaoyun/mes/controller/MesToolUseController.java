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
import com.liaoyun.mes.domain.vo.MesToolUseVo;
import com.liaoyun.mes.domain.bo.MesToolUseBo;
import com.liaoyun.mes.service.IMesToolUseService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 夹具使用
 *
 * @author Liao Liao
 * @date 2024-10-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/toolUse")
public class MesToolUseController extends BaseController {

    private final IMesToolUseService mesToolUseService;

    /**
     * 查询夹具使用列表
     */
    @SaCheckPermission("mes:toolUse:list")
    @GetMapping("/list")
    public TableDataInfo<MesToolUseVo> list(MesToolUseBo bo, PageQuery pageQuery) {
        return mesToolUseService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出夹具使用列表
     */
    @SaCheckPermission("mes:toolUse:export")
    @Log(title = "夹具使用", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesToolUseBo bo, HttpServletResponse response) {
        List<MesToolUseVo> list = mesToolUseService.queryList(bo);
        ExcelUtil.exportExcel(list, "夹具使用", MesToolUseVo.class, response);
    }

    /**
     * 获取夹具使用详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:toolUse:query")
    @GetMapping("/{id}")
    public R<MesToolUseVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesToolUseService.queryById(id));
    }

    /**
     * 新增夹具使用
     */
    @SaCheckPermission("mes:toolUse:add")
    @Log(title = "夹具使用", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add( @RequestBody List<MesToolUseBo> bo) {
        String ids = "";
        for (int i = 0; i < bo.size(); i++) {
            ids += mesToolUseService.insertByBo(bo.get(i)).toString();
            if (i < bo.size() - 1) {
                ids += ",";
            }
        }
        return R.ok(ids);
    }

    /**
     * 修改夹具使用
     */
    @SaCheckPermission("mes:toolUse:edit")
    @Log(title = "夹具使用", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesToolUseBo bo) {
        return toAjax(mesToolUseService.updateByBo(bo));
    }

    /**
     * 删除夹具使用
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:toolUse:remove")
    @Log(title = "夹具使用", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesToolUseService.deleteWithValidByIds(List.of(ids), true));
    }
}
