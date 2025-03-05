package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesProcessType;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * ⑦工艺类别业务对象 mes_process_type
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesProcessType.class, reverseConvertGenerate = false)
public class MesProcessTypeBo extends BaseEntity {

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
