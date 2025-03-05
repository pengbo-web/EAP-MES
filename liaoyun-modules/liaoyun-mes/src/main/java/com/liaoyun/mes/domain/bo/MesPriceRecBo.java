package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesPriceRec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 价格波动业务对象 mes_price_rec
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesPriceRec.class, reverseConvertGenerate = false)
public class MesPriceRecBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品
     */
    private Long proId;
    private String proname;

    /**
     * 当时数量
     */
    private Long count;

    /**
     * 价格
     */
    private BigDecimal prices;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
