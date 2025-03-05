package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesTeams;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 班组管理业务对象 mes_teams
 *
 * @author Liao Liao
 * @date 2024-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesTeams.class, reverseConvertGenerate = false)
public class MesTeamsBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 组员
     */
    private String useids;

    /**
     * 班组长
     */
    private String upids;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
