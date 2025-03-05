package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesTaskVo;
import com.liaoyun.mes.domain.bo.MesTaskBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 任务流转Service接口
 *
 * @author Liao Liao
 * @date 2024-07-07
 */
public interface IMesTaskService {

    /**
     * 查询任务流转
     *
     * @param id 主键
     * @return 任务流转
     */
    MesTaskVo queryById(Long id);

    /**
     * 分页查询任务流转列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 任务流转分页列表
     */
    TableDataInfo<MesTaskVo> queryPageList(MesTaskBo bo, PageQuery pageQuery);
    TableDataInfo<MesTaskVo> queryPageListbtidd(Long uid,Integer typ,MesTaskBo bo, PageQuery pageQuery);
    /**
     * status=3 的list
     *
     * @param bo
     * @param pageQuery
     * @param worktype
     * @return
     */
    TableDataInfo<MesTaskVo> queryPageListstatus(MesTaskBo bo, PageQuery pageQuery,String sta,Integer worktype);

    /**
     * 查询符合条件的任务流转列表
     *
     * @param bo 查询条件
     * @return 任务流转列表
     */
    List<MesTaskVo> queryList(MesTaskBo bo);
    TableDataInfo<MesTaskVo> queryListbyqua(MesTaskBo bo,PageQuery pageQuer);
    /**
     * 新增任务流转
     *
     * @param bo 任务流转
     * @return 是否新增成功
     */
    Boolean insertByBo(MesTaskBo bo);

    Boolean insertbo(MesTaskBo bo);


    /**
     * 扫码报工
     * @param bo
     * @return
     */
    Boolean scandorep(MesTaskBo bo);
    /**
     * 修改任务流转
     *
     * @param bo 任务流转
     * @return 是否修改成功
     */
    Boolean updateByBo(MesTaskBo bo);


    Boolean stockupdate(MesTaskBo bo);

    Boolean updatetask(MesTaskBo bo);


    Boolean stockupstatus(MesTaskBo bo);
    /**
     * 校验并批量删除任务流转信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
