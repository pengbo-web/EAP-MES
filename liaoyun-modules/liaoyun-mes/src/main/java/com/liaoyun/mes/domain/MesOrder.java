package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 销售订单对象 mes_order
 *
 * @author Liao Liao
 * @date 2024-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_order")
public class MesOrder extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 税点
     */
    private Long shui;

    /**
     * 客户
     */
    private Long custid;

    /**
     * 交货时间
     */
    private Date subtime;

    /**
     * 客户订单号
     */
    private String custno;

    /**
     * 付款周期
     */
    private String cycle;

    /**
     * 物料信息
     */
    private String stids;

    /**
     * 发票管理
     */
    private String fp;

    /**
     * 订单时间
     */
    private Date otime;

    /**
     *
     */
    private Integer ismrp;

    /**
     * 其他文件
     */
    private String otfile;

    /**
     * 文件
     */
    private String ctfile;

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


    private Integer tsta;

    private Integer pricount;

    private Integer istop;

    private Integer canmark;

    private Integer warmdate;


}
