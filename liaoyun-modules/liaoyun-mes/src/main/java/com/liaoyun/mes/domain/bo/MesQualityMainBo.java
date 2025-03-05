package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesQualityMain;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 质检主表业务对象 mes_quality_main
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesQualityMain.class, reverseConvertGenerate = false)
public class MesQualityMainBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 质检名称
     */
    private String title;

    /**
     * 别名
     */
    private String other;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
