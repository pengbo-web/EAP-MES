package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesYcrec;
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
 * 异常报工审核视图对象 mes_ycrec
 *
 * @author Liao Liao
 * @date 2024-11-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesYcrec.class)
public class MesYcrecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 流转
     */
    @ExcelProperty(value = "流转")
    private String sn;

    /**
     * 正常数量
     */
    @ExcelProperty(value = "正常数量")
    private Long count;

    /**
     * 图片
     */
    @ExcelProperty(value = "图片")
    private String imgs;

    /**
     * 异常数量
     */
    @ExcelProperty(value = "异常数量")
    private Long yccount;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String ycdesc;

    /**
     * 用户
     */
    @ExcelProperty(value = "用户")
    private Long userId;

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

    private MesProductVo productVo;

    private MesWorkVo workVo;

    private String username;

}
