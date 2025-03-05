package com.liaoyun.mes.domain;

import com.liaoyun.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 客户供应商对象 mes_custom
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_custom")
public class MesCustom extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 类型
     */
    private String type;
    private String cname;

    /**
     * 简称
     */
    private String descp;

    /**
     * 银行账户
     */
    private String cardnum;

    /**
     * 全称
     */
    private String title;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 社会编码
     */
    private String shnum;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 邮寄地址
     */
    private String address1;

    /**
     * 角色状态（0正常 1停用）
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
