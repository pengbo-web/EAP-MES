package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 设备点检对象 mes_equ_check
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_equ_check")
public class MesEquCheck extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    private String maintype;
    /**
     * 类别
     */
    private String types;

    /**
     * 关联设备
     */
    private String equIds;

    /**
     * 标题
     */
    private String title;

    /**
     * 角色状态（0正常 1停用）
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


}
