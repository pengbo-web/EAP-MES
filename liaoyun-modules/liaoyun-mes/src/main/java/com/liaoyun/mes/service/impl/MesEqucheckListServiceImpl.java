package com.liaoyun.mes.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.oss.core.OssClient;
import com.liaoyun.system.domain.SysOss;
import com.liaoyun.system.mapper.SysOssMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesEqucheckListBo;
import com.liaoyun.mes.domain.vo.MesEqucheckListVo;
import com.liaoyun.mes.domain.MesEqucheckList;
import com.liaoyun.mes.mapper.MesEqucheckListMapper;
import com.liaoyun.mes.service.IMesEqucheckListService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设备点检列Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@RequiredArgsConstructor
@Service
public class MesEqucheckListServiceImpl implements IMesEqucheckListService {

    private final MesEqucheckListMapper baseMapper;

    private final SysOssMapper ossMapper;

    /**
     * 查询设备点检列
     *
     * @param id 主键
     * @return 设备点检列
     */
    @Override
    public MesEqucheckListVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询设备点检列列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 设备点检列分页列表
     */
    @Override
    public TableDataInfo<MesEqucheckListVo> queryPageList(MesEqucheckListBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesEqucheckList> lqw = buildQueryWrapper(bo);
        Page<MesEqucheckListVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的设备点检列列表
     *
     * @param bo 查询条件
     * @return 设备点检列列表
     */
    @Override
    public List<MesEqucheckListVo> queryList(MesEqucheckListBo bo) {
        LambdaQueryWrapper<MesEqucheckList> lqw = buildQueryWrapper(bo);
        List<MesEqucheckListVo> list = baseMapper.selectVoList(lqw);
        for (MesEqucheckListVo vo : list) {
            if(ObjectUtils.isNotNull(vo.getImgs())){
                vo.setIiim(ossMapper.selectVoById(vo.getImgs()).getUrl());
            }
        }
        return list;
    }

    private LambdaQueryWrapper<MesEqucheckList> buildQueryWrapper(MesEqucheckListBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesEqucheckList> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEquMain() != null, MesEqucheckList::getEquMain, bo.getEquMain());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesEqucheckList::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStandards()), MesEqucheckList::getStandards, bo.getStandards());
        lqw.eq(StringUtils.isNotBlank(bo.getImgs()), MesEqucheckList::getImgs, bo.getImgs());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesEqucheckList::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增设备点检列
     *
     * @param bo 设备点检列
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesEqucheckListBo bo) {
        MesEqucheckList add = MapstructUtils.convert(bo, MesEqucheckList.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备点检列
     *
     * @param bo 设备点检列
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesEqucheckListBo bo) {
        MesEqucheckList update = MapstructUtils.convert(bo, MesEqucheckList.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesEqucheckList entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除设备点检列信息
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
