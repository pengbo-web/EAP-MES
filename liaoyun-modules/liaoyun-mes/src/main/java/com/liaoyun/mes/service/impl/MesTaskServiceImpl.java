package com.liaoyun.mes.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.controller.BeanMapHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.bo.MesOutstockBo;
import com.liaoyun.mes.domain.bo.MesWorkBo;
import com.liaoyun.mes.domain.bo.MesWorkRecBo;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.domain.vo.SysUserVo;
import com.liaoyun.system.mapper.SysUserMapper;
import com.liaoyun.system.service.impl.SysUserServiceImpl;
import jodd.util.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesTaskBo;
import com.liaoyun.mes.service.IMesTaskService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务流转Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-07
 */
@RequiredArgsConstructor
@Service
public class MesTaskServiceImpl implements IMesTaskService {

    private final MesTaskMapper baseMapper;
    private final MesProductServiceImpl productService;
    private final MesProductMapper productMapper;
    private final MesWorkMapper workService;
    private final MesWorkplacesServiceImpl workplacesService;
    private final SysUserServiceImpl service;
    private final MesProcessMapper processMapper;
    private final MesProsettingMapper prosettingMapper;
    private final MesWorkRecMapper workRecMapper;
    private final MesWorkplacesMapper workplacesMapper;
    private final SysUserMapper userMapper;
    private final MesOutstockServiceImpl outstockMapper;
    private final MesOutsourcingMapper outsourcingMapper;
    private final MesStockMapper stockMapper;

    /**
     * 查询任务流转
     *
     * @param id 主键
     * @return 任务流转
     */
    @Override
    public MesTaskVo queryById(Long id) {
        MesTaskVo vo=baseMapper.selectVoById(id);
        try{
            MesProductVo provo=productService.queryById(vo.getProId());
            if(null==provo){
                MesStock stock=stockMapper.selectById(vo.getProId());
                LambdaQueryWrapper<MesProduct> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(MesProduct::getStocks,stock.getId());
                List<MesProductVo> list=productMapper.selectVoList(queryWrapper);
                provo=list.get(0);
            }else {
                vo.setProductVo(provo);
            }

        }catch (Exception e){

        }

        try{
            vo.setWorkplacesVo(workplacesService.queryById(vo.getWorkplaceId()));
        }catch (Exception e){

        }


        vo.setUsername(service.selectUserNameById(vo.getUserId()));
        vo.setProcessVo(processMapper.selectVoById(vo.getCurrproid()));
        vo.setWorkVo(workService.selectVoById(vo.getWorkId()));
        return vo;
    }

