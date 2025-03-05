package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesProcurementplan;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.mapper.MesProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesPriceRecBo;
import com.liaoyun.mes.domain.vo.MesPriceRecVo;
import com.liaoyun.mes.domain.MesPriceRec;
import com.liaoyun.mes.mapper.MesPriceRecMapper;
import com.liaoyun.mes.service.IMesPriceRecService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 价格波动Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@RequiredArgsConstructor
@Service
public class MesPriceRecServiceImpl implements IMesPriceRecService {

    private final MesPriceRecMapper baseMapper;

    private final MesProductMapper productMapper;

    /**
     * 查询价格波动
     *
     * @param id 主键
     * @return 价格波动
     */
    @Override
    public MesPriceRecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询价格波动列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 价格波动分页列表
     */
    @Override
    public TableDataInfo<MesPriceRecVo> queryPageList(MesPriceRecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesPriceRec> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesProduct> wrappe=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(bo.getProname())){
            wrappe.like(MesProduct::getTitle,bo.getProname());
            List<MesProduct> stockList = productMapper.selectList(wrappe);
            if(stockList.size()>0){
                List<Long> ids=new ArrayList<>();
                for(MesProduct stock:stockList){
                    ids.add(stock.getId());
                }
                lqw.in(MesPriceRec::getProId,ids);
            }
        }
        Page<MesPriceRecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesPriceRecVo vo : result.getRecords()) {
            vo.setProname(baseMapper.selectproTitleById(vo.getProId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的价格波动列表
     *
     * @param bo 查询条件
     * @return 价格波动列表
     */
    @Override
    public List<MesPriceRecVo> queryList(MesPriceRecBo bo) {
        LambdaQueryWrapper<MesPriceRec> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesPriceRec> buildQueryWrapper(MesPriceRecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesPriceRec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProId() != null, MesPriceRec::getProId, bo.getProId());
        lqw.eq(bo.getCount() != null, MesPriceRec::getCount, bo.getCount());
        lqw.eq(bo.getPrices() != null, MesPriceRec::getPrices, bo.getPrices());
        return lqw;
    }

    /**
     * 新增价格波动
     *
     * @param bo 价格波动
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesPriceRecBo bo) {
        MesPriceRec add = MapstructUtils.convert(bo, MesPriceRec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改价格波动
     *
     * @param bo 价格波动
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesPriceRecBo bo) {
        MesPriceRec update = MapstructUtils.convert(bo, MesPriceRec.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesPriceRec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除价格波动信息
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
