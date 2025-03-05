package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesCustom;
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
 * 客户供应商视图对象 mes_custom
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesCustom.class)
public class MesCustomVo implements Serializable {

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
    @ExcelProperty(value = "类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "customtype")
    private String type;

    private String cname;
    /**
     * 简称
     */
    @ExcelProperty(value = "简称")
    private String descp;

    /**
     * 银行账户
     */
    @ExcelProperty(value = "银行账户")
    private String cardnum;

    /**
     * 全称
     */
    @ExcelProperty(value = "全称")
    private String title;

    /**
     * 联系方式
     */
    @ExcelProperty(value = "联系方式")
    private String phone;

    /**
     * 社会编码
     */
    @ExcelProperty(value = "社会编码")
    private String shnum;

    /**
     * 收货地址
     */
    @ExcelProperty(value = "收货地址")
    private String address;

    /**
     * 邮寄地址
     */
    @ExcelProperty(value = "邮寄地址")
    private String address1;

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


}
