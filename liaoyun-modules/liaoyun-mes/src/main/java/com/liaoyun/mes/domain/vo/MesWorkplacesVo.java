package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesEtool;
import com.liaoyun.mes.domain.MesToolUse;
import com.liaoyun.mes.domain.MesWorkplaces;
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
 * 工作站视图对象 mes_workplaces
 *
 * @author Liao Liao
 * @date 2024-06-05
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesWorkplaces.class)
public class MesWorkplacesVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 编码
     */
    @ExcelProperty(value = "编码")
    private String sn;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 车间ID
     */
    @ExcelProperty(value = "车间ID")
    private Long workshopId;
    private String workshopName;
    /**
     * 设备
     */
    @ExcelProperty(value = "设备")
    private String equIds;
    private String equIdnames;
    private String[] equIdsarray;

    /**
     * 班组
     */
    @ExcelProperty(value = "班组")
    private String teams;
    private String teamnames;
    private String[] teamsarray;

    /**
     * 夹具
     */
    @ExcelProperty(value = "夹具")
    private String tongsIds;
    private String tongames;
    private List<MesEtool> toolUses;
    private String[] tongarray;
    /**
     * 工序
     */
    @ExcelProperty(value = "工序")
    private String processIds;
    private String processNames;
    private String[] processIdsarray;
    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Integer counts;

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


}
