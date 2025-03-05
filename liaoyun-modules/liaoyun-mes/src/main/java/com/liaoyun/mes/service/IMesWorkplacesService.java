package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.MesCuttingUse;
import com.liaoyun.mes.domain.MesToolUse;
import com.liaoyun.mes.domain.vo.MesEquipmentVo;
import com.liaoyun.mes.domain.vo.MesEtoolVo;
import com.liaoyun.mes.domain.vo.MesWorkplacesVo;
import com.liaoyun.mes.domain.bo.MesWorkplacesBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 工作站Service接口
 *
 * @author Liao Liao
 * @date 2024-06-05
 */
public interface IMesWorkplacesService {

    /**
     * 查询工作站
     *
     * @param id 主键
     * @return 工作站
     */
    MesWorkplacesVo queryById(Long id);

    /**
     * 分页查询工作站列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 工作站分页列表
     */
    TableDataInfo<MesWorkplacesVo> queryPageList(MesWorkplacesBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的工作站列表
     *
     * @param bo 查询条件
     * @return 工作站列表
     */
    List<MesWorkplacesVo> queryList(MesWorkplacesBo bo);

    /**
     * 新增工作站
     *
     * @param bo 工作站
     * @return 是否新增成功
     */
    Boolean insertByBo(MesWorkplacesBo bo);

    /**
     * 修改工作站
     *
     * @param bo 工作站
     * @return 是否修改成功
     */
    Boolean updateByBo(MesWorkplacesBo bo);

    /**
     * 校验并批量删除工作站信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    List<MesEquipmentVo> quertallgetlist();
    List<MesToolUse> gettonglist();
    List<MesCuttingUse> getcuttinglist();
}
