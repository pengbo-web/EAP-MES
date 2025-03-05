package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * ④车间设置对象 mes_workplace
 *
 * @author Kylin W
 * @date 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_workplace")
public class MesWorkplace extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 车间编码
     */
    private String sn;

    /**
     * 车间名称
     */
    private String title;

    /**
     * 面积
     */
    private String area;

    /**
     * 负责人
     */
    private Long curator;

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
