package com.liaoyun.mes.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.mes.domain.MesModelrec;
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
 * 模具使用记录视图对象 mes_modelrec
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesModelrec.class)
public class MesModelrecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 磨具编号
     */
    @ExcelProperty(value = "磨具编号")
    private Long modelid;

    /**
     * 使用地址
     */
    @ExcelProperty(value = "使用地址")
    private String wname;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;

    /**
     * 外发名称
     */
    @ExcelProperty(value = "外发名称")
    private String cname;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话")
    private String phone;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址")
    private String address;

    /**
     * 归还日期
     */
    @ExcelProperty(value = "归还日期")
    private Date endtime;

    /**
     * 备注2
     */
    @ExcelProperty(value = "备注2")
    private String remark1;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
