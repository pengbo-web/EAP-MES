package com.liaoyun.mes.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.liaoyun.mes.domain.vo.MesProcessVo;
import com.liaoyun.mes.domain.bo.MesProcessBo;
import com.liaoyun.mes.service.IMesProcessService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * ⑧工艺管理
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/process")
public class MesProcessController extends BaseController {

    private final IMesProcessService mesProcessService;

    /**
     * 查询⑧工艺管理列表
     */
    @SaCheckPermission("mes:process:list")
    @GetMapping("/list")
    public TableDataInfo<MesProcessVo> list(MesProcessBo bo, PageQuery pageQuery) {
        return mesProcessService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询⑧工艺管理列表
     */
    @SaCheckPermission("mes:process:list")
    @GetMapping("/listby")
    public TableDataInfo<MesProcessVo> listby(MesProcessBo bo, PageQuery pageQuery) {
        return mesProcessService.queryPageListby(bo, pageQuery);
    }

    /**
     * 导出⑧工艺管理列表
     */
    @SaCheckPermission("mes:process:export")
    @Log(title = "⑧工艺管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesProcessBo bo, HttpServletResponse response) {
        List<MesProcessVo> list = mesProcessService.queryList(bo);
        ExcelUtil.exportExcel(list, "⑧工艺管理", MesProcessVo.class, response);
    }

    /**
     * 获取⑧工艺管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:process:query")
    @GetMapping("/{id}")
    public R<MesProcessVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesProcessService.queryById(id));
    }

    /**
     * 新增⑧工艺管理
     */
    @SaCheckPermission("mes:process:add")
    @Log(title = "⑧工艺管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesProcessBo bo) {
        return toAjax(mesProcessService.insertByBo(bo));
    }

    /**
     * 查询⑧工艺管理列表
     */
    @SaCheckPermission("mes:process:list")
    @PostMapping("/getstrings")
    public R<List<MesProcessVo>> getstrings(@RequestBody String[] bo) {
        List<Long> longList = Arrays.stream(bo).map(Long::parseLong)
            .collect(Collectors.toList());
        return R.ok(mesProcessService.queryListBystring(longList));
    }

    /**
     * 修改⑧工艺管理
     */
    @SaCheckPermission("mes:process:edit")
    @Log(title = "⑧工艺管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesProcessBo bo) {
        return toAjax(mesProcessService.updateByBo(bo));
    }

    /**
     * 删除⑧工艺管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:process:remove")
    @Log(title = "⑧工艺管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesProcessService.deleteWithValidByIds(List.of(ids), true));
    }
}
