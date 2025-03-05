package com.liaoyun.mes.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.service.DeptService;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesWorkplacesBo;
import com.liaoyun.mes.service.IMesWorkplacesService;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工作站Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-06-05
 */
@RequiredArgsConstructor
@Service
public class MesWorkplacesServiceImpl implements IMesWorkplacesService {

    private final MesWorkplacesMapper baseMapper;
    private final DeptService deptService;
    private final MesProcessMapper processMapper;
    private final MesEquipmentMapper equipmentMapper;

    private final MesEquRecMapper equRecMapper;
    private final MesEtoolMapper etoolMapper;
    private final MesToolUseMapper toolUseMapper;

    private final MesCuttingMapper cuttingMapper;
    private final MesCuttingUseMapper cuttingUseMapper;
    private final MesCuttingRecMapper cuttingRecMapper;
    /**
     * 查询工作站
     *
     * @param id 主键
     * @return 工作站
     */
    @Override
    public MesWorkplacesVo queryById(Long id){
        MesWorkplacesVo vo= baseMapper.selectVoById(id);
        if(null!=vo){
            if(null!=vo.getProcessIds()){
                String[] prolist= baseMapper.selectProcessTitleById(vo.getProcessIds()).toArray(new String[0]);
                vo.setProcessIdsarray(vo.getProcessIds().split(","));
                vo.setProcessNames(String.join(StringUtils.SEPARATOR, prolist));
            }
            if (null!=(vo.getEquIds())){
                String[] equlist= baseMapper.selectEquTitleById(vo.getEquIds()).toArray(new String[0]);
                vo.setEquIdsarray(vo.getEquIds().split(","));
                vo.setEquIdnames(String.join(StringUtils.SEPARATOR, equlist));
            }
            if(null!=(vo.getTeams())){
                String[] teamlist= baseMapper.selectTeamTitleById(vo.getTeams()).toArray(new String[0]);
                vo.setTeamsarray(vo.getTeams().split(","));
                vo.setTeamnames(String.join(StringUtils.SEPARATOR, teamlist));
            }
            try{
                if(ObjectUtils.isNotNull(vo.getTongsIds())){
                    String[] tonglist= baseMapper.selecttongsTitleById(vo.getTongsIds()).toArray(new String[0]);
                    vo.setTongarray(tonglist);
                    List<MesEtool> toolUses=new ArrayList<>();
                    String[] li=vo.getTongsIds().split(",");
                    for (String ttid:li){
                        MesToolUse toolUse=toolUseMapper.selectById(Long.valueOf(ttid));
                        MesEtool etool=etoolMapper.selectById(toolUse.getToolid());
                        etool.setCount(toolUse.getCount().intValue());
                        toolUses.add(etool);
                    }
                    vo.setToolUses(toolUses);
                    vo.setTongames(String.join(StringUtils.SEPARATOR, tonglist));
                }
            }catch (Exception e){

            }
            if(null!=(vo.getWorkshopId())){
                vo.setWorkshopName(baseMapper.selectWorkplaceTitleById(vo.getWorkshopId()));
            }
        }

        return vo;
    }

