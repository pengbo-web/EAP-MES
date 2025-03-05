package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 设备点检记录对象 mes_equ_rec
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_equ_rec")
public class MesEquRec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 主表关联
     */
    private Long equmian;

    /**
     * 列表关联
     */
    private Long listid;

    /**
     * 标题
     */
    private String title;

    /**
     * 异常内容
     */
    private String conts;

    /**
     * 结果
     */
    private String rest;

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

    private Long equid;
}
