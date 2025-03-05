package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.MesStocktaking;
import com.liaoyun.mes.mapper.MesStockMapper;
import com.liaoyun.mes.mapper.MesStockTypeMapper;
import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesStinfoBo;
import com.liaoyun.mes.domain.vo.MesStinfoVo;
import com.liaoyun.mes.domain.MesStinfo;
import com.liaoyun.mes.mapper.MesStinfoMapper;
import com.liaoyun.mes.service.IMesStinfoService;

import java.math.BigDecimal;
import java.util.*;

/**
 * 库位调拨Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-01
 */
@RequiredArgsConstructor
@Service
public class MesStinfoServiceImpl implements IMesStinfoService {

    private final MesStinfoMapper baseMapper;
    private final MesStockMapper stockMapper;
    private final MesStockTypeMapper stockTypeMapper;
    private final MesInventoryLedgerMapper inventoryLedgerMapper;

    /**
     * 查询库位调拨
     *
     * @param id 主键
     * @return 库位调拨
     */
    @Override
    public MesStinfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询库位调拨列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 库位调拨分页列表
     */
    @Override
    public TableDataInfo<MesStinfoVo> queryPageList(MesStinfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesStinfo> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesStock> wrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(bo.getStockname())){
            wrapper.like(MesStock::getTilte,bo.getStockname());
            List<MesStock> stockList = stockMapper.selectList(wrapper);
            if(stockList.size()>0){
                List<Long> ids=new ArrayList<>();
                for(MesStock stock:stockList){
                    ids.add(stock.getId());
                }
                lqw.in(MesStinfo::getStockid,ids);
            }
        }
        Page<MesStinfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesStinfoVo vo : result.getRecords()) {
            MesStock stock=stockMapper.selectById(vo.getStockid());
            vo.setStock(stock);
            vo.setStockType(stockTypeMapper.selectById(vo.getStocktypeid()));
            if(stock!=null){
                vo.setKw(stockTypeMapper.selectVoById(stock.getStockid()).getSn());
            }

        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的库位调拨列表
     *
     * @param bo 查询条件
     * @return 库位调拨列表
     */
    @Override
    public List<MesStinfoVo> queryList(MesStinfoBo bo) {
        LambdaQueryWrapper<MesStinfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesStinfo> buildQueryWrapper(MesStinfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesStinfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStockid() != null, MesStinfo::getStockid, bo.getStockid());
        lqw.eq(bo.getStocktypeid() != null, MesStinfo::getStocktypeid, bo.getStocktypeid());
        lqw.eq(bo.getCounts() != null, MesStinfo::getCounts, bo.getCounts());
//        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesStinfo::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesStinfo::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增库位调拨
     *
     * @param bo 库位调拨
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesStinfoBo bo) {
        MesStinfo add = MapstructUtils.convert(bo, MesStinfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            Map<String,Object> map=new HashMap<>();
                map.put("stockid",bo.getStockid());
                map.put("count",bo.getCounts().intValue());
                map.put("kwid",bo.getStocktypeid());
                map.put("tenantid", LoginHelper.getTenantId());
                map.put("msg",0);
            inventoryLedgerMapper.callInOutProcedure(map);
            if(map.get("msg").equals(1)){
                MesInventoryLedger inventoryLedger=new MesInventoryLedger();
                inventoryLedger.setStockid(add.getStockid());
                inventoryLedger.setLocationId(add.getStocktypeid());
                inventoryLedger.setQuantity(add.getCounts().intValue());
                inventoryLedger.setOperation(1);
                inventoryLedgerMapper.insert(inventoryLedger);
            }

            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改库位调拨
     *
     * @param bo 库位调拨
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesStinfoBo bo) {
        MesStinfo update = MapstructUtils.convert(bo, MesStinfo.class);
        validEntityBeforeSave(update);
        if(StringUtils.equals(bo.getStatus(),"1")){
//            LambdaQueryWrapper<MesStinfo> wrapper2=new LambdaQueryWrapper<>();
//            wrapper2.eq(MesStinfo::getStockid,update.getStockid());
//            wrapper2.eq(MesStinfo::getStocktypeid,update.getStocktypeid());
//            List<MesStinfo> list2 = baseMapper.selectList(wrapper2);
//            //有相似
//            if(list2.size()>0){
//                //增加移库库位
//                MesStinfo old=list2.get(0);
//                old.setCounts(old.getCounts().add(bo.getCounts()));
//                baseMapper.updateById(old);
//                //老库位减少库存
//                MesStock stock=stockMapper.selectById(bo.getStockid());
//                LambdaQueryWrapper<MesStinfo> wrapper=new LambdaQueryWrapper<>();
//                wrapper.eq(MesStinfo::getStockid,update.getStockid());
//                wrapper.eq(MesStinfo::getStocktypeid,stock.getStockid());
//                List<MesStinfo> list=baseMapper.selectList(wrapper);
//                MesStinfo stinfo=new MesStinfo();
//                if(list.size()>0){
//                    stinfo=list.get(0);
//                    BigDecimal hb=stinfo.getCounts().subtract(update.getCounts());
//                    stinfo.setCounts(hb);
//                    baseMapper.updateById(stinfo);
//                }else{
//                    stinfo.setCounts(stock.getNowstock().subtract(update.getCounts()));
//                    stinfo.setStockid(stock.getId());
//                    stinfo.setStocktypeid(stock.getStockid());
//                    baseMapper.insert(stinfo);
//                }
//            }else{
//                //老库位减少库存
//                MesStock stock=stockMapper.selectById(bo.getStockid());
//                LambdaQueryWrapper<MesStinfo> wrapper=new LambdaQueryWrapper<>();
//                wrapper.eq(MesStinfo::getStockid,update.getStockid());
//                wrapper.eq(MesStinfo::getStocktypeid,stock.getStockid());
//                List<MesStinfo> list=baseMapper.selectList(wrapper);
//                MesStinfo stinfo=new MesStinfo();
//                if(list.size()>0){
//                    stinfo=list.get(0);
//                    BigDecimal hb=stinfo.getCounts().subtract(update.getCounts());
//                    stinfo.setCounts(hb);
//                    baseMapper.updateById(stinfo);
//                }else{
//                    stinfo.setCounts(stock.getNowstock().subtract(update.getCounts()));
//                    stinfo.setStockid(stock.getId());
//                    stinfo.setStocktypeid(stock.getStockid());
//                    baseMapper.insert(stinfo);
//                }
//            }

        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesStinfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除库位调拨信息
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
