package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesStockBo;
import com.liaoyun.mes.service.IMesStockService;

import java.math.BigDecimal;
import java.util.*;

/**
 * 仓库信息Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-10
 */
@RequiredArgsConstructor
@Service
public class MesStockServiceImpl implements IMesStockService {

    private final MesStockMapper baseMapper;
    private final MesStockTypeMapper stockTypeMapper;
    private final MesUnitmeasureMapper unitmeasureMapper;
    private final MesCustomMapper customMapper;
    private final MesInstockMapper instockMapper;
    private final MesInrecMapper inrecMapper;
    private final MesOutorderMapper outorderMapper;
    private final MesOutstockMapper outstockMapper;
    private final MesOurecMapper ourecMapper;
    private final MesInstMapper instMapper;

    private final MesAllrecMapper allrecMapper;
    private final MesInventoryLedgerMapper inventoryLedgerMapper;

    private final MesOutorderDeMapper outorderDeMapper;

    private final MesSaleoutMapper saleoutMapper;
    /**
     * 查询仓库信息
     *
     * @param id 主键
     * @return 仓库信息
     */
    @Override
    public MesStockVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    @Override
    public List<MesStockVo> Listw(MesStockBo bo) {
        String bos=bo.getType();
        List<MesStockVo> list=baseMapper.selectlist(bos);
        return list;
    }



