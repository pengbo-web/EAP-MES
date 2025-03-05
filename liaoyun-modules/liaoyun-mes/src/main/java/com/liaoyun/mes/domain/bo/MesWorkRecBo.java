package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesWorkRec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 工作记录业务对象 mes_work_rec
 *
 * @author Liao Liao
 * @date 2024-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesWorkRec.class, reverseConvertGenerate = false)
public class MesWorkRecBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户
     */
    private Long userId;
    private String username;

    private String proname;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 已完成数量
     */
    private Long lastcount;
    /**
     * 不良品数量
     */
    private Integer defective;

    /**
     * 报工数量
     */
    private Long count;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 角色状态（0正常 1进行中 2已完成 3异常完工）
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

    private Long proid;

}
