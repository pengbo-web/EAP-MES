package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesQualityListVo;
import com.liaoyun.mes.domain.vo.MesQualityMainVo;
import com.liaoyun.mes.domain.bo.MesQualityMainBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 质检主表Service接口
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
public interface IMesQualityMainService {

    /**
     * 查询质检主表
     *
     * @param id 主键
     * @return 质检主表
     */
    MesQualityMainVo queryById(Long id);

    /**
     * 分页查询质检主表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 质检主表分页列表
     */
    TableDataInfo<MesQualityMainVo> queryPageList(MesQualityMainBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的质检主表列表
     *
     * @param bo 查询条件
     * @return 质检主表列表
     */
    List<MesQualityMainVo> queryList(MesQualityMainBo bo);



    List<MesQualityListVo> getqualityList(Long bo);
    /**
     * 新增质检主表
     *
     * @param bo 质检主表
     * @return 是否新增成功
     */
    Boolean insertByBo(MesQualityMainBo bo);

    /**
     * 修改质检主表
     *
     * @param bo 质检主表
     * @return 是否修改成功
     */
    Boolean updateByBo(MesQualityMainBo bo);

    /**
     * 校验并批量删除质检主表信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
