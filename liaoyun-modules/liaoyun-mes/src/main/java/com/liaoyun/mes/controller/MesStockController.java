package com.liaoyun.mes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.mes.domain.vo.MesCuttingVo;
import com.liaoyun.mes.listener.MesCuttingImportListener;
import com.liaoyun.mes.listener.MesStockImportListener;
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
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.domain.bo.MesStockBo;
import com.liaoyun.mes.service.IMesStockService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 仓库信息
 *
 * @author Liao Liao
 * @date 2024-11-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/stock")
public class MesStockController extends BaseController {

    private final IMesStockService mesStockService;

    /**
     * 查询仓库信息列表
     */
    @SaCheckPermission("mes:stock:list")
    @GetMapping("/list")
    public TableDataInfo<MesStockVo> list(MesStockBo bo, PageQuery pageQuery) {
        return mesStockService.queryPageList(bo, pageQuery);
    }
    @GetMapping("/getcust/{id}")
    public R<List<Map<String,Object>>> getcust(@NotNull(message = "主键不能为空")
                                                 @PathVariable Long id) {
        return R.ok(mesStockService.queryMap(id));
    }


    @GetMapping("/listw")
    public R<List<MesStockVo>> listw(MesStockBo bo, PageQuery pageQuery) {
        return R.ok(mesStockService.Listw(bo));
    }

    @GetMapping("/rulist/{id}")
    public List<MesStockVo> rulist(@PathVariable Long id, PageQuery pageQuery) {
        return mesStockService.rukulist(id);
    }
    @GetMapping("/chulist/{id}")
    public List<MesStockVo> chulist(@PathVariable Long id, PageQuery pageQuery) {
        return mesStockService.chukulist(id);
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
        ExcelResult<MesStockVo> result = ExcelUtil.importExcel(file.getInputStream(), MesStockVo.class, new MesStockImportListener(updateSupport));
        return R.ok();
    }
    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "仓库数据", MesStockVo.class, response);
    }
    /**
     * 导出仓库信息列表
     */
    @SaCheckPermission("mes:stock:export")
    @Log(title = "仓库信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesStockBo bo, HttpServletResponse response) {
        List<MesStockVo> list = mesStockService.queryList(bo);
        ExcelUtil.exportExcel(list, "仓库信息", MesStockVo.class, response);
    }

    /**
     * 获取仓库信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:stock:query")
    @GetMapping("/{id}")
    public R<MesStockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesStockService.queryById(id));
    }

    /**
     * 新增仓库信息
     */
    @SaCheckPermission("mes:stock:add")
    @Log(title = "仓库信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesStockBo bo) {
        return toAjax(mesStockService.insertByBo(bo));
    }

    /**
     * 修改仓库信息
     */
    @SaCheckPermission("mes:stock:edit")
    @Log(title = "仓库信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesStockBo bo) {
        return toAjax(mesStockService.updateByBo(bo));
    }

    /**
     * 删除仓库信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:stock:remove")
    @Log(title = "仓库信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesStockService.deleteWithValidByIds(List.of(ids), true));
    }
}
