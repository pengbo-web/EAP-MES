package com.liaoyun.mes.domain.bo;

import com.liaoyun.mes.domain.MesModelrec;
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
 * 模具使用记录业务对象 mes_modelrec
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MesModelrec.class, reverseConvertGenerate = false)
public class MesModelrecBo extends BaseEntity {

    /**
     * 编号
     */
    private Long id;

    /**
     * 磨具编号
     */
    private Long modelid;

    /**
     * 使用地址
     */
    private String wname;

    /**
     * 类型
     */
    private String type;

    /**
     * 外发名称
     */
    private String cname;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 归还日期
     */
    private Date endtime;

    /**
     * 备注2
     */
    private String remark1;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
