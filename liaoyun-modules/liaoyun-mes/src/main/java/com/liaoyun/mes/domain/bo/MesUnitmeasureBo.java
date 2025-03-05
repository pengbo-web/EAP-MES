package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesUnitmeasure;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 计量单位业务对象 mes_unitmeasure
 *
 * @author Kylin
 * @date 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesUnitmeasure.class, reverseConvertGenerate = false)
public class MesUnitmeasureBo extends BaseEntity {
    private Long id;
    /**
     * 单位编码
     */
    private String unitsn;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 是否主单位
     */
    private String isMain;

    /**
     * 进率
     */
    private Integer grow;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
