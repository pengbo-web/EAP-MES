package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesQuaform;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 来料检业务对象 mes_quaform
 *
 * @author Liao Liao
 * @date 2024-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesQuaform.class, reverseConvertGenerate = false)
public class MesQuaformBo extends BaseEntity {

    /**
     * 编码
     */
    private Long id;

    /**
     * 产品
     */
    private Long proid;

    /**
     * 批次
     */
    private String sn;

    /**
     * 标题
     */
    private String title;

    private String proname;

    /**
     * 结果
     */
    private String resu;

    /**
     * 是否入库
     */
    private String instock;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
