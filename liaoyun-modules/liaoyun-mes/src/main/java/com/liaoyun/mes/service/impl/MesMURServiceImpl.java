package com.liaoyun.mes.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesMURBo;
import com.liaoyun.mes.domain.vo.MesMURVo;
import com.liaoyun.mes.domain.MesMUR;
import com.liaoyun.mes.mapper.MesMURMapper;
import com.liaoyun.mes.service.IMesMURService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 移动套餐Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-08-06
 */
@RequiredArgsConstructor
@Service
public class MesMURServiceImpl implements IMesMURService {

    private final MesMURMapper baseMapper;

    /**
     * 查询移动套餐
     *
     * @param id 主键
     * @return 移动套餐
     */
    @Override
    public MesMURVo queryById(Long id){
        MesMURVo vo = baseMapper.selectVoById(id);
        if (ObjectUtils.isNotNull(vo.getIds())){
            String[] equlist= baseMapper.selectTitleById(vo.getIds()).toArray(new String[0]);
            vo.setIdsarray(vo.getIds().split(","));
            vo.setIdsname(String.join(StringUtils.SEPARATOR, equlist));
        }
        return vo;
    }

    /**
     * 分页查询移动套餐列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 移动套餐分页列表
     */
    @Override
    public TableDataInfo<MesMURVo> queryPageList(MesMURBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesMUR> lqw = buildQueryWrapper(bo);
        Page<MesMURVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesMURVo vo : result.getRecords()) {
            if (ObjectUtils.isNotNull(vo.getIds())){
                String[] equlist= baseMapper.selectTitleById(vo.getIds()).toArray(new String[0]);
                vo.setIdsarray(vo.getIds().split(","));
                vo.setIdsname(String.join(StringUtils.SEPARATOR, equlist));

            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的移动套餐列表
     *
     * @param bo 查询条件
     * @return 移动套餐列表
     */
    @Override
    public List<MesMURVo> queryList(MesMURBo bo) {
        LambdaQueryWrapper<MesMUR> lqw = buildQueryWrapper(bo);
        List<MesMURVo> list = baseMapper.selectVoList(lqw);
        for (MesMURVo vo : list) {
                if (ObjectUtils.isNotNull(vo.getIds())){
                    String[] equlist= baseMapper.selectTitleById(vo.getIds()).toArray(new String[0]);
                    vo.setIdsarray(vo.getIds().split(","));
                    vo.setIdsname(String.join(StringUtils.SEPARATOR, equlist));
                }

        }
        return list;
    }

    private LambdaQueryWrapper<MesMUR> buildQueryWrapper(MesMURBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesMUR> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesMUR::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getIds()), MesMUR::getIds, bo.getIds());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesMUR::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增移动套餐
     *
     * @param bo 移动套餐
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesMURBo bo) {
        MesMUR add = MapstructUtils.convert(bo, MesMUR.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改移动套餐
     *
     * @param bo 移动套餐
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesMURBo bo) {
        MesMUR update = MapstructUtils.convert(bo, MesMUR.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesMUR entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除移动套餐信息
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
