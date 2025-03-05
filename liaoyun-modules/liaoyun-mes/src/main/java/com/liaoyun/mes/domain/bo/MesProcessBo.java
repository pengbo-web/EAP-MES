package com.liaoyun.mes.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.mes.domain.MesProcess;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * ⑧工艺管理业务对象 mes_process
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesProcess.class, reverseConvertGenerate = false)
public class MesProcessBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 编码
     */
    private String sn;

    /**
     * 类别
     */
    private Long typeId;

    /**
     * 标准
     */
    private String standards;

    /**
     * 浮动
     */
    private String refvalue;

    /**
     * 是否关键工序
     */

    private Integer mustbe;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 角色状态（0正常 1停用）
     */
    @NotBlank(message = "角色状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
    private String remark;

    private Integer moneytype;

    private BigDecimal moneyprice;

    private String userids;
}
