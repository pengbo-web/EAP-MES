package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesToolRec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 夹具记录业务对象 mes_tool_rec
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesToolRec.class, reverseConvertGenerate = false)
public class MesToolRecBo extends BaseEntity {

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

    private String username;

    /**
     * 原因
     */
    private String rebak;

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

    private String workId;
    private Long useId;
}
