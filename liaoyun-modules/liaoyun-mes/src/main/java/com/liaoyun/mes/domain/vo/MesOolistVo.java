package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesOolist;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 委外入库详情视图对象 mes_oolist
 *
 * @author Liao Liao
 * @date 2024-12-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOolist.class)
public class MesOolistVo implements Serializable {

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

    private MesOutorderVo outorderVo;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private BigDecimal counts;

    /**
     * 主编号
     */
    @ExcelProperty(value = "主编号")
    private Long mainid;

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

    private Date createTime;


    private String remark2;

    private Long kwid;


    private String orderid;
}
