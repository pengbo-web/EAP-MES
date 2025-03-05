package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesWorkplaces;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 工作站业务对象 mes_workplaces
 *
 * @author Liao Liao
 * @date 2024-06-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesWorkplaces.class, reverseConvertGenerate = false)
public class MesWorkplacesBo extends BaseEntity {

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
     * 车间ID
     */
    private Long workshopId;

    /**
     * 设备
     */
    private String equIds;
    private String[] equIdsarray;

    /**
     * 班组
     */
    private String teams;
    private String[] teamsarray;

    /**
     * 夹具
     */
    private String tongsIds;
    private String[] tongsIdsarray;

    /**
     * 工序
     */
    private String processIds;
    private String[] processIdsarray;

    /**
     * 数量
     */
    private Integer counts;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
