package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesStocktaking;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 库位盘点业务对象 mes_stocktaking
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesStocktaking.class, reverseConvertGenerate = false)
public class MesStocktakingBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 物料
     */
    @NotNull(message = "物料不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long stockid;
    private String stockname;

    /**
     * 库位
     */
    @NotNull(message = "库位不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long stocktypeid;

    /**
     * 盘查数量
     */
    @NotNull(message = "盘查数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal counts;

    /**
     * 异常数量
     */
    private BigDecimal yccounts;

    /**
     * 异常原因
     */
    private String ycrec;

    /**
     * 结存量
     */
    private BigDecimal jc;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
