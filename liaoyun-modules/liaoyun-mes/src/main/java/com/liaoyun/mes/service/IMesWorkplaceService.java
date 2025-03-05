package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.MesWorkplace;
import com.liaoyun.mes.domain.vo.MesWorkplaceVo;
import com.liaoyun.mes.domain.bo.MesWorkplaceBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * ④车间设置Service接口
 *
 * @author Kylin W
 * @date 2024-05-14
 */
public interface IMesWorkplaceService {

    /**
     * 查询④车间设置
     */
    MesWorkplaceVo queryById(Long id);

    /**
     * 查询④车间设置列表
     */
    TableDataInfo<MesWorkplaceVo> queryPageList(MesWorkplaceBo bo, PageQuery pageQuery);

    /**
     * 查询④车间设置列表
     */
    List<MesWorkplaceVo> queryList(MesWorkplaceBo bo);

    /**
     * 新增④车间设置
     */
    Boolean insertByBo(MesWorkplaceBo bo);

    /**
     * 修改④车间设置
     */
    Boolean updateByBo(MesWorkplaceBo bo);

    /**
     * 校验并批量删除④车间设置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
