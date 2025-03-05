package com.liaoyun. mes.domain.bo;

import com.liaoyun. mes.domain.MesInstock;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库信息业务对象 mes_instock
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesInstock.class, reverseConvertGenerate = false)
public class MesInstockBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 产品
     */
    private Long proId;
    private String proname;
    private String stocksn;

    /**
     * 数量
     */
    private Long count;

    /**
     * 来源
     */
    private String fromtype;

    /**
     * 价格
     */
    private BigDecimal nowprice;

    /**
     * 总价
     */
    private BigDecimal allprice;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private String kwname;

    private Integer st;
    private Date subtime;
    private Long stockid;
    private Long kwid;
    private String kwsn;
    private String kwrec;
}
