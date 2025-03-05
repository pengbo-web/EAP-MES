package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 质检内容对象 mes_quality_list
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_quality_list")
public class MesQualityList extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    private String title;
    /**
     * 名称
     */
    private Long qualitymainId;

    /**
     * 标准
     */
    private String standards;

    /**
     * 公差
     */
    private String refvalue;

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
