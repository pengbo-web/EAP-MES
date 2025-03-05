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
import com.liaoyun.mes.domain.vo.MesWorkplacesVo;
import com.liaoyun.mes.domain.bo.MesWorkplacesBo;
import com.liaoyun.mes.service.IMesWorkplacesService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 工作站
 *
 * @author Liao Liao
 * @date 2024-06-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/workplaces")
public class MesWorkplacesController extends BaseController {

    private final IMesWorkplacesService mesWorkplacesService;

    /**
     * 查询工作站列表
     */
    @SaCheckPermission("mes:workplaces:list")
    @GetMapping("/list")
    public TableDataInfo<MesWorkplacesVo> list(MesWorkplacesBo bo, PageQuery pageQuery) {
        return mesWorkplacesService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出工作站列表
     */
    @SaCheckPermission("mes:workplaces:export")
    @Log(title = "工作站", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesWorkplacesBo bo, HttpServletResponse response) {
        List<MesWorkplacesVo> list = mesWorkplacesService.queryList(bo);
        ExcelUtil.exportExcel(list, "工作站", MesWorkplacesVo.class, response);
    }

    /**
     * 获取工作站详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:workplaces:query")
    @GetMapping("/{id}")
    public R<MesWorkplacesVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesWorkplacesService.queryById(id));
    }

    /**
     * 新增工作站
     */
    @SaCheckPermission("mes:workplaces:add")
    @Log(title = "工作站", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesWorkplacesBo bo) {
        return toAjax(mesWorkplacesService.insertByBo(bo));
    }

    /**
     * 修改工作站
     */
    @SaCheckPermission("mes:workplaces:edit")
    @Log(title = "工作站", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesWorkplacesBo bo) {
        return toAjax(mesWorkplacesService.updateByBo(bo));
    }

    /**
     * 删除工作站
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:workplaces:remove")
    @Log(title = "工作站", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesWorkplacesService.deleteWithValidByIds(List.of(ids), true));
    }
}
