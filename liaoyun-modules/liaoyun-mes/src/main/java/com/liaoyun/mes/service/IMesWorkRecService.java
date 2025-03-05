package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesWorkRecVo;
import com.liaoyun.mes.domain.bo.MesWorkRecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 工作记录Service接口
 *
 * @author Liao Liao
 * @date 2024-07-09
 */
public interface IMesWorkRecService {

    /**
     * 查询工作记录
     *
     * @param id 主键
     * @return 工作记录
     */
    MesWorkRecVo queryById(Long id);

    /**
     * 分页查询工作记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 工作记录分页列表
     */
    TableDataInfo<MesWorkRecVo> queryPageList(MesWorkRecBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的工作记录列表
     *
     * @param bo 查询条件
     * @return 工作记录列表
     */
    List<MesWorkRecVo> queryList(MesWorkRecBo bo);

    /**
     * 新增工作记录
     *
     * @param bo 工作记录
     * @return 是否新增成功
     */
    Boolean insertByBo(MesWorkRecBo bo);


    Map<String,String> getinfo();

    /**
     * 修改工作记录
     *
     * @param bo 工作记录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesWorkRecBo bo);

    /**
     * 校验并批量删除工作记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
