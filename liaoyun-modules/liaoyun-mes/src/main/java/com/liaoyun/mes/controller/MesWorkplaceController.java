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
import com.liaoyun.mes.domain.vo.MesWorkplaceVo;
import com.liaoyun.mes.domain.bo.MesWorkplaceBo;
import com.liaoyun.mes.service.IMesWorkplaceService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * ④车间设置
 *
 * @author Kylin W
 * @date 2024-05-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/workplace")
public class MesWorkplaceController extends BaseController {

    private final IMesWorkplaceService mesWorkplaceService;

    /**
     * 查询④车间设置列表
     */
    @SaCheckPermission("mes:workplace:list")
    @GetMapping("/list")
    public TableDataInfo<MesWorkplaceVo> list(MesWorkplaceBo bo, PageQuery pageQuery) {
        return mesWorkplaceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出④车间设置列表
     */
    @SaCheckPermission("mes:workplace:export")
    @Log(title = "④车间设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesWorkplaceBo bo, HttpServletResponse response) {
        List<MesWorkplaceVo> list = mesWorkplaceService.queryList(bo);
        ExcelUtil.exportExcel(list, "④车间设置", MesWorkplaceVo.class, response);
    }

    /**
     * 获取④车间设置详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:workplace:query")
    @GetMapping("/{id}")
    public R<MesWorkplaceVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesWorkplaceService.queryById(id));
    }

    /**
     * 新增④车间设置
     */
    @SaCheckPermission("mes:workplace:add")
    @Log(title = "④车间设置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesWorkplaceBo bo) {
        return toAjax(mesWorkplaceService.insertByBo(bo));
    }

    /**
     * 修改④车间设置
     */
    @SaCheckPermission("mes:workplace:edit")
    @Log(title = "④车间设置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesWorkplaceBo bo) {
        return toAjax(mesWorkplaceService.updateByBo(bo));
    }

    /**
     * 删除④车间设置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:workplace:remove")
    @Log(title = "④车间设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesWorkplaceService.deleteWithValidByIds(List.of(ids), true));
    }
}
