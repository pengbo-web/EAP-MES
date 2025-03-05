package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesEquCheck;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 设备点检业务对象 mes_equ_check
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesEquCheck.class, reverseConvertGenerate = false)
public class MesEquCheckBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    private String maintype;

    /**
     * 类别
     */
    private String types;

    /**
     * 关联设备
     */
    private String equIds;
    private String[] equIdsarray;

    /**
     * 标题
     */
    private String title;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
