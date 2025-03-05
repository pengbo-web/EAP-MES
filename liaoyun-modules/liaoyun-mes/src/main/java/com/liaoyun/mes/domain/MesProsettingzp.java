package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 组装排程对象 mes_prosettingzp
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_prosettingzp")
public class MesProsettingzp extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 工作站
     */
    private String workshopId;

    /**
     * 物料
     */
    private Long proId;

    /**
     * 需组装数量
     */
    private Long zpcount;

    /**
     * 排序
     */
    private Integer sort;

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

    /**
     * 是否关键工序
     */
    private Integer mustbe;

    /**
     * 是否工艺流转
     */
    private Integer istruec;


}
