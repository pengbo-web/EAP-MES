package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOutRec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 外协记录业务对象 mes_out_rec
 *
 * @author Liao Liao
 * @date 2024-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOutRec.class, reverseConvertGenerate = false)
public class MesOutRecBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;


    private String sn;
    /**
     * 外协
     */
    private Long outId;
    private Long taskId;

    private String proname;

    /**
     * 产品
     */
    private Long proId;

    /**
     * 数量
     */
    private Long count;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
