package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 组装流转对象 mes_taskc
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_taskc")
public class MesTaskc extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 流转批次号
     */
    private String sn;

    /**
     * 产品
     */
    private Long proId;

    /**
     * 工作站
     */
    private Long workplaceId;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 所需数量
     */
    private Long needcount;

    /**
     * 生产数量
     */
    private Long markcount;

    /**
     * 完成工序
     */
    private Long okprocess;

    /**
     * 报废数量
     */
    private Long scrapcount;

    /**
     * 质检次数
     */
    private Long isCheck;

    /**
     * 流程id
     */
    private Long currproid;

    /**
     * 当前流程
     */
    private Long currprocess;

    /**
     * 所有工序
     */
    private Long allprocess;

    /**
     * 返工数量
     */
    private Long redoingcount;

    /**
     * 返工id
     */
    private Long redoingid;

    /**
     * 是否加急
     */
    private Integer isTop;

    /**
     * 主表id
     */
    private Long workId;

    /**
     * 类型
     */
    private String type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否关键工序
     */
    private Integer mustbe;

    /**
     * 是否够量
     */
    private Integer enough;

    /**
     * 需出库数量
     */
    private Long stocknum;

    /**
     * 出库数量
     */
    private Long outstocknum;

    /**
     * 质检次数
     */
    private Long checknum;

    /**
     * 是否通过质检
     */
    private Integer ischecknew;

    /**
     * 流转类型
     */
    private Long worktype;

    /**
     * 备注
     */
    private String remark2;


}
