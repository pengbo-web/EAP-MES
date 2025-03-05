package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 移动套餐对象 mes_m_u_r
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_m_u_r")
public class MesMUR extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 套餐名
     */
    private String title;

    /**
     * 套餐内容
     */
    private String ids;

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


    private Date createTime;


}
