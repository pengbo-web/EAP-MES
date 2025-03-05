package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesPriceRecVo;
import com.liaoyun.mes.domain.bo.MesPriceRecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 价格波动Service接口
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
public interface IMesPriceRecService {

    /**
     * 查询价格波动
     *
     * @param id 主键
     * @return 价格波动
     */
    MesPriceRecVo queryById(Long id);

    /**
     * 分页查询价格波动列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 价格波动分页列表
     */
    TableDataInfo<MesPriceRecVo> queryPageList(MesPriceRecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的价格波动列表
     *
     * @param bo 查询条件
     * @return 价格波动列表
     */
    List<MesPriceRecVo> queryList(MesPriceRecBo bo);

    /**
     * 新增价格波动
     *
     * @param bo 价格波动
     * @return 是否新增成功
     */
    Boolean insertByBo(MesPriceRecBo bo);

    /**
     * 修改价格波动
     *
     * @param bo 价格波动
     * @return 是否修改成功
     */
    Boolean updateByBo(MesPriceRecBo bo);

    /**
     * 校验并批量删除价格波动信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
