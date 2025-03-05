package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesWork;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.checkerframework.checker.formatter.qual.Format;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 生产派单视图对象 mes_work
 *
 * @author Liao Liao
 * @date 2024-07-03
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesWork.class)
public class MesWorkVo implements Serializable {

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
     * 来源类型
     */
    @ExcelProperty(value = "来源类型")
    private Integer fromtype;

    /**
     * 产品关联
     */
    @ExcelProperty(value = "产品关联")
    private Long proId;

    /**
     * 所需数量
     */
    @ExcelProperty(value = "所需数量")
    private Integer neednum;

    /**
     * 当前数量
     */
    @ExcelProperty(value = "当前数量")
    private Integer currnum;

    private Integer isturn;

    /**
     * 工艺数量
     */
    @ExcelProperty(value = "工艺数量")
    private Long processcount;

    /**
     * 质检次数
     */
    @ExcelProperty(value = "质检次数")
    private Long checkcount;

    /**
     * 是否加急
     */
    @ExcelProperty(value = "是否加急", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "diyisno")
    private Integer istop;

    /**
     * 需求日期
     */
    @ExcelProperty(value = "需求日期")
    private String endtime;

    /**
     * 当前工序
     */
    @ExcelProperty(value = "当前工序")
    private String currprocess;

    /**
     * 关联订单号
     */
    @ExcelProperty(value = "关联订单号")
    private Long orderid;
    /**
     * 关联订单号
     */
    @ExcelProperty(value = "已完成工序")
    private Integer okprocess;


    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;


    private String tenantId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 关联内容
     */
    private MesProductVo proinfo;

    private MesProcessVo processVo;

    private String ismrp;

    private String remark2;
    private Date createTime;

    private MesOrderVo orderVo;

    private Integer wul;

    private String pricount;

    private String inallcount;
}
