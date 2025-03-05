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
import com.liaoyun.mes.domain.vo.MesPrimarkVo;
import com.liaoyun.mes.domain.bo.MesPrimarkBo;
import com.liaoyun.mes.service.IMesPrimarkService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 打单列表
 *
 * @author Liao Liao
 * @date 2024-12-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/primark")
public class MesPrimarkController extends BaseController {

    private final IMesPrimarkService mesPrimarkService;

    /**
     * 查询打单列表列表
     */
    @SaCheckPermission("mes:primark:list")
    @GetMapping("/list")
    public TableDataInfo<MesPrimarkVo> list(MesPrimarkBo bo, PageQuery pageQuery) {
        return mesPrimarkService.queryPageList(bo, pageQuery);
    }
    @GetMapping("/lists/{id}")
    public R<List<MesPrimarkVo>> lists(@PathVariable Long id) {
        MesPrimarkBo bo = new MesPrimarkBo();
        bo.setOrderid(id);
        return R.ok(mesPrimarkService.queryList(bo));
    }

    /**
     * 导出打单列表列表
     */
    @SaCheckPermission("mes:primark:export")
    @Log(title = "打单列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesPrimarkBo bo, HttpServletResponse response) {
        List<MesPrimarkVo> list = mesPrimarkService.queryList(bo);
        ExcelUtil.exportExcel(list, "打单列表", MesPrimarkVo.class, response);
    }

    /**
     * 获取打单列表详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:primark:query")
    @GetMapping("/{id}")
    public R<MesPrimarkVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesPrimarkService.queryById(id));
    }

    /**
     * 新增打单列表
     */
    @SaCheckPermission("mes:primark:add")
    @Log(title = "打单列表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody MesPrimarkBo bo) {
        return R.ok(mesPrimarkService.insertByBo(bo));
    }

    /**
     * 修改打单列表
     */
    @SaCheckPermission("mes:primark:edit")
    @Log(title = "打单列表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesPrimarkBo bo) {
        return toAjax(mesPrimarkService.updateByBo(bo));
    }

    /**
     * 删除打单列表
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:primark:remove")
    @Log(title = "打单列表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesPrimarkService.deleteWithValidByIds(List.of(ids), true));
    }
}
