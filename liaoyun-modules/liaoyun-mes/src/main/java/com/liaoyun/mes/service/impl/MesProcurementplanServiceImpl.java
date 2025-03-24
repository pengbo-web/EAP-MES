package com.liaoyun.mes.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.bo.MesInstockBo;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesProcurementplanBo;
import com.liaoyun.mes.service.IMesProcurementplanService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 采购计划Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-22
 */
@RequiredArgsConstructor
@Service
public class MesProcurementplanServiceImpl implements IMesProcurementplanService {

    private final MesProcurementplanMapper baseMapper;
    private final MesProductMapper productMapper;
    private final MesWorkMapper workMapper;
    private final SysUserMapper sysUserMapper;
    private final MesUnitmeasureMapper unitmeasureMapper;
    private final MesInstockServiceImpl mesInstockService;
    private final MesStockMapper stockMapper;
    private final MesTaskMapper taskMapper;

    /**
     * 查询采购计划
     *
     * @param id 主键
     * @return 采购计划
     */
    @Override
    public MesProcurementplanVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询采购计划列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 采购计划分页列表
     */
    @Override
    public TableDataInfo<MesProcurementplanVo> queryPageList(MesProcurementplanBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesProcurementplan> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesProduct> wrappe=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(bo.getProname())){
            wrappe.like(MesProduct::getTitle,bo.getProname());
            List<MesProduct> stockList = productMapper.selectList(wrappe);
            if(stockList.size()>0){
                List<Long> ids=new ArrayList<>();
                for(MesProduct stock:stockList){
                    ids.add(stock.getId());
                }
                lqw.in(MesProcurementplan::getProId,ids);
            }
        }
        Page<MesProcurementplanVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesProcurementplanVo vo : result.getRecords()) {
            if(ObjectUtils.isNotNull(vo.getProId())){
                //暂时这样处理后续删除
                if(null!=vo.getWorkId()){
                    if(vo.getWorkId()==100){
                        LambdaQueryWrapper<MesProduct> wrapper=new LambdaQueryWrapper<>();
                        wrapper.eq(MesProduct::getStocks,vo.getProId());
                        MesProduct product=productMapper.selectList(wrapper).get(0);
                        vo.setProduct(product);
                        MesStockVo stockVo=stockMapper.selectVoById(vo.getProId());
                        vo.setStockVo(stockVo);
                        try{
                            vo.setMj(calculateAndPrintDimensions(stockVo.getGk()));
                        }catch (Exception e){

                        }

                    }else{
                        MesStockVo stockVo=stockMapper.selectVoById(vo.getProId());
                        if(null!=stockVo){
                            LambdaQueryWrapper<MesProduct> wrapper=new LambdaQueryWrapper<>();
                            wrapper.eq(MesProduct::getStocks,stockVo.getId());
                            MesProduct product=productMapper.selectList(wrapper).get(0);
                            vo.setProduct(product);
                            vo.setStockVo(stockVo);
                        }else{
                            MesProduct product=productMapper.selectById(vo.getProId());
                            vo.setProduct(product);
                            MesUnitmeasureVo p=unitmeasureMapper.selectVoById(product.getStockId());
                            vo.getProduct().setStockname(p.getTitle());
                            vo.setWorkVo(workMapper.selectVoById(vo.getWorkId()));
                        }

                    }

                }else{
                    MesStockVo stockVo=stockMapper.selectVoById(vo.getProId());
                    if(null!=stockVo){
                        LambdaQueryWrapper<MesProduct> wrapper=new LambdaQueryWrapper<>();
                        wrapper.eq(MesProduct::getStocks,stockVo.getId());
                        MesProduct product=productMapper.selectList(wrapper).get(0);
                        vo.setProduct(product);
                        vo.setStockVo(stockVo);
                    }else{
                        MesProduct product=productMapper.selectById(vo.getProId());
                        vo.setProduct(product);
                        MesUnitmeasureVo p=unitmeasureMapper.selectVoById(product.getStockId());
                        vo.getProduct().setStockname(p.getTitle());
                        vo.setWorkVo(workMapper.selectVoById(vo.getWorkId()));
                    }
                }
                }


            if(ObjectUtils.isNotNull(vo.getCreateBy())){
                vo.setUsername(sysUserMapper.selectById(vo.getCreateBy()).getNickName());
            }
        }
        return TableDataInfo.build(result);
    }
    /**
     * 解析字符串并计算体积
     *
     * @param input 输入的字符串（如 "67.5*51*25.5cm" 或 "12*28cm"）
     */
    public static double calculateAndPrintDimensions(String input) {
        // 提取后缀（如 "cm"）
        String suffix = input.replaceAll("[0-9.*]", ""); // 去掉数字和*，保留后缀
        String dimensionsStr = input.replaceAll("[^0-9.*]", ""); // 去掉非数字和*，保留长宽高

        // 分割长宽高
        String[] dimensions = dimensionsStr.split("\\*");
        if (dimensions.length < 2 || dimensions.length > 3) {
            throw new IllegalArgumentException("Invalid input format: " + input);
        }

        // 解析长宽高
        double length = Double.parseDouble(dimensions[0]);
        double width = Double.parseDouble(dimensions[1]);
        double height = dimensions.length == 3 ? Double.parseDouble(dimensions[2]) : 1.0; // 如果只有长和宽，高度默认为1

        // 计算体积
        double volume = length * width * height;

        return volume;
    }
    /**
     * 查询符合条件的采购计划列表
     *
     * @param bo 查询条件
     * @return 采购计划列表
     */
    @Override
    public List<MesProcurementplanVo> queryList(MesProcurementplanBo bo) {
        LambdaQueryWrapper<MesProcurementplan> lqw = buildQueryWrapper(bo);
        List<MesProcurementplanVo> list=baseMapper.selectVoList(lqw);
        for (MesProcurementplanVo vo : list) {
            if(ObjectUtils.isNotNull(vo.getProId())){
                MesStockVo stockVo=stockMapper.selectVoById(vo.getProId());
                if(null!=stockVo){
                    LambdaQueryWrapper<MesProduct> wrapper=new LambdaQueryWrapper<>();
                    wrapper.eq(MesProduct::getStocks,vo.getProId());
                    MesProduct product=productMapper.selectList(wrapper).get(0);
                    vo.setProduct(product);
                    MesUnitmeasureVo p=unitmeasureMapper.selectVoById(product.getUnitId());
                    vo.getProduct().setStockname(p.getTitle());
                }else{
                    vo.setProduct(productMapper.selectById(vo.getProId()));
                    MesUnitmeasureVo p=unitmeasureMapper.selectVoById(vo.getProduct().getStockId());
                    vo.getProduct().setStockname(p.getTitle());
                }

            }
            vo.setWorkVo(workMapper.selectVoById(vo.getWorkId()));
            if(ObjectUtils.isNotNull(vo.getCreateBy())){
                vo.setUsername(sysUserMapper.selectById(vo.getCreateBy()).getNickName());
            }
        }
        return list;
    }

    private LambdaQueryWrapper<MesProcurementplan> buildQueryWrapper(MesProcurementplanBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesProcurementplan> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesProcurementplan::getSn, bo.getSn());
        lqw.eq(bo.getProId() != null, MesProcurementplan::getProId, bo.getProId());
        lqw.eq(bo.getCount() != null, MesProcurementplan::getCount, bo.getCount());
        lqw.eq(bo.getWorkId() != null, MesProcurementplan::getWorkId, bo.getWorkId());

        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesProcurementplan::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getFromtype()), MesProcurementplan::getFromtype, bo.getFromtype());
        return lqw;
    }

    /**
     * 新增采购计划
     *
     * @param bo 采购计划
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesProcurementplanBo bo) {
        MesProcurementplan add = MapstructUtils.convert(bo, MesProcurementplan.class);
        validEntityBeforeSave(add);
        MesTask taskVo=taskMapper.selectById(bo.getTaskId());
        if(null!=taskVo){
            taskVo.setProcure(1);
            taskMapper.updateById(taskVo);
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改采购计划
     *
     * @param bo 采购计划
     * @return 是否修改成功
     */
    @Override
    @DSTransactional
    public Boolean updateByBo(MesProcurementplanBo bo) {
        MesProcurementplan update = MapstructUtils.convert(bo, MesProcurementplan.class);
        validEntityBeforeSave(update);
        try {
            MesStockVo stockVo=stockMapper.selectVoById(update.getProId());
            MesInstockBo bo1=new MesInstockBo();
            if(null!=stockVo){
                bo1.setProId(update.getProId());
            }else {
                MesProductVo productVo=productMapper.selectVoById(update.getProId());
                bo1.setProId(productVo.getStocks());
            }
            bo1.setCount(update.getCount());
            bo1.setFromtype("流转入库");
            mesInstockService.insertByBo(bo1,1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesProcurementplan entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除采购计划信息
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