    @Override
    public List<Map<String, Object>> queryMap(Long id){
        MesStockVo vo=baseMapper.selectVoById(id);
        List<Map<String, Object>> map=new ArrayList<>();
        if(vo!=null){
            if(vo.getCustid()!=null){
                List<String> tearms = Arrays.asList(vo.getCustid().split(","));
                try {
                    List<Long> longList = tearms.stream()
                        .map(Long::parseLong)
                        .toList();
                    for (Long ids:longList){
                        MesCustomVo vo1=customMapper.selectVoById(ids);
                        if(vo1!=null){
                            Map<String,Object> map1=new HashMap<>();
                            map1.put("value",ids);
                            map1.put("label",vo1.getTitle());
                            map.add(map1);
                        }
                    }
                }catch (Exception e){

                }

            }
        }
        return map;
    }
    /**
     * 分页查询仓库信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 仓库信息分页列表
     */
    @Override
    public TableDataInfo<MesStockVo> queryPageList(MesStockBo bo, PageQuery pageQuery) {

        LambdaQueryWrapper<MesStock> lqw = buildQueryWrapper(bo);
        List<MesStockVo> list = new ArrayList<>();
//        for (MesStock stock : list) {
//            chukulist(stock.getId());
//            rukulist(stock.getId());
//        }

        Page<MesStockVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesStockVo vo : result.getRecords()) {
            BigDecimal pn=BigDecimal.ZERO;
            try{
               pn=new BigDecimal(inventoryLedgerMapper.getStockByProductId(vo.getId()));
            }catch (Exception e){

            }

            vo.setNowstock(pn);
            //只用一次
            if(null!=vo.getStockid()){
                MesStockTypeVo stockTypeVo = stockTypeMapper.selectVoById(vo.getStockid());
                vo.setStockname(stockTypeVo.getSn());
            }
            if(null!=vo.getUnitid()){
                MesUnitmeasureVo unitmeasureVo = unitmeasureMapper.selectVoById(vo.getUnitid());
                vo.setUnitname(unitmeasureVo.getTitle());
            }
            if(null!=vo.getCustid()){
                try{
                    List<String> names=baseMapper.selectTeamTitleById(vo.getCustid());
                    vo.setCustname(StringUtils.join(names, ","));
                    List<Long> longList = new ArrayList<>();

                    // 按逗号拆分字符串
                    String[] numberArray = vo.getCustid().split(",");

                    for (String numStr : numberArray) {
                        longList.add(Long.parseLong(numStr));
                    }
                    LambdaQueryWrapper<MesCustom> custwrapper =new LambdaQueryWrapper<>();
                    custwrapper.in(MesCustom::getId, longList);
                    vo.setCustomList(customMapper.selectList(custwrapper));
                }catch (Exception e){

                }

            }
        }
        return TableDataInfo.build(result);
    }
    @Override
    public List<MesStockVo> rukulist(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }

        List<MesStockVo> list = new ArrayList<>();
        if(id==1858328235746349058L){
            String a="!11";
            System.out.printf("111");
        }
        // 采购入库信息
        LambdaQueryWrapper<MesInstock> instockWrapper = new LambdaQueryWrapper<>();
        instockWrapper.eq(MesInstock::getProId, id)
            .eq(MesInstock::getStatus, "1");
        List<MesInstock> purchaseList = instockMapper.selectList(instockWrapper);

        // 其他入库信息
        LambdaQueryWrapper<MesInrec> inrecWrapper = new LambdaQueryWrapper<>();
        inrecWrapper.eq(MesInrec::getStockid, id);
        List<MesInrec> inrecListz = inrecMapper.selectList(inrecWrapper);
        List<MesInrec> inrecList=new ArrayList<>();
        for (MesInrec inrec : inrecListz) {
            LambdaQueryWrapper<MesInst> instwrs = new LambdaQueryWrapper<>();
            instwrs.like(MesInst::getRecids, inrec.getId()).eq(MesInst::getStatus, "1");
            List<MesInstVo> instVos=instMapper.selectVoList(instwrs);
            if(instVos.size()>0){
                inrecList.add(inrec);
            }
        }
        // 外协入库
        LambdaQueryWrapper<MesOutorder> outorderWrapper = new LambdaQueryWrapper<>();
        outorderWrapper.eq(MesOutorder::getStoid, id)
            .eq(MesOutorder::getStatus, "1"); // 添加状态判断
        List<MesOutorder> outorderList = outorderMapper.selectList(outorderWrapper);

        // 转换采购入库数据
        purchaseList.stream()
            .map(mesInstock -> {
                MesStockVo vo = new MesStockVo();
                vo.setRemark(mesInstock.getFromtype());
                BigDecimal ng=BigDecimal.ZERO;
                try{
                    ng=mesInstock.getAllprice().divide(BigDecimal.valueOf(mesInstock.getCount()));
                }catch (Exception e){

                }
                vo.setPricein(ng);
                vo.setInstock(BigDecimal.valueOf(mesInstock.getCount()));
                vo.setCreatetime(mesInstock.getCreateTime());
                vo.setRemark2(mesInstock.getRemark());
                return vo;
            })
            .forEach(list::add);

        // 转换其他入库数据
            inrecList.stream()
            .map(mesInrec -> {
                MesStockVo vo = new MesStockVo();
                vo.setRemark(mesInrec.getFromtype());
                try{
                    MesCustomVo customVo=customMapper.selectVoById(mesInrec.getCustId());
                    vo.setNcname(customVo.getTitle());
                }catch (Exception e){

                }
                vo.setInstock(mesInrec.getCount());
                vo.setPricein(mesInrec.getNprice());
                vo.setRemark2(mesInrec.getRemark());
                vo.setCreatetime(mesInrec.getCreateTime());
                return vo;
            })
            .forEach(list::add);

        // 转换外协入库数据
            outorderList.stream()
            .map(mesOutorder -> {
                MesStockVo vo = new MesStockVo();
                vo.setRemark("委外入库");
                try{
                    MesCustomVo customVo=customMapper.selectVoById(mesOutorder.getCustid());
                    vo.setNcname(customVo.getTitle());
                }catch (Exception e){

                }

                vo.setRemark2(mesOutorder.getRemark());
                vo.setPricein(mesOutorder.getPrices());
                vo.setInstock(BigDecimal.valueOf(mesOutorder.getCounts()));
                vo.setCreatetime(mesOutorder.getCreateTime()); // 添加创建时间
                return vo;
            })
            .forEach(list::add);
        for (MesStockVo vo : list) {
            MesAllrec allrec = new MesAllrec();
            LambdaQueryWrapper<MesAllrec> allrecWrapper = new LambdaQueryWrapper<>();
            allrecWrapper.eq(MesAllrec::getStockid, id);
            allrecWrapper.eq(MesAllrec::getCreateTime, vo.getCreatetime());
            allrecWrapper.eq(MesAllrec::getCounts, vo.getInstock());
            allrecWrapper.eq(MesAllrec::getFromtype, vo.getRemark());
            List<MesAllrec> allrecList = allrecMapper.selectList(allrecWrapper);
            if(allrecList.isEmpty()){
                allrec.setPrices(vo.getPricein());
                allrec.setCounts(vo.getInstock());
                allrec.setCreateTime(vo.getCreatetime());
                allrec.setFromtype(vo.getRemark());
                allrec.setRemark(vo.getRemark2());
                allrec.setCname(vo.getNcname());
                allrec.setType("入库");
                allrec.setStockid(id);
                allrecMapper.insert(allrec);
            }
        }
        // 按创建时间排序
        list.sort(Comparator.comparing(MesStockVo::getCreatetime).reversed());


        return list;
    }
    @Override
    public List<MesStockVo> chukulist(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }

        List<MesStockVo> list = new ArrayList<>();

        // 其他出库信息
        LambdaQueryWrapper<MesOutstock> instockWrapper = new LambdaQueryWrapper<>();
        instockWrapper.eq(MesOutstock::getProId, id)
            .eq(MesOutstock::getStatus, "1");
        List<MesOutstock> outstockList = outstockMapper.selectList(instockWrapper);

        // 委外出库
        LambdaQueryWrapper<MesOurec> inrecWrapper = new LambdaQueryWrapper<>();
        inrecWrapper.eq(MesOurec::getStockid, id)
            .eq(MesOurec::getStatus, "1");
        List<MesOurec> ourecList = ourecMapper.selectList(inrecWrapper);

        // 外协出库
        LambdaQueryWrapper<MesOutorderDe> outorderWrapper = new LambdaQueryWrapper<>();
        outorderWrapper.eq(MesOutorderDe::getStockid, id); // 添加状态判断
        List<MesOutorderDe> outorderList = outorderDeMapper.selectList(outorderWrapper);

        // 销售出库
        LambdaQueryWrapper<MesSaleout> salewra = new LambdaQueryWrapper<>();
        salewra.eq(MesSaleout::getStockid, id); // 添加状态判断
        List<MesSaleout> saleouts = saleoutMapper.selectList(salewra);

        // 领料出库
        outstockList.stream()
            .map(mesInstock -> {
                MesStockVo vo = new MesStockVo();
                vo.setRemark(mesInstock.getFromtype());
                vo.setRemark2(mesInstock.getRemark());
                vo.setPricein(BigDecimal.ZERO);
                vo.setInstock((mesInstock.getCount()));
                vo.setCreatetime(mesInstock.getCreateTime());
                return vo;
            })
            .forEach(list::add);

        // 转换其他入库数据
        ourecList.stream()
            .map(mesInrec -> {
                MesStockVo vo = new MesStockVo();
                vo.setRemark("其他出库");
                vo.setInstock(mesInrec.getCount());
                vo.setRemark2(mesInrec.getRemark());
                vo.setPricein(BigDecimal.ZERO);
                vo.setCreatetime(mesInrec.getCreateTime());
                return vo;
            })
            .forEach(list::add);

        // 转换外协入库数据
        outorderList.stream()
            .map(mesOutorder -> {
                MesStockVo vo = new MesStockVo();
                vo.setRemark("委外出库");
                vo.setRemark2(mesOutorder.getRemark());
                BigDecimal tnv=BigDecimal.ZERO;
                try{
                    tnv=mesOutorder.getAllprice().divide(BigDecimal.valueOf(mesOutorder.getCounts()));
                }catch (Exception e){

                }
                vo.setPricein(tnv);
                vo.setInstock(BigDecimal.valueOf(mesOutorder.getCounts()));
                vo.setCreatetime(mesOutorder.getCreateTime()); // 添加创建时间
                return vo;
            })
            .forEach(list::add);
        // 转换外协入库数据
        saleouts.stream()
            .map(mesSaleout -> {
                MesStockVo vo = new MesStockVo();
                vo.setRemark("销售出库");
                vo.setRemark2(mesSaleout.getRemark());
                BigDecimal tnv=BigDecimal.ZERO;
                vo.setPricein(tnv);
                vo.setInstock(BigDecimal.valueOf(mesSaleout.getCounts()));
                vo.setCreatetime(mesSaleout.getCreateTime()); // 添加创建时间
                return vo;
            })
            .forEach(list::add);
        for (MesStockVo vo : list) {
            MesAllrec allrec = new MesAllrec();
            LambdaQueryWrapper<MesAllrec> allrecWrapper = new LambdaQueryWrapper<>();
            allrecWrapper.eq(MesAllrec::getStockid, id);
            allrecWrapper.eq(MesAllrec::getCreateTime, vo.getCreatetime());
            allrecWrapper.eq(MesAllrec::getCounts, vo.getInstock());
            allrecWrapper.eq(MesAllrec::getFromtype, vo.getRemark());
            List<MesAllrec> allrecList = allrecMapper.selectList(allrecWrapper);
            if(allrecList.isEmpty()){
                allrec.setPrices(vo.getPricein());
                allrec.setCounts(vo.getInstock());
                allrec.setCreateTime(vo.getCreatetime());
                allrec.setFromtype(vo.getRemark());
                allrec.setRemark(vo.getRemark2());
                allrec.setType("出库");
                allrec.setStockid(id);
                allrecMapper.insert(allrec);
            }
        }
        // 按创建时间排序
        list.sort(Comparator.comparing(MesStockVo::getCreatetime).reversed());

        return list;
    }
    /**
     * 查询符合条件的仓库信息列表
     *
     * @param bo 查询条件
     * @return 仓库信息列表
     */
    @Override
    public List<MesStockVo> queryList(MesStockBo bo) {
        LambdaQueryWrapper<MesStock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesStock> buildQueryWrapper(MesStockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesStock> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTilte()), MesStock::getTilte, bo.getTilte());
        lqw.eq(bo.getWarnnum() != null, MesStock::getWarnnum, bo.getWarnnum());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), MesStock::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getIid()), MesStock::getIid, bo.getIid());
        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesStock::getSn, bo.getSn());
        lqw.like(StringUtils.isNotBlank(bo.getGk()), MesStock::getGk, bo.getGk());
        lqw.eq(bo.getStockid() != null, MesStock::getStockid, bo.getStockid());
        lqw.eq(bo.getInstock() != null, MesStock::getInstock, bo.getInstock());
        lqw.eq(bo.getOutstock()!=null, MesStock::getOutstock, bo.getOutstock());
        lqw.eq(bo.getNowstock() != null, MesStock::getNowstock, bo.getNowstock());
        lqw.eq(bo.getUnitid() != null, MesStock::getUnitid, bo.getUnitid());
        lqw.eq(bo.getSales() != null, MesStock::getSales, bo.getSales());
        return lqw;
    }

    /**
     * 新增仓库信息
     *
     * @param bo 仓库信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesStockBo bo) {
        MesStock add = MapstructUtils.convert(bo, MesStock.class);
        validEntityBeforeSave(add);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            MesInventoryLedger inventoryLedger=new MesInventoryLedger();
            inventoryLedger.setStockid(add.getId());
            inventoryLedger.setLocationId(0L);
            inventoryLedger.setQuantity(add.getNowstock().intValue());
            inventoryLedger.setOperation(1);
            inventoryLedger.setRemark(add.getRemark());
            inventoryLedgerMapper.insert(inventoryLedger);
        }
        return flag;
    }

    /**
     * 修改仓库信息
     *
     * @param bo 仓库信息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesStockBo bo) {
        MesStock update = MapstructUtils.convert(bo, MesStock.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesStock entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除仓库信息信息
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
