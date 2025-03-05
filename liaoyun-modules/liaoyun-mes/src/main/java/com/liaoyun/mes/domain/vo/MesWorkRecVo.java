package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesWorkRec;
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
 * 工作记录视图对象 mes_work_rec
 *
 * @author Liao Liao
 * @date 2024-07-09
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesWorkRec.class)
public class MesWorkRecVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    private String username;

    /**
     * 用户
     */
    @ExcelProperty(value = "用户")
    private Long userId;

    /**
     * 任务id
     */
    @ExcelProperty(value = "任务id")
    private Long taskId;

    /**
     * 不良品数量
     */
    private Integer defective;

    /**
     * 已完成数量
     */
    @ExcelProperty(value = "已完成数量")
    private Long lastcount;

    /**
     * 报工数量
     */
    @ExcelProperty(value = "报工数量")
    private Long count;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;

    /**
     * 角色状态（0正常 1进行中 2已完成 3异常完工）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=进行中,2=已完成,3=异常完工")
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

    private Date createTime;

    private Long proid;

    private String proname;
    private MesProductVo productVo;
    private MesTaskVo taskVo;
    private MesProcessVo processVo;
    private MesWorkVo workVo;
    private MesStockVo stockVo;
}
