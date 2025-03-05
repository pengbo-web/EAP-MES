package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesProcess;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * ⑧工艺管理视图对象 mes_process
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesProcess.class)
public class MesProcessVo implements Serializable {

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
     * 类别
     */
    @ExcelProperty(value = "类别")
    private Long typeId;

    /**
     * 标准
     */
    @ExcelProperty(value = "标准")
    private String standards;

    /**
     * 浮动
     */
    @ExcelProperty(value = "浮动")
    private String refvalue;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;
    /**
     * 是否关键工序
     */
    @ExcelProperty(value = "是否关键工序")
    private Integer mustbe;

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

    private Integer moneytype;


    private String typename;

    private String wospname;
    private Long wrid;
    private BigDecimal moneyprice;

    private String userids;
    private Integer sort;
    private List<Long> ids;
    private String usernames;
}
