package com.liaoyun.mes.controller;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.bo.MesPriceRecBo;
import com.liaoyun.mes.domain.vo.MesPriceRecVo;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.listener.MesProcuctImportListener;
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
import com.liaoyun.mes.domain.vo.MesProductVo;
import com.liaoyun.mes.domain.bo.MesProductBo;
import com.liaoyun.mes.service.IMesProductService;
import org.springframework.web.multipart.MultipartFile;

/**
 * ③产品管理
 *
 * @author Kylin W
 * @date 2024-05-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/product")
public class MesProductController extends BaseController {

    private final IMesProductService mesProductService;

    /**
     * 查询③产品管理列表
     */
    @SaCheckPermission("mes:product:list")
    @GetMapping("/list")
    public R<List<MesProductVo>> list(MesProductBo bo) {
        List<MesProductVo> list = mesProductService.queryList(bo);
        return R.ok(list);
    }
    @GetMapping("/getbom/{id}")
    public R<List<MesStockVo>> getbom(@PathVariable Long id) {
        List<MesStockVo> list = mesProductService.getbom(id);
        return R.ok(list);
    }
    @GetMapping("/getboml/{id}")
    public R<List<MesProductVo>> getboml(@PathVariable Long id) {
        List<MesProductVo> list = mesProductService.getboml(id);
        return R.ok(list);
    }

    /**
     * 查询价格波动列表
     */
    @SaCheckPermission("mes:priceRec:list")
    @GetMapping("/lists")
    public TableDataInfo<MesProductVo> list(MesProductBo bo, PageQuery pageQuery) {
        return mesProductService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出③产品管理列表
     */
    @SaCheckPermission("mes:product:export")
    @Log(title = "③产品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesProductBo bo, HttpServletResponse response) {
        List<MesProductVo> list = mesProductService.queryList(bo);
        ExcelUtil.exportExcel(list, "③产品管理", MesProductVo.class, response);
    }
    /**
     * 导入数据
     *
     * @param file          导入文件
     * @param updateSupport 是否更新已存在数据
     */
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @SaCheckPermission("system:user:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file,boolean updateSupport) throws Exception {
        // 导入方法
        ExcelResult<MesProductVo> result = ExcelUtil.importExcel(file.getInputStream(), MesProductVo.class, new MesProcuctImportListener(updateSupport));
        return R.ok(result.getAnalysis());
    }
    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "用户数据", MesProductVo.class, response);
    }


    /**
     * 获取③产品管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:product:query")
    @GetMapping("/{id}")
    public R<MesProductVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesProductService.queryById(id));
    }

    /**
     * 新增③产品管理
     */
    @SaCheckPermission("mes:product:add")
    @Log(title = "③产品管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesProductBo bo) {
        return toAjax(mesProductService.insertByBo(bo));
    }

    @PostMapping("/ins")
    public R<Void> adds(@RequestBody MesProductBo bo) {

        return toAjax(mesProductService.insertsByBo(bo,bo.getProductBos()));
    }

    /**
     * 修改③产品管理
     */
    @SaCheckPermission("mes:product:edit")
    @Log(title = "③产品管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesProductBo bo) {
        return toAjax(mesProductService.updateByBo(bo));
    }

    /**
     * 删除③产品管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:product:remove")
    @Log(title = "③产品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        MesProductBo bo = new MesProductBo();
        for (Long id:ids){
            bo.setPid(id);
            List<MesProductVo> productVos=mesProductService.queryList(bo);
            if(productVos.size()>0){
                return R.fail("请先移除子栏目");
            }
        }

        return toAjax(mesProductService.deleteWithValidByIds(List.of(ids), true));
    }
}
