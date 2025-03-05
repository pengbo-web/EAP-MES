package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesEquipment;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 设备列表业务对象 mes_equipment
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesEquipment.class, reverseConvertGenerate = false)
public class MesEquipmentBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 编码
     */
    private String sn;

    /**
     * 类别
     */
    @NotNull(message = "类别不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long typeId;

    /**
     * 品牌
     */
    @NotBlank(message = "品牌不能为空", groups = { AddGroup.class, EditGroup.class })
    private String brand;

    /**
     * 保养次数
     */
    private Integer count;

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
