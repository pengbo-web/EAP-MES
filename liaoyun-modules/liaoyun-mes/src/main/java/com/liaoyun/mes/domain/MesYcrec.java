package com.liaoyun.mes.domain;

import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 异常报工审核对象 mes_ycrec
 *
 * @author Liao Liao
 * @date 2024-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_ycrec")
public class MesYcrec extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 流转
     */
    private String sn;

    /**
     * 正常数量
     */
    private Long count;

    /**
     * 图片
     */
    private String imgs;

    /**
     * 异常数量
     */
    private Long yccount;

    /**
     * 描述
     */
    private String ycdesc;

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


}
