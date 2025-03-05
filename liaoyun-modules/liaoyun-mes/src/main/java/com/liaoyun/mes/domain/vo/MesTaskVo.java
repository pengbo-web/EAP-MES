package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesTask;
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
 * 任务流转视图对象 mes_task
 *
 * @author Liao Liao
 * @date 2024-07-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesTask.class)
public class MesTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;


    /**
     * 所有工序
     */
    private Integer allprocess;
    /**
     * 流转批次号
     */
    @ExcelProperty(value = "流转批次号")
    private String sn;

    /**
     * 产品
     */
    @ExcelProperty(value = "产品")
    private Long proId;

    /**
     * 工作站
     */
    @ExcelProperty(value = "工作站")
    private Long workplaceId;

    /**
     * 用户
     */
    @ExcelProperty(value = "用户")
    private Long userId;

    /**
     * 所需数量
     */
    @ExcelProperty(value = "所需数量")
    private Long needcount;

    /**
     * 生产数量
     */
    @ExcelProperty(value = "生产数量")
    private Long markcount;

    /**
     * 报废数量
     */
    @ExcelProperty(value = "报废数量")
    private Long scrapcount;

    /**
     * 是否质检
     */
    @ExcelProperty(value = "是否质检")
    private Integer isCheck;

    /**
     * 当前流程
     */
    @ExcelProperty(value = "当前流程")
    private Integer currprocess;

    /**
     * 返工数量
     */
    @ExcelProperty(value = "返工数量")
    private Long redoingcount;

    /**
     * 返工id
     */
    @ExcelProperty(value = "返工id")
    private Long redoingid;


    /**
     * 完工工序
     */
    @ExcelProperty(value = "完工数量")
    private Integer okprocess;

    /**
     * 是否加急
     */
    @ExcelProperty(value = "是否加急")
    private Integer isTop;

    /**
     * 主表id
     */
    @ExcelProperty(value = "主表id")
    private Long workId;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;
    private Date updateTime;

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
     * 工序id
     */
    private Long currproid;
    /**
     * 是否关键工序
     */
    @ExcelProperty(value = "是否关键工序")
    private Integer mustbe;

    /**
     * 产品列表
     */
    private MesProductVo productVo;

    /**
     * 车间关联
     */
    private MesWorkplacesVo workplacesVo;

    /**
     * 用户名
     */
    private String username;

    /**
     * 工序关联
     */
    private MesProcessVo processVo;

    /**
     * 是否够量
     */
    private Integer enough;

    /**
     * 需出库数量
     */
    private Integer stocknum;
    /**
     * 出库数量
     */
    private Integer outstocknum;

    /**
     * 主表关联
     */
    private MesWorkVo workVo;

    /**
     * 质检次数
     */
    private Integer checknum;
    /**
     * 是否通过质检
     */
    private Integer ischecknew;

    private Integer worktype;

    private MesOutsourcingVo outsourcingVo;

    private Integer mycount;
    private Integer procure;
    private String  proname;
}
