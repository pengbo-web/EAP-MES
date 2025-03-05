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
import com.liaoyun.mes.domain.vo.MesStinfoVo;
import com.liaoyun.mes.domain.bo.MesStinfoBo;
import com.liaoyun.mes.service.IMesStinfoService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 库位调拨
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/stinfo")
public class MesStinfoController extends BaseController {

    private final IMesStinfoService mesStinfoService;

    /**
     * 查询库位调拨列表
     */
    @SaCheckPermission("mes:stinfo:list")
    @GetMapping("/list")
    public TableDataInfo<MesStinfoVo> list(MesStinfoBo bo, PageQuery pageQuery) {
        return mesStinfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出库位调拨列表
     */
    @SaCheckPermission("mes:stinfo:export")
    @Log(title = "库位调拨", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesStinfoBo bo, HttpServletResponse response) {
        List<MesStinfoVo> list = mesStinfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "库位调拨", MesStinfoVo.class, response);
    }

    /**
     * 获取库位调拨详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:stinfo:query")
    @GetMapping("/{id}")
    public R<MesStinfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesStinfoService.queryById(id));
    }

    /**
     * 新增库位调拨
     */
    @SaCheckPermission("mes:stinfo:add")
    @Log(title = "库位调拨", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesStinfoBo bo) {
        return toAjax(mesStinfoService.insertByBo(bo));
    }

    /**
     * 修改库位调拨
     */
    @SaCheckPermission("mes:stinfo:edit")
    @Log(title = "库位调拨", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesStinfoBo bo) {
        return toAjax(mesStinfoService.updateByBo(bo));
    }

    /**
     * 删除库位调拨
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:stinfo:remove")
    @Log(title = "库位调拨", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesStinfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
