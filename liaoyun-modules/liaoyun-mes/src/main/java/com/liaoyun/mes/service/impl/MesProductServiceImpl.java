package com.liaoyun.mes.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.controller.UnitConverter;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.bo.MesInstockBo;
import com.liaoyun.mes.domain.bo.MesTaskBo;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesProductBo;
import com.liaoyun.mes.service.IMesProductService;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * ③产品管理Service业务层处理
 *
 * @author Kylin W
 * @date 2024-05-11
 */
@RequiredArgsConstructor
@Service
public class MesProductServiceImpl implements IMesProductService {

    private final MesProductMapper baseMapper;
    private final MesProtypeMapper typeMapper;
    private final MesUnitmeasureMapper unitmeasureMapper;
    private final MesTaskMapper taskMapper;
    private final MesStockMapper stockMapper;
    private final MesWorkMapper workMapper;
    private final MesOutstockMapper outstockMapper;

    /**
     * 查询③产品管理
     */
    @Override
    public MesProductVo queryById(Long id) {
        MesProductVo vo = baseMapper.selectVoById(id);
        if (null != vo) {
            vo.setTypename("顶级");
            if (ObjectUtil.isNotNull(vo.getProtypeId()) && vo.getProtypeId() != 0L) {
                MesProtypeVo up = typeMapper.selectVoById(vo.getProtypeId());
                vo.setTypename(up.getTitle());
            }
            vo.setPidname("顶级");
            if (ObjectUtil.isNotNull(vo.getPid()) && vo.getPid() != 0L) {
                try {
                    MesProductVo p = baseMapper.selectVoById(vo.getPid());
                    vo.setPidname(p.getTitle());
                } catch (Exception e) {
                    System.out.println(vo);
                }
            }
            vo.setUnit("顶级");
            if (null != vo) {
                if (ObjectUtil.isNotNull(vo.getUnitId()) && vo.getUnitId() != 0L) {
                    try {
                        MesUnitmeasureVo p = unitmeasureMapper.selectVoById(vo.getUnitId());
                        vo.setUnit(p.getTitle());
                    } catch (Exception e) {
                        System.out.println(vo);
                    }


                }
                if (ObjectUtil.isNotNull(vo.getStockId())) {
                    MesStockVo p = stockMapper.selectVoById(vo.getStocks());
                    if (null != p) {
                        vo.setStockname(p.getTilte());
                    }

                }
            }
        }


        return vo;
    }

