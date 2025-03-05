package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOutorder;
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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 委外订单业务对象 mes_outorder
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOutorder.class, reverseConvertGenerate = false)
public class MesOutorderBo extends BaseEntity {

    /**
     * 编码
     */
    @NotNull(message = "编码不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 委外名称
     */
    private String title;

    /**
     * 委外商家
     */
    private Long custid;

    private Long stockid;
    private Long kwid;

    private String custname;

    /**
     * 订单号
     */
    private String orders;

    /**
     * 外部编号
     */
    private String sn;

    /**
     * 委外物料清单
     */
    private String stockids;

    /**
     * 出库库位
     */
    private String stocktype;

    /**
     * 价格
     */
    private BigDecimal prices;

    /**
     * 要求完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date subtime;

    /**
     * 出库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "UTC")
    private Date outtime;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private Long stoid;

    private Integer counts;

    private String fromtype;

    private String remark2;
}
