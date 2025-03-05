package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesCuttingUse;
import com.liaoyun.mes.mapper.MesEtoolMapper;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesToolUseBo;
import com.liaoyun.mes.domain.vo.MesToolUseVo;
import com.liaoyun.mes.domain.MesToolUse;
import com.liaoyun.mes.mapper.MesToolUseMapper;
import com.liaoyun.mes.service.IMesToolUseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 夹具使用Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-10-11
 */
@RequiredArgsConstructor
@Service
public class MesToolUseServiceImpl implements IMesToolUseService {

    private final MesToolUseMapper baseMapper;
    private final MesEtoolMapper etoolMapper;
    private final SysUserMapper sysUserMapper;
    /**
     * 查询夹具使用
     *
     * @param id 主键
     * @return 夹具使用
     */
    @Override
    public MesToolUseVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询夹具使用列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 夹具使用分页列表
     */
    @Override
    public TableDataInfo<MesToolUseVo> queryPageList(MesToolUseBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesToolUse> lqw = buildQueryWrapper(bo);
        if(StringUtils.isNotBlank(bo.getUsername())){
            LambdaQueryWrapper<SysUser> wrapper3=new LambdaQueryWrapper<>();
            wrapper3.like(SysUser::getNickName,bo.getUsername());
            List<SysUser> list3=sysUserMapper.selectList(wrapper3);
            List<Long> ids=new ArrayList<Long>();
            if(list3.size()>0){
                for(SysUser mesProduct:list3){
                    ids.add(mesProduct.getUserId());
                }
                lqw.in(MesToolUse::getUserId,ids);
            }
        }
        Page<MesToolUseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesToolUseVo vo : result.getRecords()) {
            vo.setMesEtoolVo(etoolMapper.selectVoById(vo.getToolid()));
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的夹具使用列表
     *
     * @param bo 查询条件
     * @return 夹具使用列表
     */
    @Override
    public List<MesToolUseVo> queryList(MesToolUseBo bo) {
        LambdaQueryWrapper<MesToolUse> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesToolUse> buildQueryWrapper(MesToolUseBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesToolUse> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getToolid() != null, MesToolUse::getToolid, bo.getToolid());
        lqw.eq(bo.getCount() != null, MesToolUse::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getImgs()), MesToolUse::getImgs, bo.getImgs());
        lqw.eq(StringUtils.isNotBlank(bo.getRebak()), MesToolUse::getRebak, bo.getRebak());
        lqw.eq(bo.getUserId() != null, MesToolUse::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getWorkId()), MesToolUse::getWorkId, bo.getWorkId());
        return lqw;
    }

    /**
     * 新增夹具使用
     *
     * @param bo 夹具使用
     * @return 是否新增成功
     */
    @Override
    public Long insertByBo(MesToolUseBo bo) {
        MesToolUse add = MapstructUtils.convert(bo, MesToolUse.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return bo.getId();
    }

    /**
     * 修改夹具使用
     *
     * @param bo 夹具使用
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesToolUseBo bo) {
        MesToolUse update = MapstructUtils.convert(bo, MesToolUse.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesToolUse entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除夹具使用信息
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
