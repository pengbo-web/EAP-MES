package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesQuaform;
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
 * 来料检视图对象 mes_quaform
 *
 * @author Liao Liao
 * @date 2024-10-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesQuaform.class)
public class MesQuaformVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @ExcelProperty(value = "编码")
    private Long id;

    /**
     * 产品
     */
    @ExcelProperty(value = "产品")
    private Long proid;

    /**
     * 批次
     */
    @ExcelProperty(value = "批次")
    private String sn;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 结果
     */
    @ExcelProperty(value = "结果")
    private String resu;

    /**
     * 是否入库
     */
    @ExcelProperty(value = "是否入库")
    private String instock;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;

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

    private Date createTime;

    private String username;

    private Long createBy;
}
