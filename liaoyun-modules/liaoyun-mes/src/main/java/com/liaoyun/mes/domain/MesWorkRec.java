package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 工作记录对象 mes_work_rec
 *
 * @author Liao Liao
 * @date 2024-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_work_rec")
public class MesWorkRec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 已完成数量
     */
    private Long lastcount;
    /**
     * 不良品数量
     */
    private Integer defective;
    /**
     * 报工数量
     */
    private Long count;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 角色状态（0正常 1进行中 2已完成 3异常完工）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否关键工序
     */
    private Integer mustbe;

    private Long proid;

    /**
     * 报工设备编码(EAP快照, 二次开发新增)
     */
    private String deviceCode;

    /**
     * 工艺参数JSON快照(EAP, 二次开发新增; 字段名避让 BaseEntity.params)
     */
    @TableField("params")
    private String eapParams;

}
