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
import com.liaoyun.mes.domain.vo.MesWorkRecVo;
import com.liaoyun.mes.domain.bo.MesWorkRecBo;
import com.liaoyun.mes.service.IMesWorkRecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 工作记录
 *
 * @author Liao Liao
 * @date 2024-07-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workRec/workRec")
public class MesWorkRecController extends BaseController {

    private final IMesWorkRecService mesWorkRecService;

    /**
     * 查询工作记录列表
     */
    @SaCheckPermission("workRec:workRec:list")
    @GetMapping("/list")
    public TableDataInfo<MesWorkRecVo> list(MesWorkRecBo bo, PageQuery pageQuery) {
        return mesWorkRecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出工作记录列表
     */
    @SaCheckPermission("workRec:workRec:export")
    @Log(title = "工作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesWorkRecBo bo, HttpServletResponse response) {
        List<MesWorkRecVo> list = mesWorkRecService.queryList(bo);
        ExcelUtil.exportExcel(list, "工作记录", MesWorkRecVo.class, response);
    }

    /**
     * 获取工作记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("workRec:workRec:query")
    @GetMapping("/{id}")
    public R<MesWorkRecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesWorkRecService.queryById(id));
    }

    /**
     * 新增工作记录
     */
    @SaCheckPermission("workRec:workRec:add")
    @Log(title = "工作记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesWorkRecBo bo) {
        return toAjax(mesWorkRecService.insertByBo(bo));
    }

    /**
     * 修改工作记录
     */
    @SaCheckPermission("workRec:workRec:edit")
    @Log(title = "工作记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesWorkRecBo bo) {
        return toAjax(mesWorkRecService.updateByBo(bo));
    }

    /**
     * 删除工作记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workRec:workRec:remove")
    @Log(title = "工作记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesWorkRecService.deleteWithValidByIds(List.of(ids), true));
    }
}
