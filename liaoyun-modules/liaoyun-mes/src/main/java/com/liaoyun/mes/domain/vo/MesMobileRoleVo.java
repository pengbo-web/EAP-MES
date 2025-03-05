package com.liaoyun.mes.domain.vo;

import com.liaoyun.mes.domain.MesMobileRole;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.liaoyun.common.excel.annotation.ExcelDictFormat;
import com.liaoyun.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 移动端权限视图对象 mes_mobile_role
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MesMobileRole.class)
public class MesMobileRoleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 用户
     */
    @ExcelProperty(value = "用户")
    private String userId;

    /**
     * 目录
     */
    @ExcelProperty(value = "目录")
    private String menuids;

    /**
     * 角色状态（1正常 2进行中 3已完成 0异常完工,4待检查）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=正常,2=进行中,3=已完成,0=异常完工,4待检查")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private String idaname;
    private String[] userIdarray;
    private String rolename;


}
