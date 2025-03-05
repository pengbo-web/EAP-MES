package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesAllrec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOrderDeBo;
import com.liaoyun.mes.domain.vo.MesOrderDeVo;
import com.liaoyun.mes.domain.MesOrderDe;
import com.liaoyun.mes.mapper.MesOrderDeMapper;
import com.liaoyun.mes.service.IMesOrderDeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 订单详情Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-17
 */
@RequiredArgsConstructor
@Service
public class MesOrderDeServiceImpl implements IMesOrderDeService {

    private final MesOrderDeMapper baseMapper;

    /**
     * 查询订单详情
     *
     * @param id 主键
     * @return 订单详情
     */
    @Override
    public MesOrderDeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询订单详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 订单详情分页列表
     */
    @Override
    public TableDataInfo<MesOrderDeVo> queryPageList(MesOrderDeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOrderDe> lqw = buildQueryWrapper(bo);
        Page<MesOrderDeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的订单详情列表
     *
     * @param bo 查询条件
     * @return 订单详情列表
     */
    @Override
    public List<MesOrderDeVo> queryList(MesOrderDeBo bo) {
        LambdaQueryWrapper<MesOrderDe> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOrderDe> buildQueryWrapper(MesOrderDeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOrderDe> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStockid() != null, MesOrderDe::getStockid, bo.getStockid());
        lqw.eq(bo.getUnid() != null, MesOrderDe::getUnid, bo.getUnid());
        lqw.eq(bo.getPrices() != null, MesOrderDe::getPrices, bo.getPrices());
        lqw.eq(bo.getCounts() != null, MesOrderDe::getCounts, bo.getCounts());
        lqw.eq(bo.getAllprice() != null, MesOrderDe::getAllprice, bo.getAllprice());
        lqw.eq(bo.getCtime() != null, MesOrderDe::getCtime, bo.getCtime());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesOrderDe::getStatus, bo.getStatus());

        return lqw;
    }

    /**
     * 新增订单详情
     *
     * @param bo 订单详情
     * @return 是否新增成功
     */
    @Override
    public Long insertByBo(MesOrderDeBo bo) {
        MesOrderDe add = MapstructUtils.convert(bo, MesOrderDe.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return bo.getId();
    }

    /**
     * 修改订单详情
     *
     * @param bo 订单详情
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesOrderDeBo bo) {
        MesOrderDe update = MapstructUtils.convert(bo, MesOrderDe.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOrderDe entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除订单详情信息
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
