package com.liaoyun.system.domain.bo;

import com.liaoyun.system.domain.MesConfig;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * MES配置业务对象 mes_config
 *
 * @author Liao Liao
 * @date 2025-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesConfig.class, reverseConvertGenerate = false)
public class MesConfigBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 工作时间
     */
    private String times;

    /**
     * 检查时间
     */
    private String chetime;


}
