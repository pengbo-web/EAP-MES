package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOutstock;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 出库管理业务对象 mes_outstock
 *
 * @author Liao Liao
 * @date 2024-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOutstock.class, reverseConvertGenerate = false)
public class MesOutstockBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 物料
     */
    private Long proId;
    private String proname;

    /**
     * 出库单位
     */
    private Long unitId;

    private Long kwid;

    /**
     * 来源
     */
    private String fromtype;

    /**
     * 数量
     */
    private BigDecimal count;

    /**
     * 流转码
     */
    private String sn;

    /**
     * 任务关联
     */
    private Long taskId;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
