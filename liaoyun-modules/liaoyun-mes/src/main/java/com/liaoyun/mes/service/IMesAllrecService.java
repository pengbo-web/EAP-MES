package com.liaoyun.mes.service;

import com.liaoyun.mes.domain.vo.MesAllrecVo;
import com.liaoyun.mes.domain.bo.MesAllrecBo;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 出入库记录Service接口
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
public interface IMesAllrecService {

    /**
     * 查询出入库记录
     *
     * @param id 主键
     * @return 出入库记录
     */
    MesAllrecVo queryById(Long id);

    /**
     * 分页查询出入库记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 出入库记录分页列表
     */
    TableDataInfo<MesAllrecVo> queryPageList(MesAllrecBo bo, PageQuery pageQuery);
    List<Map<String,String>> queryPageLists();
    /**
     * 查询符合条件的出入库记录列表
     *
     * @param bo 查询条件
     * @return 出入库记录列表
     */
    List<MesAllrecVo> queryList(MesAllrecBo bo);

    /**
     * 新增出入库记录
     *
     * @param bo 出入库记录
     * @return 是否新增成功
     */
    Boolean insertByBo(MesAllrecBo bo);

    /**
     * 修改出入库记录
     *
     * @param bo 出入库记录
     * @return 是否修改成功
     */
    Boolean updateByBo(MesAllrecBo bo);

    /**
     * 校验并批量删除出入库记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
