package com.liaoyun.mes.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.liaoyun.mes.domain.vo.MesSaleoutVo;
import com.liaoyun.mes.domain.bo.MesSaleoutBo;
import com.liaoyun.mes.service.IMesSaleoutService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 销售出库
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mes/saleout")
public class MesSaleoutController extends BaseController {

    private final IMesSaleoutService mesSaleoutService;

    /**
     * 查询销售出库列表
     */
    @GetMapping("/list")
    public TableDataInfo<MesSaleoutVo> list(MesSaleoutBo bo, PageQuery pageQuery) {
        return mesSaleoutService.queryPageList(bo, pageQuery);
    }
    /**
     * 查询销售出库列表
     */
    @GetMapping("/reclist")
    public R<Map<String, List<MesSaleoutVo>>> reclist(MesSaleoutBo bo, PageQuery pageQuery) {
        List<MesSaleoutVo> list=mesSaleoutService.queryList(bo);
        for (MesSaleoutVo vo : list) {
            vo.setCount(vo.getCounts());
        }
        Map<String, List<MesSaleoutVo>> groupedBySn = list.stream()
            .filter(item -> item.getSn() != null) // 过滤掉 sn 为 null 的元素
            .collect(Collectors.groupingBy(MesSaleoutVo::getSn));
        return R.ok(groupedBySn);
    }

    /**
     * 导出销售出库列表
     */
    @Log(title = "销售出库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesSaleoutBo bo, HttpServletResponse response) {
        List<MesSaleoutVo> list = mesSaleoutService.queryList(bo);
        ExcelUtil.exportExcel(list, "销售出库", MesSaleoutVo.class, response);
    }

    /**
     * 获取销售出库详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("mes:saleout:query")
    @GetMapping("/{id}")
    public R<MesSaleoutVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesSaleoutService.queryById(id));
    }

    /**
     * 新增销售出库
     */
    @Log(title = "销售出库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesSaleoutBo bo) {
        return toAjax(mesSaleoutService.insertByBo(bo));
    }

    /**
     * 修改销售出库
     */
    @Log(title = "销售出库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MesSaleoutBo bo) {
        return toAjax(mesSaleoutService.updateByBo(bo));
    }

    /**
     * 删除销售出库
     *
     * @param ids 主键串
     */
    @SaCheckPermission("mes:saleout:remove")
    @Log(title = "销售出库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesSaleoutService.deleteWithValidByIds(List.of(ids), true));
    }
}
