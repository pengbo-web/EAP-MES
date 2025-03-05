package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesQualityList;
import com.liaoyun.mes.domain.MesTask;
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
 * 任务流转业务对象 mes_task
 *
 * @author Liao Liao
 * @date 2024-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesTask.class, reverseConvertGenerate = false)
public class MesTaskBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 流转批次号
     */
    private String sn;

    /**
     * 产品
     */
    private Long proId;

    /**
     * 工作站
     */
    private Long workplaceId;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 所需数量
     */
    private Long needcount;

    /**
     * 生产数量
     */
    private Long markcount;

    /**
     * 是否够量
     */
    private Integer enough;

    /**
     * 报废数量
     */
    private Long scrapcount;

    /**
     * 是否质检
     */
    private Integer isCheck;

    /**
     * 当前流程
     */
    private Integer currprocess;

    /**
     * 返工数量
     */
    private Long redoingcount;

    /**
     * 完成工序
     */
    private Integer okprocess;
    /**
     * 返工id
     */
    private Long redoingid;

    /**
     * 是否加急
     */
    private Integer isTop;

    /**
     * 主表id
     */
    private Long workId;

    /**
     * 所有工序
     */
    private Integer allprocess;

    /**
     * 类型
     */
    private String type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 工序id
     */
    private Long currproid;

    /**
     * 角色状态（0正常 1进行中 2已完成 3异常完工）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


    private String remark1;

    /**
     * 是否关键工序
     */
    private Integer mustbe;

    /**
     * 需出库数量
     */
    private Integer stocknum;
    /**
     * 出库数量
     */
    private Integer outstocknum;

    /**
     * 姓名（查询用
     */
    private String nickName;

    /**
     * 手机号—（查询用
     */
    private String phonenumber;

    /**
     * 质检次数
     */
    private Integer checknum;
    /**
     * 是否通过质检
     */
    private Integer ischecknew;

    private Integer worktype;


    private Long workiid;

    private BigDecimal count;
    /**
     * list Quality
     *
     */
    private List<MesQualityList> qualityListList;

}
