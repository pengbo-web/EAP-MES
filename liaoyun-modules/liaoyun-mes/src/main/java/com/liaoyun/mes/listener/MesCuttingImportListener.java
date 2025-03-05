package com.liaoyun.mes.listener;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.utils.SpringUtils;
import com.liaoyun.common.core.utils.ValidatorUtils;
import com.liaoyun.common.core.validate.EditGroup;
import com.liaoyun.common.excel.core.ExcelListener;
import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.domain.MesCutting;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.bo.MesCuttingBo;
import com.liaoyun.mes.domain.bo.MesProductBo;
import com.liaoyun.mes.domain.bo.MesProtypeBo;
import com.liaoyun.mes.domain.bo.MesUnitmeasureBo;
import com.liaoyun.mes.domain.vo.MesCuttingVo;
import com.liaoyun.mes.domain.vo.MesProductVo;
import com.liaoyun.mes.domain.vo.MesProtypeVo;
import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;
import com.liaoyun.mes.service.IMesCuttingService;
import com.liaoyun.mes.service.IMesProductService;
import com.liaoyun.mes.service.IMesProtypeService;
import com.liaoyun.mes.service.IMesUnitmeasureService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MesCuttingImportListener extends AnalysisEventListener<MesCuttingVo> implements ExcelListener<MesCuttingVo> {
    private final IMesCuttingService cuttingService;
    private final Boolean isUpdateSupport;
    public MesCuttingImportListener(Boolean isUpdateSupport) {
//        super();
        this.cuttingService = SpringUtils.getBean(IMesCuttingService.class);
        this.isUpdateSupport = isUpdateSupport;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        super.onException(exception, context);
    }

    @Override
    public void invoke(MesCuttingVo mesCuttingVo, AnalysisContext analysisContext) {
        MesCuttingBo boinsert = (MesCuttingBo) BeanMapHelper.Bean2Bean(mesCuttingVo, MesCuttingBo.class);

        try{


            if(isUpdateSupport){
                List<MesCuttingVo> lists=cuttingService.queryList(boinsert);
                if(ObjectUtil.isNotNull(lists)){
                    boinsert.setId(lists.get(0).getId());
                    cuttingService.updateByBo(boinsert);
                }else {
                    cuttingService.insertByBo(boinsert);
                }
            }else{
                cuttingService.insertByBo(boinsert);
            }
            // 处理完毕以后判断是否符合规则

            // 添加到处理结果中
            getExcelResult().getList().add(mesCuttingVo);
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println(mesCuttingVo.getSn());
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return super.hasNext(context);
    }

    @Override
    public ExcelResult<MesCuttingVo> getExcelResult() {
        return null;
    }
}
