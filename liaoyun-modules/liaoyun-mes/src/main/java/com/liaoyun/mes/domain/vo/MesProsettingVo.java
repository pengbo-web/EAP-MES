package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesProsetting;
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
 * 排程设置视图对象 mes_prosetting
 *
 * @author Liao Liao
 * @date 2024-06-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesProsetting.class)
public class MesProsettingVo implements Serializable {

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
    private String workshopname;

    /**
     * 工序
     */
    @ExcelProperty(value = "工序")
    private String processId;
    private String processname;
    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;


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

    /**
     * 是否关键工序
     */
    @ExcelProperty(value = "是否关键工序")
    private Integer mustbe;



    private Long proId;


    private String proname;


}
