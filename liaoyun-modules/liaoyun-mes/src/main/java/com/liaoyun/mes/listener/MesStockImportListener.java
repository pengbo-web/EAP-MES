package com.liaoyun.mes.listener;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.utils.SpringUtils;
import com.liaoyun.common.excel.core.ExcelListener;
import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.domain.MesCustom;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.MesStockType;
import com.liaoyun.mes.domain.MesUnitmeasure;
import com.liaoyun.mes.domain.vo.MesStockTypeVo;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;
import com.liaoyun.mes.mapper.MesCustomMapper;
import com.liaoyun.mes.mapper.MesStockMapper;
import com.liaoyun.mes.mapper.MesStockTypeMapper;
import com.liaoyun.mes.mapper.MesUnitmeasureMapper;
import com.liaoyun.mes.service.IMesStockTypeService;

import java.util.ArrayList;
import java.util.List;

public class MesStockImportListener extends AnalysisEventListener<MesStockVo> implements ExcelListener<MesStockVo> {
    private final MesStockTypeMapper mesStockTypeMapper;
    private final MesStockMapper stockMapper;
    private final Boolean isUpdateSupport;
    private final MesUnitmeasureMapper unitmeasureMapper;
    private final MesCustomMapper mesCustomMapper;
    public MesStockImportListener(Boolean isUpdateSupport) {
//        super();
        this.mesStockTypeMapper = SpringUtils.getBean(MesStockTypeMapper.class);
        this.unitmeasureMapper=SpringUtils.getBean(MesUnitmeasureMapper.class);
        this.stockMapper=SpringUtils.getBean(MesStockMapper.class);
        this.mesCustomMapper=SpringUtils.getBean(MesCustomMapper.class);
        this.isUpdateSupport = isUpdateSupport;

    }
    @Override
    public ExcelResult<MesStockVo> getExcelResult() {
        return null;
    }

    @Override
    public void invoke(MesStockVo mesStockVo, AnalysisContext analysisContext) {
        // 先校验必填
        MesStock boinsert = (MesStock) BeanMapHelper.Bean2Bean(mesStockVo, MesStock.class);
        LambdaQueryWrapper<MesStockType> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(MesStockType::getSn, mesStockVo.getStockname());
        try {
            //库位ID
            List<MesStockTypeVo> list=mesStockTypeMapper.selectVoList(wrapper);
            try{
                if(list.size()>0){
                    MesStockTypeVo stockType=list.get(0);
                    boinsert.setStockid(stockType.getId());
                }else{
                    boinsert.setRemark(mesStockVo.getStockname());
                }
            }catch (Exception e){

            }
            boinsert.setStatus("1");
            LambdaQueryWrapper<MesUnitmeasure> unitwrapper=new LambdaQueryWrapper<>();
            unitwrapper.eq(MesUnitmeasure::getTitle,mesStockVo.getUnitname());
            List<MesUnitmeasureVo> list1=unitmeasureMapper.selectVoList(unitwrapper);
            if(list1.size()>0){
                MesUnitmeasureVo unitmeasure=list1.get(0);
                boinsert.setUnitid(unitmeasure.getId());
            }
            if(null!=mesStockVo.getCustname()){
                try {
                    String[] numberArray = mesStockVo.getCustname().split("[,，、]");
                    List<String> ids=new ArrayList<>();
                    for(int i=0;i<numberArray.length;i++){
                        LambdaQueryWrapper<MesCustom> custwrapper=new LambdaQueryWrapper<>();
                        custwrapper.eq(MesCustom::getTitle,numberArray[i]);
                        MesCustom cust=mesCustomMapper.selectOne(custwrapper);
                        if(null!=cust){
                            ids.add(cust.getId().toString());
                        }else{
                            MesCustom custom=new MesCustom();
                            custom.setTitle(numberArray[i]);
                            mesCustomMapper.insert(custom);
                            ids.add(custom.getId().toString());
                        }
                    }
                    boinsert.setCustid(String.join(",",ids));
                }catch (Exception e){

                }


            }
            if(isUpdateSupport){
                LambdaQueryWrapper<MesStock> mesStockLambdaQueryWrapper=new LambdaQueryWrapper<>();
                mesStockLambdaQueryWrapper.eq(MesStock::getSn,boinsert.getSn());
                List<MesStock> stocks=stockMapper.selectList(mesStockLambdaQueryWrapper);
                if(stocks.size()>0){
                    boinsert.setId(stocks.get(0).getId());
                    stockMapper.updateById(boinsert);
                }
            }else{
                stockMapper.insert(boinsert);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
