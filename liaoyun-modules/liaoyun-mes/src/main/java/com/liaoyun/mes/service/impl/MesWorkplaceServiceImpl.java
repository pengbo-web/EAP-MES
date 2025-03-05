package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesWorkplaceBo;
import com.liaoyun.mes.domain.vo.MesWorkplaceVo;
import com.liaoyun.mes.domain.MesWorkplace;
import com.liaoyun.mes.mapper.MesWorkplaceMapper;
import com.liaoyun.mes.service.IMesWorkplaceService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * ④车间设置Service业务层处理
 *
 * @author Kylin W
 * @date 2024-05-14
 */
@RequiredArgsConstructor
@Service
public class MesWorkplaceServiceImpl implements IMesWorkplaceService {

    private final MesWorkplaceMapper baseMapper;

    private final SysUserMapper sysUserMapper;

    /**
     * 查询④车间设置
     */
    @Override
    public MesWorkplaceVo queryById(Long id){

        return baseMapper.selectVoById(id);
    }

    /**
     * 查询④车间设置列表
     */
    @Override
    public TableDataInfo<MesWorkplaceVo> queryPageList(MesWorkplaceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesWorkplace> lqw = buildQueryWrapper(bo);
        Page<MesWorkplaceVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesWorkplaceVo vo : result.getRecords()) {
            try{
                vo.setUsename(sysUserMapper.selectVoById(vo.getCurator()).getNickName());
            }catch (Exception e){

            }


        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询④车间设置列表
     */
    @Override
    public List<MesWorkplaceVo> queryList(MesWorkplaceBo bo) {
        LambdaQueryWrapper<MesWorkplace> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesWorkplace> buildQueryWrapper(MesWorkplaceBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesWorkplace> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesWorkplace::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesWorkplace::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getArea()), MesWorkplace::getArea, bo.getArea());
        lqw.eq(bo.getCurator() != null, MesWorkplace::getCurator, bo.getCurator());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesWorkplace::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增④车间设置
     */
    @Override
    public Boolean insertByBo(MesWorkplaceBo bo) {
        MesWorkplace add = MapstructUtils.convert(bo, MesWorkplace.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改④车间设置
     */
    @Override
    public Boolean updateByBo(MesWorkplaceBo bo) {
        MesWorkplace update = MapstructUtils.convert(bo, MesWorkplace.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesWorkplace entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除④车间设置
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
