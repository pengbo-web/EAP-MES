package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 计量单位对象 mes_unitmeasure
 *
 * @author Kylin
 * @date 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_unitmeasure")
public class MesUnitmeasure extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 单位编码
     */
    private String unitsn;

    /**
     * 名称
     */
    private String title;

    /**
     * 是否主单位
     */
    private String isMain;

    /**
     * 进率
     */
    private Integer grow;

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
