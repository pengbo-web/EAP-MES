package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesSaleoutVo;
import com.liaoyun.mes.domain.bo.MesSaleoutBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 销售出库Service接口
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
public interface IMesSaleoutService {

    /**
     * 查询销售出库
     *
     * @param id 主键
     * @return 销售出库
     */
    MesSaleoutVo queryById(Long id);

    /**
     * 分页查询销售出库列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 销售出库分页列表
     */
    TableDataInfo<MesSaleoutVo> queryPageList(MesSaleoutBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的销售出库列表
     *
     * @param bo 查询条件
     * @return 销售出库列表
     */
    List<MesSaleoutVo> queryList(MesSaleoutBo bo);

    /**
     * 新增销售出库
     *
     * @param bo 销售出库
     * @return 是否新增成功
     */
    Boolean insertByBo(MesSaleoutBo bo);

    /**
     * 修改销售出库
     *
     * @param bo 销售出库
     * @return 是否修改成功
     */
    Boolean updateByBo(MesSaleoutBo bo);

    /**
     * 校验并批量删除销售出库信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
