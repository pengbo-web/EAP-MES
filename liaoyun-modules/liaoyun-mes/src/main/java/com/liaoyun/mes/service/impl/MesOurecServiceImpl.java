package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesAllrec;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.vo.MesStockTypeVo;
import com.liaoyun.mes.mapper.MesAllrecMapper;
import com.liaoyun.mes.mapper.MesStockMapper;
import com.liaoyun.mes.mapper.MesStockTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOurecBo;
import com.liaoyun.mes.domain.vo.MesOurecVo;
import com.liaoyun.mes.domain.MesOurec;
import com.liaoyun.mes.mapper.MesOurecMapper;
import com.liaoyun.mes.service.IMesOurecService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 出库记录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-14
 */
@RequiredArgsConstructor
@Service
public class MesOurecServiceImpl implements IMesOurecService {

    private final MesOurecMapper baseMapper;
    private final MesStockMapper stockMapper;

    private final MesAllrecMapper allrecMapper;

    private final MesStockTypeMapper typeMapper;
    /**
     * 查询出库记录
     *
     * @param id 主键
     * @return 出库记录
     */
    @Override
    public MesOurecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询出库记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 出库记录分页列表
     */
    @Override
    public TableDataInfo<MesOurecVo> queryPageList(MesOurecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOurec> lqw = buildQueryWrapper(bo);
        Page<MesOurecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesOurecVo vo : result.getRecords()) {
            vo.setStockname(stockMapper.selectVoById(vo.getStockid()).getTilte());
            try {
                MesStockTypeVo typeVo=typeMapper.selectVoById(vo.getStocktypeid());
                vo.setStypename(typeVo.getTitle());
            }catch (Exception e){

            }

        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的出库记录列表
     *
     * @param bo 查询条件
     * @return 出库记录列表
     */
    @Override
    public List<MesOurecVo> queryList(MesOurecBo bo) {
        LambdaQueryWrapper<MesOurec> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOurec> buildQueryWrapper(MesOurecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOurec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStockid() != null, MesOurec::getStockid, bo.getStockid());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesOurec::getSn, bo.getSn());
        lqw.eq(bo.getCustId() != null, MesOurec::getCustId, bo.getCustId());
        lqw.eq(bo.getNprice() != null, MesOurec::getNprice, bo.getNprice());
        lqw.eq(bo.getStocktypeid() != null, MesOurec::getStocktypeid, bo.getStocktypeid());
        lqw.eq(bo.getShuidian() != null, MesOurec::getShuidian, bo.getShuidian());
        lqw.eq(StringUtils.isNotBlank(bo.getMet()), MesOurec::getMet, bo.getMet());
        lqw.eq(bo.getCount() != null, MesOurec::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesOurec::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增出库记录
     *
     * @param bo 出库记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesOurecBo bo) {
        MesOurec add = MapstructUtils.convert(bo, MesOurec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改出库记录
     *
     * @param bo 出库记录
     * @return 是否修改成功
     */
    @Override
    @Transactional
    public Boolean updateByBo(MesOurecBo bo) {
        MesOurec update = MapstructUtils.convert(bo, MesOurec.class);
        validEntityBeforeSave(update);
        if(StringUtils.equals(bo.getStatus(),"1")){
            try{
                MesStock stock = stockMapper.selectById(update.getStockid());
                BigDecimal nes = update.getCount();
                BigDecimal newStock = stock.getNowstock().subtract(nes);
                stock.setNowstock(newStock);
                if(newStock.intValue()<0){
                    return false;
                }
                MesAllrec allrec=new MesAllrec();
                allrec.setType("出库");
                allrec.setFromtype("其他出库");
                allrec.setStockid(stock.getId());
                allrec.setCounts(bo.getCount());
                allrec.setRemark(bo.getRemark());
                allrec.setPrices(bo.getNprice());
                allrecMapper.insert(allrec);
                stockMapper.updateById(stock);
            }catch (Exception e){

            }

        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOurec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除出库记录信息
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
        for (Long id : ids) {
            MesOurec o = baseMapper.selectById(id);
            o.setRemark2("手动删除");
            baseMapper.updateById(o);
            MesStock stock = stockMapper.selectById(o.getStockid());
            BigDecimal nowstock = stock.getNowstock();
            nowstock=nowstock.add(o.getCount());
            stock.setNowstock(nowstock);
            stockMapper.updateById(stock);
            //其他出库删除
            LambdaQueryWrapper<MesAllrec> lqw = Wrappers.lambdaQuery();
            lqw.eq(MesAllrec::getStockid, o.getStockid());
            lqw.eq(MesAllrec::getCreateTime,o.getCreateTime());
            lqw.eq(MesAllrec::getCounts,o.getCount());
            lqw.eq(MesAllrec::getType,"出库");
            for (MesAllrec allrec:allrecMapper.selectList(lqw)) {
                allrec.setRemark2("手动删除");
                allrecMapper.updateById(allrec);
                allrecMapper.deleteById(allrec);
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
