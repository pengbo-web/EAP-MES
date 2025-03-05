package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesCuttingRec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 刀具记录审核业务对象 mes_cutting_rec
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesCuttingRec.class, reverseConvertGenerate = false)
public class MesCuttingRecBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 主表
     */
    private Long toolid;

    /**
     * 数量
     */
    private Long count;

    /**
     * 图片
     */
    private String imgs;

    /**
     * 原因
     */
    private String rebak;

    private String username;

    /**
     * 状态
     */
    private String type;

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

    /**
     * 工作中
     */
    private String workId;

    /**
     *
     */
    private Long useId;

    private String proce;

}
