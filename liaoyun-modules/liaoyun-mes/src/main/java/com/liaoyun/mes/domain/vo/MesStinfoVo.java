package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import com.liaoyun.mes.domain.MesStinfo;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.MesStockType;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 库位调拨视图对象 mes_stinfo
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesStinfo.class)
public class MesStinfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @ExcelProperty(value = "编码")
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
     * 数量
     */
    @ExcelProperty(value = "数量")
    private BigDecimal counts;

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

    private MesStock stock;

    private MesStockType stockType;

    private String kw;


}
