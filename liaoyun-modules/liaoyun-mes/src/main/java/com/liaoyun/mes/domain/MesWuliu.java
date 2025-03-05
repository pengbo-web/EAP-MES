package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 物流信息对象 mes_wuliu
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_wuliu")
public class MesWuliu extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 物流号
     */
    private String sn;

    /**
     * 名称
     */
    private String cname;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 信息
     */
    private String conts;

    /**
     * 状态（0正常 1停用）
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
