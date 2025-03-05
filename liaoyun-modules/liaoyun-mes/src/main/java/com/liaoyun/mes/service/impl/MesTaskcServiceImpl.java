package com.liaoyun.mes.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.mes.controller.ArithHelper;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.domain.vo.SysUserVo;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesTaskcBo;
import com.liaoyun.mes.service.IMesTaskcService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 组装流转Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-10-16
 */
@RequiredArgsConstructor
@Service
public class MesTaskcServiceImpl implements IMesTaskcService {

    private final MesTaskcMapper baseMapper;
    private final MesProductMapper productMapper;
    private final MesTaskMapper taskMapper;
    private final MesOutRecMapper outRecMapper;
    private final MesInstockMapper instockMapper;
    private final MesWorkRecMapper workRecMapper;
    private final MesWorkMapper workMapper;

    /**
     * 查询组装流转
     *
     * @param id 主键
     * @return 组装流转
     */
    @Override
    public MesTaskcVo queryById(Long id){
        MesTaskcVo mesTaskc = baseMapper.selectVoById(id);
        mesTaskc.setWorkname(baseMapper.getdeptname(mesTaskc.getWorkplaceId()));
        mesTaskc.setUsername(baseMapper.getusername(mesTaskc.getUserId()));
        mesTaskc.setProname(baseMapper.gettitle(mesTaskc.getProId()));
        mesTaskc.setProductVo(productMapper.selectVoById(mesTaskc.getProId()));
        List<BigDecimal> bigDecimals=new ArrayList<>();
        try {
            LambdaQueryWrapper<MesProduct> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesProduct::getPid,mesTaskc.getProId());
            List<MesProduct> list = productMapper.selectList(wrapper);
            for (MesProduct product : list) {
                int a= (int) (mesTaskc.getNeedcount()*product.getNeedCount());
                product.setMc(a);

                List<MesTask> mesTasks= DataPermissionHelper.ignore(() -> {
                    LambdaQueryWrapper<MesTask> taskLambdaQueryWrapper=new LambdaQueryWrapper<>();
                    taskLambdaQueryWrapper.eq(MesTask::getSn,mesTaskc.getSn());
                    taskLambdaQueryWrapper.eq(MesTask::getProId,product.getId());
                    return taskMapper.selectList(taskLambdaQueryWrapper);
                });
                if(mesTasks.isEmpty()){
                    product.setJindu("无");
                }
                    product.setTasks(mesTasks);

                BigDecimal df =BigDecimal.ZERO;
                BigDecimal ad=new BigDecimal(a);
                BigDecimal t=BigDecimal.ZERO;
                try {
                    if(null!=product.getStock()){
                        t=product.getStock();
                    }
                }catch (Exception e){

                }

                df= ArithHelper.div(t,ad);
                bigDecimals.add(df);
                product.setShengc(df.toString());
            }
            BigDecimal min = Collections.min(bigDecimals);
            mesTaskc.setProductList(list);
            mesTaskc.setKc(min);
        }catch (Exception e){

        }
        return mesTaskc;
    }

    /**
     * 分页查询组装流转列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 组装流转分页列表
     */
    @Override
    public TableDataInfo<MesTaskcVo> queryPageList(MesTaskcBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesTaskc> lqw = buildQueryWrapper(bo);
        Page<MesTaskcVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesTaskcVo vo : result.getRecords()) {
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
            vo.setProname(baseMapper.gettitle(vo.getProId()));
            vo.setWorkname(baseMapper.getdeptname(vo.getWorkplaceId()));
        }
        return TableDataInfo.build(result);
    }
    @Override
    public TableDataInfo<MesTaskcVo> queryPagecList(MesTaskcBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesTaskc> lqw = buildQueryWrapper(bo);
        lqw.ne(MesTaskc::getStatus,"3");
        Page<MesTaskcVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesTaskcVo vo : result.getRecords()) {
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
            vo.setProname(baseMapper.gettitle(vo.getProId()));
            vo.setWorkname(baseMapper.getdeptname(vo.getWorkplaceId()));
            vo.setProductVo(productMapper.selectVoById(vo.getProId()));
            try {
                LambdaQueryWrapper<MesProduct> wrapper=new LambdaQueryWrapper<>();
                wrapper.eq(MesProduct::getPid,vo.getProId());
                List<MesProduct> list = productMapper.selectList(wrapper);
                for (MesProduct product : list) {
                   int a= (int) (vo.getNeedcount()*product.getNeedCount());
                   product.setMc(a);
                   LambdaQueryWrapper<MesTask> taskLambdaQueryWrapper=new LambdaQueryWrapper<>();
                   taskLambdaQueryWrapper.eq(MesTask::getSn,vo.getSn());
                   taskLambdaQueryWrapper.eq(MesTask::getProId,vo.getProId());
                   List<MesTask> mesTasks=taskMapper.selectList(taskLambdaQueryWrapper);
                   if(mesTasks.isEmpty()){
                       product.setJindu("无");
                   }else{
                       product.setTasks(mesTasks);
                   }
                    BigDecimal df = new BigDecimal("0.00");
                   BigDecimal ad=new BigDecimal(a);
                   df= ArithHelper.div(df,ad);
                   product.setShengc(df.toString());
                }
                vo.setProductList(list);
            }catch (Exception e){

            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的组装流转列表
     *
     * @param bo 查询条件
     * @return 组装流转列表
     */
    @Override
    public List<MesTaskcVo> queryList(MesTaskcBo bo) {
        LambdaQueryWrapper<MesTaskc> lqw = buildQueryWrapper(bo);
        List<MesTaskcVo> list = baseMapper.selectVoList(lqw);
        for (MesTaskcVo vo : list) {
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
            vo.setProname(baseMapper.gettitle(vo.getProId()));
            vo.setWorkname(baseMapper.getdeptname(vo.getWorkplaceId()));
        }
        return list;
    }

    private LambdaQueryWrapper<MesTaskc> buildQueryWrapper(MesTaskcBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesTaskc> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesTaskc::getSn, bo.getSn());
        lqw.eq(bo.getProId() != null, MesTaskc::getProId, bo.getProId());
        lqw.eq(bo.getWorkplaceId() != null, MesTaskc::getWorkplaceId, bo.getWorkplaceId());
        lqw.eq(bo.getUserId() != null, MesTaskc::getUserId, bo.getUserId());
        lqw.eq(bo.getNeedcount() != null, MesTaskc::getNeedcount, bo.getNeedcount());
        lqw.eq(bo.getMarkcount() != null, MesTaskc::getMarkcount, bo.getMarkcount());
        lqw.eq(bo.getOkprocess() != null, MesTaskc::getOkprocess, bo.getOkprocess());
        lqw.eq(bo.getScrapcount() != null, MesTaskc::getScrapcount, bo.getScrapcount());
        lqw.eq(bo.getIsCheck() != null, MesTaskc::getIsCheck, bo.getIsCheck());
        lqw.eq(bo.getCurrproid() != null, MesTaskc::getCurrproid, bo.getCurrproid());
        lqw.eq(bo.getCurrprocess() != null, MesTaskc::getCurrprocess, bo.getCurrprocess());
        lqw.eq(bo.getAllprocess() != null, MesTaskc::getAllprocess, bo.getAllprocess());
        lqw.eq(bo.getRedoingcount() != null, MesTaskc::getRedoingcount, bo.getRedoingcount());
        lqw.eq(bo.getRedoingid() != null, MesTaskc::getRedoingid, bo.getRedoingid());
        lqw.eq(bo.getIsTop() != null, MesTaskc::getIsTop, bo.getIsTop());
        lqw.eq(bo.getWorkId() != null, MesTaskc::getWorkId, bo.getWorkId());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), MesTaskc::getType, bo.getType());
        lqw.eq(bo.getSort() != null, MesTaskc::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesTaskc::getStatus, bo.getStatus());
        lqw.eq(bo.getMustbe() != null, MesTaskc::getMustbe, bo.getMustbe());
        lqw.eq(bo.getEnough() != null, MesTaskc::getEnough, bo.getEnough());
        lqw.eq(bo.getStocknum() != null, MesTaskc::getStocknum, bo.getStocknum());
        lqw.eq(bo.getOutstocknum() != null, MesTaskc::getOutstocknum, bo.getOutstocknum());
        lqw.eq(bo.getChecknum() != null, MesTaskc::getChecknum, bo.getChecknum());
        lqw.eq(bo.getIschecknew() != null, MesTaskc::getIschecknew, bo.getIschecknew());
        lqw.eq(bo.getWorktype() != null, MesTaskc::getWorktype, bo.getWorktype());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark2()), MesTaskc::getRemark2, bo.getRemark2());
        return lqw;
    }

    /**
     * 新增组装流转
     *
     * @param bo 组装流转
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesTaskcBo bo) {
        MesTaskc add = MapstructUtils.convert(bo, MesTaskc.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改组装流转
     *
     * @param bo 组装流转
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(MesTaskcBo bo) {
        MesTaskc update = MapstructUtils.convert(bo, MesTaskc.class);
        validEntityBeforeSave(update);
        if(null==bo.getMarkcount()){
            bo.setMarkcount(0L);
        }
        if(null==update)
        {
            return Boolean.FALSE;
        }
        List<MesTaskc> listc1= DataPermissionHelper.ignore(() -> {
            LambdaQueryWrapper<MesTaskc> otherwarp = new LambdaQueryWrapper<>();
            otherwarp.eq(MesTaskc::getSn, update.getSn());
            otherwarp.eq(MesTaskc::getProId, update.getProId());
            otherwarp.eq(MesTaskc::getWorkplaceId, update.getWorkplaceId());
            otherwarp.eq(MesTaskc::getNeedcount, update.getNeedcount());
            otherwarp.eq(MesTaskc::getStatus,"3");
            return baseMapper.selectList(otherwarp);
        });
        if(listc1.size()>0){
            return Boolean.FALSE;
        }
        List<MesTaskc> listc2= DataPermissionHelper.ignore(() -> {
            LambdaQueryWrapper<MesTaskc> otherwarp = new LambdaQueryWrapper<>();
            otherwarp.eq(MesTaskc::getSn, update.getSn());
            otherwarp.eq(MesTaskc::getProId, update.getProId());
            otherwarp.eq(MesTaskc::getWorkplaceId, update.getWorkplaceId());
            otherwarp.eq(MesTaskc::getNeedcount, update.getNeedcount());
            otherwarp.eq(MesTaskc::getCurrprocess, update.getCurrprocess());
            otherwarp.ne(MesTaskc::getUserId, update.getUserId());
            return baseMapper.selectList(otherwarp);

        });
        int tocount=0;
        try{
            tocount=bo.getMarkcount().intValue();
        }catch (Exception e){

        }
        if(tocount>0){
                for (MesTaskc msk : listc2) {
                    msk.setMarkcount(Long.valueOf(tocount));
                    DataPermissionHelper.ignore(() -> {
                        LambdaQueryWrapper<MesTaskc> ots = new LambdaQueryWrapper<>();
                        ots.eq(MesTaskc::getId, msk.getId());

                        baseMapper.update(msk,ots);
                    });
                }
        }
        MesTaskc update1 = baseMapper.selectById(update.getId());
        update.setMarkcount(Long.valueOf(update1.getMarkcount().intValue()+tocount));
        if(update.getMarkcount()>=update1.getNeedcount()){
            update.setStatus("3");
        }
        if (StringUtils.equals(update.getStatus(), "3")) {
            //清楚同组内其他人员任务
            for (MesTaskc mesTask : listc2) {
                mesTask.setDelFlag("2");
                mesTask.setStatus("8");
                mesTask.setRemark2("同班组人完成，自动关闭");
                DataPermissionHelper.ignore(() -> {
                    LambdaQueryWrapper<MesTaskc> ot = new LambdaQueryWrapper<>();
                    ot.eq(MesTaskc::getId, mesTask.getId());
                    baseMapper.update(mesTask,ot);
                });
            }
        }
        //报工的产品等于work的产品 更新已生产数量
        MesWork mesWork=workMapper.selectById(bo.getWorkId());
        if(Objects.equals(bo.getProId(), mesWork.getProId())){
            mesWork.setCurrnum(mesWork.getCurrnum()+bo.getShenc());
            if(Objects.equals(mesWork.getNeednum(), mesWork.getCurrnum())){
                mesWork.setStatus("3");
            }
            workMapper.updateById(mesWork);
        }
        //录入MesWrok
        MesWorkRec mesWorkRec=new MesWorkRec();
        mesWorkRec.setUserId(update.getUserId());
        mesWorkRec.setCount(Long.valueOf(bo.getShenc()));
        mesWorkRec.setProid(bo.getProId());
        mesWorkRec.setTaskId(bo.getId());
        mesWorkRec.setStatus("2");
        mesWorkRec.setRemark("组装报工");
        workRecMapper.insert(mesWorkRec);
        //成品入口
        MesProduct mesProduct=productMapper.selectById(bo.getProId());
        BigDecimal stock=null==mesProduct.getStock()? BigDecimal.ZERO :mesProduct.getStock();
        BigDecimal st=new BigDecimal(bo.getShenc());
        stock=ArithHelper.add(stock,st);
        mesProduct.setStock(stock);
        productMapper.updateById(mesProduct);
        MesInstock lastone=instockMapper.selectLatest(bo.getProId());
        MesInstock instock=new MesInstock();
        instock.setProId(bo.getProId());
        instock.setCount(bo.getShenc());
        if (null==lastone){
            instock.setNowprice(BigDecimal.ZERO);
            instock.setAllprice(BigDecimal.ZERO);
        }else{
            instock.setNowprice(lastone.getNowprice());
            instock.setAllprice(lastone.getAllprice());
        }
        instock.setRemark(bo.getSn());
        instock.setFromtype("组装入库");
        instockMapper.insert(instock);
        //其他出库
        LambdaQueryWrapper<MesProduct> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(MesProduct::getPid,bo.getProId());
        List<MesProduct> list=productMapper.selectList(lqw2);
        for (MesProduct mp : list) {
           int plk= bo.getShenc()*mp.getNeedCount();
           BigDecimal mn=new BigDecimal(plk);
           mp.setStock(ArithHelper.sub(mp.getStock(),mn));
           productMapper.updateById(mp);
        }
        MesOutstock outstock=new MesOutstock();
        outstock.setProId(bo.getProId());
        outstock.setCount(BigDecimal.valueOf(bo.getShenc()));
        outstock.setFromtype("组装出库");
        outstock.setSn(bo.getSn());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesTaskc entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除组装流转信息
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
