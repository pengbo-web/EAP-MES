package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 库存分类对象 mes_stock_type
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_stock_type")
public class MesStockType extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 标题
     */
    private String title;

    private String sn;

    private String descs;

    private Integer counts;

    private Date looptime;

    /**
     * 上级
     */
    private Long pid;

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

    private Long kwid;
    private String kwsn;

}
