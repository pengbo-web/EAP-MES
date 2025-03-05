package com.liaoyun.mes.domain.vo;

import java.math.BigDecimal;

import com.liaoyun.mes.domain.MesCustom;
import com.liaoyun.mes.domain.MesStock;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 仓库信息视图对象 mes_stock
 *
 * @author Liao Liao
 * @date 2024-11-10
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesStock.class)
public class MesStockVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    private Integer pcount;

    /**
     * 库存编码
     */
    @ExcelProperty(value = "编码")
    private String sn;
    @ExcelProperty(value = "货架号")
    private String stockname;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String tilte;

    private String custid;
    @ExcelProperty(value = "供应商（多个以逗号链接）")
    private String custname;

    /**
     * 图号
     */
    @ExcelProperty(value = "图号")
    private String iid;


    /**
     * 类型
     */
    @ExcelProperty(value = "完成度", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "stocktype")
    private String type;

    @ExcelProperty(value = "品类")
    private String otype;
    @ExcelProperty(value = "规格")
    private String gk;

    private List<MesCustom> customList;
    /**
     * 当前库存
     */
    @ExcelProperty(value = "当前库存")
    private BigDecimal nowstock;
    /**
     * 报警
     */
    @ExcelProperty(value = "报警库存")
    private BigDecimal warnnum;


    private MesStockTypeVo typeVo;


    /**
     * 库存信息
     */
    private Long stockid;


    private float ckcount;

    /**
     * 库存信息
     */


    /**
     * 总入库
     */
    private BigDecimal instock;

    /**
     * 总出库
     */
    private BigDecimal outstock;


    private BigDecimal pricein;

    private BigDecimal pricesale;

    private Integer sales;

    /**
     * 规格
     */

    private Long unitid;
    @ExcelProperty(value = "单位")
    private String unitname;


    /**
     * 物料
     */
    private Long proid;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
    private String remark2;
    private String remark3;

    private List<MesStockVo> bomlist;

    private float ncout;

    private Integer iswaring;

    private Date createtime;
    private String ncname;
}
