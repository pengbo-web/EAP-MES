package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.liaoyun.common.translation.annotation.Translation;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liaoyun.common.translation.constant.TransConstant;

import java.io.Serial;

/**
 * 采购审核对象 mes_pre
 *
 * @author Liao Liao
 * @date 2025-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_pre")
public class MesPre extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 请假类型
     */
    private String leaveType;

    /**
     * 名称
     */
    private String tilte;

    /**
     * 编号
     */
    private String sn;

    /**
     * 规格
     */
    private String gk;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 请假天数
     */
    private Long leaveDays;

    /**
     * 请假原因
     */
    private String remark;

    /**
     * 状态
     */
    private String status;


    private String jj;

    private String rem;

}
