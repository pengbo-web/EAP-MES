package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 外协列对象 mes_outsourcing
 *
 * @author Liao Liao
 * @date 2024-07-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_outsourcing")
public class MesOutsourcing extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 厂商
     */
    private String title;

    /**
     * 工艺
     */
    private String process;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 地址
     */
    private String address;

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
