package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesQuaRec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 质检列表业务对象 mes_qua_rec
 *
 * @author Liao Liao
 * @date 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesQuaRec.class, reverseConvertGenerate = false)
public class MesQuaRecBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;


    private String sn;
    /**
     * 质检主键
     */
    private Long quaMian;

    /**
     * 流转号
     */

    /**
     * 标题
     */
    private String title;

    /**
     * 任务列表
     */
    private Long taskId;

    /**
     * 值
     */
    private String valuest;

    /**
     * 结果
     */
    private String res;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
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


}
