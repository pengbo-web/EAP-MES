package com.liaoyun.job.snailjob;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.client.model.ExecuteResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.bo.MesTaskBo;
import com.liaoyun.mes.domain.bo.MesTaskcBo;
import com.liaoyun.mes.domain.bo.MesWorkBo;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.mes.service.impl.*;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.domain.vo.SysUserVo;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@JobExecutor(name = "autotaskadd")
@RequiredArgsConstructor
public class AutoTaskaddExecutor {

    private final MesProductMapper productMapper;
    private final MesWorkMapper workService;
    private final MesWorkplacesServiceImpl workplacesService;
    private final MesTaskCopyMapper taskMapper;
    private final MesProsettingMapper prosettingMapper;
    private final MesWorkplacesMapper workplacesMapper;
    private final SysUserMapper userMapper;
    private final MesProcessMapper processMapper;
    private final MesProsettingzpMapper prosettingzpMapper;
    private final MesTaskcMapper taskcMapper;
    private final MesStockMapper stockMapper;
    public ExecuteResult jobExecute(JobArgs jobArgs) {
        //获取刚开始的任务，流转到Task
        LambdaQueryWrapper<MesWork> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesWork::getStatus, 0);
        List<MesWork> list = workService.selectList(wrapper);
        List<Long> ps = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            for (MesWork mesWork : list) {
                //递归查询有工序的proid
                List<MesProductVo> listproids=new ArrayList<>();
                Set<Long> processedIds = new HashSet<>();
                listproids=getAllChilds(mesWork.getProId(),mesWork.getNeednum(),listproids,processedIds);
                getAllChild(mesWork.getProId(), ps);
                List<MesProductVo> childIdList = new ArrayList<>();
                for (MesProductVo vo : listproids) {
                    LambdaQueryWrapper<MesProsetting> wrapper1 = new LambdaQueryWrapper<>();
                    wrapper1.eq(MesProsetting::getProId, vo.getId());
                    wrapper1.eq(MesProsetting::getSort, 1);
                    List<MesProsettingVo> list1 = prosettingMapper.selectVoList(wrapper1);
                    if (list1.size() > 0) {
                        childIdList.add(vo);
                    }
                    //TODO 组装线任务流转
                    if("2".equals(vo.getMustbe())){
                        //查询
                        LambdaQueryWrapper<MesProsettingzp> wrapper2 = new LambdaQueryWrapper<>();
                        wrapper2.eq(MesProsettingzp::getProId, vo.getId());
                        List<MesProsettingzpVo> prosettingzpVos=prosettingzpMapper.selectVoList(wrapper2);
                        for (MesProsettingzpVo vo1:prosettingzpVos){
                            //查询班组内所有人员并 下达任务
                            //-----先查询是否已经有任务

                            List<MesTaskcVo> list2= DataPermissionHelper.ignore(() -> {
                                LambdaQueryWrapper<MesTaskc> twrapper = new LambdaQueryWrapper<>();
                                twrapper.eq(MesTaskc::getProId, vo1.getProId());
                                twrapper.eq(MesTaskc::getSn, mesWork.getSn());
                                twrapper.eq(MesTaskc::getNeedcount,mesWork.getNeednum());
                                return taskcMapper.selectVoList(twrapper);
                            });

                            if (list2.isEmpty()) {
                                LambdaQueryWrapper<SysUser> wrapper3 = new LambdaQueryWrapper<>();
                                wrapper3.eq(SysUser::getDeptId,vo1.getWorkshopId());
                                List<SysUser> sysUsers=userMapper.selectList(wrapper3);
                                for (SysUser sysUser : sysUsers) {
                                    MesTaskc taskcBo1=new MesTaskc();
                                    taskcBo1.setUserId(sysUser.getUserId());
                                    taskcBo1.setSn(mesWork.getSn());
                                    taskcBo1.setProId(vo.getId());
                                    taskcBo1.setWorkplaceId(Long.valueOf(vo1.getWorkshopId()));
                                    taskcBo1.setNeedcount(mesWork.getNeednum().longValue());
                                    taskcBo1.setChecknum(0L);
                                    taskcBo1.setWorkId(mesWork.getId());
                                    taskcBo1.setTenantId(mesWork.getTenantId());
                                    taskcMapper.insert(taskcBo1);
                                }
                            }

                        }
//                        LambdaQueryWrapper<MesProduct> wrapper2 = new LambdaQueryWrapper<>();
//                        wrapper2.eq(MesProduct::getPid,vo.getId());
//                        List<MesProduct> list2 = productMapper.selectList(wrapper2);
//                        if (list2.size() > 0) {
//
//
//                        }
                    }
                }

                //查询第一站,分发第一个工序到人
                for (MesProductVo prsd : childIdList) {
                    //根据有工序的物料，查询工序流转
                    LambdaQueryWrapper<MesProsetting> prowrapper = new LambdaQueryWrapper<>();
                    prowrapper.eq(MesProsetting::getProId, prsd.getId());
                    List<MesProsettingVo> vo = prosettingMapper.selectVoList(prowrapper);
                    if (ObjectUtils.isNotEmpty(vo)) {
                        for (MesProsettingVo prosettingVo : vo) {
                            //工作站和工作站归属的用户
                            MesWorkplacesVo workplacesVo = workplacesMapper.selectVoById(prosettingVo.getWorkshopId());
                            LambdaQueryWrapper<SysUser> wrapper1 = new LambdaQueryWrapper<>();
                            List<String> tearms = Arrays.asList(workplacesVo.getTeams().split(","));
                            List<Long> longList = tearms.stream()
                                .map(Long::parseLong)
                                .toList();
                            wrapper1.in(SysUser::getDeptId, longList);
                            List<SysUserVo> userVoList = userMapper.selectVoList(wrapper1);
                            if (mesWork.getIsmrp().equals("0")) {
                                //所有工序一起下发
                                if (mesWork.getIsturn() == 0) {
                                    for (SysUserVo sysUserVo : userVoList) {
                                        addts(mesWork, prsd, prosettingVo, sysUserVo, vo.size(), prosettingVo.getSort(), Long.valueOf(prosettingVo.getProcessId()),1);
                                    }
                                } else {
                                    if (prosettingVo.getSort() == 1) {
                                        for (SysUserVo sysUserVo : userVoList) {
                                            addts(mesWork, prsd, prosettingVo, sysUserVo, vo.size(), 1, Long.valueOf(prosettingVo.getProcessId()),1);
                                        }
                                    }
                                }
                            }
                            if (mesWork.getIsmrp().equals("1")) {
                                int getd=1;
                                    try{
                                        if(null!=prsd.getStocks()){
                                            MesStock stock=stockMapper.selectById(prsd.getStockId());
                                            getd=stock.getNowstock().intValue();
                                        }else{
                                            getd=prsd.getStock().intValue();
                                        }
                                    }catch (Exception e){

                                    }
                                if(getd>=prsd.getNeedCount()){
                                    MesProcessVo processVo = processMapper.selectVoById(prosettingVo.getProcessId());
                                    if(processVo.getTypeId()==2L){
                                        for (SysUserVo sysUserVo : userVoList) {
                                            addts(mesWork, prsd, prosettingVo, sysUserVo, vo.size(), 1, Long.valueOf(prosettingVo.getProcessId()),0);
                                        }
                                    }
                                }else{
//                                    int needmark=prsd.getNeedCount()-getd;
//                                    //所有工序一起下发
//                                    prsd.setNeedCount(needmark);
                                    if (mesWork.getIsturn() == 0) {
                                        for (SysUserVo sysUserVo : userVoList) {
                                            addts(mesWork, prsd, prosettingVo, sysUserVo, vo.size(), prosettingVo.getSort(), Long.valueOf(prosettingVo.getProcessId()),1);
                                        }
                                    } else {
                                        if (prosettingVo.getSort() == 1) {
                                            for (SysUserVo sysUserVo : userVoList) {
                                                addts(mesWork, prsd, prosettingVo, sysUserVo, vo.size(), 1, Long.valueOf(prosettingVo.getProcessId()),1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return ExecuteResult.success("流转成功");
    }

    /**
     * 插入任务
     *
     * @param workVo     主任务
     * @param prosetting 排成id
     * @param user       执行用户
     * @param longsize   总工序
     */
    public void addts(MesWork workVo, MesProductVo prsd, MesProsettingVo prosetting, SysUserVo user, int longsize, int currp, Long proid,int enough) {
        LambdaQueryWrapper<MesTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesTask::getSn, workVo.getSn());
        wrapper.eq(MesTask::getProId, prosetting.getProId());
        wrapper.eq(MesTask::getUserId, user.getUserId());
        wrapper.eq(MesTask::getWorkplaceId, Long.valueOf(prosetting.getWorkshopId()));
        wrapper.eq(MesTask::getCurrprocess, currp);
        List<MesTaskVo> list = taskMapper.selectVoList(wrapper);
        if (ObjectUtils.isEmpty(list)) {
            MesTask bo = new MesTask();
            bo.setSn(workVo.getSn());
            bo.setProId(prsd.getId());
            //关于
            bo.setWorkplaceId(Long.valueOf(prosetting.getWorkshopId()));
            bo.setUserId(user.getUserId());

            bo.setNeedcount(Long.valueOf(prsd.getNeedCount()));
            bo.setMarkcount(0L);
            bo.setScrapcount(0L);
            bo.setIsTop(workVo.getIstop());
            bo.setCurrprocess(currp);
            bo.setRedoingcount(0L);
            bo.setIsCheck(0);
            bo.setCurrproid(proid);
            bo.setWorkId(workVo.getId());
            bo.setStatus("1");
            bo.setEnough(enough);
            bo.setTenantId(workVo.getTenantId());
            bo.setAllprocess(longsize);
            taskMapper.insert(bo);
            workVo.setStatus("1");
            workService.updateById(workVo);
        }

    }

    /**
     * 递归查询
     *
     * @param pid
     * @param childIds
     * @return
     */
    public List<Long> getAllChild(Long pid, List<Long> childIds) {
        // 判断当前节点是否已经处理过
        if (!childIds.contains(pid)) {
            childIds.add(pid); // 添加当前节点的ID
            LambdaQueryWrapper<MesProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MesProduct::getPid, pid);
            List<MesProductVo> list = productMapper.selectVoList(wrapper);
            if (ObjectUtils.isNotEmpty(list)) {
                for (MesProductVo vo : list) {
                    getAllChild(vo.getId(), childIds);
                }
            }
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

    public List<MesProductVo> getAllChilds(Long pid, int count, List<MesProductVo> childIds, Set<Long> processedIds) {
        // 判断当前节点是否已经处理过
        if (!processedIds.contains(pid)) {
            MesProductVo tvo = productMapper.selectVoById(pid);
            if(null!=tvo){
                tvo.setNeedCount(count);
                childIds.add(tvo);
                processedIds.add(pid);

                LambdaQueryWrapper<MesProduct> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(MesProduct::getPid, pid);
                List<MesProductVo> list = productMapper.selectVoList(wrapper);
                if (ObjectUtils.isNotEmpty(list)) {
                    for (MesProductVo vo : list) {
                        BigDecimal childCount = BigDecimal.valueOf(count).multiply(BigDecimal.valueOf(vo.getNeedCount()));

                        getAllChilds(vo.getId(), childCount.intValue(), childIds, processedIds);


                    }
                }
            }

        }
        return childIds;
    }
}
