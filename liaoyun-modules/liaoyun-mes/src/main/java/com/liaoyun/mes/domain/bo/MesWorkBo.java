package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesWork;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 生产派单业务对象 mes_work
 *
 * @author Liao Liao
 * @date 2024-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesWork.class, reverseConvertGenerate = false)
public class MesWorkBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    private Integer pcount;
    /**
     * 编码
     */
    private String sn;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 来源类型
     */
    private Integer fromtype;

    private String custid;
    private String custname;

    /**
     * 产品关联
     */
    @NotNull(message = "产品关联不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long proId;

    /**
     * 所需数量
     */
    @NotNull(message = "所需数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer neednum;

    /**
     * 当前数量
     */
    private Integer currnum;

    /**
     * 工艺数量
     */
    private Long processcount;

    /**
     * 质检次数
     */
    private Long checkcount;

    /**
     * 是否加急
     */
    private Integer istop;

    /**
     * 需求日期
     */
    private String endtime;

    /**
     * 当前工序
     */
    private String currprocess;

    /**
     * 当前工序
     */
    private Integer okprocess;

    /**
     * 关联订单号
     */
    private Long orderid;

    /**
     * 备注
     */
    private String remark;


    private Integer isturn;

    private String ismrp;
    private String status;
    private String remark2;
    private String mustbe;

    private String stockname;
    private String stockcode;

}
