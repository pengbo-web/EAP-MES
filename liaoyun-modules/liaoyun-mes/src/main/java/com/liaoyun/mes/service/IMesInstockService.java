package com.liaoyun. mes.service;

import com.liaoyun. mes.domain.vo.MesInstockVo;
import com.liaoyun. mes.domain.bo.MesInstockBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 入库信息Service接口
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
public interface IMesInstockService {

    /**
     * 查询入库信息
     *
     * @param id 主键
     * @return 入库信息
     */
    MesInstockVo queryById(Long id);

    /**
     * 分页查询入库信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 入库信息分页列表
     */
    TableDataInfo<MesInstockVo> queryPageList(MesInstockBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的入库信息列表
     *
     * @param bo 查询条件
     * @return 入库信息列表
     */
    List<MesInstockVo> queryList(MesInstockBo bo);

    /**
     * 新增入库信息
     *
     * @param bo 入库信息
     * @return 是否新增成功
     */
    Boolean insertByBo(MesInstockBo bo,int st);

    /**
     * 修改入库信息
     *
     * @param bo 入库信息
     * @return 是否修改成功
     */
    Boolean updateByBo(MesInstockBo bo);

    /**
     * 校验并批量删除入库信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
