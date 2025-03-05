package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 来料检对象 mes_quaform
 *
 * @author Liao Liao
 * @date 2024-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_quaform")
public class MesQuaform extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 产品
     */
    private Long proid;

    /**
     * 批次
     */
    private String sn;

    /**
     * 标题
     */
    private String title;

    /**
     * 结果
     */
    private String resu;

    /**
     * 是否入库
     */
    private String instock;

    /**
     * 排序
     */
    private Integer sort;

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
