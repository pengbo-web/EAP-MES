package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesProsettingzp;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 组装排程业务对象 mes_prosettingzp
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesProsettingzp.class, reverseConvertGenerate = false)
public class MesProsettingzpBo extends BaseEntity {

    /**
     * 编号
     */
    private Long id;

    /**
     * 工作站
     */
    private String workshopId;

    /**
     * 物料
     */
    private Long proId;

    /**
     * 需组装数量
     */
    private Long zpcount;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否关键工序
     */
    private Integer mustbe;

    /**
     * 是否工艺流转
     */
    private Integer istruec;


}
