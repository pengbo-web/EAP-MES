package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesProtypeBo;
import com.liaoyun.mes.domain.vo.MesProtypeVo;
import com.liaoyun.mes.domain.MesProtype;
import com.liaoyun.mes.mapper.MesProtypeMapper;
import com.liaoyun.mes.service.IMesProtypeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * ②产品类别Service业务层处理
 *
 * @author Kylin W
 * @date 2024-05-11
 */
@RequiredArgsConstructor
@Service
public class MesProtypeServiceImpl implements IMesProtypeService {

    private final MesProtypeMapper baseMapper;

    /**
     * 查询②产品类别
     */
    @Override
    public MesProtypeVo queryById(Long id){

        return baseMapper.selectVoById(id);
    }


    /**
     * 查询②产品类别列表
     */
    @Override
    public List<MesProtypeVo> queryList(MesProtypeBo bo) {
        LambdaQueryWrapper<MesProtype> lqw = buildQueryWrapper(bo);

        List<MesProtypeVo> list=baseMapper.selectVoList(lqw);
        for (MesProtypeVo vo:list){
            vo.setUpname("顶级");
            if(0!=vo.getPid()){
                MesProtypeVo up= baseMapper.selectVoById(vo.getPid());
                vo.setUpname(up.getTitle());
            }
        }
        return list;
    }

    private LambdaQueryWrapper<MesProtype> buildQueryWrapper(MesProtypeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesProtype> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesProtype::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesProtype::getTitle, bo.getTitle());
        lqw.eq(bo.getPid() != null, MesProtype::getPid, bo.getPid());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesProtype::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增②产品类别
     */
    @Override
    public Boolean insertByBo(MesProtypeBo bo) {
        MesProtype add = MapstructUtils.convert(bo, MesProtype.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改②产品类别
     */
    @Override
    public Boolean updateByBo(MesProtypeBo bo) {
        MesProtype update = MapstructUtils.convert(bo, MesProtype.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesProtype entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除②产品类别
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
