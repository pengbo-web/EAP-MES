package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.mapper.MesProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesProsettingzpBo;
import com.liaoyun.mes.domain.vo.MesProsettingzpVo;
import com.liaoyun.mes.domain.MesProsettingzp;
import com.liaoyun.mes.mapper.MesProsettingzpMapper;
import com.liaoyun.mes.service.IMesProsettingzpService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 组装排程Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@RequiredArgsConstructor
@Service
public class MesProsettingzpServiceImpl implements IMesProsettingzpService {

    private final MesProsettingzpMapper baseMapper;
    private final MesProductMapper productMapper;

    /**
     * 查询组装排程
     *
     * @param id 主键
     * @return 组装排程
     */
    @Override
    public MesProsettingzpVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询组装排程列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 组装排程分页列表
     */
    @Override
    public TableDataInfo<MesProsettingzpVo> queryPageList(MesProsettingzpBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesProsettingzp> lqw = buildQueryWrapper(bo);
        Page<MesProsettingzpVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesProsettingzpVo vo : result.getRecords()) {
            vo.setWorkshopname(baseMapper.selectTypeTitleById(Long.valueOf(vo.getWorkshopId())));
            try{
                vo.setProname(productMapper.selectVoById(vo.getProId()).getTitle());
            }catch (Exception e){

            }


        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的组装排程列表
     *
     * @param bo 查询条件
     * @return 组装排程列表
     */
    @Override
    public List<MesProsettingzpVo> queryList(MesProsettingzpBo bo) {
        LambdaQueryWrapper<MesProsettingzp> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesProsettingzp> buildQueryWrapper(MesProsettingzpBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesProsettingzp> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getWorkshopId()), MesProsettingzp::getWorkshopId, bo.getWorkshopId());
        lqw.eq(bo.getProId() != null, MesProsettingzp::getProId, bo.getProId());
        lqw.eq(bo.getZpcount() != null, MesProsettingzp::getZpcount, bo.getZpcount());
        lqw.eq(bo.getSort() != null, MesProsettingzp::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesProsettingzp::getStatus, bo.getStatus());
        lqw.eq(bo.getMustbe() != null, MesProsettingzp::getMustbe, bo.getMustbe());
        lqw.eq(bo.getIstruec() != null, MesProsettingzp::getIstruec, bo.getIstruec());
        return lqw;
    }

    /**
     * 新增组装排程
     *
     * @param bo 组装排程
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesProsettingzpBo bo) {
        MesProsettingzp add = MapstructUtils.convert(bo, MesProsettingzp.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改组装排程
     *
     * @param bo 组装排程
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesProsettingzpBo bo) {
        MesProsettingzp update = MapstructUtils.convert(bo, MesProsettingzp.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesProsettingzp entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除组装排程信息
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
