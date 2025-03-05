package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import com.liaoyun.mes.domain.MesStocktaking;
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
 * 库位盘点视图对象 mes_stocktaking
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesStocktaking.class)
public class MesStocktakingVo implements Serializable {

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
     * 库位
     */
    @ExcelProperty(value = "库位")
    private Long stocktypeid;

    /**
     * 盘查数量
     */
    @ExcelProperty(value = "盘查数量")
    private BigDecimal counts;

    /**
     * 异常数量
     */
    @ExcelProperty(value = "异常数量")
    private BigDecimal yccounts;

    /**
     * 异常原因
     */
    @ExcelProperty(value = "异常原因")
    private String ycrec;

    /**
     * 结存量
     */
    @ExcelProperty(value = "结存量")
    private BigDecimal jc;

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

    private MesStockVo stockVo;

    private MesStockTypeVo stypeinfo;

    private Date createTime;

}
