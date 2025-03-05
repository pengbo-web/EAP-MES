package com.liaoyun.mes.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesEqucheckList;
import com.liaoyun.mes.domain.vo.MesEqucheckListVo;
import com.liaoyun.mes.mapper.MesEqucheckListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesEquCheckBo;
import com.liaoyun.mes.domain.vo.MesEquCheckVo;
import com.liaoyun.mes.domain.MesEquCheck;
import com.liaoyun.mes.mapper.MesEquCheckMapper;
import com.liaoyun.mes.service.IMesEquCheckService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设备点检Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-27
 */
@RequiredArgsConstructor
@Service
public class MesEquCheckServiceImpl implements IMesEquCheckService {

    private final MesEquCheckMapper baseMapper;
    private final MesEqucheckListMapper listMapper;

    /**
     * 查询设备点检
     *
     * @param id 主键
     * @return 设备点检
     */
    @Override
    public MesEquCheckVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询设备点检列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 设备点检分页列表
     */
    @Override
    public TableDataInfo<MesEquCheckVo> queryPageList(MesEquCheckBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesEquCheck> lqw = buildQueryWrapper(bo);
        Page<MesEquCheckVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesEquCheckVo vo : result.getRecords()) {
            if (ObjectUtils.isNotNull(vo.getEquIds())){
                String[] equlist= baseMapper.selectEquTitleById(vo.getEquIds()).toArray(new String[0]);
                vo.setEquIdsarray(equlist);
                vo.setEquIdnames(String.join(StringUtils.SEPARATOR, equlist));
            }
            LambdaQueryWrapper<MesEqucheckList> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesEqucheckList::getEquMain,vo.getId());
            vo.setListvo(listMapper.selectVoList(wrapper));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的设备点检列表
     *
     * @param bo 查询条件
     * @return 设备点检列表
     */
    @Override
    public List<MesEquCheckVo> queryList(MesEquCheckBo bo) {
        LambdaQueryWrapper<MesEquCheck> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesEquCheck> buildQueryWrapper(MesEquCheckBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesEquCheck> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTypes()), MesEquCheck::getTypes, bo.getTypes());
        lqw.eq(StringUtils.isNotBlank(bo.getEquIds()), MesEquCheck::getEquIds, bo.getEquIds());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesEquCheck::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesEquCheck::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增设备点检
     *
     * @param bo 设备点检
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesEquCheckBo bo) {
        if(bo.getEquIdsarray().length>0) {
            bo.setEquIds(String.join(",", bo.getEquIdsarray()));
        }
        MesEquCheck add = MapstructUtils.convert(bo, MesEquCheck.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备点检
     *
     * @param bo 设备点检
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesEquCheckBo bo) {
        MesEquCheck update = MapstructUtils.convert(bo, MesEquCheck.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesEquCheck entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除设备点检信息
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
