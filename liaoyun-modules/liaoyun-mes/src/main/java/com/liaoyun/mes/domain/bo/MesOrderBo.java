package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOrder;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 销售订单业务对象 mes_order
 *
 * @author Liao Liao
 * @date 2024-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOrder.class, reverseConvertGenerate = false)
public class MesOrderBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 税点
     */
    private Long shui;

    /**
     * 客户
     */
    private Long custid;

    /**
     * 交货时间
     */
    private Date subtime;

    /**
     * 客户订单号
     */
    private String custno;

    /**
     * 付款周期
     */
    private String cycle;

    /**
     * 物料信息
     */
    private String stids;

    /**
     * 发票管理
     */
    private String fp;

    /**
     * 订单时间
     */
    private Date otime;

    /**
     *
     */
    private Integer ismrp;

    /**
     * 其他文件
     */
    private String otfile;

    /**
     * 文件
     */
    private String ctfile;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


    private Integer tsta;

    private String stockname;

    private String custname;

    private Integer canmark;

    private Integer warmdate;
}
