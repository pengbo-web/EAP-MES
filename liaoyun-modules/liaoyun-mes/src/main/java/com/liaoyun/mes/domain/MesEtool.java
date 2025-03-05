package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 工装夹具对象 mes_etool
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_etool")
public class MesEtool extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 名称
     */
    private String title;

    /**
     * 编码
     */
    private String sn;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 类别
     */
    private String otype;

    /**
     * 当前库存
     */
    private Long stocknum;

    /**
     * 历史库存
     */
    private Long stockrec;

    /**
     * 报废库存
     */
    private Long otstock;

    /**
     * 使用中数量
     */
    private Long usestock;

    /**
     * 保养周期
     */
    private Date otime;

    /**
     * 下次周期
     */
    private Date nexttime;

    /**
     * 可用库存量
     */
    private Long instock;

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

    @TableField(exist = false)
    private int count;

}
