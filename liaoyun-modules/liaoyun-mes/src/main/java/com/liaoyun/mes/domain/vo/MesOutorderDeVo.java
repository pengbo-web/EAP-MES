package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.mes.domain.MesOutorderDe;
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
 * 委外详情视图对象 mes_outorder_de
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOutorderDe.class)
public class MesOutorderDeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 物料
     */
    @ExcelProperty(value = "物料")
    private Long stockid;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    private Long unid;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal prices;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long counts;

    /**
     * 总价
     */
    @ExcelProperty(value = "总价")
    private BigDecimal allprice;

    /**
     * 交付时间
     */
    @ExcelProperty(value = "交付时间")
    private Date ctime;

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

    /**
     * 已计划数量
     */
    @ExcelProperty(value = "已计划数量")
    private Long pcount;

    private MesStockVo stockVo;

    private String fromtype;

    private Date createTime;

    private String remark2;

    private String orderid;

    private Long mainid;
}
