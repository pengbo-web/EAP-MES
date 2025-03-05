package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesEquCheck;
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
 * 设备点检视图对象 mes_equ_check
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesEquCheck.class)
public class MesEquCheckVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 类别
     */
    @ExcelProperty(value = "类别")
    private String types;

    /**
     * 关联设备
     */
    @ExcelProperty(value = "关联设备")
    private String equIds;
    private String equIdnames;
    private String[] equIdsarray;
    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

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

    private String maintype;


    private List<MesEqucheckListVo> listvo;


}
