package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOutsourcing;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 外协列业务对象 mes_outsourcing
 *
 * @author Liao Liao
 * @date 2024-07-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOutsourcing.class, reverseConvertGenerate = false)
public class MesOutsourcingBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 厂商
     */
    private String title;

    /**
     * 工艺
     */
    private String process;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
