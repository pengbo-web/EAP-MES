package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOurec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 出库记录业务对象 mes_ourec
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOurec.class, reverseConvertGenerate = false)
public class MesOurecBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 物料
     */
    private Long stockid;

    /**
     * 批次号
     */
    private String sn;

    /**
     * 供应商
     */
    private Long custId;

    /**
     * 进货价
     */
    private BigDecimal nprice;

    /**
     * 入库货架
     */
    private Long stocktypeid;

    /**
     * 税点
     */
    private Long shuidian;

    /**
     * 结算方式
     */
    private String met;

    /**
     * 数量
     */
    private BigDecimal count;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


    private String remark2;

    private String orderid;


}
