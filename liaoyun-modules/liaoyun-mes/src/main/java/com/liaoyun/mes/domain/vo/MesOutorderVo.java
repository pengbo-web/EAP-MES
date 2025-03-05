package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.mes.domain.MesOutorder;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import com.liaoyun.mes.domain.MesOutorderDe;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 委外订单视图对象 mes_outorder
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOutorder.class)
public class MesOutorderVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @ExcelProperty(value = "编码")
    private Long id;

    /**
     * 委外名称
     */
    @ExcelProperty(value = "委外名称")
    private String title;

    /**
     * 委外商家
     */
    @ExcelProperty(value = "委外商家")
    private Long custid;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private String orders;

    /**
     * 外部编号
     */
    @ExcelProperty(value = "外部编号")
    private String sn;

    /**
     * 委外物料清单
     */
    @ExcelProperty(value = "委外物料清单")
    private String stockids;

    /**
     * 出库库位
     */
    @ExcelProperty(value = "出库库位")
    private String stocktype;

    /**
     * 价格
     */
    @ExcelProperty(value = "价格")
    private BigDecimal prices;

    /**
     * 要求完成日期
     */
    @ExcelProperty(value = "要求完成日期")
    private Date subtime;

    /**
     * 出库时间
     */
    @ExcelProperty(value = "出库时间")
    private Date outtime;


    private String cname;

    private String stonames;
    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    private List<MesOutorderDeVo> devo;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private Long stoid;

    private String stoname;

    private Integer counts;
    private String fromtype;

    private List<MesOolistVo> oolistVos;

    private String remark2;
}
