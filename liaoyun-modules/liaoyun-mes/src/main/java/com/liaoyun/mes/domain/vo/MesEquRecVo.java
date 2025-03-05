package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesEquRec;
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
 * 设备点检记录视图对象 mes_equ_rec
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesEquRec.class)
public class MesEquRecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 主表关联
     */
    @ExcelProperty(value = "主表关联")
    private Long equmian;

    /**
     * 列表关联
     */
    @ExcelProperty(value = "列表关联")
    private Long listid;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 异常内容
     */
    @ExcelProperty(value = "异常内容")
    private String conts;

    /**
     * 结果
     */
    @ExcelProperty(value = "结果")
    private String rest;

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

    private Long equid;

    private Date createTime;


    private String equname;


    private String mainname;


    private String username;

    private Long createBy;
}
