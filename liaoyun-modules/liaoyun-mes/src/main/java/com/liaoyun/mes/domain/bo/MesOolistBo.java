package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOolist;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 委外入库详情业务对象 mes_oolist
 *
 * @author Liao Liao
 * @date 2024-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOolist.class, reverseConvertGenerate = false)
public class MesOolistBo extends BaseEntity {

    /**
     * 编号
     */
    private Long id;

    /**
     * 物料
     */
    private Long stockid;

    /**
     * 数量
     */
    private BigDecimal counts;

    /**
     * 主编号
     */
    private Long mainid;

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

    private Long kwid;
}
