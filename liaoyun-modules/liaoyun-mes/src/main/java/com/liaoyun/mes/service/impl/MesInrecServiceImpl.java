package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.mapper.MesStockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesInrecBo;
import com.liaoyun.mes.domain.vo.MesInrecVo;
import com.liaoyun.mes.domain.MesInrec;
import com.liaoyun.mes.mapper.MesInrecMapper;
import com.liaoyun.mes.service.IMesInrecService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 入库记录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@RequiredArgsConstructor
@Service
public class MesInrecServiceImpl implements IMesInrecService {

    private final MesInrecMapper baseMapper;

    private final MesStockMapper stockMapper;

    /**
     * 查询入库记录
     *
     * @param id 主键
     * @return 入库记录
     */
    @Override
    public MesInrecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询入库记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 入库记录分页列表
     */
    @Override
    public TableDataInfo<MesInrecVo> queryPageList(MesInrecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesInrec> lqw = buildQueryWrapper(bo);
        List<MesStock> stock=new ArrayList<>();
        LambdaQueryWrapper<MesStock> stockwra=new LambdaQueryWrapper<>();
        List<Long> ids=new ArrayList<>();
        if(StringUtils.isNotBlank(bo.getStockcode())){
            stockwra.eq(MesStock::getSn,bo.getStockcode());
            List<MesStock> list = stockMapper.selectList(stockwra);
            if(!list.isEmpty()){
                ids=list.stream().map(MesStock::getId).toList();
            }
        }
        if(StringUtils.isNotBlank(bo.getStockname())){
            stockwra.eq(MesStock::getTilte,bo.getStockname());
            List<MesStock> list2 = stockMapper.selectList(stockwra);
            if(!list2.isEmpty()){
                ids=list2.stream().map(MesStock::getId).toList();
            }
        }
        if(!ids.isEmpty()){
            lqw.in(MesInrec::getStockid, ids);
        }

        Page<MesInrecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesInrecVo vo : result.getRecords()) {
            try{
                vo.setStockVo(stockMapper.selectVoById(vo.getStockid()));
            }catch (Exception e){

            }


        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的入库记录列表
     *
     * @param bo 查询条件
     * @return 入库记录列表
     */
    @Override
    public List<MesInrecVo> queryList(MesInrecBo bo) {
        LambdaQueryWrapper<MesInrec> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesInrec> buildQueryWrapper(MesInrecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesInrec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStockid() != null, MesInrec::getStockid, bo.getStockid());
        lqw.eq(bo.getCustId() != null, MesInrec::getCustId, bo.getCustId());
        lqw.eq(bo.getNprice() != null, MesInrec::getNprice, bo.getNprice());
        lqw.eq(bo.getShuidian() != null, MesInrec::getShuidian, bo.getShuidian());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), MesInrec::getRemark, bo.getRemark());
        lqw.eq(StringUtils.isNotBlank(bo.getFromtype()), MesInrec::getFromtype, bo.getFromtype());

        lqw.eq(StringUtils.isNotBlank(bo.getMet()), MesInrec::getMet, bo.getMet());
        lqw.eq(bo.getCount() != null, MesInrec::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesInrec::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增入库记录
     *
     * @param bo 入库记录
     * @return 是否新增成功
     */
    @Override
    public Long insertByBo(MesInrecBo bo) {
        MesInrec add = MapstructUtils.convert(bo, MesInrec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return bo.getId();
    }

    /**
     * 修改入库记录
     *
     * @param bo 入库记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesInrecBo bo) {
        MesInrec update = MapstructUtils.convert(bo, MesInrec.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesInrec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除入库记录信息
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
