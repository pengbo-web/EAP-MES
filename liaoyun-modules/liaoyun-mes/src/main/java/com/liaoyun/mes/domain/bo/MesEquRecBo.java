package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesEquRec;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 设备点检记录业务对象 mes_equ_rec
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesEquRec.class, reverseConvertGenerate = false)
public class MesEquRecBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 主表关联
     */
    private Long equmian;

    /**
     * 列表关联
     */
    private Long listid;

    /**
     * 标题
     */
    private String title;

    /**
     * 异常内容
     */
    private String conts;

    /**
     * 结果
     */
    private String rest;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private List<MesEqucheckListBo> list;

    private Long equid;
}
