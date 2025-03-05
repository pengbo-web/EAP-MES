package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.MesProsetting;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import com.liaoyun.mes.domain.vo.MesProcessVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 排程设置业务对象 mes_prosetting
 *
 * @author Liao Liao
 * @date 2024-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesProsetting.class, reverseConvertGenerate = false)
public class MesProsettingBo extends BaseEntity {

    /**
     * 编号
     */
    private Long id;

    /**
     * 工作站
     */
    @NotBlank(message = "工作站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String workshopId;

    private List<MesProduct> prolist;

    private List<MesProcessVo> processList;

    /**
     * 工序
     */
    @NotBlank(message = "工序不能为空", groups = { AddGroup.class, EditGroup.class })
    private String processId;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer sort;

    /**
     * 角色状态（0正常 1停用）
     */
    @NotBlank(message = "角色状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否关键工序
     */
    private Integer mustbe;


    private Long proId;


}
