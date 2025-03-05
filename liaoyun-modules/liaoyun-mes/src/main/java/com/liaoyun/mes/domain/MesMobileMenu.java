package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 移动端目录对象 mes_mobile_menu
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_mobile_menu")
public class MesMobileMenu extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 图标
     */
    private String icoimg;

    /**
     * 名称
     */
    private String title;

    /**
     * 类别
     */
    private Integer type;

    /**
     * 连接
     */
    private String urls;

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
