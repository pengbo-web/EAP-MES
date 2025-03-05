package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesOutRec;
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
 * 外协记录视图对象 mes_out_rec
 *
 * @author Liao Liao
 * @date 2024-07-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesOutRec.class)
public class MesOutRecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;


    private String sn;

    /**
     * 外协
     */
    @ExcelProperty(value = "外协")
    private Long outId;

    /**
     * 产品
     */
    @ExcelProperty(value = "产品")
    private Long proId;


    private Long taskId;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long count;

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

    private MesProductVo productVo;

    private MesOutsourcingVo outsourcingVo;

    private MesTaskVo taskVo;

    private String createTime;
}