    /**
     * 分页查询入库信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 入库信息分页列表
     */
    @Override
    public TableDataInfo<MesProductVo> queryPageList(MesProductBo bo, PageQuery pageQuery) {
        if (ObjectUtil.isNotNull(bo.getProtypeId())) {
            if (bo.getProtypeId() == 0L) {
                bo.setProtypeId(null);
            }
        }
        LambdaQueryWrapper<MesProduct> lqw = buildQueryWrapper(bo);
        if (null == (bo.getTitle()) && null == (bo.getSn())) {
            lqw.eq(MesProduct::getPid, 0L);
        }
        Page<MesProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesProductVo vo : result.getRecords()) {
            vo.setTypename("顶级");
            if (ObjectUtil.isNotNull(vo.getProtypeId()) && vo.getProtypeId() != 0L) {
                MesProtypeVo up = typeMapper.selectVoById(vo.getProtypeId());
                vo.setTypename(up.getTitle());
            }
            vo.setPidname("顶级");
            if (ObjectUtil.isNotNull(vo.getPid()) && vo.getPid() != 0L) {
                MesProductVo p = baseMapper.selectVoById(vo.getPid());
                vo.setPidname(p.getTitle());
            }
            vo.setUnit("顶级");
            if (ObjectUtil.isNotNull(vo.getUnitId()) && vo.getUnitId() != 0L) {
                MesUnitmeasureVo p = unitmeasureMapper.selectVoById(vo.getUnitId());
                vo.setUnit(p.getTitle());
            }
            if (ObjectUtil.isNotNull(vo.getStockId())) {
                MesUnitmeasureVo p = unitmeasureMapper.selectVoById(vo.getStockId());
                vo.setStockname(p.getTitle());
            }
            List<MesProductVo> allChildren = new ArrayList<>();
            List<MesProductVo> result1 = getAllChild2(vo.getId(), allChildren);
            vo.setDowncount(result1.size());
        }
        return TableDataInfo.build(result);
    }

    @Override
    public List<MesProductVo> getboml(Long bo) {
        // 创建一个空的列表用于存储所有子节点信息
        List<MesProductVo> allChildren = new ArrayList<>();

// 调用递归方法，传入根节点的 pid 和空的列表
        List<MesProductVo> result = getAllChild2(bo, allChildren);
        for (MesProductVo vo : result) {
            if (ObjectUtil.isNotNull(vo.getProtypeId()) && vo.getProtypeId() != 0L) {
                MesProtypeVo up = typeMapper.selectVoById(vo.getProtypeId());
                vo.setTypename(up.getTitle());
            }
            if (ObjectUtil.isNotNull(vo.getPid()) && vo.getPid() != 0L) {
                MesProductVo p = baseMapper.selectVoById(vo.getPid());
                vo.setPidname(p.getTitle());
            }
            if (ObjectUtil.isNotNull(vo.getUnitId()) && vo.getUnitId() != 0L) {
                MesUnitmeasureVo p = unitmeasureMapper.selectVoById(vo.getUnitId());
                vo.setUnit(p.getTitle());
            }
            if (ObjectUtil.isNotNull(vo.getStockId())) {
                MesUnitmeasureVo p = unitmeasureMapper.selectVoById(vo.getStockId());
                vo.setStockname(p.getTitle());
            }
        }
        return result;
    }

    /**
     * 查询③产品管理列表
     */
    @Override
    public List<MesProductVo> queryList(MesProductBo bo) {
        //再次排查下上级
        if (ObjectUtil.isNotNull(bo.getProtypeId())) {
            if (bo.getProtypeId() == 0L) {
                bo.setProtypeId(null);
            }
        }
        LambdaQueryWrapper<MesProduct> lqw = buildQueryWrapper(bo);
        List<MesProductVo> list = baseMapper.selectVoList(lqw);
        for (MesProductVo vo : list) {
            vo.setTypename("顶级");
            if (ObjectUtil.isNotNull(vo.getProtypeId()) && vo.getProtypeId() != 0L) {
                MesProtypeVo up = typeMapper.selectVoById(vo.getProtypeId());
                vo.setTypename(up.getTitle());
            }
            vo.setPidname("顶级");
            if (ObjectUtil.isNotNull(vo.getPid()) && vo.getPid() != 0L) {
                try {
                    MesProductVo p = baseMapper.selectVoById(vo.getPid());
                    vo.setPidname(p.getTitle());
                } catch (Exception e) {
                    System.out.println(vo);
                }
            }
            vo.setUnit("顶级");
            if (ObjectUtil.isNotNull(vo.getUnitId()) && vo.getUnitId() != 0L) {
                try {
                    MesUnitmeasureVo p = unitmeasureMapper.selectVoById(vo.getUnitId());
                    vo.setUnit(p.getTitle());
                } catch (Exception e) {
                    System.out.println(vo);
                }
            }
            if (ObjectUtil.isNotNull(vo.getStockId())) {
                MesUnitmeasureVo p = unitmeasureMapper.selectVoById(vo.getStockId());
                vo.setStockname(p.getTitle());
            }
        }
        return list;
    }

    @Override
    public List<MesStockVo> getbom(Long bo) {
        MesWork work = workMapper.selectById(bo);
//        MesProductVo productVo=baseMapper.selectVoById(work.getProId());
        LambdaQueryWrapper<MesProduct> wrapper = new LambdaQueryWrapper<>();
        List<MesUnitmeasure> lists = unitmeasureMapper.selectList();
        wrapper.eq(MesProduct::getPid, work.getProId());
        List<MesProduct> list = baseMapper.selectList(wrapper);
        List<MesStockVo> vos = new ArrayList<>();
        for (MesProduct mesProduct : list) {
            MesStockVo vo = new MesStockVo();
            vo = stockMapper.selectVoById(mesProduct.getStocks());
            float hh = 0;
            MesUnitmeasure so = unitmeasureMapper.selectById(mesProduct.getUnitId());
            MesUnitmeasure tar = unitmeasureMapper.selectById(vo.getUnitid());
            hh = mesProduct.getNeedCount() * work.getNeednum() * mesProduct.getWeight();
            BigDecimal jinlu3 = UnitConverter.convertUnit(so.getTitle(), tar.getTitle(), new BigDecimal(hh), lists);
            if (vo.getType().equals("色粉")) {
                vo.setNcout(jinlu3.floatValue() / 25);
            } else {
                vo.setNcout(jinlu3.floatValue());
            }
            LambdaQueryWrapper<MesOutstock> owr = new LambdaQueryWrapper<>();
            owr.eq(MesOutstock::getRemark1, work.getOrderid());
            owr.eq(MesOutstock::getProId, vo.getId());
            owr.eq(MesOutstock::getStatus, '1');
            List<MesOutstock> list1 = outstockMapper.selectList(owr);
            float op = 0;
            if (ObjectUtil.isNotNull(list1) && list1.size() > 0) {
                for (MesOutstock mesOutstock : list1) {
                    op += mesOutstock.getCount().floatValue();
                }
            }
            vo.setCkcount(op);
            vos.add(vo);
        }
        return vos;
    }

    private LambdaQueryWrapper<MesProduct> buildQueryWrapper(MesProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesProduct> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesProduct::getSn, bo.getSn());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesProduct::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getColors()), MesProduct::getColors, bo.getColors());
        lqw.eq(bo.getProtypeId() != null, MesProduct::getProtypeId, bo.getProtypeId());
        lqw.eq(bo.getNeedCount() != null, MesProduct::getNeedCount, bo.getNeedCount());
        lqw.eq(bo.getMustbe() != null, MesProduct::getMustbe, bo.getMustbe());
        lqw.eq(bo.getPid() != null, MesProduct::getPid, bo.getPid());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesProduct::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增③产品管理
     */
    @Override
    public Boolean insertByBo(MesProductBo bo) {
        MesProduct add = MapstructUtils.convert(bo, MesProduct.class);
        validEntityBeforeSave(add);
        //添加库存
        LambdaQueryWrapper<MesStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesStock::getIid, add.getSn());
        List<MesStock> stockList = stockMapper.selectList(wrapper);
        if (stockList.size() > 0) {
            MesStock stock = stockList.get(0);
            add.setStocks(stock.getId());
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean insertsByBo(MesProductBo bo, List<MesProductBo> list) {
        LambdaQueryWrapper<MesProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesProduct::getStocks, bo.getId());
        List<MesProduct> productList = baseMapper.selectList(wrapper);
        MesProduct add = new MesProduct();
        if (productList.size() > 0) {
            add = productList.get(0);
            add.setStime(bo.getStime());
            add.setPrices(bo.getPrices());
            baseMapper.updateById(add);
        } else {
            //半成品
            if(bo.getLs()==1){
                MesProduct mesProduct=baseMapper.selectById(bo.getId());
                mesProduct.setId(null);
                mesProduct.setPid(add.getId());
                baseMapper.insert(mesProduct);
            }else{
                add.setSn(bo.getSn());
                add.setTitle(bo.getTilte());
                add.setUnitId(bo.getUnitId());
                add.setStime(bo.getStime());
                add.setPrices(bo.getPrices());
                add.setWeight(bo.getWeight());
                try {
                    LambdaQueryWrapper<MesProtype> wrapper1 = new LambdaQueryWrapper<>();
                    wrapper1.eq(MesProtype::getTitle, bo.getOtype());
                    List<MesProtype> protypes = typeMapper.selectList(wrapper1);
                    if (protypes.size() > 0) {
                        MesProtype protype = protypes.get(0);
                        add.setProtypeId(protype.getId());
                    }
                } catch (Exception e) {
                    add.setProtypeId(null);
                }
                add.setNeedCount(1);
                add.setStockId(bo.getUnits());
                add.setStocks(add.getId());
                add.setId(null);
                baseMapper.insert(add);

            }
        }
//        LambdaQueryWrapper<MesProduct> wrapper2 = new LambdaQueryWrapper<>();
//        wrapper2.eq(MesProduct::getPid, add.getId());
//        List<MesProduct> productList2 = baseMapper.selectList(wrapper2);
//        if (productList2.size() > 0) {
//            for (MesProduct product : productList2) {
//
//                baseMapper.deleteById(product.getId());
//            }
//        }
        MesProduct add2 = new MesProduct();
        for (MesProductBo vo : list) {
            add2.setSn(vo.getSn());
            add2.setTitle(vo.getTilte());
            add2.setUnitId(vo.getUnitId());
            add2.setStime(vo.getStime());
            add2.setPrices(vo.getPrices());
            add2.setWeight(vo.getWeight());
            try {
                LambdaQueryWrapper<MesProtype> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(MesProtype::getTitle, vo.getOtype());
                List<MesProtype> protypes = typeMapper.selectList(wrapper1);
                if (protypes.size() > 0) {
                    MesProtype protype = protypes.get(0);
                    add2.setProtypeId(protype.getId());
                }
            } catch (Exception e) {
                add2.setProtypeId(null);
            }
            add2.setNeedCount(vo.getNeedCount());
            add2.setStockId(vo.getUnits());
            add2.setStocks(add.getId());
            add2.setPid(add.getId());
            add2.setId(null);
            baseMapper.insert(add2);
        }
        return true;
    }

    /**
     * 修改③产品管理
     */
    @Override
    public Boolean updateByBo(MesProductBo bo) {
        MesProduct update = MapstructUtils.convert(bo, MesProduct.class);
        validEntityBeforeSave(update);
        LambdaQueryWrapper<MesStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesStock::getSn, update.getSn());
        List<MesStock> stockList = stockMapper.selectList(wrapper);
        if (stockList.size() > 0) {
            MesStock stock = stockList.get(0);
            update.setStocks(stock.getId());
        }
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesProduct entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除③产品管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<MesProductVo> getallstock(Long id, String type) {
        MesTaskVo taskVo = taskMapper.selectVoById(id);

        List<MesProductVo> list = new ArrayList<>();
        Set<Long> processedIds = new HashSet<>();
        //剔除，暂时不用递归查询。只查询下级
//        list=getAllChild(taskVo.getProId(),taskVo.getNeedcount().intValue(),list,type,processedIds);
        LambdaQueryWrapper<MesProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesProduct::getPid, taskVo.getProId());
        list = baseMapper.selectVoList(wrapper);
        for (MesProductVo vo : list) {
            if (null != vo.getStocks()) {
                MesStock stock = stockMapper.selectById(vo.getStocks());
                vo.setUnitname(baseMapper.selectTypeTitleById(vo.getUnitId()));
                vo.setStockname(baseMapper.selectTypeTitleById(stock.getUnitid()));
                vo.setEndstock(BigDecimal.valueOf(vo.getStock().intValue() * vo.getGrow()));

                vo.setId(stock.getId());
            } else {
                vo.setUnitname(baseMapper.selectTypeTitleById(vo.getUnitId()));
                vo.setStockname(baseMapper.selectTypeTitleById(vo.getStockId()));
                vo.setEndstock(BigDecimal.valueOf(vo.getStock().intValue() * vo.getGrow()));
            }
            vo.setNeedCount(taskVo.getNeedcount().intValue()*vo.getNeedCount());
        }
        if (ObjectUtil.isNull(taskVo.getStocknum())) {
            MesTask update = MapstructUtils.convert(taskVo, MesTask.class);
            update.setStocknum(list.size());
            taskMapper.updateById(update);
        }

        return list;
    }

    /**
     * 递归查询
     *
     * @param pid
     * @param childIds
     * @return
     */
    public List<MesProductVo> getAllChild2(Long pid, List<MesProductVo> childIds) {
        // 查询当前节点信息
        MesProductVo currentNode = baseMapper.selectVoById(pid);
        if (currentNode == null) {
            return childIds;
        }

        // 将当前节点添加到结果列表中
        childIds.add(currentNode);

        // 查询当前节点的子节点
        LambdaQueryWrapper<MesProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesProduct::getPid, pid);
        List<MesProductVo> children = baseMapper.selectVoList(wrapper);

        // 递归处理子节点
        for (MesProductVo child : children) {
            getAllChild2(child.getId(), childIds);
        }

        return childIds;
    }

    /**
     * 递归查询
     *
     * @param pid
     * @param childIds
     * @return
     */

    public List<MesProductVo> getAllChild(Long pid, int count, List<MesProductVo> childIds, String type, Set<Long> processedIds) {
        // 判断当前节点是否已经处理过
        if (!processedIds.contains(pid)) {
            MesProductVo tvo = baseMapper.selectVoById(pid);
            tvo.setNeedCount(count);
            childIds.add(tvo);
            processedIds.add(pid);

            LambdaQueryWrapper<MesProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MesProduct::getPid, pid);
            List<MesProductVo> list = baseMapper.selectVoList(wrapper);
            if (ObjectUtils.isNotEmpty(list)) {
                for (MesProductVo vo : list) {
                    if (vo.getMustbe().equals(type)) {
                        BigDecimal childCount = BigDecimal.valueOf(count).multiply(BigDecimal.valueOf(vo.getNeedCount()));

                        getAllChild(vo.getId(), childCount.intValue(), childIds, type, processedIds);
                    }
                }
            }
        }

        return childIds;
    }

    private boolean containsId(List<Map<String, BigDecimal>> childIds, Long id) {
        for (Map<String, BigDecimal> childMap : childIds) {
            for (String key : childMap.keySet()) {
                if (key.equals(id.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
