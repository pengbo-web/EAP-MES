package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesEquipmentType;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * ⑤设备类别业务对象 mes_equipment_type
 *
 * @author Kylin
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesEquipmentType.class, reverseConvertGenerate = false)
public class MesEquipmentTypeBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 编码
     */
    private String sn;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
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
