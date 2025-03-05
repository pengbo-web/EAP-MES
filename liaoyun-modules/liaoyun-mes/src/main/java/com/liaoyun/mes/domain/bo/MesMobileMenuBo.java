package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesMobileMenu;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 移动端目录业务对象 mes_mobile_menu
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesMobileMenu.class, reverseConvertGenerate = false)
public class MesMobileMenuBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 图标
     */
    private String icoimg;

    /**
     * 名称
     */
    private String title;

    /**
     * 类别
     */
    private Integer type;

    /**
     * 连接
     */
    private String urls;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
