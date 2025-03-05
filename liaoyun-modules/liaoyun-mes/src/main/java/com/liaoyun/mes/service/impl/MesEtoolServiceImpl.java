package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesToolRec;
import com.liaoyun.mes.domain.MesToolUse;
import com.liaoyun.mes.domain.vo.MesToolRecVo;
import com.liaoyun.mes.domain.vo.MesToolUseVo;
import com.liaoyun.mes.mapper.MesToolRecMapper;
import com.liaoyun.mes.mapper.MesToolUseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesEtoolBo;
import com.liaoyun.mes.domain.vo.MesEtoolVo;
import com.liaoyun.mes.domain.MesEtool;
import com.liaoyun.mes.mapper.MesEtoolMapper;
import com.liaoyun.mes.service.IMesEtoolService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 工装夹具Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@RequiredArgsConstructor
@Service
public class MesEtoolServiceImpl implements IMesEtoolService {

    private final MesEtoolMapper baseMapper;

    private final MesEtoolMapper etoolMapper;

    private final MesToolRecMapper toolRecMapper;

    private final MesToolUseMapper toolUseMapper;

    /**
     * 查询工装夹具
     *
     * @param id 主键
     * @return 工装夹具
     */
    @Override
    public MesEtoolVo queryById(Long id){
        MesEtoolVo vo=etoolMapper.selectVoById(id);
        LambdaQueryWrapper<MesToolRec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesToolRec::getToolid, id);
        List<MesToolRecVo> list=toolRecMapper.selectVoList(wrapper);
        LambdaQueryWrapper<MesToolUse> wrapperUse = new LambdaQueryWrapper<>();
        wrapperUse.eq(MesToolUse::getToolid, id);
        List<MesToolUseVo> listUse=toolUseMapper.selectVoList(wrapperUse);
        for (MesToolUseVo use:listUse){
            use.setUsername(toolUseMapper.getusername(use.getUserId()));
        }
        for (MesToolRecVo recVo:list){
            recVo.setUsername(toolRecMapper.getusername(recVo.getUserId()));
        }
        try{
            vo.setUseList(listUse);
            vo.setRecVoList(list);
        }catch (Exception e){

        }

        return vo;
    }

    /**
     * 分页查询工装夹具列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 工装夹具分页列表
     */
    @Override
    public TableDataInfo<MesEtoolVo> queryPageList(MesEtoolBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesEtool> lqw = buildQueryWrapper(bo);
        Page<MesEtoolVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的工装夹具列表
     *
     * @param bo 查询条件
     * @return 工装夹具列表
     */
    @Override
    public List<MesEtoolVo> queryList(MesEtoolBo bo) {
        LambdaQueryWrapper<MesEtool> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesEtool> buildQueryWrapper(MesEtoolBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesEtool> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesEtool::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesEtool::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getBrand()), MesEtool::getBrand, bo.getBrand());
        lqw.eq(StringUtils.isNotBlank(bo.getOtype()), MesEtool::getOtype, bo.getOtype());
        lqw.eq(bo.getStocknum() != null, MesEtool::getStocknum, bo.getStocknum());
        lqw.eq(bo.getStockrec() != null, MesEtool::getStockrec, bo.getStockrec());
        lqw.eq(bo.getOtstock() != null, MesEtool::getOtstock, bo.getOtstock());
        lqw.eq(bo.getUsestock() != null, MesEtool::getUsestock, bo.getUsestock());
        lqw.eq(bo.getOtime() != null, MesEtool::getOtime, bo.getOtime());
        lqw.eq(bo.getNexttime() != null, MesEtool::getNexttime, bo.getNexttime());
        lqw.eq(bo.getInstock() != null, MesEtool::getInstock, bo.getInstock());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesEtool::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增工装夹具
     *
     * @param bo 工装夹具
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesEtoolBo bo) {
        MesEtool add = MapstructUtils.convert(bo, MesEtool.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改工装夹具
     *
     * @param bo 工装夹具
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesEtoolBo bo) {
        MesEtool update = MapstructUtils.convert(bo, MesEtool.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesEtool entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除工装夹具信息
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
