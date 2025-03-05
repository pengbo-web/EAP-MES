package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesProcurementplan;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 采购计划业务对象 mes_procurementplan
 *
 * @author Liao Liao
 * @date 2024-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesProcurementplan.class, reverseConvertGenerate = false)
public class MesProcurementplanBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 流转号
     */
    private String sn;

    /**
     * 产品
     */
    private Long proId;
    private String proname;

    /**
     * 数量
     */
    private Long count;

    /**
     * 任务主表
     */
    private Long workId;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private Long taskId;

    private String remark2;

    private BigDecimal prices;

    private String fromtype;
}