    /**
     * 分页查询工作站列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 工作站分页列表
     */
    @Override
    public TableDataInfo<MesWorkplacesVo> queryPageList(MesWorkplacesBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesWorkplaces> lqw = buildQueryWrapper(bo);
        Page<MesWorkplacesVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesWorkplacesVo vo:result.getRecords()){

            //以后处理质检显示问题
            if(ObjectUtils.isNotNull(vo.getProcessIds())){
                List<String> prolist=new ArrayList<>();
                for (String ttid:vo.getProcessIds().split(",")){
                    //查询质检类别
                    MesProcessVo voinfo=processMapper.selectVoById(ttid);
                    if(ObjectUtils.isNotNull(vo.getRemark())){
                        if(vo.getRemark().equals("1")){
                            prolist.add(voinfo.getTypename());
                        }
                    }
                    else{
                        prolist.add(voinfo.getTitle());
                    }
                }
//                String[] prolist= baseMapper.selectProcessTitleById(vo.getProcessIds()).toArray(new String[0]);
                vo.setProcessIdsarray(prolist.toArray(new String[prolist.size()]));
                vo.setProcessNames(String.join(StringUtils.SEPARATOR, prolist));
            }
            if (ObjectUtils.isNotNull(vo.getEquIds())){
                String[] equlist= baseMapper.selectEquTitleById(vo.getEquIds()).toArray(new String[0]);
                vo.setEquIdsarray(equlist);
                vo.setEquIdnames(String.join(StringUtils.SEPARATOR, equlist));
            }
            if(ObjectUtils.isNotNull(vo.getTeams())){
                String[] teamlist= baseMapper.selectTeamTitleById(vo.getTeams()).toArray(new String[0]);
                vo.setEquIdsarray(teamlist);
               vo.setTeamnames(String.join(StringUtils.SEPARATOR, teamlist));
            }
            try{
                if(ObjectUtils.isNotNull(vo.getTongsIds())){
                    String[] tonglist= baseMapper.selecttongsTitleById(vo.getTongsIds()).toArray(new String[0]);
                    vo.setTongarray(tonglist);
                    List<MesEtool> toolUses=new ArrayList<>();
                    String[] li=vo.getTongsIds().split(",");
                    for (String ttid:li){
                        MesToolUse toolUse=toolUseMapper.selectById(Long.valueOf(ttid));
                        MesEtool etool=etoolMapper.selectById(toolUse.getToolid());
                        etool.setCount(toolUse.getCount().intValue());
                        toolUses.add(etool);
                    }
                    vo.setToolUses(toolUses);
                    vo.setTongames(String.join(StringUtils.SEPARATOR, tonglist));
                }
            }catch (Exception e){

            }

            if(ObjectUtils.isNotNull(vo.getWorkshopId())){
                vo.setWorkshopName(baseMapper.selectWorkplaceTitleById(vo.getWorkshopId()));
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的工作站列表
     *
     * @param bo 查询条件
     * @return 工作站列表
     */
    @Override
    public List<MesWorkplacesVo> queryList(MesWorkplacesBo bo) {
        LambdaQueryWrapper<MesWorkplaces> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public List<MesEquipmentVo> quertallgetlist(){
        Long te= LoginHelper.getDeptId();
        LambdaQueryWrapper<MesWorkplaces> lqw = new LambdaQueryWrapper<>();
        lqw.like(MesWorkplaces::getTeams,te);
        List<MesWorkplaces> list=baseMapper.selectList(lqw);
        Set<Long> idSet = new HashSet<>();
        for (MesWorkplaces mesWorkplaces:list){
            if (ObjectUtils.isNotNull(mesWorkplaces.getEquIds())){
                Long[] svaid= Convert.toLongArray(mesWorkplaces.getEquIds().split(","));
                idSet.addAll(Arrays.asList(svaid));
            }
        }
        List<Long> ids = new ArrayList<>(idSet);  // 转换为 List
        LambdaQueryWrapper<MesEquipment> lqw1 = new LambdaQueryWrapper<>();
        if(ids.size()>0){
            lqw1.in(MesEquipment::getId,ids);
        }else{
            lqw1.eq(MesEquipment::getId,0);
        }
        List<MesEquipmentVo> equipmentVos=equipmentMapper.selectVoList(lqw1);
        for (MesEquipmentVo equipmentVo:equipmentVos){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dates=sdf.format(System.currentTimeMillis());
            String beg=dates+" 00:00:00";
            String end=dates+" 23:59:59";
            //查询当日是否完成
            LambdaQueryWrapper<MesEquRec> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesEquRec::getEquid,equipmentVo.getId());
            wrapper.between(MesEquRec::getCreateTime,beg,end);
            List<MesEquRecVo> list1=equRecMapper.selectVoList(wrapper);
            equipmentVo.setRemark("今日未完成");
            equipmentVo.setTocolor("red");
            if(list1.size()>0){
                equipmentVo.setRemark("今日已完成");
                equipmentVo.setTocolor("green");
            }
        }

        return equipmentVos;
    }
    @Override
    public List<MesToolUse> gettonglist(){
        Long te = LoginHelper.getDeptId();
        LambdaQueryWrapper<MesWorkplaces> lqw = new LambdaQueryWrapper<>();
        lqw.like(MesWorkplaces::getTeams, te);
        List<MesWorkplaces> list = baseMapper.selectList(lqw);

        List<Long> idCountMap =new ArrayList<>();
        for (MesWorkplaces mesWorkplaces : list) {
            if (ObjectUtils.isNotNull(mesWorkplaces.getTongsIds())) {
                Long[] svaid = Convert.toLongArray(mesWorkplaces.getTongsIds().split(","));
                for (Long id : svaid) {
                    idCountMap.add(id);
                }
            }
        }


        List<MesToolUse> etoolVos = new ArrayList<>();
//        LambdaQueryWrapper<MesEtool> lqw1 = new LambdaQueryWrapper<>();
        if (!idCountMap.isEmpty()) {
            for (Long id : idCountMap) {
                MesToolUse etoolUse=toolUseMapper.selectById(id);
                try{
                    etoolUse.setMesEtool(etoolMapper.selectById(etoolUse.getToolid()));
                }catch (Exception e){

                }
                etoolVos.add(etoolUse);
            }
            // 将重复数量添加到相应对象的 counts 字段中
//            for (MesEtoolVo etoolVo : etoolVos) {
//                Long id = etoolVo.getId();
//                if (idCountMap.containsKey(id)) {
//                    etoolVo.setCounts(idCountMap.get(id));
//                }
//            }
        }

// 此时 etoolVos 中的 MesEtoolVo 对象包含了对应 id 的重复数量
//        for (MesEtoolVo etoolVo:etoolVos){
//            LambdaQueryWrapper<MesToolRec> wrapper=new LambdaQueryWrapper<>();
//            wrapper.eq(MesToolRec::getId,etoolVo.getId());
//                for(Long id:idSet){
//wrapper.or().like(MesToolRec::getId,String.valueOf(id));
//                }
//        }
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String dates=sdf.format(System.currentTimeMillis());
//            String beg=dates+" 00:00:00";
//            String end=dates+" 23:59:59";
//            //查询当日是否完成
//            LambdaQueryWrapper<MesEquRec> wrapper=new LambdaQueryWrapper<>();
//            wrapper.eq(MesEquRec::getEquid,equipmentVo.getId());
//            wrapper.between(MesEquRec::getCreateTime,beg,end);
//            List<MesEquRecVo> list1=equRecMapper.selectVoList(wrapper);
//            equipmentVo.setRemark("今日未完成");
//            equipmentVo.setTocolor("red");
//            if(list1.size()>0){
//                equipmentVo.setRemark("今日已完成");
//                equipmentVo.setTocolor("green");
//            }
//        }

        return etoolVos;
    }
    @Override
    public List<MesCuttingUse> getcuttinglist(){
        Long te = LoginHelper.getDeptId();
        LambdaQueryWrapper<MesWorkplaces> lqw = new LambdaQueryWrapper<>();
        lqw.like(MesWorkplaces::getTeams, te);
        List<MesWorkplaces> list = baseMapper.selectList(lqw);
        List<Long> idCountMap =new ArrayList<>();
        for (MesWorkplaces mesWorkplaces : list) {
            if (ObjectUtils.isNotNull(mesWorkplaces.getTongsIds())) {
                Long[] svaid = Convert.toLongArray(mesWorkplaces.getTongsIds().split(","));
                for (Long id : svaid) {
                    idCountMap.add(id);
                }
            }
        }


        List<MesCuttingUse> cuttingUses = new ArrayList<>();
//        LambdaQueryWrapper<MesEtool> lqw1 = new LambdaQueryWrapper<>();
        if (!idCountMap.isEmpty()) {
            for (Long id : idCountMap) {
                MesCuttingUse etoolUse=cuttingUseMapper.selectById(id);
                try{
                    etoolUse.setMesCutting(cuttingMapper.selectById(etoolUse.getToolid()));
                }catch (Exception e){

                }
                cuttingUses.add(etoolUse);
            }
        }


        return cuttingUses;
    }

    private LambdaQueryWrapper<MesWorkplaces> buildQueryWrapper(MesWorkplacesBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesWorkplaces> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesWorkplaces::getSn, bo.getSn());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesWorkplaces::getTitle, bo.getTitle());
        lqw.eq(bo.getWorkshopId() != null, MesWorkplaces::getWorkshopId, bo.getWorkshopId());
        lqw.eq(StringUtils.isNotBlank(bo.getEquIds()), MesWorkplaces::getEquIds, bo.getEquIds());
        lqw.eq(StringUtils.isNotBlank(bo.getTeams()), MesWorkplaces::getTeams, bo.getTeams());
        lqw.eq(StringUtils.isNotBlank(bo.getTongsIds()), MesWorkplaces::getTongsIds, bo.getTongsIds());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessIds()), MesWorkplaces::getProcessIds, bo.getProcessIds());
        lqw.eq(bo.getCounts() != null, MesWorkplaces::getCounts, bo.getCounts());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesWorkplaces::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增工作站
     *
     * @param bo 工作站
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesWorkplacesBo bo) {

        if(null!=bo.getTongsIds()){
            bo.setTongsIds(String.join(",", bo.getTongsIdsarray()));
        }
        MesWorkplaces add = MapstructUtils.convert(bo, MesWorkplaces.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            if(null!=bo.getTongsIds()){
                for (String td:bo.getTongsIdsarray()){
                    MesToolUse toolUse=toolUseMapper.selectById(Long.valueOf(td));
                    if(toolUse!=null){
                        toolUse.setWorkId(add.getId().toString());
                        toolUseMapper.updateById(toolUse);
                        toolUseMapper.deleteall();
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 修改工作站
     *
     * @param bo 工作站
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesWorkplacesBo bo) {
        if(bo.getEquIdsarray().length>0) {
            bo.setEquIds(String.join(",", bo.getEquIdsarray()));
        }
        if(bo.getProcessIdsarray().length>0) {
            bo.setProcessIds(String.join(",", bo.getProcessIdsarray()));
        }
        if(bo.getTeamsarray().length>0) {
            bo.setTeams(String.join(",", bo.getTeamsarray()));
        }
        if(null!=bo.getTongsIdsarray()){
            if(bo.getTongsIdsarray().length>0){
                bo.setTongsIds(String.join(",", bo.getTongsIdsarray()));
            }
        }
        MesWorkplaces update = MapstructUtils.convert(bo, MesWorkplaces.class);
        try{
            for (String td:bo.getTongsIdsarray()){
                LambdaQueryWrapper<MesToolUse> wrapper=new LambdaQueryWrapper<>();
                wrapper.eq(MesToolUse::getWorkId,update.getId());
                List<MesToolUse> list=toolUseMapper.selectList(wrapper);
                for (MesToolUse mtu:list){
                    mtu.setWorkId(null);
                    toolUseMapper.updateById(mtu);
                }
                MesToolUse toolUse=toolUseMapper.selectById(Long.valueOf(td));
                toolUse.setWorkId(update.getId().toString());
                toolUseMapper.updateById(toolUse);
                toolUseMapper.deleteall();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesWorkplaces entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除工作站信息
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
