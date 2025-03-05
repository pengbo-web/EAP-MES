package com.liaoyun.mes.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.mes.domain.MesProduct;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * ③产品管理视图对象 mes_product
 *
 * @author Kylin W
 * @date 2024-05-11
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesProduct.class)
public class MesProductVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
//    @ExcelProperty(value = "主键")
    @ExcelIgnore
    private Long id;
    /**
     * 标题
     */
    @ExcelProperty(value = "物料/产品名称")
    @NotEmpty(message = "物料/产品名称不能为空", groups = AddGroup.class)
    private String title;
    /**
     * 编码
     */
    @ExcelProperty(value = "编码（图号）")
    private String sn;
    /**
     * 单位Id
     */
    @ExcelProperty(value = "使用单位")
    @NotEmpty(message = "计量单位不能为空", groups = AddGroup.class)
    private String unit;
    @ExcelIgnore
    private Long unitId;
    /**
     * 类别
     */
    @ExcelProperty(value = "品类")
    @NotEmpty(message = "库存名称不能为空", groups = AddGroup.class)
    private String protype;
    /**
     * 品类
     */
    @ExcelIgnore
    private Long protypeId;
    /**
     * 组成件
     */
    @ExcelProperty(value = "组成件")
    private String pidname;
    @ExcelProperty(value = "类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "markpro")
    private String mustbe;

    /**
     * 需求数量
     */
    @ExcelProperty(value = "BOM需求数量")
    @NotEmpty(message = "数量不能为空", groups = AddGroup.class)
    private Integer needCount;

    @ExcelProperty(value = "重量")
    private float weight;


    /**
     * 需要组成件数量
     */
    @ExcelProperty(value = "采购价格")
    private BigDecimal bcount;


    /**
     * 组成件id
     */
    @ExcelIgnore
    private Long pid;

    /**
     * 版本
     */
    @ExcelProperty(value = "版本（默认1）")
    private Integer ver=1;



    /**
     * 状态（0正常 1停用）
     */
    @ExcelIgnore
    private String status;

    /**
     * 备注
     */
//    @ExcelProperty(value = "备注")
    @ExcelIgnore
    private String remark;

    @ExcelIgnore
    private String typename;



    private Long stockId;
    @ExcelIgnore
    private BigDecimal stock;
    @ExcelIgnore
    private String stockname;

    private String unitname;

    private BigDecimal endstock;

    private Integer grow;


    private Integer iswarning;

    private Integer warningnum;

    private Integer quacount;

    private Long stocks;
    private String imgs;

    private Integer downcount;

    /**
     * 产能
     */
    private BigDecimal stime;
    /**
     * 工费
     */
    private BigDecimal prices;
}
