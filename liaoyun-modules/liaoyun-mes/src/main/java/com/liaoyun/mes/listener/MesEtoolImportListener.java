package com.liaoyun.mes.listener;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.liaoyun.common.core.utils.SpringUtils;
import com.liaoyun.common.excel.core.ExcelListener;
import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.domain.bo.MesCuttingBo;
import com.liaoyun.mes.domain.bo.MesEtoolBo;
import com.liaoyun.mes.domain.vo.MesCuttingVo;
import com.liaoyun.mes.domain.vo.MesEtoolVo;
import com.liaoyun.mes.service.IMesCuttingService;
import com.liaoyun.mes.service.IMesEtoolService;

import java.util.List;

public class MesEtoolImportListener extends AnalysisEventListener<MesEtoolVo> implements ExcelListener<MesEtoolVo> {

    private final IMesEtoolService etoolService;
    private final Boolean isUpdateSupport;
    public MesEtoolImportListener(Boolean isUpdateSupport) {
//        super();
        this.etoolService = SpringUtils.getBean(IMesEtoolService.class);
        this.isUpdateSupport = isUpdateSupport;
    }
    @Override
    public ExcelResult<MesEtoolVo> getExcelResult() {
        return null;
    }

    @Override
    public void invoke(MesEtoolVo mesEtoolVo, AnalysisContext analysisContext) {
        MesEtoolBo boinsert = (MesEtoolBo) BeanMapHelper.Bean2Bean(mesEtoolVo, MesEtoolBo.class);

        try{


            if(isUpdateSupport){
                List<MesEtoolVo> lists=etoolService.queryList(boinsert);
                if(ObjectUtil.isNotNull(lists)){
                    boinsert.setId(lists.get(0).getId());
                    etoolService.updateByBo(boinsert);
                }else {
                    etoolService.insertByBo(boinsert);
                }
            }else{
                etoolService.insertByBo(boinsert);
            }
            // 处理完毕以后判断是否符合规则

            // 添加到处理结果中
            getExcelResult().getList().add(mesEtoolVo);
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println(mesEtoolVo.getSn());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
