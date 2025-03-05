package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * ③产品管理业务对象 mes_product
 *
 * @author Kylin W
 * @date 2024-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesProduct.class, reverseConvertGenerate = false)
public class MesProductBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 编码
     */
    private String sn;

    private List<MesProductBo> productBos;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String title;

    private String tilte;
    private String otype;

    private Integer ls;
    /**
     * 单位
     */
    private Long unitId;
    private Long units;

    /**
     * 类别
     */
    @NotNull(message = "类别不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long protypeId;

    /**
     * 需要组成件数量
     */
    private BigDecimal bcount;

    /**
     * 组成件
     */
    @NotNull(message = "组成件不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long pid;

    /**
     * 版本
     */
    private Integer ver;

    /**
     * 需求数量
     */
    private Integer needCount;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


    private float weight;

    private Long stockId;

    private BigDecimal stock;

    private String mustbe;

    private Integer grow;

    private Integer iswarning;

    private Integer warningnum;
    /**
     * 安全库存
     */
    private Integer quacount;
    /**
     * 关联物料
     */
    private Long stocks;
    /**
     * 图片
     */
    private String imgs;

    /**
     * 产能
     */
    private BigDecimal stime;
    /**
     * 工费
     */
    private BigDecimal prices;
    /**
     * 颜色
     */
    private String colors;
}
