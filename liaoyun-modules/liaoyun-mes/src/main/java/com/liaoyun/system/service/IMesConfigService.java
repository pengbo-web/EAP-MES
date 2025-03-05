package com.liaoyun.system.service;

import com.liaoyun.system.domain.vo.MesConfigVo;
import com.liaoyun.system.domain.bo.MesConfigBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * MES配置Service接口
 *
 * @author Liao Liao
 * @date 2025-02-25
 */
public interface IMesConfigService {

    /**
     * 查询MES配置
     *
     * @param id 主键
     * @return MES配置
     */
    MesConfigVo queryById(Long id);

    /**
     * 分页查询MES配置列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return MES配置分页列表
     */
    TableDataInfo<MesConfigVo> queryPageList(MesConfigBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的MES配置列表
     *
     * @param bo 查询条件
     * @return MES配置列表
     */
    List<MesConfigVo> queryList(MesConfigBo bo);

    /**
     * 新增MES配置
     *
     * @param bo MES配置
     * @return 是否新增成功
     */
    Boolean insertByBo(MesConfigBo bo);

    /**
     * 修改MES配置
     *
     * @param bo MES配置
     * @return 是否修改成功
     */
    Boolean updateByBo(MesConfigBo bo);

    /**
     * 校验并批量删除MES配置信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
