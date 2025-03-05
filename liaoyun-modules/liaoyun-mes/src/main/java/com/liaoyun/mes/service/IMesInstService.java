package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesInstVo;
import com.liaoyun.mes.domain.bo.MesInstBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 其他入库Service接口
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
public interface IMesInstService {

    /**
     * 查询其他入库
     *
     * @param id 主键
     * @return 其他入库
     */
    MesInstVo queryById(Long id);

    /**
     * 分页查询其他入库列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 其他入库分页列表
     */
    TableDataInfo<MesInstVo> queryPageList(MesInstBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的其他入库列表
     *
     * @param bo 查询条件
     * @return 其他入库列表
     */
    List<MesInstVo> queryList(MesInstBo bo);

    /**
     * 新增其他入库
     *
     * @param bo 其他入库
     * @return 是否新增成功
     */
    Boolean insertByBo(MesInstBo bo);

    /**
     * 修改其他入库
     *
     * @param bo 其他入库
     * @return 是否修改成功
     */
    Boolean updateByBo(MesInstBo bo);

    /**
     * 校验并批量删除其他入库信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
