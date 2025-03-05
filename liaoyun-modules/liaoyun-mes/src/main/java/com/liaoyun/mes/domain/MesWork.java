package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 生产派单对象 mes_work
 *
 * @author Liao Liao
 * @date 2024-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_work")
public class MesWork extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 编码
     */
    private String sn;

    /**
     * 标题
     */
    private String title;

    /**
     * 来源类型
     */
    private Integer fromtype;

    /**
     * 产品关联
     */
    private Long proId;

    private Integer isturn;


    /**
     * 所需数量
     */
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
    @OrderBy
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
     * 关联订单号
     */
    private Long orderid;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;


    private Integer okprocess;

    /**
     * 备注
     */
    private String remark;

    private String ismrp;

    private Integer wul;
}
