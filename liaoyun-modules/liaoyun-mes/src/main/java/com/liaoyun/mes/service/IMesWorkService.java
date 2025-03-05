package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesWorkVo;
import com.liaoyun.mes.domain.bo.MesWorkBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

/**
 * 生产派单Service接口
 *
 * @author Liao Liao
 * @date 2024-07-03
 */
public interface IMesWorkService {

    /**
     * 查询生产派单
     *
     * @param id 主键
     * @return 生产派单
     */
    MesWorkVo queryById(Long id);

    /**
     * 分页查询生产派单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 生产派单分页列表
     */
    TableDataInfo<MesWorkVo> queryPageList(MesWorkBo bo, PageQuery pageQuery) throws ParseException;

    /**
     * 查询符合条件的生产派单列表
     *
     * @param bo 查询条件
     * @return 生产派单列表
     */
    List<MesWorkVo> queryList(MesWorkBo bo);

    /**
     * 新增生产派单
     *
     * @param bo 生产派单
     * @return 是否新增成功
     */
    Boolean insertByBo(MesWorkBo bo);

    /**
     * 修改生产派单
     *
     * @param bo 生产派单
     * @return 是否修改成功
     */
    Boolean updateByBo(MesWorkBo bo);

    /**
     * 校验并批量删除生产派单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
