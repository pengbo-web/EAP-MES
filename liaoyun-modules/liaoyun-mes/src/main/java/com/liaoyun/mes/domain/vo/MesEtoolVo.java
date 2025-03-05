package com.liaoyun.mes.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.mes.domain.MesEtool;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import com.liaoyun.mes.domain.MesToolUse;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 工装夹具视图对象 mes_etool
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesEtool.class)
public class MesEtoolVo implements Serializable {

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
     * 保养周期
     */
    @ExcelProperty(value = "保养周期")
    private Date otime;

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
    @ExcelDictFormat(dictType = "diyisno")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private Integer counts;

    private List<MesToolRecVo> recVoList;


    private List<MesToolUseVo> useList;

}
