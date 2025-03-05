package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesQualityList;
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
 * 质检内容视图对象 mes_quality_list
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesQualityList.class)
public class MesQualityListVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    private String title;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private Long qualitymainId;

    /**
     * 标准
     */
    @ExcelProperty(value = "标准")
    private String standards;

    /**
     * 公差
     */
    @ExcelProperty(value = "公差")
    private String refvalue;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
