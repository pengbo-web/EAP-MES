package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.MesOrderVo;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesSaleoutBo;
import com.liaoyun.mes.domain.vo.MesSaleoutVo;
import com.liaoyun.mes.service.IMesSaleoutService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 销售出库Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-06
 */
@RequiredArgsConstructor
@Service
public class MesSaleoutServiceImpl implements IMesSaleoutService {

    private final MesSaleoutMapper baseMapper;

    private final MesStockMapper stockMapper;

    private final MesOrderMapper orderMapper;

    private final MesOrderDeMapper orderDeMapper;

    private final MesAllrecMapper allrecMapper;

    private final MesCustomMapper customMapper;

    private final MesInventoryLedgerMapper inventoryLedgerMapper;
    /**
     * 查询销售出库
     *
     * @param id 主键
     * @return 销售出库
     */
    @Override
    public MesSaleoutVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询销售出库列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 销售出库分页列表
     */
    @Override
    public TableDataInfo<MesSaleoutVo> queryPageList(MesSaleoutBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesSaleout> lqw = buildQueryWrapper(bo);
        Page<MesSaleoutVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesSaleoutVo vo : result.getRecords()) {
            try {
                MesOrderVo vo1=orderMapper.selectVoById(vo.getOrderid());
                MesCustom custom=customMapper.selectById(vo1.getCustid());
                vo1.setCustname(custom.getTitle());
                vo.setOrderVo(vo1);
                vo.setStockVo(stockMapper.selectVoById(vo.getStockid()));
            }catch (Exception e){

            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的销售出库列表
     *
     * @param bo 查询条件
     * @return 销售出库列表
     */
    @Override
    public List<MesSaleoutVo> queryList(MesSaleoutBo bo) {
        LambdaQueryWrapper<MesSaleout> lqw = buildQueryWrapper(bo);
        List<MesSaleoutVo> list = baseMapper.selectVoList(lqw);
        for (MesSaleoutVo mesSaleout : list) {
            MesStockVo vo=stockMapper.selectVoById(mesSaleout.getStockid());
            mesSaleout.setStockVo(vo);
        }
        return list;
    }

    private LambdaQueryWrapper<MesSaleout> buildQueryWrapper(MesSaleoutBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesSaleout> lqw = Wrappers.lambdaQuery();
        lqw.like(bo.getOrderid() != null, MesSaleout::getOrderid, bo.getOrderid());
        lqw.like(bo.getStockid() != null, MesSaleout::getStockid, bo.getStockid());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesSaleout::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增销售出库
     *
     * @param bo 销售出库
     * @return 是否新增成功
     */
    @Override
    @Transactional
    public Boolean insertByBo(MesSaleoutBo bo) {
        MesSaleout add = MapstructUtils.convert(bo, MesSaleout.class);
        validEntityBeforeSave(add);
        if (!StringUtils.equals(bo.getStatus(),"0")){
            MesStock stock = stockMapper.selectById(bo.getStockid());
            if(bo.getCounts()>stock.getNowstock().intValue()){
                return false;
            }
            MesOrder order = orderMapper.selectById(bo.getOrderid());
            MesCustom custom=customMapper.selectById(order.getCustid());
//            BigDecimal nc=stock.getNowstock().subtract(BigDecimal.valueOf(bo.getCounts()));
//            stock.setNowstock(nc);
//            stockMapper.updateById(stock);
            //出入库记录
            MesAllrec allrec=new MesAllrec();
            allrec.setStockid(stock.getId());
            allrec.setType("出库");
            allrec.setFromtype("销售出库");
            allrec.setCounts(BigDecimal.valueOf(bo.getCounts()));
            allrec.setPrices(BigDecimal.ZERO);
            allrec.setCname(custom.getTitle());
            try {
                allrec.setOrderid(bo.getCustno());
            }catch (Exception e){

            }
            allrecMapper.insert(allrec);
            //订单增加出库数量
            List<Long> deords=Arrays.stream(order.getStids().split(","))
                .map(Long::parseLong)
                .toList();
            LambdaQueryWrapper<MesOrderDe> lqwc = Wrappers.lambdaQuery();
            lqwc.eq(MesOrderDe::getStockid,stock.getId());
            lqwc.in(MesOrderDe::getId,deords);
            try{
                MesOrderDe orderDe = orderDeMapper.selectList(lqwc).get(0);
                BigDecimal nc1=orderDe.getCkcounts().add(BigDecimal.valueOf(bo.getCounts()));
                orderDe.setCkcounts(nc1);
                orderDeMapper.updateById(orderDe);
            }catch (Exception e){
            }

        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改销售出库
     *
     * @param bo 销售出库
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesSaleoutBo bo) {
        MesSaleout update = MapstructUtils.convert(bo, MesSaleout.class);
//        validEntityBeforeSave(update);
        Map<String,Object> map=new HashMap<>();
        if(bo.getStatus().equals("2")){
            if(bo.getKwid()==0){
                bo.setKwid(null);
            }

            map.put("stockid",bo.getStockid());
            map.put("count",bo.getCounts().intValue());
                map.put("kwid",bo.getKwid());
                map.put("tenantid", LoginHelper.getTenantId());
            map.put("msg",0);
            inventoryLedgerMapper.callInOutProcedure(map);
        }
//
        System.out.println(map.get("msg"));
        if(map.get("msg").equals(1)){
            baseMapper.updateById(update);
            return true;
        }
        return false;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesSaleout entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除销售出库信息
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
