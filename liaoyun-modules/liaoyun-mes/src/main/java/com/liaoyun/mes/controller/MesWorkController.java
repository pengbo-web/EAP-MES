package com.liaoyun.mes.controller;

import java.text.ParseException;
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
import com.liaoyun.mes.domain.vo.MesWorkVo;
import com.liaoyun.mes.domain.bo.MesWorkBo;
import com.liaoyun.mes.service.IMesWorkService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 生产派单
 *
 * @author Liao Liao
 * @date 2024-07-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/work")

public class MesWorkController extends BaseController {

    private final IMesWorkService mesWorkService;

    /**
     * 查询生产派单列表
     */
    @SaCheckPermission("mes:work:list")
    @GetMapping("/list")
    public TableDataInfo<MesWorkVo> list(MesWorkBo bo, PageQuery pageQuery) throws ParseException {
        return mesWorkService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出生产派单列表
     */
    @SaCheckPermission("mes:work:export")
    @Log(title = "生产派单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesWorkBo bo, HttpServletResponse response) {
        List<MesWorkVo> list = mesWorkService.queryList(bo);
        ExcelUtil.exportExcel(list, "生产派单", MesWorkVo.class, response);
    }

    /**
     * 获取生产派单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:work:query")
    @GetMapping("/{id}")
    public R<MesWorkVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesWorkService.queryById(id));
    }

    /**
     * 新增生产派单
     */
    @SaCheckPermission("mes:work:add")
    @Log(title = "生产派单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesWorkBo bo) {
        return toAjax(mesWorkService.insertByBo(bo));
    }

    /**
     * 修改生产派单
     */
    @SaCheckPermission("mes:work:edit")
    @Log(title = "生产派单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesWorkBo bo) {
        return toAjax(mesWorkService.updateByBo(bo));
    }

    /**
     * 删除生产派单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:work:remove")
    @Log(title = "生产派单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesWorkService.deleteWithValidByIds(List.of(ids), true));
    }
}
