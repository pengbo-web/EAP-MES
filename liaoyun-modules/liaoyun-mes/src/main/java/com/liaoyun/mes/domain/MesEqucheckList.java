package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 设备点检列对象 mes_equcheck_list
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_equcheck_list")
public class MesEqucheckList extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 关联主表
     */
    private Long equMain;

    /**
     * 名称
     */
    private String title;

    /**
     * 标准
     */
    private String standards;

    /**
     * 图片
     */
    private String imgs;

    /**
     * 状态（0正常 1停用 2维修）
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

    private String upimg;

    private String rebak;



}
