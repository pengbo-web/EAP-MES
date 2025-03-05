package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesCuttingRec;
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
 * 刀具记录审核视图对象 mes_cutting_rec
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesCuttingRec.class)
public class MesCuttingRecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 主表
     */
    @ExcelProperty(value = "主表")
    private Long toolid;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long count;

    /**
     * 图片
     */
    @ExcelProperty(value = "图片")
    private String imgs;

    /**
     * 原因
     */
    @ExcelProperty(value = "原因")
    private String rebak;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String type;

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

    /**
     * 工作中
     */
    @ExcelProperty(value = "工作中")
    private String workId;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long useId;


    private String cuttingname;

    private String username;

    private MesCuttingVo mesCuttingVo;
    private Date createTime;
}
