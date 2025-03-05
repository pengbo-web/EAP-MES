package com.liaoyun.mes.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.mes.domain.MesCutting;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import com.liaoyun.mes.domain.MesCuttingUse;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 刀具管理视图对象 mes_cutting
 *
 * @author Liao Liao
 * @date 2024-10-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesCutting.class)
public class MesCuttingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String title;

    /**
     * 图号
     */
    @ExcelProperty(value = "图号")
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
     * 历史库存
     */
    @ExcelProperty(value = "历史库存")
    private Long stockrec;

    /**
     * 报废库存
     */
    @ExcelProperty(value = "报废库存")
    private Long otstock;

    /**
     * 使用中数量
     */
    @ExcelProperty(value = "使用中数量")
    private Long usestock;

    /**
     * 支领日期
     */
    @ExcelProperty(value = "支领日期")
    private Integer otime;

    /**
     * 下次周期
     */
    @ExcelProperty(value = "下次周期")
    private Date nexttime;

    /**
     * 可用库存量
     */
    @ExcelProperty(value = "可用库存量")
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

    /**
     * 已加工数量
     */
    @ExcelProperty(value = "已加工数量")
    private Long markcount;

    /**
     * 加工产品
     */
    @ExcelProperty(value = "加工产品")
    private String proids;

    private String names;

    private MesEtoolVo etoolVo;

    private Date createTime;

    private List<MesCuttingRecVo> recVoList;
    private List<MesCuttingUseVo> useVoList;

}
