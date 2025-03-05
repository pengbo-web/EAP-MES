package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesYcrec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 异常报工审核业务对象 mes_ycrec
 *
 * @author Liao Liao
 * @date 2024-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesYcrec.class, reverseConvertGenerate = false)
public class MesYcrecBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 流转
     */
    private String sn;

    /**
     * 正常数量
     */
    private Long count;

    /**
     * 图片
     */
    private String imgs;

    /**
     * 异常数量
     */
    private Long yccount;

    /**
     * 描述
     */
    private String ycdesc;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private String username;

    private String proname;


}
