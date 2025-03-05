package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 仓库信息业务对象 mes_stock
 *
 * @author Liao Liao
 * @date 2024-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesStock.class, reverseConvertGenerate = false)
public class MesStockBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String tilte;

    private String custid;

    /**
     * 报警
     */
    private BigDecimal warnnum;

    private String gk;

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
    private String sn;

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
     * 备注
     */
    private String remark;

    private Integer sales;
}
