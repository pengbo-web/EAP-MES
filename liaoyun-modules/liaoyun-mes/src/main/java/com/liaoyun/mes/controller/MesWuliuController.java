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
import com.liaoyun.mes.domain.vo.MesWuliuVo;
import com.liaoyun.mes.domain.bo.MesWuliuBo;
import com.liaoyun.mes.service.IMesWuliuService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 物流信息
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/wuliu")
public class MesWuliuController extends BaseController {

    private final IMesWuliuService mesWuliuService;

    /**
     * 查询物流信息列表
     */
    @SaCheckPermission("mes:wuliu:list")
    @GetMapping("/list")
    public TableDataInfo<MesWuliuVo> list(MesWuliuBo bo, PageQuery pageQuery) {
        return mesWuliuService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出物流信息列表
     */
    @SaCheckPermission("mes:wuliu:export")
    @Log(title = "物流信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesWuliuBo bo, HttpServletResponse response) {
        List<MesWuliuVo> list = mesWuliuService.queryList(bo);
        ExcelUtil.exportExcel(list, "物流信息", MesWuliuVo.class, response);
    }

    /**
     * 获取物流信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:wuliu:query")
    @GetMapping("/{id}")
    public R<MesWuliuVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesWuliuService.queryById(id));
    }

    /**
     * 新增物流信息
     */
    @SaCheckPermission("mes:wuliu:add")
    @Log(title = "物流信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesWuliuBo bo) {
        return toAjax(mesWuliuService.insertByBo(bo));
    }

    /**
     * 修改物流信息
     */
    @SaCheckPermission("mes:wuliu:edit")
    @Log(title = "物流信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesWuliuBo bo) {
        return toAjax(mesWuliuService.updateByBo(bo));
    }

    /**
     * 删除物流信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:wuliu:remove")
    @Log(title = "物流信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesWuliuService.deleteWithValidByIds(List.of(ids), true));
    }
}
