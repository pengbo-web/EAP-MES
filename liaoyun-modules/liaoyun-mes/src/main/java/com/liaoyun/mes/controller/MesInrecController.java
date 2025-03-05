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
import com.liaoyun.mes.domain.vo.MesInrecVo;
import com.liaoyun.mes.domain.bo.MesInrecBo;
import com.liaoyun.mes.service.IMesInrecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 入库记录
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/inrec")
public class MesInrecController extends BaseController {

    private final IMesInrecService mesInrecService;

    /**
     * 查询入库记录列表
     */
    @SaCheckPermission("mes:inrec:list")
    @GetMapping("/list")
    public TableDataInfo<MesInrecVo> list(MesInrecBo bo, PageQuery pageQuery) {
        return mesInrecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出入库记录列表
     */
    @SaCheckPermission("mes:inrec:export")
    @Log(title = "入库记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesInrecBo bo, HttpServletResponse response) {
        List<MesInrecVo> list = mesInrecService.queryList(bo);
        ExcelUtil.exportExcel(list, "入库记录", MesInrecVo.class, response);
    }

    /**
     * 获取入库记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:inrec:query")
    @GetMapping("/{id}")
    public R<MesInrecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesInrecService.queryById(id));
    }

    /**
     * 新增入库记录
     */
    @SaCheckPermission("mes:inrec:add")
    @Log(title = "入库记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody MesInrecBo bo) {
        Long id= mesInrecService.insertByBo(bo);
        return R.ok(id);
    }

    /**
     * 修改入库记录
     */
    @SaCheckPermission("mes:inrec:edit")
    @Log(title = "入库记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesInrecBo bo) {
        return toAjax(mesInrecService.updateByBo(bo));
    }

    /**
     * 删除入库记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:inrec:remove")
    @Log(title = "入库记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesInrecService.deleteWithValidByIds(List.of(ids), true));
    }
}
