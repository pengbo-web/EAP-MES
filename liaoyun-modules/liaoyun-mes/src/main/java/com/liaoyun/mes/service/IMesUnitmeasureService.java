package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.MesUnitmeasure;
import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;
import com.liaoyun.mes.domain.bo.MesUnitmeasureBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 计量单位Service接口
 *
 * @author Kylin
 * @date 2024-05-10
 */
public interface IMesUnitmeasureService {

    /**
     * 查询计量单位
     */
    MesUnitmeasureVo queryById(Long id);

    /**
     * 查询计量单位列表
     */
    TableDataInfo<MesUnitmeasureVo> queryPageList(MesUnitmeasureBo bo, PageQuery pageQuery);

    /**
     * 查询计量单位列表
     */
    List<MesUnitmeasureVo> queryList(MesUnitmeasureBo bo);

    /**
     * 新增计量单位
     */
    Boolean insertByBo(MesUnitmeasureBo bo);

    /**
     * 修改计量单位
     */
    Boolean updateByBo(MesUnitmeasureBo bo);

    /**
     * 校验并批量删除计量单位信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
