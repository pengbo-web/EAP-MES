package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 其他入库对象 mes_inst
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_inst")
public class MesInst extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    private String sn;
    /**
     * 采购列表
     */
    private String recids;

    /**
     * 入库位置
     */
    private Long stocktype;

    /**
     * 总价
     */
    private BigDecimal allprice;

    /**
     * 结算方式
     */
    private String met;

    /**
     * 税点
     */
    private String shui;

    /**
     * 文件
     */
    private String files;

    /**
     * 采购时间
     */
    private Date ctime;

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

    private Integer ispri;
    private String fromtype;


    private String remark2;

    private String orderid;
}
