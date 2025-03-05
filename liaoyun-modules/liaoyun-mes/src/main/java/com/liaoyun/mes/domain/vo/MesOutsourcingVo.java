package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesOutsourcing;
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
 * 外协列视图对象 mes_outsourcing
 *
 * @author Liao Liao
 * @date 2024-07-16
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOutsourcing.class)
public class MesOutsourcingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 厂商
     */
    @ExcelProperty(value = "厂商")
    private String title;

    /**
     * 工艺
     */
    @ExcelProperty(value = "工艺")
    private String process;

    /**
     * 联系方式
     */
    @ExcelProperty(value = "联系方式")
    private String mobile;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址")
    private String address;

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


}
