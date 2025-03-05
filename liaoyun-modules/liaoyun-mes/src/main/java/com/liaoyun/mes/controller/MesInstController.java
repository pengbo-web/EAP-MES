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
import com.liaoyun.mes.domain.vo.MesInstVo;
import com.liaoyun.mes.domain.bo.MesInstBo;
import com.liaoyun.mes.service.IMesInstService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 其他入库
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/inst")
public class MesInstController extends BaseController {

    private final IMesInstService mesInstService;

    /**
     * 查询其他入库列表
     */
    @SaCheckPermission("mes:inst:list")
    @GetMapping("/list")
    public TableDataInfo<MesInstVo> list(MesInstBo bo, PageQuery pageQuery) {
        return mesInstService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出其他入库列表
     */
    @SaCheckPermission("mes:inst:export")
    @Log(title = "其他入库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesInstBo bo, HttpServletResponse response) {
        List<MesInstVo> list = mesInstService.queryList(bo);
        ExcelUtil.exportExcel(list, "其他入库", MesInstVo.class, response);
    }

    /**
     * 获取其他入库详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:inst:query")
    @GetMapping("/{id}")
    public R<MesInstVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesInstService.queryById(id));
    }

    /**
     * 新增其他入库
     */
    @SaCheckPermission("mes:inst:add")
    @Log(title = "其他入库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesInstBo bo) {
        return toAjax(mesInstService.insertByBo(bo));
    }

    /**
     * 修改其他入库
     */
    @SaCheckPermission("mes:inst:edit")
    @Log(title = "其他入库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesInstBo bo) {
        return toAjax(mesInstService.updateByBo(bo));
    }

    /**
     * 删除其他入库
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:inst:remove")
    @Log(title = "其他入库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesInstService.deleteWithValidByIds(List.of(ids), true));
    }
}
