package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesToolUse;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 夹具使用业务对象 mes_tool_use
 *
 * @author Liao Liao
 * @date 2024-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesToolUse.class, reverseConvertGenerate = false)
public class MesToolUseBo extends BaseEntity {

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

    private String username;

    /**
     * 图片
     */
    private String imgs;

    /**
     * 原因
     */
    private String rebak;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 工作中
     */
    private String workId;


}
