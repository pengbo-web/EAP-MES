package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesWorkplace;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * ④车间设置业务对象 mes_workplace
 *
 * @author Kylin W
 * @date 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesWorkplace.class, reverseConvertGenerate = false)
public class MesWorkplaceBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 车间编码
     */
    private String sn;

    /**
     * 车间名称
     */
    @NotBlank(message = "车间名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 面积
     */
    private String area;

    /**
     * 负责人
     */
    @NotNull(message = "负责人不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long curator;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
