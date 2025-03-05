package com.liaoyun.mes.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.controller.ArithHelper;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.bo.MesPriceRecBo;
import com.liaoyun.mes.domain.bo.MesProductBo;
import com.liaoyun.mes.domain.bo.MesTaskBo;
import com.liaoyun.mes.domain.vo.MesProductVo;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.mapper.MesPriceRecMapper;
import com.liaoyun.mes.mapper.MesProductMapper;
import com.liaoyun.mes.mapper.MesStockMapper;
import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesInstockBo;
import com.liaoyun.mes.domain.vo.MesInstockVo;
import com.liaoyun.mes.mapper.MesInstockMapper;
import com.liaoyun.mes.service.IMesInstockService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 入库信息Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@RequiredArgsConstructor
@Service
public class MesInstockServiceImpl implements IMesInstockService {

    private final MesInstockMapper baseMapper;

    private final MesProductMapper mesProductMapper;

    private final SysUserMapper sysUserMapper;

    private final MesPriceRecMapper mesPriceRecMapper;

    private final MesStockMapper mesStockMapper;

    private final MesInventoryLedgerMapper inventoryLedgerMapper;

    /**
     * 查询入库信息
     *
     * @param id 主键
     * @return 入库信息
     */
    @Override
    public MesInstockVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询入库信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 入库信息分页列表
     */
    @Override
    public TableDataInfo<MesInstockVo> queryPageList(MesInstockBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesInstock> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesStock> wrappe = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(bo.getProname())) {
            wrappe.like(MesStock::getTilte, bo.getProname());
            List<MesStock> stockList = mesStockMapper.selectList(wrappe);
            if (stockList.size() > 0) {
                List<Long> ids = new ArrayList<>();
                for (MesStock stock : stockList) {
                    ids.add(stock.getId());
                }
                lqw.in(MesInstock::getProId, ids);
            }
        }
        if (StringUtils.isNotBlank(bo.getStocksn())) {
            wrappe.like(MesStock::getSn, bo.getStocksn());
            List<MesStock> stockList = mesStockMapper.selectList(wrappe);
            if (stockList.size() > 0) {
                List<Long> ids = new ArrayList<>();
                for (MesStock stock : stockList) {
                    ids.add(stock.getId());
                }
                lqw.in(MesInstock::getProId, ids);
            }
        }
        Page<MesInstockVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesInstockVo vo : result.getRecords()) {
            vo.setProName(baseMapper.selectproTitleById(vo.getProId()));
            vo.setStockVo(mesStockMapper.selectVoById(vo.getProId()));
            vo.setUsername(sysUserMapper.selectById(vo.getCreateBy()).getNickName());
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的入库信息列表
     *
     * @param bo 查询条件
     * @return 入库信息列表
     */
    @Override
    public List<MesInstockVo> queryList(MesInstockBo bo) {
        LambdaQueryWrapper<MesInstock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesInstock> buildQueryWrapper(MesInstockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesInstock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProId() != null, MesInstock::getProId, bo.getProId());
        lqw.eq(StringUtils.isNotBlank(bo.getFromtype()), MesInstock::getFromtype, bo.getFromtype());
        lqw.eq(bo.getNowprice() != null, MesInstock::getNowprice, bo.getNowprice());
        lqw.eq(bo.getAllprice() != null, MesInstock::getAllprice, bo.getAllprice());
        lqw.between(params.get("beginSubtime") != null && params.get("endSubtime") != null,
            MesInstock::getSubtime, params.get("beginSubtime"), params.get("endSubtime"));
        return lqw;
    }

    /**
     * 新增入库信息
     *
     * @param bo 入库信息
     * @return 是否新增成功
     */
    @Override
    @DSTransactional
    public Boolean insertByBo(MesInstockBo bo, int st) {


        MesInstock add = MapstructUtils.convert(bo, MesInstock.class);
        Date date = new Date();
        add.setSubtime(date);
        validEntityBeforeSave(add);
        //流转
        add.setSt(st);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改入库信息
     *
     * @param bo 入库信息
     * @return 是否修改成功
     */
    @Override
    @DSTransactional
    public Boolean updateByBo(MesInstockBo bo) {
        bo.setSubtime(new Date());
        MesInstock update = MapstructUtils.convert(bo, MesInstock.class);

        validEntityBeforeSave(update);
        if (StringUtils.equals(bo.getStatus(), "1")) {
            MesInstock add = baseMapper.selectById(update.getId());

            MesStock mesStockVo = mesStockMapper.selectById(add.getProId());
            mesStockVo.setKwrec(mesStockVo.getKwrec() + "," + update.getKwid());
            update.setOldst(mesStockVo.getNowstock());
            BigDecimal lc = new BigDecimal(add.getCount());
            LambdaQueryWrapper<MesProduct> lambdaQueryWrappq = new LambdaQueryWrapper<>();
            lambdaQueryWrappq.eq(MesProduct::getStocks, mesStockVo.getId());
            MesProductVo productVo = new MesProductVo();
            List<MesProductVo> productVoList = mesProductMapper.selectVoList(lambdaQueryWrappq);
            if (productVoList.size() > 0) {
                productVo = productVoList.get(0);
            }
            if (productVo != null) {
                if (add.getSt() == 0) {
                    BigDecimal nowp = BigDecimal.ZERO;
                    try {
                        nowp = ArithHelper.div(add.getAllprice(), lc);
                    } catch (Exception e) {

                    }
                    add.setNowprice(nowp);
                    productVo.setBcount(nowp);
                    MesPriceRec priceRec = new MesPriceRec();
                    priceRec.setProId(add.getProId());
                    priceRec.setCount(add.getCount());
                    priceRec.setPrices(nowp);
                    mesPriceRecMapper.insert(priceRec);
                } else {
                    BigDecimal b = BigDecimal.ZERO;
                    try {
                        b = baseMapper.selectLatest(productVo.getId()).getNowprice();
                    } catch (Exception e) {

                    }
                    add.setNowprice(b);
                    BigDecimal newb = ArithHelper.mul(b, new BigDecimal(add.getCount()));
                    add.setAllprice(newb);
                }
                //增加来料质检
                if (bo.getRemark() != null) {
                    productVo.setRemark(productVo.getRemark() + "," + bo.getRemark());
                    productVo.setQuacount(productVo.getQuacount() + 1);
                }
                MesProduct update1 = (MesProduct) BeanMapHelper.Bean2Bean(productVo, MesProduct.class);
                mesProductMapper.updateById(update1);
            }

//            BigDecimal nowp = mesStockVo.getNowstock();
//            nowp = nowp.add(lc);
//            mesStockVo.setNowstock(nowp);
//            mesStockMapper.updateById(mesStockVo);
            MesInventoryLedger ledger=new MesInventoryLedger();
            ledger.setStockid(mesStockVo.getId());
            if(null==add.getKwid()){
                ledger.setLocationId(0L);
            }else {
                ledger.setLocationId(add.getKwid());
            }
            ledger.setQuantity(add.getCount());
            ledger.setOperation(1);
            ledger.setSn(null);
            ledger.setRemark(add.getRemark());
            inventoryLedgerMapper.insert(ledger);
        }

        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesInstock entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除入库信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
