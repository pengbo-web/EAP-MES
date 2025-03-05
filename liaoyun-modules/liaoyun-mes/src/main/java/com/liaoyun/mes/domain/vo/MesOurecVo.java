package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import com.liaoyun.mes.domain.MesOurec;
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
 * 出库记录视图对象 mes_ourec
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOurec.class)
public class MesOurecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    private String stockname;
    /**
     * 物料
     */
    @ExcelProperty(value = "物料")
    private Long stockid;

    /**
     * 批次号
     */
    @ExcelProperty(value = "批次号")
    private String sn;

    /**
     * 供应商
     */
    @ExcelProperty(value = "供应商")
    private Long custId;

    /**
     * 进货价
     */
    @ExcelProperty(value = "进货价")
    private BigDecimal nprice;

    /**
     * 入库货架
     */
    @ExcelProperty(value = "入库货架")
    private Long stocktypeid;
    private String stypename;

    /**
     * 税点
     */
    @ExcelProperty(value = "税点")
    private Long shuidian;

    /**
     * 结算方式
     */
    @ExcelProperty(value = "结算方式")
    private String met;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private BigDecimal count;

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


    private String remark2;

    private String orderid;


}
