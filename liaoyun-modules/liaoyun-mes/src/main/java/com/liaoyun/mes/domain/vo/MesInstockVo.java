package com.liaoyun. mes.domain.vo;

import java.math.BigDecimal;
import com.liaoyun. mes.domain.MesInstock;
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
 * 入库信息视图对象 mes_instock
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesInstock.class)
public class MesInstockVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 产品
     */
    @ExcelProperty(value = "产品")
    private Long proId;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long count;

    /**
     * 来源
     */
    @ExcelProperty(value = "来源")
    private String fromtype;

    /**
     * 价格
     */
    @ExcelProperty(value = "价格")
    private BigDecimal nowprice;

    /**
     * 总价
     */
    @ExcelProperty(value = "总价")
    private BigDecimal allprice;

    private BigDecimal oldst;

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


    private String proName;

    private String kwname;

    private Date createTime;

    private String username;

    private MesStockVo stockVo;

    private Long createBy;

    private Integer st;

    private Date updateTime;

    private Date subtime;

    private Long stockid;
    private Long kwid;
    private String kwsn;
}
