package com.liaoyun.mes.dispatch;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liaoyun.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 工作站暂停对象 mes_workplace_suspend(二次开发新增, 报警联动)
 *
 * @author pengbo
 * @date 2026-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_workplace_suspend")
public class MesWorkplaceSuspend extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 工作站id
     */
    private Long workplaceId;

    /**
     * 暂停原因
     */
    private String reason;

    /**
     * 暂停至
     */
    private Date suspendUntil;

    /**
     * 删除标志
     */
    @TableLogic
    private String delFlag;
}
