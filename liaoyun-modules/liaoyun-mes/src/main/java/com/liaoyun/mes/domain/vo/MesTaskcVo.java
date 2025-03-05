package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.MesTaskc;
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
 * 组装流转视图对象 mes_taskc
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesTaskc.class)
public class MesTaskcVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

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
     * 完成工序
     */
    @ExcelProperty(value = "完成工序")
    private Long okprocess;

    /**
     * 报废数量
     */
    @ExcelProperty(value = "报废数量")
    private Long scrapcount;

    /**
     * 质检次数
     */
    @ExcelProperty(value = "质检次数")
    private Long isCheck;

    /**
     * 流程id
     */
    @ExcelProperty(value = "流程id")
    private Long currproid;

    /**
     * 当前流程
     */
    @ExcelProperty(value = "当前流程")
    private Long currprocess;

    /**
     * 所有工序
     */
    @ExcelProperty(value = "所有工序")
    private Long allprocess;

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

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=正常,2=进行中,3=已完成,0=异常完工,4待检查")
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

    /**
     * 是否够量
     */
    @ExcelProperty(value = "是否够量")
    private Integer enough;

    /**
     * 需出库数量
     */
    @ExcelProperty(value = "需出库数量")
    private Long stocknum;

    /**
     * 出库数量
     */
    @ExcelProperty(value = "出库数量")
    private Long outstocknum;

    /**
     * 质检次数
     */
    @ExcelProperty(value = "质检次数")
    private Long checknum;

    /**
     * 是否通过质检
     */
    @ExcelProperty(value = "是否通过质检")
    private Integer ischecknew;

    /**
     * 流转类型
     */
    @ExcelProperty(value = "流转类型")
    private Long worktype;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark2;

    private String proname;
    private String username;
    private String workname;
    private MesProductVo productVo;
    private List<MesProduct>  productList;

    private BigDecimal kc;

}
