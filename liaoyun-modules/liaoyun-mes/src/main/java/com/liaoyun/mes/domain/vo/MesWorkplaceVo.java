package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesWorkplace;
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
 * ④车间设置视图对象 mes_workplace
 *
 * @author Kylin W
 * @date 2024-05-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesWorkplace.class)
public class MesWorkplaceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 车间编码
     */
    @ExcelProperty(value = "车间编码")
    private String sn;

    /**
     * 车间名称
     */
    @ExcelProperty(value = "车间名称")
    private String title;

    /**
     * 面积
     */
    @ExcelProperty(value = "面积")
    private String area;

    /**
     * 负责人
     */
    @ExcelProperty(value = "负责人")
    private Long curator;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


    private String usename;


}
