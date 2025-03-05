package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesQuaRec;
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
 * 质检列表视图对象 mes_qua_rec
 *
 * @author Liao Liao
 * @date 2024-07-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesQuaRec.class)
public class MesQuaRecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 质检主键
     */
    @ExcelProperty(value = "质检主键")
    private Long quaMian;
    private String quaname;

    /**
     * 流转号
     */
    @ExcelProperty(value = "流转号")
    private String sn;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 任务列表
     */
    @ExcelProperty(value = "任务列表")
    private Long taskId;

    /**
     * 值
     */
    @ExcelProperty(value = "值")
    private String valuest;

    /**
     * 结果
     */
    @ExcelProperty(value = "结果")
    private String res;

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

    /**
     * 是否关键工序
     */
    @ExcelProperty(value = "是否关键工序")
    private Integer mustbe;


    private MesTaskVo taskinfo;

    private MesQualityMainVo maininfo;

    private Long createBy;
    private String createByName;

    private Date createTime;
}
