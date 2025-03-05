package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesEqucheckList;
import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 设备点检列业务对象 mes_equcheck_list
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesEqucheckList.class, reverseConvertGenerate = false)
public class MesEqucheckListBo extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 关联主表
     */
    private Long equMain;

    /**
     * 名称
     */
    private String title;

    /**
     * 标准
     */
    private String standards;

    /**
     * 图片
     */
    private String imgs;

    /**
     * 状态（0正常 1停用 2维修）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private String upimg;

    private String rebak;

    private Long equid;
}
