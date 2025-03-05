package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesQualityList;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 质检内容业务对象 mes_quality_list
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesQualityList.class, reverseConvertGenerate = false)
public class MesQualityListBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private Long qualitymainId;

    private String title;

    /**
     * 标准
     */
    private String standards;

    /**
     * 公差
     */
    private String refvalue;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
