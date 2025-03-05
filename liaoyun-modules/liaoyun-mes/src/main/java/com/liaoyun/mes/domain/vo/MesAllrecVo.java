package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import com.liaoyun.mes.domain.MesAllrec;
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
 * 出入库记录视图对象 mes_allrec
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesAllrec.class)
public class MesAllrecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @ExcelProperty(value = "编码")
    private Long id;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;

    /**
     * 来源
     */
    @ExcelProperty(value = "来源")
    private String fromtype;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private BigDecimal counts;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    private Long unitid;

    /**
     * 价格
     */
    @ExcelProperty(value = "价格")
    private BigDecimal prices;

    /**
     * 物料
     */

    private Long stockid;

    /**
     * 供应商
     */
    @ExcelProperty(value = "供应商")
    private String cname;

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
    @ExcelProperty(value = "物料名称")
    private String stockname;
    @ExcelProperty(value = "物料编码")
    private String stockcode;
    @ExcelProperty(value = "物料规格")
    private String stockgk;
    private MesStockVo stockVo;

    private MesUnitmeasureVo unitVo;
    @ExcelProperty(value = "时间")
    private Date createTime;

    private String remark2;

    private Long stocktype;
    /**
     * 订单号
     */
    private String orderid;
}
