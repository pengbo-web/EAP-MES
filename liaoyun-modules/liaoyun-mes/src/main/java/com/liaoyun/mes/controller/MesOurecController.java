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
import com.liaoyun.mes.domain.vo.MesOurecVo;
import com.liaoyun.mes.domain.bo.MesOurecBo;
import com.liaoyun.mes.service.IMesOurecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 出库记录
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/ourec")
public class MesOurecController extends BaseController {

    private final IMesOurecService mesOurecService;

    /**
     * 查询出库记录列表
     */
    @SaCheckPermission("mes:ourec:list")
    @GetMapping("/list")
    public TableDataInfo<MesOurecVo> list(MesOurecBo bo, PageQuery pageQuery) {
        return mesOurecService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出出库记录列表
     */
    @SaCheckPermission("mes:ourec:export")
    @Log(title = "出库记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesOurecBo bo, HttpServletResponse response) {
        List<MesOurecVo> list = mesOurecService.queryList(bo);
        ExcelUtil.exportExcel(list, "出库记录", MesOurecVo.class, response);
    }

    /**
     * 获取出库记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:ourec:query")
    @GetMapping("/{id}")
    public R<MesOurecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesOurecService.queryById(id));
    }

    /**
     * 新增出库记录
     */
    @SaCheckPermission("mes:ourec:add")
    @Log(title = "出库记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesOurecBo bo) {
        return toAjax(mesOurecService.insertByBo(bo));
    }

    /**
     * 修改出库记录
     */
    @SaCheckPermission("mes:ourec:edit")
    @Log(title = "出库记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesOurecBo bo) {
        Boolean b=mesOurecService.updateByBo(bo);
        if(b){
            return R.ok();
        }else {
            return R.fail("库存不足,未能完成出库！");
        }
    }

    /**
     * 删除出库记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:ourec:remove")
    @Log(title = "出库记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesOurecService.deleteWithValidByIds(List.of(ids), true));
    }
}
