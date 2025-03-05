package com.liaoyun.mes.controller;

import java.util.ArrayList;
import java.util.List;

import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.mes.domain.vo.MesCuttingVo;
import com.liaoyun.mes.listener.MesCuttingImportListener;
import com.liaoyun.mes.listener.MesEtoolImportListener;
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
import com.liaoyun.mes.domain.vo.MesEtoolVo;
import com.liaoyun.mes.domain.bo.MesEtoolBo;
import com.liaoyun.mes.service.IMesEtoolService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 工装夹具
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/etool")
public class MesEtoolController extends BaseController {

    private final IMesEtoolService mesEtoolService;

    /**
     * 查询工装夹具列表
     */
    @SaCheckPermission("mes:etool:list")
    @GetMapping("/list")
    public TableDataInfo<MesEtoolVo> list(MesEtoolBo bo, PageQuery pageQuery) {
        return mesEtoolService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出工装夹具列表
     */
    @SaCheckPermission("mes:etool:export")
    @Log(title = "工装夹具", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesEtoolBo bo, HttpServletResponse response) {
        List<MesEtoolVo> list = mesEtoolService.queryList(bo);
        ExcelUtil.exportExcel(list, "工装夹具", MesEtoolVo.class, response);
    }

    /**
     * 获取工装夹具详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:etool:query")
    @GetMapping("/{id}")
    public R<MesEtoolVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesEtoolService.queryById(id));
    }

    /**
     * 新增工装夹具
     */
    @SaCheckPermission("mes:etool:add")
    @Log(title = "工装夹具", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesEtoolBo bo) {
        return toAjax(mesEtoolService.insertByBo(bo));
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
        ExcelResult<MesEtoolVo> result = ExcelUtil.importExcel(file.getInputStream(), MesEtoolVo.class, new MesEtoolImportListener(updateSupport));
        return R.ok();
    }
    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "用户数据", MesCuttingVo.class, response);
    }
    /**
     * 修改工装夹具
     */
    @SaCheckPermission("mes:etool:edit")
    @Log(title = "工装夹具", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesEtoolBo bo) {
        return toAjax(mesEtoolService.updateByBo(bo));
    }

    /**
     * 删除工装夹具
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:etool:remove")
    @Log(title = "工装夹具", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesEtoolService.deleteWithValidByIds(List.of(ids), true));
    }
}
