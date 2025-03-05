package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesStinfo;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 库位调拨业务对象 mes_stinfo
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesStinfo.class, reverseConvertGenerate = false)
public class MesStinfoBo extends BaseEntity {

    /**
     * 编码
     */
    private Long id;

    /**
     * 物料
     */
    private Long stockid;
    private String stockname;

    /**
     * 库位
     */
    private Long stocktypeid;

    /**
     * 数量
     */
    private BigDecimal counts;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
