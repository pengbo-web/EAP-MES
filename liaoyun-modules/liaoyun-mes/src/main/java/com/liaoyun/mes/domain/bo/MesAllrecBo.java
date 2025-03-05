package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesAllrec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 出入库记录业务对象 mes_allrec
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesAllrec.class, reverseConvertGenerate = false)
public class MesAllrecBo extends BaseEntity {

    /**
     * 编码
     */
    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     * 来源
     */
    private String fromtype;

    /**
     * 数量
     */
    private BigDecimal counts;

    /**
     * 单位
     */
    private Long unitid;

    /**
     * 价格
     */
    private BigDecimal prices;

    /**
     * 物料
     */
    private Long stockid;

    /**
     * 供应商
     */
    private String cname;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private String stockcode;

    private String stockname;

    private String remark2;

    private Long stocktype;
    /**
     * 订单号
     */
    private String orderid;
}
