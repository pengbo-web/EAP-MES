package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesOust;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 其他出库业务对象 mes_oust
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesOust.class, reverseConvertGenerate = false)
public class MesOustBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 采购列表
     */
    private String recids;

    /**
     * 批次号
     */
    private String sn;

    /**
     * 入库位置
     */
    private Long stocktype;

    /**
     * 总价
     */
    private BigDecimal allprice;

    /**
     * 结算方式
     */
    private String MET;

    /**
     * 税点
     */
    private String SHUI;

    /**
     * 文件
     */
    private String files;

    /**
     * 采购时间
     */
    private Date ctime;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
