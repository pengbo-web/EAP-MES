package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesProtype;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * ②产品类别业务对象 mes_protype
 *
 * @author Kylin W
 * @date 2024-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesProtype.class, reverseConvertGenerate = false)
public class MesProtypeBo extends BaseEntity {

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
     * 上级
     */
    @NotNull(message = "上级不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long pid;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
