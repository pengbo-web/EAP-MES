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
import com.liaoyun.mes.domain.bo.MesModelrecBo;
import com.liaoyun.mes.domain.vo.MesModelrecVo;
import com.liaoyun.mes.domain.MesModelrec;
import com.liaoyun.mes.mapper.MesModelrecMapper;
import com.liaoyun.mes.service.IMesModelrecService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 模具使用记录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-09
 */
@RequiredArgsConstructor
@Service
public class MesModelrecServiceImpl implements IMesModelrecService {

    private final MesModelrecMapper baseMapper;

    /**
     * 查询模具使用记录
     *
     * @param id 主键
     * @return 模具使用记录
     */
    @Override
    public MesModelrecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询模具使用记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 模具使用记录分页列表
     */
    @Override
    public TableDataInfo<MesModelrecVo> queryPageList(MesModelrecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesModelrec> lqw = buildQueryWrapper(bo);
        Page<MesModelrecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的模具使用记录列表
     *
     * @param bo 查询条件
     * @return 模具使用记录列表
     */
    @Override
    public List<MesModelrecVo> queryList(MesModelrecBo bo) {
        LambdaQueryWrapper<MesModelrec> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesModelrec> buildQueryWrapper(MesModelrecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesModelrec> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getWname()), MesModelrec::getWname, bo.getWname());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), MesModelrec::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getCname()), MesModelrec::getCname, bo.getCname());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), MesModelrec::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), MesModelrec::getAddress, bo.getAddress());
        lqw.eq(bo.getEndtime() != null, MesModelrec::getEndtime, bo.getEndtime());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark1()), MesModelrec::getRemark1, bo.getRemark1());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesModelrec::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增模具使用记录
     *
     * @param bo 模具使用记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesModelrecBo bo) {
        MesModelrec add = MapstructUtils.convert(bo, MesModelrec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改模具使用记录
     *
     * @param bo 模具使用记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesModelrecBo bo) {
        MesModelrec update = MapstructUtils.convert(bo, MesModelrec.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesModelrec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除模具使用记录信息
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
