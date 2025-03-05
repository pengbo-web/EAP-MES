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
import com.liaoyun.mes.domain.vo.MesTeamsVo;
import com.liaoyun.mes.domain.bo.MesTeamsBo;
import com.liaoyun.mes.service.IMesTeamsService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 班组管理
 *
 * @author Liao Liao
 * @date 2024-10-25
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/teams")
public class MesTeamsController extends BaseController {

    private final IMesTeamsService mesTeamsService;

    /**
     * 查询班组管理列表
     */
    @SaCheckPermission("mes:teams:list")
    @GetMapping("/list")
    public TableDataInfo<MesTeamsVo> list(MesTeamsBo bo, PageQuery pageQuery) {
        return mesTeamsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出班组管理列表
     */
    @SaCheckPermission("mes:teams:export")
    @Log(title = "班组管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesTeamsBo bo, HttpServletResponse response) {
        List<MesTeamsVo> list = mesTeamsService.queryList(bo);
        ExcelUtil.exportExcel(list, "班组管理", MesTeamsVo.class, response);
    }

    /**
     * 获取班组管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:teams:query")
    @GetMapping("/{id}")
    public R<MesTeamsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesTeamsService.queryById(id));
    }

    /**
     * 新增班组管理
     */
    @SaCheckPermission("mes:teams:add")
    @Log(title = "班组管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesTeamsBo bo) {
        return toAjax(mesTeamsService.insertByBo(bo));
    }

    /**
     * 修改班组管理
     */
    @SaCheckPermission("mes:teams:edit")
    @Log(title = "班组管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesTeamsBo bo) {
        return toAjax(mesTeamsService.updateByBo(bo));
    }

    /**
     * 删除班组管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:teams:remove")
    @Log(title = "班组管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesTeamsService.deleteWithValidByIds(List.of(ids), true));
    }
}
