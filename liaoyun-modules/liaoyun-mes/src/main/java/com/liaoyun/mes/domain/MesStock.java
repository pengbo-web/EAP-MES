package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import java.io.Serial;

/**
 * 仓库信息对象 mes_stock
 *
 * @author Liao Liao
 * @date 2024-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_stock")
public class MesStock extends TenantEntity {

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
    private String tilte;
    private Integer pcount;

    private String custid;

    /**
     * 报警
     */
    private BigDecimal warnnum;

    /**
     * 类型
     */
    private String type;

    private String otype;
    /**
     * 图号
     */
    private String iid;

    /**
     * 库存编码
     */
    @OrderBy
    private String sn;


    private String gk;

    /**
     * 库存信息
     */
    private Long stockid;

    /**
     * 总入库
     */
    private BigDecimal instock;

    /**
     * 总出库
     */
    private BigDecimal outstock;

    /**
     * 当前库存
     */
    private BigDecimal nowstock;

    /**
     * 规格
     */
    private Long unitid;

    /**
     * 物料
     */
    private Long proid;

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

    private String kwrec;

    private Integer sales;
}
