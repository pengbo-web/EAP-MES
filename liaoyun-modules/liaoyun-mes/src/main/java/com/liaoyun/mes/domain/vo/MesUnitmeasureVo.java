package com.liaoyun.mes.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.liaoyun.mes.domain.MesUnitmeasure;
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
 * 计量单位视图对象 mes_unitmeasure
 *
 * @author Kylin
 * @date 2024-05-10
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesUnitmeasure.class)
public class MesUnitmeasureVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = "Id")
    private Long id;

    /**
     * 单位编码
     */
    @ExcelProperty(value = "单位编码")
    private String unitsn;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String title;

    /**
     * 是否主单位
     */
    @ExcelProperty(value = "是否主单位")
    @ExcelDictFormat(dictType = "sys_yes_no")
    private String isMain;

    /**
     * 进率
     */
    @ExcelProperty(value = "进率")
    private Integer grow;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private MesUnitmeasureVo vo;

    private String upname;

}
