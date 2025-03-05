package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesCustomBo;
import com.liaoyun.mes.domain.vo.MesCustomVo;
import com.liaoyun.mes.domain.MesCustom;
import com.liaoyun.mes.mapper.MesCustomMapper;
import com.liaoyun.mes.service.IMesCustomService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 客户供应商Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@RequiredArgsConstructor
@Service
public class MesCustomServiceImpl implements IMesCustomService {

    private final MesCustomMapper baseMapper;

    /**
     * 查询客户供应商
     *
     * @param id 主键
     * @return 客户供应商
     */
    @Override
    public MesCustomVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询客户供应商列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客户供应商分页列表
     */
    @Override
    public TableDataInfo<MesCustomVo> queryPageList(MesCustomBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesCustom> lqw = buildQueryWrapper(bo);
        Page<MesCustomVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的客户供应商列表
     *
     * @param bo 查询条件
     * @return 客户供应商列表
     */
    @Override
    public List<MesCustomVo> queryList(MesCustomBo bo) {
        LambdaQueryWrapper<MesCustom> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesCustom> buildQueryWrapper(MesCustomBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesCustom> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getType()), MesCustom::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getDescp()), MesCustom::getDescp, bo.getDescp());
        lqw.like(StringUtils.isNotBlank(bo.getCardnum()), MesCustom::getCardnum, bo.getCardnum());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesCustom::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), MesCustom::getPhone, bo.getPhone());
        lqw.like(StringUtils.isNotBlank(bo.getShnum()), MesCustom::getShnum, bo.getShnum());
        lqw.like(StringUtils.isNotBlank(bo.getAddress()), MesCustom::getAddress, bo.getAddress());

        lqw.like(StringUtils.isNotBlank(bo.getAddress1()), MesCustom::getAddress1, bo.getAddress1());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesCustom::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增客户供应商
     *
     * @param bo 客户供应商
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesCustomBo bo) {
        MesCustom add = MapstructUtils.convert(bo, MesCustom.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }
    @Override
    public MesCustom insertByBo2(MesCustomBo bo) {
        MesCustom add = MapstructUtils.convert(bo, MesCustom.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return add;
    }

    /**
     * 修改客户供应商
     *
     * @param bo 客户供应商
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesCustomBo bo) {
        MesCustom update = MapstructUtils.convert(bo, MesCustom.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesCustom entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除客户供应商信息
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
