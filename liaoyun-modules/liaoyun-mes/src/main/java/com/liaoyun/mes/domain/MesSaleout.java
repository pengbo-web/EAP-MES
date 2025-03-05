package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 销售出库对象 mes_saleout
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_saleout")
public class MesSaleout extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 订单
     */
    private Long orderid;

    /**
     * 物料
     */
    private Long stockid;

    /**
     * 数量
     */
    private Integer counts;

    /**
     * 文件
     */
    private String files;

    /**
     * 物流信息
     */
    private String wuliu;

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


    /**
     * 编码
     */
    private String sn;


}
