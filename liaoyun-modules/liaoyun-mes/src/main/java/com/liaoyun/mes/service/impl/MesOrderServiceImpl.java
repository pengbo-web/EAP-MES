package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.controller.UnitConverter;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOrderBo;
import com.liaoyun.mes.service.IMesOrderService;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 销售订单Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-16
 */
@RequiredArgsConstructor
@Service
public class MesOrderServiceImpl implements IMesOrderService {

    private final MesOrderMapper baseMapper;

    private final MesOrderDeMapper deMapper;

    private final MesCustomMapper customMapper;

    private final MesStockMapper stockMapper;

    private final MesStockTypeMapper stockTypeMapper;

    private final MesUnitmeasureMapper uintMapper;

    private final MesProductMapper productMapper;

    private final MesUnitmeasureMapper unitmeasureMapper;

    private final MesSaleoutMapper saleoutMapper;

    private final MesInrecMapper inrecMapper;

    private final MesOutorderDeMapper outorderDeMapper;

    private final MesOolistMapper oolistMapper;

    private final MesWorkMapper workMapper;

    private final MesInventoryLedgerMapper inventoryLedgerMapper;
    /**
     * 查询销售订单
     *
     * @param id 主键
     * @return 销售订单
     */
    @Override
    public MesOrderVo queryById(Long id){
        MesOrderVo vo=baseMapper.selectVoById(id);
        String orders=vo.getCustno();
        List<MesUnitmeasure> list=unitmeasureMapper.selectList();
        //采购关联
        LambdaQueryWrapper<MesInrec> wrapper=new LambdaQueryWrapper<>();
        wrapper.like(MesInrec::getOrderid,orders);
        List<MesInrecVo> inreclist=inrecMapper.selectVoList(wrapper);
        for(MesInrecVo inrec:inreclist){
            inrec.setStockVo(stockMapper.selectVoById(inrec.getStockid()));
        }
        vo.setInrecList(inreclist);
        //外协出库关联
        LambdaQueryWrapper<MesOutorderDe> wrapper2=new LambdaQueryWrapper<>();
        wrapper2.like(MesOutorderDe::getOrderid,orders);
        List<MesOutorderDeVo> devolist=outorderDeMapper.selectVoList(wrapper2);
        for(MesOutorderDeVo devo:devolist){
            devo.setStockVo(stockMapper.selectVoById(devo.getStockid()));
        }
        vo.setOutorderList(devolist);
        //外协入库
        LambdaQueryWrapper<MesOolist> wrapper3=new LambdaQueryWrapper<>();
        wrapper3.like(MesOolist::getOrderid,orders);
        List<MesOolistVo> oolistVos=oolistMapper.selectVoList(wrapper3);
        for(MesOolistVo oolist:oolistVos){
            oolist.setStockVo(stockMapper.selectVoById(oolist.getStockid()));
        }
        vo.setOolistVos(oolistVos);
        //其他出入库关联
        LambdaQueryWrapper<MesSaleout> wrapper4=new LambdaQueryWrapper<>();
        wrapper4.like(MesSaleout::getOrderid,orders);
        List<MesSaleoutVo> saleoutList=saleoutMapper.selectVoList(wrapper4);
        for(MesSaleoutVo saleout:saleoutList){
            saleout.setStockVo(stockMapper.selectVoById(saleout.getStockid()));
        }

        //出库记录
        LambdaQueryWrapper<MesSaleout> swra=new LambdaQueryWrapper<>();
        swra.eq(MesSaleout::getOrderid, vo.getId());
        List<MesSaleoutVo> saleoutVos=saleoutMapper.selectVoList(swra);
        if(!saleoutVos.isEmpty()){
            for (MesSaleoutVo saleoutVo : saleoutVos) {
                //便利List查询
                MesStockVo sast=stockMapper.selectVoById(saleoutVo.getStockid());
                sast.setUnitname(sast.getUnitname());
                saleoutVo.setStockVo(sast);
            }
            vo.setSaleoutList(saleoutVos);
        }
        //其他关联
        MesCustomVo customVo = customMapper.selectVoById(vo.getCustid());
        if(null==customVo){
            System.out.println(vo);
        }
        vo.setCustname(customVo.getTitle());
        vo.setCustomVo(customVo);
        if (!vo.getStids().isEmpty()){
            String[] ids=vo.getStids().split(",");
            List<Long> longIds = Arrays.stream(ids)
                .map(Long::parseLong)
                .toList();
            LambdaQueryWrapper<MesOrderDe> wrapper33=new LambdaQueryWrapper<>();
            wrapper33.in(MesOrderDe::getId, longIds);
            //订单详情
            List<MesOrderDeVo> deList=deMapper.selectVoList(wrapper33);
            for (MesOrderDeVo deVo : deList) {
                MesStockVo stockVo=stockMapper.selectVoById(deVo.getStockid());
                if (null==stockVo){
                    System.out.println(vo);
                }
                BigDecimal pn=BigDecimal.ZERO;
                try{
                    pn=new BigDecimal(inventoryLedgerMapper.getStockByProductId(stockVo.getId()));
                }catch (Exception e){

                }

                stockVo.setNowstock(pn);
                deVo.setStockVo(stockVo);
//                    try {
//                        if("包材".equals(stockVo.getType())){
//                            float newc=
//                            stockVo.setNcout();
//                        }
//
//                    }

                //加入Bom系统
                List<MesStockVo> bmlist=new ArrayList<>();
                try {
                    // 查询一级物料
                    LambdaQueryWrapper<MesProduct> firstLevelWrapper = new LambdaQueryWrapper<>();
                    firstLevelWrapper.eq(MesProduct::getStocks, stockVo.getId());
                    MesProductVo firstLevelProduct = productMapper.selectVoList(firstLevelWrapper).stream().findFirst().orElse(null);
                    if(firstLevelProduct.getSn().trim().equals("BY000494")){
                        System.out.println("!23");
                    }
                    if (firstLevelProduct != null) {
                        deVo.setProid(firstLevelProduct.getId());
                        // 查询二级物料
                        LambdaQueryWrapper<MesProduct> secondLevelWrapper = new LambdaQueryWrapper<>();
                        secondLevelWrapper.eq(MesProduct::getPid, firstLevelProduct.getId());
                        List<MesProductVo> secondLevelProducts = productMapper.selectVoList(secondLevelWrapper);

                        for (MesProductVo secondLevelProduct : secondLevelProducts) {
                            MesStockVo stock = stockMapper.selectVoById(secondLevelProduct.getStocks());
                            stock.setUnitname(uintMapper.selectVoById(stock.getUnitid()).getTitle());
                            BigDecimal pn1=BigDecimal.ZERO;
                            try{
                                pn1=new BigDecimal(inventoryLedgerMapper.getStockByProductId(stock.getId()));
                            }catch (Exception e){

                            }
                            stock.setNowstock(pn1);
                            MesUnitmeasure sourceUnit = unitmeasureMapper.selectById(secondLevelProduct.getUnitId());
                            MesUnitmeasure targetUnit = unitmeasureMapper.selectById(stock.getUnitid());

                            float allc = secondLevelProduct.getNeedCount() * secondLevelProduct.getWeight() * deVo.getCounts();
                            float nt = secondLevelProduct.getNeedCount() * secondLevelProduct.getWeight();

                            BigDecimal convertedAmount = UnitConverter.convertUnit(sourceUnit.getTitle(), targetUnit.getTitle(), new BigDecimal(allc), list);
                            if ("包材".equals(stock.getType())) {
                                float newNcout=0.0f;
                                if (nt != 0) {
                                    newNcout= deVo.getCounts() / nt;

                                }
                                stock.setNcout(newNcout);
                            }else{
                                stock.setNcout(convertedAmount.floatValue());
                            }
                            stock.setProid(secondLevelProduct.getId());
                            stock.setNcname(sourceUnit.getTitle());
                            bmlist.add(stock);

                            // 查询三级物料
                            LambdaQueryWrapper<MesProduct> thirdLevelWrapper = new LambdaQueryWrapper<>();
                            thirdLevelWrapper.eq(MesProduct::getPid, secondLevelProduct.getId());
                            List<MesProductVo> thirdLevelProducts = productMapper.selectVoList(thirdLevelWrapper);

                            if (!thirdLevelProducts.isEmpty()) {
                                List<MesStockVo> thirdLevelBomList = new ArrayList<>();
                                for (MesProductVo thirdLevelProduct : thirdLevelProducts) {
                                    LambdaQueryWrapper<MesProduct> fourthLevelWrapper = new LambdaQueryWrapper<>();
                                    fourthLevelWrapper.eq(MesProduct::getPid, thirdLevelProduct.getId());
                                    List<MesProductVo> fourthLevelProducts = productMapper.selectVoList(fourthLevelWrapper);

//                                        for (MesProductVo fourthLevelProduct : fourthLevelProducts) {
                                    MesStockVo thirdLevelStock = stockMapper.selectVoById(thirdLevelProduct.getStocks());
                                    BigDecimal pn2=BigDecimal.ZERO;
                                    try{
                                        pn2=new BigDecimal(inventoryLedgerMapper.getStockByProductId(thirdLevelStock.getId()));
                                    }catch (Exception e){

                                    }
                                    thirdLevelStock.setNowstock(pn2);
                                    MesUnitmeasure thirdLevelUnit = unitmeasureMapper.selectById(thirdLevelStock.getUnitid());
                                    MesUnitmeasure fourthLevelUnit = unitmeasureMapper.selectById(thirdLevelProduct.getUnitId());

                                    thirdLevelStock.setUnitname(thirdLevelUnit.getTitle());
                                    float allc2 = thirdLevelProduct.getNeedCount() * deVo.getCounts() * thirdLevelProduct.getWeight() * nt;

                                    BigDecimal convertedAmount2 = UnitConverter.convertUnit(fourthLevelUnit.getTitle(), thirdLevelUnit.getTitle(), new BigDecimal(allc2), list);
                                    if (thirdLevelStock.getType().equals("色粉")) {
                                        thirdLevelStock.setNcout(convertedAmount2.floatValue() / 25);
                                    } else if ("包材".equals(thirdLevelStock.getType())) {
                                        float newNcout=0.0f;
                                        if (nt != 0) {
                                            newNcout = deVo.getCounts() / nt;

                                        }
                                        thirdLevelStock.setNcout(newNcout);
                                    }else{
                                        thirdLevelStock.setNcout(convertedAmount2.floatValue());
                                    }

                                    thirdLevelStock.setNcname(fourthLevelUnit.getTitle());
                                    thirdLevelStock.setProid(thirdLevelProduct.getId());
                                    thirdLevelBomList.add(thirdLevelStock);

//                                        }

                                }
                                stock.setBomlist(thirdLevelBomList);
                            }
                        }
                    }
                }catch (Exception e){

                }
                try{
                    stockVo.setBomlist(bmlist);
                }catch (Exception e){
                    System.out.println(vo);
                }


            }
            vo.setOrderList(deList);
        }
        vo.setSaleoutList(saleoutList);
        return vo;
    }