    @Override
    public TableDataInfo<MesTaskVo> queryListbyqua(MesTaskBo bo, PageQuery pageQuery) {
        if(StringUtils.isNotBlank(pageQuery.getSn())){
            bo.setSn(pageQuery.getSn());
        }
        LambdaQueryWrapper<MesTask> lqw = buildQueryWrapper(bo);
        lqw.eq(MesTask::getIschecknew, 0);//质检不通过
        if(pageQuery.getWorktype()!=null){
            lqw.eq(MesTask::getWorktype,pageQuery.getWorktype());
        }
//        lqw.eq(MesTask::getWorktype,0);
        if(StringUtils.isNotBlank(pageQuery.getNickName())){
            LambdaQueryWrapper<SysUser> sysl=new LambdaQueryWrapper<>();
            sysl.like(SysUser::getNickName, pageQuery.getNickName());
            List<SysUser> users=userMapper.selectList(sysl);
            List<Long> userids=new ArrayList<>();
            if(ObjectUtil.isNotEmpty(users)){
                for(SysUser user:users){
                    userids.add(user.getUserId());
                }
            }
            lqw.in(MesTask::getUserId,userids);
        }
        Page<MesTaskVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesTaskVo vo : result.getRecords()) {
            vo.setProductVo(productService.queryById(vo.getProId()));
            vo.setWorkplacesVo(workplacesService.queryById(vo.getWorkplaceId()));
            vo.setUsername(service.selectUserNameById(vo.getUserId()));
            vo.setProcessVo(processMapper.selectVoById(vo.getCurrproid()));
            vo.setWorkVo(workService.selectVoById(vo.getWorkId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 分页查询任务流转列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 任务流转分页列表
     */
    @Override
    public TableDataInfo<MesTaskVo> queryPageList(MesTaskBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesTask> lqw = buildQueryWrapper(bo);
        lqw.eq(MesTask::getWorktype,0);
        if (ObjectUtil.isNotNull(bo.getNickName()) || ObjectUtil.isNotNull(bo.getPhonenumber())) {
            if (bo.getNickName().length() > 1 || bo.getPhonenumber().length() > 1) {
                LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
                userWrapper.like(bo.getPhonenumber() != null, SysUser::getPhonenumber, bo.getPhonenumber());
                userWrapper.like(bo.getNickName() != null, SysUser::getNickName, bo.getNickName()).select(SysUser::getUserId);
                List<Object> userIds = userMapper.selectObjs(userWrapper);
                if (!userIds.isEmpty()) {
                    lqw.in(MesTask::getUserId, userIds);
                }
            }
        }
        Page<MesTaskVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesTaskVo vo : result.getRecords()) {
            vo.setProductVo(productService.queryById(vo.getProId()));
            vo.setWorkplacesVo(workplacesService.queryById(vo.getWorkplaceId()));
            vo.setUsername(service.selectUserNameById(vo.getUserId()));
            vo.setProcessVo(processMapper.selectVoById(vo.getCurrproid()));
            vo.setWorkVo(workService.selectVoById(vo.getWorkId()));
        }
        return TableDataInfo.build(result);
    }
    @Override
    public TableDataInfo<MesTaskVo> queryPageListbtidd(Long uid,Integer typ,MesTaskBo bo, PageQuery pageQuery){
        Page<MesTaskVo> result=DataPermissionHelper.ignore(()->{
            LambdaQueryWrapper<MesTask> lqw = buildQueryWrapper(bo);
            lqw.eq(MesTask::getUserId,uid);
            if(typ==1){
                lqw.gt(MesTask::getIschecknew,0);//大与  异常
            }
            if(typ==2){
                lqw.gt(MesTask::getChecknum,0);//大与  质检过
            }
            if(typ==3){
                lqw.eq(MesTask::getChecknum,0);//大与  未质检
            }
            return baseMapper.selectVoPage(pageQuery.build(), lqw);
        });
        for (MesTaskVo vo : result.getRecords()) {
            vo.setProductVo(productService.queryById(vo.getProId()));
            vo.setWorkplacesVo(workplacesService.queryById(vo.getWorkplaceId()));
            vo.setUsername(service.selectUserNameById(vo.getUserId()));
            vo.setProcessVo(processMapper.selectVoById(vo.getCurrproid()));
            vo.setWorkVo(workService.selectVoById(vo.getWorkId()));
        }
        return TableDataInfo.build(result);
    }

    @Override
    @DSTransactional
    public Boolean insertbo(MesTaskBo bo) {
        MesTask mesTask = new MesTask();
        mesTask.setUserId(LoginHelper.getUserId());
        mesTask.setProId(0L);
        mesTask.setNeedcount(0L);
        mesTask.setAllprocess(0);
        mesTask.setSn("0");
        mesTask.setWorktype(1);
        mesTask.setStatus("2");
        mesTask.setWorkplaceId(0L);
        mesTask.setIsTop(0);
        mesTask.setWorkId(0L);
        mesTask.setMarkcount(0L);
        mesTask.setRemark("单独报工");
        if (ObjectUtils.isNotNull(bo.getSn())) {
            mesTask.setSn(bo.getSn());
            LambdaQueryWrapper<MesWork> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MesWork::getSn, bo.getSn());
            List<MesWork> list = workService.selectList(wrapper);
            if (list.size() > 0) {
                MesWork mesWork = list.get(0);
                mesTask.setProId(mesWork.getProId());
                mesTask.setNeedcount(Long.valueOf(mesWork.getNeednum()));
                mesTask.setAllprocess(mesWork.getProcesscount().intValue());
                mesTask.setWorkId(mesWork.getId());
            }
            mesTask.setRemark("扫码报工");
        }

        mesTask.setCurrproid(bo.getCurrproid());
        return baseMapper.insert(mesTask) > 0;
    }

    @Override
    @DSTransactional
    public Boolean scandorep(MesTaskBo bo) {
        MesTask update = MapstructUtils.convert(bo, MesTask.class);
        MesTaskVo mesTaskVo = baseMapper.selectVoById(update.getId());
        MesWorkRec recBo = new MesWorkRec();
        recBo.setUserId(mesTaskVo.getUserId());
        recBo.setTaskId(mesTaskVo.getId());
        recBo.setLastcount(mesTaskVo.getMarkcount());
        recBo.setCount(mesTaskVo.getMarkcount());
        int scrp=0;
        try {
            scrp=bo.getScrapcount().intValue();
        }catch (Exception e) {

        }
        int redo=0;
        try {
            redo=bo.getRedoingcount().intValue();
        }catch (Exception e) {

        }
        recBo.setDefective(scrp+redo);
        recBo.setStatus("2");
        if (!update.getSn().equals("0")) {
            LambdaQueryWrapper<MesWork> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MesWork::getSn, update.getSn());
            List<MesWork> list = workService.selectList(wrapper);
            if (list.size() > 0) {
                if (bo.getStatus().equals("3")) {
                    MesWork mesWork = list.get(0);
                    int okt = mesWork.getOkprocess() + 1;
                    mesWork.setOkprocess(okt);
                    MesProcessVo processVo = processMapper.selectVoById(mesTaskVo.getCurrproid());
                    mesWork.setCurrprocess(mesWork.getCurrprocess() + "," + processVo.getTitle() + ":扫码完工");
                    if (okt == mesWork.getProcesscount()) {
                        mesWork.setStatus("3");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        mesWork.setEndtime(sdf.format(System.currentTimeMillis()));
                    }
                    workService.updateById(mesWork);
                }
            }
        }
        workRecMapper.insert(recBo);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    @DSTransactional
    public Boolean stockupdate(MesTaskBo bo) {
        MesTask update = MapstructUtils.convert(bo, MesTask.class);
        update.setMarkcount(bo.getNeedcount());
        update.setStatus("3");
        update.setRemark("库存出库");
        LambdaQueryWrapper<MesProsetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesProsetting::getProId, bo.getProId());
        List<MesProsettingVo> list = prosettingMapper.selectVoList(wrapper);
        MesWorkVo mesWorkVo = workService.selectVoById(bo.getWorkId());
        mesWorkVo.setOkprocess(list.size());
        MesWork update2 = MapstructUtils.convert(mesWorkVo, MesWork.class);
        workService.updateById(update2);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    @DSTransactional
    public Boolean stockupstatus(MesTaskBo bo) {
        MesProductVo productVo = productService.queryById(bo.getProId());
        MesTask update = MapstructUtils.convert(bo, MesTask.class);
        MesOutstockBo outstockBo = new MesOutstockBo();
        outstockBo.setProId(update.getProId());//已变更
        outstockBo.setUnitId(productVo.getUnitId());
        outstockBo.setCount(bo.getCount());
        outstockMapper.insertByBo(outstockBo);
//        if (update.getStocknum() == update.getStocknum()) {
//            update.setMarkcount(bo.getNeedcount());
//            update.setStatus("3");
//        } else {
//            update.setOutstocknum(update.getOutstocknum() + 1);
//        }
        MesWorkVo mesWorkVo = workService.selectVoById(bo.getWorkId());
        mesWorkVo.setOkprocess(mesWorkVo.getOkprocess() + 1);
        MesWork update2 = MapstructUtils.convert(mesWorkVo, MesWork.class);
        workService.updateById(update2);
        if (mesWorkVo.getIsturn() == 1) {
            MesTask update1 = MapstructUtils.convert(bo, MesTask.class);
            if (bo.getOkprocess() == bo.getAllprocess()) {

            } else {
                LambdaQueryWrapper<MesProsetting> prowra = new LambdaQueryWrapper<>();
                prowra.eq(MesProsetting::getProId, bo.getProId());
                prowra.eq(MesProsetting::getSort, bo.getCurrprocess() + 1);
                List<MesProsettingVo> prosettingVo = prosettingMapper.selectVoList(prowra);
                MesProsettingVo pinfo = new MesProsettingVo();
                if (prosettingVo.size() > 0) {
                    pinfo = prosettingVo.get(0);
                }
                MesProcessVo processVo = processMapper.selectVoById(Long.valueOf(pinfo.getProcessId()));
                //工作站和工作站归属的用户
                MesWorkplacesVo workplacesVo = workplacesMapper.selectVoById(pinfo.getWorkshopId());
                LambdaQueryWrapper<SysUser> wrapper1 = new LambdaQueryWrapper<>();
                List<String> tearms = Arrays.asList(workplacesVo.getTeams().split(","));
                List<Long> longList = tearms.stream()
                    .map(Long::parseLong)
                    .toList();
                wrapper1.in(SysUser::getDeptId, longList);
                List<SysUserVo> userVoList = userMapper.selectVoList(wrapper1);
                for (SysUserVo sysUserVo : userVoList) {
                    MesTask bo1 = new MesTask();
                    bo1.setSn(bo.getSn());
                    bo1.setProId(bo.getProId());
                    //关于
                    bo1.setWorkplaceId(Long.valueOf(pinfo.getWorkshopId()));
                    bo1.setUserId(sysUserVo.getUserId());
                    bo1.setNeedcount(bo.getNeedcount());
                    bo1.setMarkcount(0L);
                    bo1.setScrapcount(0L);
                    bo1.setIsTop(mesWorkVo.getIstop());
                    bo1.setCurrprocess(bo.getCurrprocess() + 1);
                    bo1.setRedoingcount(0L);
                    bo1.setIsCheck(0);
                    bo1.setOkprocess(bo.getOkprocess() + 1);
                    bo1.setCurrproid(processVo.getId());
                    bo1.setWorkId(bo.getWorkId());
                    bo1.setStatus("1");
                    bo1.setAllprocess(bo.getAllprocess());
                    baseMapper.insert(bo1);
                }
            }
        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 分页查询任务流转列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 任务流转分页列表
     */
    @Override
    public TableDataInfo<MesTaskVo> queryPageListstatus(MesTaskBo bo, PageQuery pageQuery, String sta, Integer worktype) {
        if(StringUtils.isNotBlank(pageQuery.getSn())){
            bo.setSn(pageQuery.getSn());
        }

        if(StringUtils.isNotBlank(pageQuery.getCurrprocess())){
            try {
                bo.setCurrprocess(Integer.valueOf(pageQuery.getCurrprocess()));
            }catch (Exception e){

            }

        }
        LambdaQueryWrapper<MesTask> lqw = buildQueryWrapper(bo);
        if (sta.equals("7")) {
            lqw.ne(MesTask::getStatus, "3");
        } else {
            lqw.eq(MesTask::getStatus, sta);
        }
        if(StringUtils.isNotBlank(pageQuery.getProsn())){
            LambdaQueryWrapper<MesProduct> wpro=new LambdaQueryWrapper<>();
            wpro.like(MesProduct::getSn, pageQuery.getProsn());
            List<MesProduct> list=productMapper.selectList(wpro);
            MesProduct mesProduct=new MesProduct();
            if(list.size()>0){
                //TODO
                // 构造一个包含所有 MesProduct id 的集合
                List<Long> productIds = list.stream()
                    .map(MesProduct::getId)
                    .collect(Collectors.toList());
                // 设置条件，使 MesTask 的 getProid 等于集合内所有 id
                lqw.in(MesTask::getProId, productIds);

            }

        }
        lqw.eq(MesTask::getWorktype, worktype);
        lqw.ne(MesTask::getStatus, "8");
        Page<MesTaskVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<MesTaskVo> list =new ArrayList<>();
        for (MesTaskVo vo : result.getRecords()) {
            if (vo.getIsTop() != 0) {
                vo.setProductVo(productService.queryById(vo.getProId()));
            }
            if (vo.getWorkplaceId() != 0) {
                vo.setWorkplacesVo(workplacesService.queryById(vo.getWorkplaceId()));
            }
            vo.setProductVo(productService.queryById(vo.getProId()));
            vo.setUsername(service.selectUserNameById(vo.getUserId()));
            MesProcessVo processVo=processMapper.selectVoById(vo.getCurrproid());
            vo.setProcessVo(processVo);
            if (vo.getWorkId() != 0) {
                vo.setWorkVo(workService.selectVoById(vo.getWorkId()));
            }


            if(processVo!=null){
                if(null!=processVo.getUserids()){
                    Long uid=vo.getUserId();
                    String[] olduids=processVo.getUserids().split(",");
                    List<String> oldUidList = Arrays.asList(olduids);
                    if(oldUidList.contains(uid.toString())){
                        list.add(vo);
                    }
                }
            }
            if(null!=vo.getProcessVo()){
                if (vo.getProcessVo().getTypeId() == 3L) {
                    vo.setOutsourcingVo(outsourcingMapper.selectVoById(Long.valueOf(vo.getProcessVo().getStandards())));
                }
            }
            if(null!=vo.getProId()){

                vo.setProname( DataPermissionHelper.ignore(() -> {
                    return   baseMapper.getproname(vo.getProId());
                }));
            }

        }
        if(list.size()>0){
            result.setRecords(list);
            result.setTotal(result.getRecords().size());
        }
        return TableDataInfo.build(result);
    }

    @Override
    public Boolean updatetask(MesTaskBo bo) {
        MesTask update = MapstructUtils.convert(bo, MesTask.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 查询符合条件的任务流转列表
     *
     * @param bo 查询条件
     * @return 任务流转列表
     */
    @Override
    public List<MesTaskVo> queryList(MesTaskBo bo) {
        LambdaQueryWrapper<MesTask> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesTask> buildQueryWrapper(MesTaskBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesTask> lqw = Wrappers.lambdaQuery();

        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesTask::getSn, bo.getSn());
        LambdaQueryWrapper<MesProduct> lqp = Wrappers.lambdaQuery();
        lqp.like(MesProduct::getTitle,bo.getProId());
        List<MesProduct> productList=productMapper.selectList(lqp);
        List<Long> proids=Optional.ofNullable(productList)
            .orElse(Collections.emptyList())
            .stream()
            .map(MesProduct::getId)
            .collect(Collectors.toList());
        lqw.in(bo.getProId() != null, MesTask::getProId, proids);
        lqw.eq(bo.getWorkplaceId() != null, MesTask::getWorkplaceId, bo.getWorkplaceId());
        lqw.eq(bo.getUserId() != null, MesTask::getUserId, bo.getUserId());
        lqw.eq(bo.getNeedcount() != null, MesTask::getNeedcount, bo.getNeedcount());
        lqw.eq(bo.getMarkcount() != null, MesTask::getMarkcount, bo.getMarkcount());
        lqw.eq(bo.getScrapcount() != null, MesTask::getScrapcount, bo.getScrapcount());
        lqw.eq(bo.getIsCheck() != null, MesTask::getIsCheck, bo.getIsCheck());
        lqw.eq(bo.getCurrprocess() != null, MesTask::getCurrprocess, bo.getCurrprocess());
        lqw.eq(bo.getRedoingcount() != null, MesTask::getRedoingcount, bo.getRedoingcount());
        lqw.eq(bo.getRedoingid() != null, MesTask::getRedoingid, bo.getRedoingid());
        lqw.eq(bo.getIsTop() != null, MesTask::getIsTop, bo.getIsTop());
        lqw.eq(bo.getWorkId() != null, MesTask::getWorkId, bo.getWorkId());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), MesTask::getType, bo.getType());
        lqw.eq(bo.getSort() != null, MesTask::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesTask::getStatus, bo.getStatus());
        lqw.eq(bo.getMustbe() != null, MesTask::getMustbe, bo.getMustbe());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            MesTask::getUpdateTime, params.get("beginTime"), params.get("endTime"));
        return lqw;
    }

    /**
     * 新增任务流转
     *
     * @param bo 任务流转
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesTaskBo bo) {
        MesTask add = MapstructUtils.convert(bo, MesTask.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改任务流转
     *
     * @param bo 任务流转
     * @return 是否修改成功
     */
    @Override
    @DSTransactional
    @InterceptorIgnore(dataPermission = "true")
    public Boolean updateByBo(MesTaskBo bo) {
        MesTask add = baseMapper.selectById(bo.getId());
        if(null==bo.getMarkcount()){
            bo.setMarkcount(0L);
        }
        if(null==add)
        {
            return Boolean.FALSE;
        }
        //已报工完成的直接返回
        List<MesTask> listc1= DataPermissionHelper.ignore(() -> {
            LambdaQueryWrapper<MesTask> otherwarp = new LambdaQueryWrapper<>();
            otherwarp.eq(MesTask::getSn, add.getSn());
            otherwarp.eq(MesTask::getProId, add.getProId());
            otherwarp.eq(MesTask::getWorkplaceId, add.getWorkplaceId());
            otherwarp.eq(MesTask::getNeedcount, add.getNeedcount());
            otherwarp.eq(MesTask::getCurrprocess, add.getCurrprocess());
            otherwarp.eq(MesTask::getStatus,"3");
            return baseMapper.selectList(otherwarp);
        });
        if(listc1.size()>0){
            return Boolean.FALSE;
        }
        //总数量更新，想通任务
        List<MesTask> listc2= DataPermissionHelper.ignore(() -> {
            LambdaQueryWrapper<MesTask> otherwarp = new LambdaQueryWrapper<>();
            otherwarp.eq(MesTask::getSn, add.getSn());
            otherwarp.eq(MesTask::getProId, add.getProId());
            otherwarp.eq(MesTask::getWorkplaceId, add.getWorkplaceId());
            otherwarp.eq(MesTask::getNeedcount, add.getNeedcount());
            otherwarp.eq(MesTask::getCurrprocess, add.getCurrprocess());
            otherwarp.ne(MesTask::getUserId, add.getUserId());
            return baseMapper.selectList(otherwarp);
        });
        int tocount=0;
        try{
            tocount=bo.getMarkcount().intValue();
        }catch (Exception e){

        }
        if(StringUtils.equals(bo.getStatus(),"1")||StringUtils.equals(bo.getStatus(),"3")){
            if(tocount>0){

                for (MesTask msk : listc2) {
                    msk.setMarkcount(Long.valueOf(tocount));
                    DataPermissionHelper.ignore(() -> {
                        LambdaQueryWrapper<MesTask> ots = new LambdaQueryWrapper<>();
                        ots.eq(MesTask::getId, msk.getId());

                        baseMapper.update(msk,ots);
                    });
                }
            }
        }
        MesTask update = MapstructUtils.convert(bo, MesTask.class);
        update.setMarkcount(Long.valueOf(tocount));
//        validEntityBeforeSave(update);
        if (StringUtils.equals(update.getStatus(), "3")) {
            List<MesTask> listc= DataPermissionHelper.ignore(() -> {
                LambdaQueryWrapper<MesTask> otherwarp = new LambdaQueryWrapper<>();
                otherwarp.eq(MesTask::getSn, update.getSn());
                otherwarp.eq(MesTask::getProId, update.getProId());
                otherwarp.eq(MesTask::getWorkplaceId, update.getWorkplaceId());
                otherwarp.eq(MesTask::getNeedcount, update.getNeedcount());
                otherwarp.eq(MesTask::getCurrprocess, update.getCurrprocess());
                otherwarp.ne(MesTask::getUserId, update.getUserId());
                return baseMapper.selectList(otherwarp);
            });
            //清楚同组内其他人员任务

            for (MesTask mesTask : listc) {
//                if (!Objects.equals(mesTask.getUserId(), bo.getUserId())) {
                    mesTask.setDelFlag("2");
                    mesTask.setStatus("8");
                    mesTask.setRemark2("同班组人完成，自动关闭");
                    DataPermissionHelper.ignore(() -> {
                        LambdaQueryWrapper<MesTask> ot = new LambdaQueryWrapper<>();
                        ot.eq(MesTask::getId, mesTask.getId());
                        baseMapper.update(mesTask,ot);
                    });
//                }
            }

            MesWorkVo mesWorkVo = workService.selectVoById(bo.getWorkId());
            MesWork workupdate = (MesWork) BeanMapHelper.Bean2Bean(mesWorkVo, MesWork.class);
            if (mesWorkVo.getIsturn() == 1) {
                int tc=bo.getCurrprocess().intValue();
                //当前工序
                if (Objects.equals(bo.getOkprocess(), bo.getAllprocess())) {
                    if(Objects.equals(bo.getProId(), workupdate.getProId())){
                        workupdate.setCurrnum(workupdate.getCurrnum()+tocount);
                        if(workupdate.getNeednum()>=workupdate.getCurrnum()){
                            workupdate.setStatus("3");
                        }
                    }
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    workupdate.setEndtime(sdf.format(System.currentTimeMillis()));
                    workupdate.setRemark("完工");
                    workService.updateById(workupdate);
                } else if(tc==bo.getAllprocess()){
                    //报工的产品等于work的产品 更新已生产数量
                    if(Objects.equals(bo.getProId(), workupdate.getProId())){
                        workupdate.setCurrnum(workupdate.getCurrnum()+tocount);
                        if(Objects.equals(workupdate.getNeednum(), workupdate.getCurrnum())){
                            workupdate.setStatus("3");
                        }
                    }
                    workupdate.setOkprocess(workupdate.getOkprocess() + 1);
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    workupdate.setEndtime(sdf.format(System.currentTimeMillis()));
                    workupdate.setRemark("完工");
                    workService.updateById(workupdate);
                    return baseMapper.updateById(update) > 0;
                }
                else {
                    //更新curr、workplace_id、needcount、所属teams
                    //查询所有工序
                    LambdaQueryWrapper<MesProsetting> prowra = new LambdaQueryWrapper<>();
                    prowra.eq(MesProsetting::getProId, bo.getProId());
                    prowra.eq(MesProsetting::getSort, bo.getCurrprocess() + 1);
                    List<MesProsettingVo> prosettingVo = prosettingMapper.selectVoList(prowra);
                    MesProsettingVo pinfo = new MesProsettingVo();
                    if (prosettingVo.size() > 0) {
                        pinfo = prosettingVo.get(0);
                    }
                    MesProcessVo processVo = processMapper.selectVoById(Long.valueOf(pinfo.getProcessId()));
                    //工作站和工作站归属的用户
                    MesWorkplacesVo workplacesVo = workplacesMapper.selectVoById(pinfo.getWorkshopId());
                    LambdaQueryWrapper<SysUser> wrapper1 = new LambdaQueryWrapper<>();
                    List<String> tearms = Arrays.asList(workplacesVo.getTeams().split(","));
                    List<Long> longList = tearms.stream()
                        .map(Long::parseLong)
                        .toList();
                    wrapper1.in(SysUser::getDeptId, longList);
                    List<SysUserVo> userVoList = userMapper.selectVoList(wrapper1);
                    for (SysUserVo sysUserVo : userVoList) {
                        MesTask bo1 = new MesTask();
                        bo1.setSn(bo.getSn());
                        bo1.setProId(bo.getProId());
                        //关于
                        bo1.setWorkplaceId(Long.valueOf(pinfo.getWorkshopId()));
                        bo1.setUserId(sysUserVo.getUserId());
                        bo1.setNeedcount(bo.getNeedcount());
                        bo1.setMarkcount(0L);
                        bo1.setScrapcount(0L);
                        bo1.setIsTop(mesWorkVo.getIstop());
                        bo1.setCurrprocess(bo.getCurrprocess() + 1);
                        bo1.setRedoingcount(0L);
                        bo1.setIsCheck(0);
                        bo1.setOkprocess(bo.getOkprocess() + 1);
                        bo1.setCurrproid(processVo.getId());
                        bo1.setWorkId(bo.getWorkId());
                        bo1.setStatus("1");
                        bo1.setAllprocess(bo.getAllprocess());
                        baseMapper.insert(bo1);
                    }
                }
            } else {
                LambdaQueryWrapper<MesTask> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(MesTask::getSn, bo.getSn())
                    .eq(MesTask::getProId, bo.getProId())
                    .eq(MesTask::getUserId, bo.getUserId());
                List<MesTaskVo> list = baseMapper.selectVoList(wrapper);
                if (list.size() > 0) {
                    for (MesTaskVo task : list) {
                        task.setOkprocess(task.getOkprocess() + 1);
                        MesTask update1 = (MesTask) BeanMapHelper.Bean2Bean(task, MesTask.class);

                        baseMapper.updateById(update1);
                    }
                    update.setOkprocess(update.getOkprocess() + 1);
                }


            }
            workupdate.setOkprocess(workupdate.getOkprocess() + 1);
            LambdaQueryWrapper<MesProsetting> prowra1 = new LambdaQueryWrapper<>();
            prowra1.eq(MesProsetting::getProId, bo.getProId())
                .eq(MesProsetting::getSort, bo.getCurrprocess());
            List<MesProsettingVo> prosettingVo1 = prosettingMapper.selectVoList(prowra1);
            MesProcessVo processVo = processMapper.selectVoById(Long.valueOf(prosettingVo1.get(0).getProcessId()));
            MesProductVo productVo = productService.queryById(bo.getProId());
            if (ObjectUtil.isNotNull(workupdate.getCurrprocess())) {
                workupdate.setCurrprocess(workupdate.getCurrprocess() + "," + productVo.getTitle() + ":" + processVo.getTitle() + "完工");

            } else {
                workupdate.setCurrprocess(productVo.getTitle() + ":" + processVo.getTitle() + "完工");
            }
            //全部完成
            MesProcessVo gettype=processMapper.selectVoById(update.getCurrproid());
            if(gettype.getTypeId()==4){
                if(update.getProId()==workupdate.getProId()){
                    workupdate.setStatus("3");
                }
            }
            workService.updateById(workupdate);

        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesTask entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除任务流转信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
