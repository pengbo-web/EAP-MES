package com.liaoyun.system.domain;

import com.liaoyun.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * MES配置对象 mes_config
 *
 * @author Liao Liao
 * @date 2025-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mes_config")
public class MesConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 工作时间
     */
    private String times;

    /**
     * 检查时间
     */
    private String chetime;


}
