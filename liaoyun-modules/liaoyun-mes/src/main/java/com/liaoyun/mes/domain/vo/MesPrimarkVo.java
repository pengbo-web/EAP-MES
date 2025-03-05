package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import com.liaoyun.mes.domain.MesPrimark;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import com.liaoyun.mes.domain.MesProduct;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 打单列表视图对象 mes_primark
 *
 * @author Liao Liao
 * @date 2024-12-16
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesPrimark.class)
public class MesPrimarkVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 工作id
     */
    @ExcelProperty(value = "工作id")
    private Long workid;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private Long orderid;

    /**
     * 交货日期
     */
    @ExcelProperty(value = "交货日期")
    private String subtime;

    /**
     * 流转码
     */
    @ExcelProperty(value = "流转码")
    private String sn;

    /**
     * 物料码
     */
    @ExcelProperty(value = "物料码")
    private Long stockid;

    /**
     * 版次
     */
    @ExcelProperty(value = "版次")
    private String banci;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private BigDecimal counts;

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

    private MesOrderVo orderVo;

    private MesStockVo stockVo;

    private MesWorkVo workVo;

    private Integer wul;

    private String uptitle;

    private MesProduct product;

    private Integer tc;
}
