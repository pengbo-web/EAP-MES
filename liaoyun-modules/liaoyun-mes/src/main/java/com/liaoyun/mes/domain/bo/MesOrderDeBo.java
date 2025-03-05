package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOrderDe;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单详情业务对象 mes_order_de
 *
 * @author Liao Liao
 * @date 2024-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOrderDe.class, reverseConvertGenerate = false)
public class MesOrderDeBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 物料
     */
    private Long stockid;

    /**
     * 单位
     */
    private Long unid;

    /**
     * 单价
     */
    private BigDecimal prices;

    /**
     * 数量
     */
    private Long counts;

    /**
     * 总价
     */
    private BigDecimal allprice;

    /**
     * 交付时间
     */
    private Date ctime;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


    private Integer pcount;

    private BigDecimal ckcounts;

}
