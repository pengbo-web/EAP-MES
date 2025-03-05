package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.mes.domain.MesOust;
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
 * 其他出库视图对象 mes_oust
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOust.class)
public class MesOustVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 采购列表
     */
    @ExcelProperty(value = "采购列表")
    private String recids;

    /**
     * 批次号
     */
    @ExcelProperty(value = "批次号")
    private String sn;

    /**
     * 入库位置
     */
    @ExcelProperty(value = "入库位置")
    private Long stocktype;

    /**
     * 总价
     */
    @ExcelProperty(value = "总价")
    private BigDecimal allprice;

    /**
     * 结算方式
     */
    @ExcelProperty(value = "结算方式")
    private String met;

    /**
     * 税点
     */
    @ExcelProperty(value = "税点")
    private String shui;

    /**
     * 文件
     */
    @ExcelProperty(value = "文件")
    private String files;

    /**
     * 采购时间
     */
    @ExcelProperty(value = "采购时间")
    private Date ctime;

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
