package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 模具使用记录对象 mes_modelrec
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_modelrec")
public class MesModelrec extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 磨具编号
     */
    private Long modelid;

    /**
     * 使用地址
     */
    private String wname;

    /**
     * 类型
     */
    private String type;

    /**
     * 外发名称
     */
    private String cname;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 归还日期
     */
    private Date endtime;

    /**
     * 备注2
     */
    private String remark1;

    /**
     * 状态（0正常 1停用）
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


}
