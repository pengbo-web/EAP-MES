package com.liaoyun.mes.domain.vo;

import com.liaoyun.common.translation.annotation.Translation;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.common.translation.constant.TransConstant;
import com.liaoyun.mes.domain.MesPre;
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
 * 采购审核视图对象 mes_pre
 *
 * @author Liao Liao
 * @date 2025-03-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesPre.class)
public class MesPreVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 请假类型
     */
    @ExcelProperty(value = "请假类型")
    private String leaveType;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String tilte;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private String sn;

    /**
     * 规格
     */
    @ExcelProperty(value = "规格")
    private String gk;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private Date startDate;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private Date endDate;

    /**
     * 请假天数
     */
    @ExcelProperty(value = "请假天数")
    private Long leaveDays;

    /**
     * 请假原因
     */
    @ExcelProperty(value = "请假原因")
    private String remark;

    /**
     * 请假原因Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "remark")
    private String remarkUrl;
    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;


    private String jj;

    private String rem;

}
