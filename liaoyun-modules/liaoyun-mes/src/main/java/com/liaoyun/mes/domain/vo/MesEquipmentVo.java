package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesEquipment;
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
 * 设备列表视图对象 mes_equipment
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesEquipment.class)
public class MesEquipmentVo implements Serializable {

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
    @ExcelProperty(value = "类别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_oper_type")
    private Long typeId;

    /**
     * 品牌
     */
    @ExcelProperty(value = "品牌")
    private String brand;

    /**
     * 保养次数
     */
    @ExcelProperty(value = "保养次数")
    private Integer count;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

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
     * 类别名称
     */
    private String typename;


    private String tocolor;

}
