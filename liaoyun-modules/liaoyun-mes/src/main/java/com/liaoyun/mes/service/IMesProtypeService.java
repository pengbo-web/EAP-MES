package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.MesProtype;
import com.liaoyun.mes.domain.vo.MesProtypeVo;
import com.liaoyun.mes.domain.bo.MesProtypeBo;

import java.util.Collection;
import java.util.List;

/**
 * ②产品类别Service接口
 *
 * @author Kylin W
 * @date 2024-05-11
 */
public interface IMesProtypeService {

    /**
     * 查询②产品类别
     */
    MesProtypeVo queryById(Long id);


    /**
     * 查询②产品类别列表
     */
    List<MesProtypeVo> queryList(MesProtypeBo bo);

    /**
     * 新增②产品类别
     */
    Boolean insertByBo(MesProtypeBo bo);

    /**
     * 修改②产品类别
     */
    Boolean updateByBo(MesProtypeBo bo);

    /**
     * 校验并批量删除②产品类别信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
