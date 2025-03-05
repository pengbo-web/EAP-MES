package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesStockType;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.Date;

/**
 * 库存分类业务对象 mes_stock_type
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesStockType.class, reverseConvertGenerate = false)
public class MesStockTypeBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    private String sn;

    private String descs;

    private Integer counts;

    private Date looptime;

    /**
     * 上级
     */
    private Long pid;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private Long kwid;
    private String kwsn;

}
