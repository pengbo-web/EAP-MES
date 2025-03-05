package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesTeams;
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
 * 班组管理视图对象 mes_teams
 *
 * @author Liao Liao
 * @date 2024-10-25
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesTeams.class)
public class MesTeamsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 组员
     */
    @ExcelProperty(value = "组员")
    private String useids;
    private List<Long> useidsList;

    /**
     * 班组长
     */
    @ExcelProperty(value = "班组长")
    private String upids;
    private List<Long> upidsList;

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

    private String stname;
    private String upnames;
    private String username;
    private String mytask;
    private String qualist;
    private String yichang;
    private String worknum;
    private String userid;
}
