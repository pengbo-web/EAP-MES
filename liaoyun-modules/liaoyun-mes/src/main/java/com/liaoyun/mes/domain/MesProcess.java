package com.liaoyun.mes.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * ⑧工艺管理对象 mes_process
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_process")
public class MesProcess extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @OrderBy
    private Long id;

    /**
     * 编码
     */
    private String sn;

    /**
     * 类别
     */
    private Long typeId;

    /**
     * 标准
     */
    private String standards;

    /**
     * 浮动
     */
    private String refvalue;

    /**
     * 标题
     */
    private String title;
    /**
     * 是否关键工序
     */
    private Integer mustbe;

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

    private Integer moneytype;

    private BigDecimal moneyprice;

    private String userids;
}
