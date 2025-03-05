package com.liaoyun.mes.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.liaoyun.common.core.utils.SpringUtils;
import com.liaoyun.common.excel.core.ExcelListener;
import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.MesTmodel;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.domain.vo.MesTmodelVo;
import com.liaoyun.mes.mapper.MesTmodelMapper;
import jodd.util.StringUtil;

public class MesTmodelImportListener extends AnalysisEventListener<MesTmodelVo> implements ExcelListener<MesTmodelVo> {
    private MesTmodelMapper mesTmodelMapper;

    public MesTmodelImportListener(Boolean isUpdateSupport) {
        this.mesTmodelMapper= SpringUtils.getBean(MesTmodelMapper.class);
    }
    @Override
    public ExcelResult<MesTmodelVo> getExcelResult() {
        return null;
    }

    @Override
    public void invoke(MesTmodelVo mesStockVo, AnalysisContext analysisContext) {
        // 先校验必填
        MesTmodel boinsert = (MesTmodel) BeanMapHelper.Bean2Bean(mesStockVo, MesTmodel.class);
        mesTmodelMapper.insert(boinsert);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
