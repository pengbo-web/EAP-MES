package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesQualityList;
import com.liaoyun.mes.domain.vo.MesProcessVo;
import com.liaoyun.mes.domain.vo.MesQualityListVo;
import com.liaoyun.mes.domain.vo.MesTaskVo;
import com.liaoyun.mes.mapper.MesProcessMapper;
import com.liaoyun.mes.mapper.MesQualityListMapper;
import com.liaoyun.mes.mapper.MesTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesQualityMainBo;
import com.liaoyun.mes.domain.vo.MesQualityMainVo;
import com.liaoyun.mes.domain.MesQualityMain;
import com.liaoyun.mes.mapper.MesQualityMainMapper;
import com.liaoyun.mes.service.IMesQualityMainService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 质检主表Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-13
 */
@RequiredArgsConstructor
@Service
public class MesQualityMainServiceImpl implements IMesQualityMainService {

    private final MesQualityMainMapper baseMapper;
    private final MesQualityListMapper listMapper;
    private final MesProcessMapper processMapper;
    private final MesTaskMapper mesTaskMapper;

    /**
     * 查询质检主表
     *
     * @param id 主键
     * @return 质检主表
     */
    @Override
    public MesQualityMainVo queryById(Long id){
        MesQualityMainVo vo = new MesQualityMainVo();
        LambdaQueryWrapper<MesQualityList> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(MesQualityList::getQualitymainId,vo.getId());
        List<MesQualityListVo> listVos=listMapper.selectVoList(wrapper);
        vo=baseMapper.selectVoById(id);
        vo.setQualityListVoList(listVos);
        return vo;
    }

    /**
     * 分页查询质检主表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 质检主表分页列表
     */
    @Override
    public TableDataInfo<MesQualityMainVo> queryPageList(MesQualityMainBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesQualityMain> lqw = buildQueryWrapper(bo);
        Page<MesQualityMainVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesQualityMainVo mainVo:result.getRecords()){
            LambdaQueryWrapper<MesQualityList> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesQualityList::getQualitymainId,mainVo.getId());
            List<MesQualityListVo> listVos=listMapper.selectVoList(wrapper);
            mainVo.setQualityListVoList(listVos);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的质检主表列表
     *
     * @param bo 查询条件
     * @return 质检主表列表
     */
    @Override
    public List<MesQualityMainVo> queryList(MesQualityMainBo bo) {
        LambdaQueryWrapper<MesQualityMain> lqw = buildQueryWrapper(bo);
        List<MesQualityMainVo> list=baseMapper.selectVoList(lqw);
        for (MesQualityMainVo mainVo:list){
            LambdaQueryWrapper<MesQualityList> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesQualityList::getQualitymainId,mainVo.getId());
            List<MesQualityListVo> listVos=listMapper.selectVoList(wrapper);
            mainVo.setQualityListVoList(listVos);
        }
        return list;
    }

    @Override
    public List<MesQualityListVo> getqualityList(Long bo){
        MesQualityMainVo mainVo=baseMapper.selectVoById(bo);
        LambdaQueryWrapper<MesQualityList> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(MesQualityList::getQualitymainId,mainVo.getId());
        List<MesQualityListVo> listVos=listMapper.selectVoList(wrapper);
       return listVos;
    }
    private LambdaQueryWrapper<MesQualityMain> buildQueryWrapper(MesQualityMainBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesQualityMain> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesQualityMain::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getOther()), MesQualityMain::getOther, bo.getOther());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesQualityMain::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增质检主表
     *
     * @param bo 质检主表
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesQualityMainBo bo) {
        MesQualityMain add = MapstructUtils.convert(bo, MesQualityMain.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改质检主表
     *
     * @param bo 质检主表
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesQualityMainBo bo) {
        MesQualityMain update = MapstructUtils.convert(bo, MesQualityMain.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesQualityMain entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除质检主表信息
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
