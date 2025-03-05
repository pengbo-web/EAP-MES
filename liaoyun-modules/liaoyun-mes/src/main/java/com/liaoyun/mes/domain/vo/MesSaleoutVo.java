package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesSaleout;
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
 * 销售出库视图对象 mes_saleout
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesSaleout.class)
public class MesSaleoutVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 订单
     */
    @ExcelProperty(value = "订单")
    private Long orderid;

    /**
     * 物料
     */
    @ExcelProperty(value = "物料")
    private Long stockid;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Integer counts;
    private Integer count;

    /**
     * 文件
     */
    @ExcelProperty(value = "文件")
    private String files;

    /**
     * 物流信息
     */
    @ExcelProperty(value = "物流信息")
    private String wuliu;

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


    private MesStockVo stockVo;

    private MesOrderVo orderVo;

    private Date createTime;


    /**
     * 编码
     */
    @ExcelProperty(value = "出库编码")
    private String sn;
}
