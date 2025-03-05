package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesMUR;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 移动套餐视图对象 mes_m_u_r
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesMUR.class)
public class MesMURVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 套餐名
     */
    @ExcelProperty(value = "套餐名")
    private String title;

    /**
     * 套餐内容
     */
    @ExcelProperty(value = "套餐内容")
    private String ids;
    private String idsname;
    private String[] idsarray;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=正常,2=进行中,3=已完成,0=异常完工,4待检查")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
