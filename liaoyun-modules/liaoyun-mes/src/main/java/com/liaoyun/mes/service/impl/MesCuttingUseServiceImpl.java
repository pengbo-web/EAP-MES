package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesCuttingRec;
import com.liaoyun.mes.domain.MesYcrec;
import com.liaoyun.mes.domain.vo.MesCuttingRecVo;
import com.liaoyun.mes.mapper.MesCuttingMapper;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesCuttingUseBo;
import com.liaoyun.mes.domain.vo.MesCuttingUseVo;
import com.liaoyun.mes.domain.MesCuttingUse;
import com.liaoyun.mes.mapper.MesCuttingUseMapper;
import com.liaoyun.mes.service.IMesCuttingUseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 刀具使用统计Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
@RequiredArgsConstructor
@Service
public class MesCuttingUseServiceImpl implements IMesCuttingUseService {

    private final MesCuttingUseMapper baseMapper;
    private final MesCuttingMapper cuttingMapper;
    private final SysUserMapper sysUserMapper;

    /**
     * 查询刀具使用统计
     *
     * @param id 主键
     * @return 刀具使用统计
     */
    @Override
    public MesCuttingUseVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询刀具使用统计列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 刀具使用统计分页列表
     */
    @Override
    public TableDataInfo<MesCuttingUseVo> queryPageList(MesCuttingUseBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesCuttingUse> lqw = buildQueryWrapper(bo);
        if(StringUtils.isNotBlank(bo.getUsername())){
            LambdaQueryWrapper<SysUser> wrapper3=new LambdaQueryWrapper<>();
            wrapper3.like(SysUser::getNickName,bo.getUsername());
            List<SysUser> list3=sysUserMapper.selectList(wrapper3);
            List<Long> ids=new ArrayList<Long>();
            if(list3.size()>0){
                for(SysUser mesProduct:list3){
                    ids.add(mesProduct.getUserId());
                }
                lqw.in(MesCuttingUse::getUserId,ids);
            }
        }
        Page<MesCuttingUseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesCuttingUseVo vo : result.getRecords()) {
            vo.setCuttingname(baseMapper.selectmainTitleById(vo.getToolid()));
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的刀具使用统计列表
     *
     * @param bo 查询条件
     * @return 刀具使用统计列表
     */
    @Override
    public List<MesCuttingUseVo> queryList(MesCuttingUseBo bo) {
        LambdaQueryWrapper<MesCuttingUse> lqw = buildQueryWrapper(bo);
        List<MesCuttingUseVo> list= baseMapper.selectVoList(lqw);
        for (MesCuttingUseVo vo : list) {
            vo.setCuttingname(baseMapper.selectmainTitleById(vo.getToolid()));
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
            vo.setCuttingVo(cuttingMapper.selectVoById(vo.getToolid()));
        }
        return list;
    }

    private LambdaQueryWrapper<MesCuttingUse> buildQueryWrapper(MesCuttingUseBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesCuttingUse> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getToolid() != null, MesCuttingUse::getToolid, bo.getToolid());
        lqw.eq(bo.getCount() != null, MesCuttingUse::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getImgs()), MesCuttingUse::getImgs, bo.getImgs());
        lqw.eq(StringUtils.isNotBlank(bo.getRebak()), MesCuttingUse::getRebak, bo.getRebak());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), MesCuttingUse::getType, bo.getType());
        lqw.eq(bo.getUserId() != null, MesCuttingUse::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesCuttingUse::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getWorkId()), MesCuttingUse::getWorkId, bo.getWorkId());
        return lqw;
    }

    /**
     * 新增刀具使用统计
     *
     * @param bo 刀具使用统计
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesCuttingUseBo bo) {
        MesCuttingUse add = MapstructUtils.convert(bo, MesCuttingUse.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改刀具使用统计
     *
     * @param bo 刀具使用统计
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesCuttingUseBo bo) {
        MesCuttingUse update = MapstructUtils.convert(bo, MesCuttingUse.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesCuttingUse entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除刀具使用统计信息
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
