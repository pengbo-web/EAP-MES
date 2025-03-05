package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesQualityMain;
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
 * 质检主表视图对象 mes_quality_main
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesQualityMain.class)
public class MesQualityMainVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 质检名称
     */
    @ExcelProperty(value = "质检名称")
    private String title;

    /**
     * 别名
     */
    @ExcelProperty(value = "别名")
    private String other;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "diyisno")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;



    private List<MesQualityListVo> qualityListVoList;


}
