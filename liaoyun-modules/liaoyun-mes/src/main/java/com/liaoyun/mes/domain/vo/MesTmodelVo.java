package com.liaoyun.mes.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.mes.domain.MesModelrec;
import com.liaoyun.mes.domain.MesTmodel;
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
 * 模具管理视图对象 mes_tmodel
 *
 * @author Liao Liao
 * @date 2024-12-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesTmodel.class)
public class MesTmodelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String title;

    /**
     * 编码
     */
    @ExcelProperty(value = "编码")
    private String sn;

    /**
     * 品牌
     */
    @ExcelProperty(value = "品牌")
    private String brand;

    /**
     * 类别
     */
    @ExcelProperty(value = "类别")
    private String otype;

    /**
     * 当前库存
     */
    @ExcelProperty(value = "当前库存")
    private Long stocknum;

    /**
     * 使用中数量
     */
    private Long usestock;

    /**
     * 保养周期
     */
    private Date otime;

    /**
     * 下次周期
     */
    private Date nexttime;

    /**
     * 可用库存量
     */
    private Long instock;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "位置")
    private String local;
    @ExcelProperty(value = "规格")
    private String gk;
    @ExcelProperty(value = "说明")
    private String descs;

    private List<MesModelrec> reclist;
}
