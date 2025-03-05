package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesMobileMenu;
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
 * 移动端目录视图对象 mes_mobile_menu
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesMobileMenu.class)
public class MesMobileMenuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String icoimg;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String title;

    /**
     * 类别
     */
    @ExcelProperty(value = "类别")
    private Integer type;

    /**
     * 连接
     */
    @ExcelProperty(value = "连接")
    private String urls;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=正常,2=进行中,3=已完成,0=异常完工,4待检查")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


    private Date createTime;


}
