package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesWuliu;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 物流信息业务对象 mes_wuliu
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesWuliu.class, reverseConvertGenerate = false)
public class MesWuliuBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 物流号
     */
    private String sn;

    /**
     * 名称
     */
    private String cname;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 信息
     */
    private String conts;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
