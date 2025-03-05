package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

/**
 * ③产品管理对象 mes_product
 *
 * @author Kylin W
 * @date 2024-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_product")
public class MesProduct extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 编码
     */
    @OrderBy
    private String sn;

    /**
     * 标题
     */
    private String title;

    /**
     * 单位
     */
    private Long unitId;

    /**
     * 类别
     */
    private Long protypeId;

    /**
     * 需要组成件数量
     */
    private BigDecimal bcount;

    /**
     * 组成件
     */
    private Long pid;

    /**
     * 版本
     */
    private Integer ver;

    /**
     * 需求数量
     */
    private Integer needCount;

    /**
     * 状态（0正常 1停用）
     */
    private String status;


    private float weight;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    private Long stockId;

    private BigDecimal stock;

    private String mustbe;
    private Integer grow;

    private Integer iswarning;

    private Integer warningnum;

    private Integer quacount;
    @TableField(exist = false)
    private Integer mc;
    @TableField(exist = false)
    private List<MesTask> tasks;
    @TableField(exist = false)
    private String jindu;
    @TableField(exist = false)
    private String shengc;
    @TableField(exist = false)
    private String stockname;

    private Long stocks;

    private String imgs;

    private String colors;
    /**
     * 产能
     */
    private BigDecimal stime;
    /**
     * 工费
     */
    private BigDecimal prices;

}
