package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesInst;
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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 其他入库业务对象 mes_inst
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesInst.class, reverseConvertGenerate = false)
public class MesInstBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 采购列表
     */
    private String recids;

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
    private String met;

    /**
     * 税点
     */
    private String shui;

    /**
     * 文件
     */
    private String files;

    /**
     * 采购时间
     */
    @DateTimeFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private Date ctime;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private Integer ispri;

    private String fromtype;

    private String remark2;

    private String orderid;
}
