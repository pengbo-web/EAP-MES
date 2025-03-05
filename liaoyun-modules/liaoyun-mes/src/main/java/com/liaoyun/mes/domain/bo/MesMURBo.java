package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesMUR;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 移动套餐业务对象 mes_m_u_r
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesMUR.class, reverseConvertGenerate = false)
public class MesMURBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 套餐名
     */
    private String title;

    /**
     * 套餐内容
     */
    private String ids;
    private String[] idsarray;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
