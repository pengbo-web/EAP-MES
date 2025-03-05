package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesStockType;
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
 * 库存分类视图对象 mes_stock_type
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesStockType.class)
public class MesStockTypeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    private String sn;

    private String descs;

    private Integer counts;

    private Date looptime;

    /**
     * 上级
     */
    @ExcelProperty(value = "上级")
    private Long pid;

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

    private Long kwid;
    private String kwsn;

}
