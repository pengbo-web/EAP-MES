package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOustBo;
import com.liaoyun.mes.domain.vo.MesOustVo;
import com.liaoyun.mes.domain.MesOust;
import com.liaoyun.mes.mapper.MesOustMapper;
import com.liaoyun.mes.service.IMesOustService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 其他出库Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
@RequiredArgsConstructor
@Service
public class MesOustServiceImpl implements IMesOustService {

    private final MesOustMapper baseMapper;

    /**
     * 查询其他出库
     *
     * @param id 主键
     * @return 其他出库
     */
    @Override
    public MesOustVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询其他出库列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 其他出库分页列表
     */
    @Override
    public TableDataInfo<MesOustVo> queryPageList(MesOustBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOust> lqw = buildQueryWrapper(bo);
        Page<MesOustVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的其他出库列表
     *
     * @param bo 查询条件
     * @return 其他出库列表
     */
    @Override
    public List<MesOustVo> queryList(MesOustBo bo) {
        LambdaQueryWrapper<MesOust> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOust> buildQueryWrapper(MesOustBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOust> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getRecids()), MesOust::getRecids, bo.getRecids());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesOust::getSn, bo.getSn());
        lqw.eq(bo.getStocktype() != null, MesOust::getStocktype, bo.getStocktype());
        lqw.eq(bo.getAllprice() != null, MesOust::getAllprice, bo.getAllprice());
        lqw.eq(StringUtils.isNotBlank(bo.getMET()), MesOust::getMet, bo.getMET());
        lqw.eq(StringUtils.isNotBlank(bo.getSHUI()), MesOust::getMet, bo.getSHUI());
        lqw.eq(StringUtils.isNotBlank(bo.getFiles()), MesOust::getFiles, bo.getFiles());
        lqw.eq(bo.getCtime() != null, MesOust::getCtime, bo.getCtime());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesOust::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增其他出库
     *
     * @param bo 其他出库
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesOustBo bo) {
        MesOust add = MapstructUtils.convert(bo, MesOust.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改其他出库
     *
     * @param bo 其他出库
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesOustBo bo) {
        MesOust update = MapstructUtils.convert(bo, MesOust.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOust entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除其他出库信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
