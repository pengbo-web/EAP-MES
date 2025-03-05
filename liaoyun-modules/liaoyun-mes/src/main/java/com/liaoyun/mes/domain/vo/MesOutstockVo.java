package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesOutstock;
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
 * 出库管理视图对象 mes_outstock
 *
 * @author Liao Liao
 * @date 2024-07-18
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOutstock.class)
public class MesOutstockVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 物料
     */
    @ExcelProperty(value = "物料")
    private Long proId;

    /**
     * 出库单位
     */
    @ExcelProperty(value = "出库单位")
    private Long unitId;

    /**
     * 来源
     */
    @ExcelProperty(value = "来源")
    private String fromtype;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private BigDecimal count;

    /**
     * 流转码
     */
    @ExcelProperty(value = "流转码")
    private String sn;

    /**
     * 任务关联
     */
    @ExcelProperty(value = "任务关联")
    private Long taskId;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "diyisno")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private String proname;

    private String uniname;
    private MesStockVo  stockVo;
    private BigDecimal laststock;

    private Long createBy;

    private String Username;
}
