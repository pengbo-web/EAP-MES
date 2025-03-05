package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 夹具记录对象 mes_tool_rec
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_tool_rec")
public class MesToolRec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 主表
     */
    private Long toolid;

    /**
     * 数量
     */
    private Long count;

    /**
     * 图片
     */
    private String imgs;

    /**
     * 原因
     */
    private String rebak;

    /**
     * 状态
     */
    private String type;

    /**
     * 用户
     */
    private Long userId;

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

    private String workId;


    private Long useId;

}
