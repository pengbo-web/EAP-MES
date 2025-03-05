package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesWuliu;
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
 * 物流信息视图对象 mes_wuliu
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesWuliu.class)
public class MesWuliuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 物流号
     */
    @ExcelProperty(value = "物流号")
    private String sn;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String cname;

    /**
     * 联系方式
     */
    @ExcelProperty(value = "联系方式")
    private String phone;

    /**
     * 信息
     */
    @ExcelProperty(value = "信息")
    private String conts;

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
