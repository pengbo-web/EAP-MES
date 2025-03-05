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
import com.liaoyun.mes.domain.vo.MesProsettingzpVo;
import com.liaoyun.mes.domain.bo.MesProsettingzpBo;
import com.liaoyun.mes.service.IMesProsettingzpService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 组装排程
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/prosettingzp")
public class MesProsettingzpController extends BaseController {

    private final IMesProsettingzpService mesProsettingzpService;

    /**
     * 查询组装排程列表
     */
    @SaCheckPermission("mes:prosettingzp:list")
    @GetMapping("/list")
    public TableDataInfo<MesProsettingzpVo> list(MesProsettingzpBo bo, PageQuery pageQuery) {
        return mesProsettingzpService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出组装排程列表
     */
    @SaCheckPermission("mes:prosettingzp:export")
    @Log(title = "组装排程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesProsettingzpBo bo, HttpServletResponse response) {
        List<MesProsettingzpVo> list = mesProsettingzpService.queryList(bo);
        ExcelUtil.exportExcel(list, "组装排程", MesProsettingzpVo.class, response);
    }

    /**
     * 获取组装排程详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:prosettingzp:query")
    @GetMapping("/{id}")
    public R<MesProsettingzpVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesProsettingzpService.queryById(id));
    }

    /**
     * 新增组装排程
     */
    @SaCheckPermission("mes:prosettingzp:add")
    @Log(title = "组装排程", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesProsettingzpBo bo) {
        return toAjax(mesProsettingzpService.insertByBo(bo));
    }

    /**
     * 修改组装排程
     */
    @SaCheckPermission("mes:prosettingzp:edit")
    @Log(title = "组装排程", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesProsettingzpBo bo) {
        return toAjax(mesProsettingzpService.updateByBo(bo));
    }

    /**
     * 删除组装排程
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:prosettingzp:remove")
    @Log(title = "组装排程", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesProsettingzpService.deleteWithValidByIds(List.of(ids), true));
    }
}
