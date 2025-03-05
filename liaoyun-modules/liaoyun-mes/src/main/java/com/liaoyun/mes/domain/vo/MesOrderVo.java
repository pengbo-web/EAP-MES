package com.liaoyun.mes.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.mes.domain.MesOrder;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import com.liaoyun.mes.domain.MesSaleout;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 销售订单视图对象 mes_order
 *
 * @author Liao Liao
 * @date 2024-11-16
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOrder.class)
public class MesOrderVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 税点
     */
    @ExcelProperty(value = "税点")
    private Long shui;

    /**
     * 客户
     */
    @ExcelProperty(value = "客户")
    private Long custid;

    /**
     * 交货时间
     */
    @ExcelProperty(value = "交货时间")
    private Date subtime;

    /**
     * 客户订单号
     */
    @ExcelProperty(value = "客户订单号")
    private String custno;

    /**
     * 付款周期
     */
    @ExcelProperty(value = "付款周期")
    private String cycle;

    /**
     * 物料信息
     */
    @ExcelProperty(value = "物料信息")
    private String stids;

    /**
     * 发票管理
     */
    @ExcelProperty(value = "发票管理")
    private String fp;

    /**
     * 订单时间
     */
    @ExcelProperty(value = "订单时间")
    private Date otime;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer ismrp;

    /**
     * 其他文件
     */
    @ExcelProperty(value = "其他文件")
    private String otfile;

    /**
     * 文件
     */
    @ExcelProperty(value = "文件")
    private String ctfile;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    private Integer istop;
    private Integer canmark;

    private Integer warmdate;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private String custname;

    private List<MesOrderDeVo> orderList;

    private MesCustomVo customVo;

    private Integer tsta;

    private List<MesSaleoutVo> saleoutList;

    private List<MesOolistVo> oolistVos;

    private List<MesInrecVo> inrecList;

    private List<MesOutorderDeVo> outorderList;

    private Date createTime;



}
