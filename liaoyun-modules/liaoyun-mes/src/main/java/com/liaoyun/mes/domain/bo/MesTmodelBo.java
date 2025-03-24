package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesTmodel;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 模具管理业务对象 mes_tmodel
 *
 * @author Liao Liao
 * @date 2024-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesTmodel.class, reverseConvertGenerate = false)
public class MesTmodelBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String title;

    /**
     * 编码
     */
    private String sn;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 类别
     */
    private String otype;

    /**
     * 当前库存
     */
    private Long stocknum;

    /**
     * 历史库存
     */
    private Long stockrec;

    /**
     * 报废库存
     */
    private Long otstock;

    /**
     * 使用中数量
     */
    private Long usestock;

    /**
     * 保养周期
     */
    private Integer otime;

    /**
     * 下次周期
     */
    private Date nexttime;

    /**
     * 可用库存量
     */
    private Long instock;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 已加工数量
     */
    private Long markcount;

    /**
     * 加工产品
     */
    private String proids;

    /**
     * 刀具名称
     */
    private String names;

    private String local;

    private String gk;

    private String descs;
}
