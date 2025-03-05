package com.liaoyun.mes.controller;

import java.util.List;
import java.util.Map;

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
import com.liaoyun.mes.domain.vo.MesAllrecVo;
import com.liaoyun.mes.domain.bo.MesAllrecBo;
import com.liaoyun.mes.service.IMesAllrecService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 出入库记录
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/allrec")
public class MesAllrecController extends BaseController {

    private final IMesAllrecService mesAllrecService;
    /**
     * 查询出入库记录列表
     */
    @SaCheckPermission("mes:allrec:list")
    @GetMapping("/list")
    public TableDataInfo<MesAllrecVo> list(MesAllrecBo bo, PageQuery pageQuery) {
        return mesAllrecService.queryPageList(bo, pageQuery);
    }


    /**
     * 导出出入库记录列表
     */
    @SaCheckPermission("mes:allrec:export")
    @Log(title = "出入库记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesAllrecBo bo, HttpServletResponse response) {
        List<MesAllrecVo> list = mesAllrecService.queryList(bo);
        ExcelUtil.exportExcel(list, "出入库记录", MesAllrecVo.class, response);
    }

    @GetMapping("/info")
    public R<List<Map<String,String>>> infocc(){
        List<Map<String,String>> maps=mesAllrecService.queryPageLists();
        return R.ok(maps);
    }
    /**
     * 获取出入库记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:allrec:query")
    @GetMapping("/{id}")
    public R<MesAllrecVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesAllrecService.queryById(id));
    }
    @GetMapping("/stock/{id}/{type}")
    public R<List<MesAllrecVo>> getstoInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id,@PathVariable String type) {
        MesAllrecBo bo=new MesAllrecBo();
        bo.setStockid(id);
        bo.setType(type);
        return R.ok(mesAllrecService.queryList(bo));
    }

    /**
     * 新增出入库记录
     */
    @SaCheckPermission("mes:allrec:add")
    @Log(title = "出入库记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesAllrecBo bo) {
        return toAjax(mesAllrecService.insertByBo(bo));
    }

    /**
     * 修改出入库记录
     */
    @SaCheckPermission("mes:allrec:edit")
    @Log(title = "出入库记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesAllrecBo bo) {
        return toAjax(mesAllrecService.updateByBo(bo));
    }

    /**
     * 删除出入库记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:allrec:remove")
    @Log(title = "出入库记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesAllrecService.deleteWithValidByIds(List.of(ids), true));
    }
}