    /**
     * 分页查询销售订单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 销售订单分页列表
     */
    @Override
    public TableDataInfo<MesOrderVo> queryPageList(MesOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOrder> lqw = buildQueryWrapper(bo);
        //查询条件
        if(StringUtils.isNotBlank(bo.getCustname())){
            LambdaQueryWrapper<MesCustom> custwrap=new LambdaQueryWrapper<>();
            custwrap.like(MesCustom::getTitle, bo.getCustname());
            List<MesCustom> custs = customMapper.selectList(custwrap);
            List<Long> ids = custs.stream().map(MesCustom::getId).toList();
            lqw.in(MesOrder::getCustid,ids);
        }
        if(StringUtils.isNotBlank(bo.getStockname())){
            LambdaQueryWrapper<MesCustom> customLambdaQueryWrapper=new LambdaQueryWrapper<>();
            customLambdaQueryWrapper.like(MesCustom::getTitle, bo.getStockname());
            List<MesCustom> customList = customMapper.selectList(customLambdaQueryWrapper);
            List<Long> customIds = customList.stream().map(MesCustom::getId).toList();
            if(!customIds.isEmpty()){
                lqw.in(MesOrder::getCustid, customIds);
            }
        }


        //获取所有单位
        List<MesUnitmeasure> list=unitmeasureMapper.selectList();
        Page<MesOrderVo> result=null;
        try{
            result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        }catch (Exception e){
            return null;
        }

        for (MesOrderVo vo : result.getRecords()) {
            //出库记录
            LambdaQueryWrapper<MesSaleout> swra=new LambdaQueryWrapper<>();
            swra.eq(MesSaleout::getOrderid, vo.getId());
            List<MesSaleoutVo> saleoutVos=saleoutMapper.selectVoList(swra);
            if(!saleoutVos.isEmpty()){
                for (MesSaleoutVo saleoutVo : saleoutVos) {
                    //便利List查询
                    MesStockVo sast=stockMapper.selectVoById(saleoutVo.getStockid());
                    sast.setUnitname(sast.getUnitname());
                    saleoutVo.setStockVo(sast);
                }
                vo.setSaleoutList(saleoutVos);
            }
            //其他关联
            MesCustomVo customVo = customMapper.selectVoById(vo.getCustid());
            if(null==customVo){
                System.out.println(vo);
            }
            try{
                vo.setCustname(customVo.getTitle());
            }catch (Exception e){
                System.out.printf(vo.getId().toString());
            }


            vo.setCustomVo(customVo);
            if (!vo.getStids().isEmpty()){
                String[] ids=vo.getStids().split(",");
                List<Long> longIds = Arrays.stream(ids)
                    .map(Long::parseLong)
                    .toList();
                LambdaQueryWrapper<MesOrderDe> wrapper=new LambdaQueryWrapper<>();
                wrapper.in(MesOrderDe::getId, longIds);
                //订单详情
                List<MesOrderDeVo> deList=deMapper.selectVoList(wrapper);
                for (MesOrderDeVo deVo : deList) {
                    MesStockVo stockVo=stockMapper.selectVoById(deVo.getStockid());
                    if (null==stockVo){
                        System.out.println(vo);
                    }
                    deVo.setStockVo(stockVo);
//                    try {
//                        if("包材".equals(stockVo.getType())){
//                            float newc=
//                            stockVo.setNcout();
//                        }
//
//                    }

                    //加入Bom系统
//                    List<MesStockVo> bmlist=new ArrayList<>();
//                    try {
//                        // 查询一级物料
//                        LambdaQueryWrapper<MesProduct> firstLevelWrapper = new LambdaQueryWrapper<>();
//                        firstLevelWrapper.eq(MesProduct::getStocks, stockVo.getId());
//                        MesProductVo firstLevelProduct = productMapper.selectVoList(firstLevelWrapper).stream().findFirst().orElse(null);
//                        if(firstLevelProduct.getSn().trim().equals("BY000494")){
//                            System.out.println("!23");
//                        }
//                        if (firstLevelProduct != null) {
//
//                            // 查询二级物料
//                            LambdaQueryWrapper<MesProduct> secondLevelWrapper = new LambdaQueryWrapper<>();
//                            secondLevelWrapper.eq(MesProduct::getPid, firstLevelProduct.getId());
//                            List<MesProductVo> secondLevelProducts = productMapper.selectVoList(secondLevelWrapper);
//
//                            for (MesProductVo secondLevelProduct : secondLevelProducts) {
//                                MesStockVo stock = stockMapper.selectVoById(secondLevelProduct.getStocks());
//                                stock.setUnitname(uintMapper.selectVoById(stock.getUnitid()).getTitle());
//
//                                MesUnitmeasure sourceUnit = unitmeasureMapper.selectById(secondLevelProduct.getUnitId());
//                                MesUnitmeasure targetUnit = unitmeasureMapper.selectById(stock.getUnitid());
//
//                                float allc = secondLevelProduct.getNeedCount() * secondLevelProduct.getWeight() * deVo.getCounts();
//                                float nt = secondLevelProduct.getNeedCount() * secondLevelProduct.getWeight();
//
//                                BigDecimal convertedAmount = UnitConverter.convertUnit(sourceUnit.getTitle(), targetUnit.getTitle(), new BigDecimal(allc), list);
//                                if ("包材".equals(stock.getType())) {
//                                    float newNcout=0.0f;
//                                    if (nt != 0) {
//                                        newNcout= deVo.getCounts() / nt;
//
//                                    }
//                                    stock.setNcout(newNcout);
//                                }else{
//                                    stock.setNcout(convertedAmount.floatValue());
//                                }
//
//                                stock.setNcname(sourceUnit.getTitle());
//                                bmlist.add(stock);
//
//                                // 查询三级物料
//                                LambdaQueryWrapper<MesProduct> thirdLevelWrapper = new LambdaQueryWrapper<>();
//                                thirdLevelWrapper.eq(MesProduct::getPid, secondLevelProduct.getId());
//                                List<MesProductVo> thirdLevelProducts = productMapper.selectVoList(thirdLevelWrapper);
//
//                                if (!thirdLevelProducts.isEmpty()) {
//                                    List<MesStockVo> thirdLevelBomList = new ArrayList<>();
//                                    for (MesProductVo thirdLevelProduct : thirdLevelProducts) {
//                                        LambdaQueryWrapper<MesProduct> fourthLevelWrapper = new LambdaQueryWrapper<>();
//                                        fourthLevelWrapper.eq(MesProduct::getPid, thirdLevelProduct.getId());
//                                        List<MesProductVo> fourthLevelProducts = productMapper.selectVoList(fourthLevelWrapper);
//
////                                        for (MesProductVo fourthLevelProduct : fourthLevelProducts) {
//                                        MesStockVo thirdLevelStock = stockMapper.selectVoById(thirdLevelProduct.getStocks());
//
//                                        MesUnitmeasure thirdLevelUnit = unitmeasureMapper.selectById(thirdLevelStock.getUnitid());
//                                        MesUnitmeasure fourthLevelUnit = unitmeasureMapper.selectById(thirdLevelProduct.getUnitId());
//
//                                        thirdLevelStock.setUnitname(thirdLevelUnit.getTitle());
//                                        float allc2 = thirdLevelProduct.getNeedCount() * deVo.getCounts() * thirdLevelProduct.getWeight() * nt;
//
//                                        BigDecimal convertedAmount2 = UnitConverter.convertUnit(fourthLevelUnit.getTitle(), thirdLevelUnit.getTitle(), new BigDecimal(allc2), list);
//                                        if (thirdLevelStock.getType().equals("色粉")) {
//                                            thirdLevelStock.setNcout(convertedAmount2.floatValue() / 25);
//                                        } else if ("包材".equals(thirdLevelStock.getType())) {
//                                            float newNcout=0.0f;
//                                            if (nt != 0) {
//                                                 newNcout = deVo.getCounts() / nt;
//
//                                            }
//                                            thirdLevelStock.setNcout(newNcout);
//                                        }else{
//                                            thirdLevelStock.setNcout(convertedAmount2.floatValue());
//                                        }
//
//                                        thirdLevelStock.setNcname(fourthLevelUnit.getTitle());
//                                        thirdLevelBomList.add(thirdLevelStock);
//
////                                        }
//
//                                    }
//                                    stock.setBomlist(thirdLevelBomList);
//                                }
//                            }
//                        }
//                    }catch (Exception e){
//
//                    }
//                    try{
//                        stockVo.setBomlist(bmlist);
//                    }catch (Exception e){
//                        System.out.println(vo);
//                    }


                }
                vo.setOrderList(deList);
            }
        }
        return TableDataInfo.build(result);
    }


