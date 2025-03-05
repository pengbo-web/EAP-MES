package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 工作站对象 mes_workplaces
 *
 * @author Liao Liao
 * @date 2024-06-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_workplaces")
public class MesWorkplaces extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 编码
     */
    private String sn;

    /**
     * 标题
     */
    private String title;

    /**
     * 车间ID
     */
    private Long workshopId;

    /**
     * 设备
     */
    private String equIds;

    /**
     * 班组
     */
    private String teams;

    /**
     * 夹具
     */
    private String tongsIds;

    /**
     * 工序
     */
    private String processIds;

    /**
     * 数量
     */
    private Integer counts;

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
