package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesProsettingzp;
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
 * 组装排程视图对象 mes_prosettingzp
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesProsettingzp.class)
public class MesProsettingzpVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 工作站
     */
    @ExcelProperty(value = "工作站")
    private String workshopId;

    /**
     * 物料
     */
    @ExcelProperty(value = "物料")
    private Long proId;

    private String proname;

    /**
     * 需组装数量
     */
    @ExcelProperty(value = "需组装数量")
    private Long zpcount;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;

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
     * 是否关键工序
     */
    @ExcelProperty(value = "是否关键工序")
    private Integer mustbe;

    /**
     * 是否工艺流转
     */
    @ExcelProperty(value = "是否工艺流转")
    private Integer istruec;

    private String workshopname;
}