    /**
     * 查询符合条件的销售订单列表
     *
     * @param bo 查询条件
     * @return 销售订单列表
     */
    @Override
    public List<MesOrderVo> queryList(MesOrderBo bo) {
        LambdaQueryWrapper<MesOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOrder> buildQueryWrapper(MesOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getShui() != null, MesOrder::getShui, bo.getShui());
        lqw.eq(bo.getSubtime() != null, MesOrder::getSubtime, bo.getSubtime());
        lqw.ne(bo.getTsta()!=null, MesOrder::getTsta, bo.getTsta());
        lqw.like(StringUtils.isNotBlank(bo.getCustno()), MesOrder::getCustno, bo.getCustno());
        lqw.eq(StringUtils.isNotBlank(bo.getCycle()), MesOrder::getCycle, bo.getCycle());
        lqw.eq(StringUtils.isNotBlank(bo.getStids()), MesOrder::getStids, bo.getStids());
        lqw.eq(StringUtils.isNotBlank(bo.getFp()), MesOrder::getFp, bo.getFp());
        lqw.between(params.get("beginoTime") != null && params.get("endoTime") != null,
            MesOrder::getOtime ,params.get("beginoTime"), params.get("endoTime"));
        lqw.eq(bo.getIsmrp() != null, MesOrder::getIsmrp, bo.getIsmrp());
        lqw.eq(StringUtils.isNotBlank(bo.getOtfile()), MesOrder::getOtfile, bo.getOtfile());
        lqw.eq(StringUtils.isNotBlank(bo.getCtfile()), MesOrder::getCtfile, bo.getCtfile());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesOrder::getStatus, bo.getStatus());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            MesOrder::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增销售订单
     *
     * @param bo 销售订单
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesOrderBo bo) {
        MesOrder add = MapstructUtils.convert(bo, MesOrder.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String datePart = sdf.format(new Date());
        Random random = new Random();
        int randomNumber = random.nextInt(9999999); // 生成六位随机数
        String orderNumber = "LLT" + datePart + String.format("%06d", randomNumber);
        add.setRemark(orderNumber);
        List<String> tearms = Arrays.asList(bo.getStids().split(","));
        List<Long> longList = tearms.stream()
            .map(Long::parseLong)
            .toList();
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        if (longList.size() > 0) {
            for (Long longId : longList) {
                MesOrderDe de=deMapper.selectById(longId);
                de.setRemark(orderNumber);
                de.setOrderid(add.getId());
                deMapper.updateById(de);
            }
        }

        return flag;
    }
    @Override
    public Boolean bequick(Long id){
        MesOrder order=baseMapper.selectById(id);
        LambdaQueryWrapper<MesWork> lqw = Wrappers.lambdaQuery();
        lqw.eq(MesWork::getOrderid,id);
        List<MesWork> list=workMapper.selectList(lqw);
        for(MesWork mesWork:list){
            mesWork.setIstop(1);
            workMapper.updateById(mesWork);
        }
        order.setIstop(1);
        baseMapper.updateById(order);
        return true;
    }

    /**
     * 修改销售订单
     *
     * @param bo 销售订单
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesOrderBo bo) {
        MesOrder update = MapstructUtils.convert(bo, MesOrder.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOrder entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除销售订单信息
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
