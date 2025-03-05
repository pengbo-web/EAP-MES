package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesOutRecVo;
import com.liaoyun.mes.domain.bo.MesOutRecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 外协记录Service接口
 *
 * @author Liao Liao
 * @date 2024-07-26
 */
public interface IMesOutRecService {

    /**
     * 查询外协记录
     *
     * @param id 主键
     * @return 外协记录
     */
    MesOutRecVo queryById(Long id);

    /**
     * 分页查询外协记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 外协记录分页列表
     */
    TableDataInfo<MesOutRecVo> queryPageList(MesOutRecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的外协记录列表
     *
     * @param bo 查询条件
     * @return 外协记录列表
     */
    List<MesOutRecVo> queryList(MesOutRecBo bo);

    /**
     * 新增外协记录
     *
     * @param bo 外协记录
     * @return 是否新增成功
     */
    Boolean insertByBo(MesOutRecBo bo);

    /**
     * 修改外协记录
     *
     * @param bo 外协记录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesOutRecBo bo);

    /**
     * 校验并批量删除外协记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
