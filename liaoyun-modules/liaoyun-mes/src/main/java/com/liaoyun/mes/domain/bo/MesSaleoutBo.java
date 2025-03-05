package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesSaleout;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 销售出库业务对象 mes_saleout
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesSaleout.class, reverseConvertGenerate = false)
public class MesSaleoutBo extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 订单
     */
    @NotNull(message = "订单不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderid;

    /**
     * 物料
     */
    @NotNull(message = "物料不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long stockid;

    private Long kwid;

    /**
     * 数量
     */
    private Integer counts;

    /**
     * 文件
     */
    private String files;

    /**
     * 物流信息
     */
    private String wuliu;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 编码
     */
    private String sn;

    private String custno;

}
