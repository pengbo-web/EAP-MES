package com.liaoyun.system.domain.vo;

import com.liaoyun.system.domain.MesConfig;
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
 * MES配置视图对象 mes_config
 *
 * @author Liao Liao
 * @date 2025-02-25
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesConfig.class)
public class MesConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 工作时间
     */
    @ExcelProperty(value = "工作时间")
    private String times;

    /**
     * 检查时间
     */
    @ExcelProperty(value = "检查时间")
    private String chetime;


}
