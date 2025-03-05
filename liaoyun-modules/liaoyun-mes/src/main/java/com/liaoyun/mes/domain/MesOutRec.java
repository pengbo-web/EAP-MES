package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 外协记录对象 mes_out_rec
 *
 * @author Liao Liao
 * @date 2024-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_out_rec")
public class MesOutRec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 外协
     */
    private Long outId;

    private String sn;
    /**
     * 产品
     */
    private Long proId;
    private Long taskId;

    /**
     * 数量
     */
    private Long count;

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


}
