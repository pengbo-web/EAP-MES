package com.liaoyun.system.domain.vo;

import com.liaoyun.system.domain.MesInventoryLedger;
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
 * 库存流水
视图对象 mes_inventory_ledger
 *
 * @author Liao Liao
 * @date 2025-02-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesInventoryLedger.class)
public class MesInventoryLedgerVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long sn;

    /**
     * 物料
     */
    @ExcelProperty(value = "物料")
    private Long stockid;

    /**
     * 库位ID（FK
     */
    @ExcelProperty(value = "库位ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "库位ID（FK")
    private Long locationId;

    /**
     * 操作数量（正数入库，负数出库）
     */
    @ExcelProperty(value = "操作数量", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "正=数入库，负数出库")
    private Long quantity;

    /**
     * 操作类型（1入库/2出库）
     */
    @ExcelProperty(value = "操作类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=入库/2出库")
    private Long operation;

    /**
     * 业务批次号
     */
    @ExcelProperty(value = "业务批次号")
    private String batchNo;

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


}
