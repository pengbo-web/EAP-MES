package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 质检列表对象 mes_qua_rec
 *
 * @author Liao Liao
 * @date 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_qua_rec")
public class MesQuaRec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 质检主键
     */
    private Long quaMian;

    /**
     * 流转号
     */
    private String sn;

    /**
     * 标题
     */
    private String title;

    /**
     * 任务列表
     */
    private Long taskId;

    /**
     * 值
     */
    private String valuest;

    /**
     * 结果
     */
    private String res;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
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


}
