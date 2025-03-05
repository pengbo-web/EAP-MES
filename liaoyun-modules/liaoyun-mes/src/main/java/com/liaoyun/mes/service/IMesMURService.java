package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesMURVo;
import com.liaoyun.mes.domain.bo.MesMURBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 移动套餐Service接口
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
public interface IMesMURService {

    /**
     * 查询移动套餐
     *
     * @param id 主键
     * @return 移动套餐
     */
    MesMURVo queryById(Long id);

    /**
     * 分页查询移动套餐列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 移动套餐分页列表
     */
    TableDataInfo<MesMURVo> queryPageList(MesMURBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的移动套餐列表
     *
     * @param bo 查询条件
     * @return 移动套餐列表
     */
    List<MesMURVo> queryList(MesMURBo bo);

    /**
     * 新增移动套餐
     *
     * @param bo 移动套餐
     * @return 是否新增成功
     */
    Boolean insertByBo(MesMURBo bo);

    /**
     * 修改移动套餐
     *
     * @param bo 移动套餐
     * @return 是否修改成功
     */
    Boolean updateByBo(MesMURBo bo);

    /**
     * 校验并批量删除移动套餐信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
