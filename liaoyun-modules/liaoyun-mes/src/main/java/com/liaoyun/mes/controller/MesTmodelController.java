package com.liaoyun.mes.controller;

import java.util.ArrayList;
import java.util.List;

import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.mes.domain.bo.MesCuttingBo;
import com.liaoyun.mes.domain.vo.MesCuttingVo;
import com.liaoyun.mes.listener.MesCuttingImportListener;
import com.liaoyun.mes.listener.MesTmodelImportListener;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.http.MediaType;
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
import com.liaoyun.mes.domain.vo.MesTmodelVo;
import com.liaoyun.mes.domain.bo.MesTmodelBo;
import com.liaoyun.mes.service.IMesTmodelService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 模具管理
 *
 * @author Liao Liao
 * @date 2024-12-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/tmodel")
public class MesTmodelController extends BaseController {

    private final IMesTmodelService mesTmodelService;

    /**
     * 查询模具管理列表
     */
    @SaCheckPermission("mes:tmodel:list")
    @GetMapping("/list")
    public TableDataInfo<MesTmodelVo> list(MesTmodelBo bo, PageQuery pageQuery) {
        return mesTmodelService.queryPageList(bo, pageQuery);
    }



    /**
     * 获取模具管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:tmodel:query")
    @GetMapping("/{id}")
    public R<MesTmodelVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesTmodelService.queryById(id));
    }

    /**
     * 新增模具管理
     */
    @SaCheckPermission("mes:tmodel:add")
    @Log(title = "模具管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesTmodelBo bo) {
        return toAjax(mesTmodelService.insertByBo(bo));
    }
    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "模具数据", MesTmodelVo.class, response);
    }
    /**
     * 导入数据
     *
     * @param file          导入文件
     * @param updateSupport 是否更新已存在数据
     */
    @Log(title = "导入管理", businessType = BusinessType.IMPORT)
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file, boolean updateSupport) throws Exception {
        // 导入方法
        ExcelResult<MesTmodelVo> result = ExcelUtil.importExcel(file.getInputStream(), MesTmodelVo.class, new MesTmodelImportListener(updateSupport));
        return R.ok();
    }
    /**
     * 导出刀具管理列表
     */
    @SaCheckPermission("mes:cutting:export")
    @Log(title = "刀具管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesTmodelBo bo, HttpServletResponse response) {
        List<MesTmodelVo> list = mesTmodelService.queryList(bo);
        ExcelUtil.exportExcel(list, "刀具管理", MesTmodelVo.class, response);
    }
    /**
     * 修改模具管理
     */
    @SaCheckPermission("mes:tmodel:edit")
    @Log(title = "模具管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesTmodelBo bo) {
        return toAjax(mesTmodelService.updateByBo(bo));
    }

    /**
     * 删除模具管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:tmodel:remove")
    @Log(title = "模具管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesTmodelService.deleteWithValidByIds(List.of(ids), true));
    }
}
