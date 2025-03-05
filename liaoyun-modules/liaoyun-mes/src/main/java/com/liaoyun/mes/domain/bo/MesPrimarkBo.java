package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesPrimark;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 打单列表业务对象 mes_primark
 *
 * @author Liao Liao
 * @date 2024-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesPrimark.class, reverseConvertGenerate = false)
public class MesPrimarkBo extends BaseEntity {

    /**
     * 编号
     */
    private Long id;

    /**
     * 工作id
     */
    private Long workid;

    /**
     * 订单号
     */
    private Long orderid;

    /**
     * 交货日期
     */
    private String subtime;

    /**
     * 流转码
     */
    private String sn;

    /**
     * 物料码
     */
    private Long stockid;

    /**
     * 版次
     */
    private String banci;

    /**
     * 数量
     */
    private BigDecimal counts;

    private BigDecimal counts1;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


    private Integer wul;

    private Integer tc;

    private Long upid;
}
