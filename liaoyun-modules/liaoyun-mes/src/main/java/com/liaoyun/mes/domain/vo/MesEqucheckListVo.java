package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesEqucheckList;
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
 * 设备点检列视图对象 mes_equcheck_list
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesEqucheckList.class)
public class MesEqucheckListVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 关联主表
     */
    @ExcelProperty(value = "关联主表")
    private Long equMain;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String title;

    /**
     * 标准
     */
    @ExcelProperty(value = "标准")
    private String standards;

    /**
     * 图片
     */
    @ExcelProperty(value = "图片")
    private String imgs;

    /**
     * 状态（0正常 1停用 2维修）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "equstatus")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private String upimg;

    private String rebak;

    private String iiim;
}
