package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import com.liaoyun.mes.domain.MesPriceRec;
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
 * 价格波动视图对象 mes_price_rec
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesPriceRec.class)
public class MesPriceRecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 商品
     */
    @ExcelProperty(value = "商品")
    private Long proId;

    /**
     * 当时数量
     */
    @ExcelProperty(value = "当时数量")
    private Long count;

    /**
     * 价格
     */
    @ExcelProperty(value = "价格")
    private BigDecimal prices;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    private String proname;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


    private Date createTime;

}
