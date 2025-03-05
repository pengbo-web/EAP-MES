package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesCustom;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 客户供应商业务对象 mes_custom
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesCustom.class, reverseConvertGenerate = false)
public class MesCustomBo extends BaseEntity {

    /**
     * 编码
     */
    private Long id;

    /**
     * 类型
     */
    private String type;

    /**
     *
     */
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
     * 备注
     */
    private String remark;


}
