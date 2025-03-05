package com.liaoyun.mes.listener;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liaoyun.common.core.exception.ServiceException;
import com.liaoyun.common.core.utils.SpringUtils;
import com.liaoyun.common.core.utils.ValidatorUtils;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import com.liaoyun.common.excel.core.ExcelListener;
import com.liaoyun.common.excel.core.ExcelResult;
import com.liaoyun.mes.domain.MesProtype;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.bo.MesProductBo;
import com.liaoyun.mes.domain.bo.MesProtypeBo;
import com.liaoyun.mes.domain.bo.MesUnitmeasureBo;
import com.liaoyun.mes.domain.vo.MesProductVo;
import com.liaoyun.mes.domain.vo.MesProtypeVo;
import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;
import com.liaoyun.mes.mapper.MesStockMapper;
import com.liaoyun.mes.service.IMesProductService;
import com.liaoyun.mes.service.IMesProtypeService;
import com.liaoyun.mes.service.IMesUnitmeasureService;
import com.liaoyun.system.domain.vo.SysUserImportVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MesProcuctImportListener extends AnalysisEventListener<MesProductVo> implements ExcelListener<MesProductVo> {
        private final IMesProtypeService protypeService;
    private final IMesUnitmeasureService unitService;
    private final IMesProductService productService;
    private final Boolean isUpdateSupport;
    private final MesStockMapper mesStockMapper;
    private final StringBuilder successMsg = new StringBuilder();
    public MesProcuctImportListener(Boolean isUpdateSupport) {
//        super();
        this.protypeService = SpringUtils.getBean(IMesProtypeService.class);
        this.unitService = SpringUtils.getBean(IMesUnitmeasureService.class);
        this.productService = SpringUtils.getBean(IMesProductService.class);
        this.mesStockMapper=SpringUtils.getBean(MesStockMapper.class);
        this.isUpdateSupport = isUpdateSupport;
    }

    @Override
    public void invoke(MesProductVo mesProductVo, AnalysisContext analysisContext) {
        // 先校验必填
//        ValidatorUtils.validate(mesProductVo, AddGroup.class);

        MesProductBo productBo=new MesProductBo();
        MesProtypeBo protypeBo=new MesProtypeBo();
        MesStock mesStock=new MesStock();
        //类型
        protypeBo.setTitle(mesProductVo.getProtype().trim());
        try {

        }catch (Exception e){

        }
        MesProtypeVo protypeVo=protypeService.queryList(protypeBo).get(0);
        MesUnitmeasureBo unitBo=new MesUnitmeasureBo();
        //单位
        unitBo.setTitle(mesProductVo.getUnit());

        MesUnitmeasureVo unitVo=unitService.queryList(unitBo).get(0);
        MesUnitmeasureBo unitBo1=new MesUnitmeasureBo();
        unitBo1.setTitle(mesProductVo.getStockname());
        MesUnitmeasureVo unitVo1=unitService.queryList(unitBo1).get(0);
        productBo.setPid(0L);
        if(ObjectUtil.isNotNull(mesProductVo.getPidname())){
            if (!mesProductVo.getPidname().equals("无")){
                MesProductBo prosearch=new MesProductBo();
                prosearch.setTitle(mesProductVo.getPidname().trim());
                MesProductVo prosearchVo=productService.queryList(prosearch).get(0);
                productBo.setPid(prosearchVo.getId());
            }
        }
        //添加库存
        LambdaQueryWrapper<MesStock> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(MesStock::getSn,mesProductVo.getSn().trim());
        List<MesStock> stockList=mesStockMapper.selectList(wrapper);
        if(stockList.size()>0){
            MesStock stock=stockList.get(0);
            productBo.setStocks(stock.getId());
        }
        productBo.setTitle(mesProductVo.getTitle().trim());
        productBo.setUnitId(unitVo.getId());
        productBo.setStockId(unitVo1.getId());
        productBo.setProtypeId(protypeVo.getId());
        productBo.setSn(mesProductVo.getSn());
        productBo.setBcount(mesProductVo.getBcount());
        productBo.setStock(mesProductVo.getStock());
        productBo.setWeight(mesProductVo.getWeight());
        productBo.setVer(1);
        productBo.setNeedCount(mesProductVo.getNeedCount());

        try{
            if(isUpdateSupport){
                MesProductBo updatepro=new MesProductBo();
                updatepro.setTitle(mesProductVo.getTitle().trim());
                updatepro.setSn(mesProductVo.getSn().trim());
                updatepro.setVer(mesProductVo.getVer());
                MesProductVo prosearchVo=productService.queryList(updatepro).get(0);
                if(ObjectUtil.isNotNull(prosearchVo)){
                    productBo.setId(prosearchVo.getId());
                    productService.updateByBo(productBo);
                }else {
                    productService.insertByBo(productBo);
                }
            }else{
                productService.insertByBo(productBo);
            }
            // 处理完毕以后判断是否符合规则
            ValidatorUtils.validate(mesProductVo, EditGroup.class);

            // 添加到处理结果中
            getExcelResult().getList().add(mesProductVo);
        }catch (Exception e){
            System.out.println(e);
        }
        successMsg.append("<br/>").append(" 导入成功");
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
    @Override
    public ExcelResult<MesProductVo> getExcelResult() {
        return new ExcelResult<>() {

            @Override
            public String getAnalysis() {

                return successMsg.toString();
            }

            @Override
            public List<MesProductVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }
}
