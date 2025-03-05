package com.liaoyun.mes.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesProcessType;
import com.liaoyun.mes.domain.MesWorkplace;
import com.liaoyun.mes.domain.MesWorkplaces;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.MesProcessTypeMapper;
import com.liaoyun.mes.mapper.MesWorkplaceMapper;
import com.liaoyun.mes.mapper.MesWorkplacesMapper;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesProcessBo;
import com.liaoyun.mes.domain.MesProcess;
import com.liaoyun.mes.mapper.MesProcessMapper;
import com.liaoyun.mes.service.IMesProcessService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ⑧工艺管理Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@RequiredArgsConstructor
@Service
public class MesProcessServiceImpl implements IMesProcessService {

    private final MesProcessMapper baseMapper;
    private final MesProcessTypeMapper typemapper;
    private final MesWorkplacesMapper workplacesMapper;
    private final SysUserMapper sysUserMapper;

    /**
     * 查询⑧工艺管理
     *
     * @param id 主键
     * @return ⑧工艺管理
     */
    @Override
    public MesProcessVo queryById(Long id){
        MesProcessVo vo=baseMapper.selectVoById(id);
        List<Long> a=new ArrayList<>();
        try{
            for(String id2:Arrays.stream(vo.getUserids().split(",")).toList()){
                a.add(Long.valueOf(id2));
            }
        }catch (Exception e){

        }
        vo.setIds(a);
        return vo;
    }

    /**
     * 分页查询⑧工艺管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ⑧工艺管理分页列表
     */
    @Override
    public TableDataInfo<MesProcessVo> queryPageList(MesProcessBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesProcess> lqw = buildQueryWrapper(bo);
        if(StringUtils.isNotEmpty(bo.getUserids())){
            LambdaQueryWrapper<SysUser> wrapper=new LambdaQueryWrapper<>();
            wrapper.like(SysUser::getNickName, bo.getUserids());
            List<SysUser> users=sysUserMapper.selectList(wrapper);
            if(users.size()>0){
                lqw.like(MesProcess::getUserids,users.get(0).getUserId());
            }
        }
        Page<MesProcessVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        // 关联查询 typename 字段
        for (MesProcessVo vo : result.getRecords()) {
            if(ObjectUtils.isNotNull(vo.getStandards())){
                String typename = baseMapper.selectTypeTitleById(Long.valueOf(vo.getStandards()));
                vo.setTypename(typename);
            }
            List<Long> a=new ArrayList<>();
            try{
                for(String id:Arrays.stream(vo.getUserids().split(",")).toList()){
                    a.add(Long.valueOf(id));
                }
            }catch (Exception e){

            }
            if(StringUtils.isNotBlank(vo.getUserids())){
                String[] equlist= baseMapper.selectTitleById(vo.getUserids()).toArray(new String[0]);
                vo.setUsernames(String.join(StringUtils.SEPARATOR, equlist));
            }

            vo.setIds(a);
        }
        return TableDataInfo.build(result);
    }
    /**
     * 分页查询⑧工艺管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ⑧工艺管理分页列表
     */
    @Override
    public TableDataInfo<MesProcessVo> queryPageListby(MesProcessBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesProcess> lqw = buildQueryWrapper(bo);
        //查询质检类别

//        List<MesProcessTypeVo> list=typemapper.selectVoList();
//        List<Long> ids=new ArrayList<>();
//        List<Long> idsh=new ArrayList<>();
//        for (MesProcessTypeVo vo:list){
//            if(0== Convert.toInt(vo.getStatus())){
//                ids.add(vo.getId());
//            }
//            if(1==Convert.toInt(vo.getStatus())){
//                idsh.add(vo.getId());
//            }
//        }
//        lqw.in(MesProcess::getTypeId,ids);
        //普通工序列表
        Page<MesProcessVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        //质检工序如果有只拿一条
//        List<MesProcessVo> mergedList = new ArrayList<>(result.getRecords());
//        for (Long toid:idsh){
//            LambdaQueryWrapper<MesProcess> lqwt=new LambdaQueryWrapper<>();
//            lqwt.eq(MesProcess::getTypeId,toid);
//            List<MesProcessVo> othlist=baseMapper.selectVoList(lqwt);
//            if(othlist.size()>0){
//                othlist.get(0).setRemark("1");
//                mergedList.add(othlist.get(0));
//            }
//        }
//        result.setRecords(mergedList);
//        // 关联查询 typename 字段
//        for (MesProcessVo vo : result.getRecords()) {
//            String typename = baseMapper.selectTypeTitleById(vo.get());
//            vo.setTypename(typename);
//        }
        return TableDataInfo.build(result);
    }


    @Override
    public List<MesProcessVo> queryListBystring(List<Long> bo) {
        LambdaQueryWrapper<MesWorkplaces> lqw = new LambdaQueryWrapper<>();
        lqw.in(MesWorkplaces::getId,bo);
        List<MesWorkplacesVo> list=workplacesMapper.selectVoList(lqw);
        List<MesProcessVo> alllist=new ArrayList<>();
        if(list.size()>0){
            for (MesWorkplacesVo vo:list){
                if(ObjectUtils.isNotNull(vo.getProcessIds())){
                    List<String> oid=Arrays.asList(vo.getProcessIds().split(","));
                    List<Long> longList = oid.stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
//                    ids.addAll(longList);
                    LambdaQueryWrapper<MesProcess> lq=new LambdaQueryWrapper<>();
                    lq.in(MesProcess::getId,longList);
                    List<MesProcessVo> volist=baseMapper.selectVoList(lq);
                    for (MesProcessVo vo1 : volist) {
                        vo1.setTitle(vo1.getTitle());
                        vo1.setWospname(vo.getTitle());
                        vo1.setWrid(vo.getId());
                    }
                    alllist.addAll(volist);
                }

            }
        }
            return alllist;


    }
    /**
     * 查询符合条件的⑧工艺管理列表
     *
     * @param bo 查询条件
     * @return ⑧工艺管理列表
     */
    @Override
    public List<MesProcessVo> queryList(MesProcessBo bo) {
        LambdaQueryWrapper<MesProcess> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesProcess> buildQueryWrapper(MesProcessBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesProcess> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesProcess::getSn, bo.getSn());
        lqw.eq(bo.getTypeId() != null, MesProcess::getTypeId, bo.getTypeId());
        lqw.eq(StringUtils.isNotBlank(bo.getStandards()), MesProcess::getStandards, bo.getStandards());
        lqw.eq(StringUtils.isNotBlank(bo.getRefvalue()), MesProcess::getRefvalue, bo.getRefvalue());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesProcess::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesProcess::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增⑧工艺管理
     *
     * @param bo ⑧工艺管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesProcessBo bo) {
        if(ObjectUtils.isNotNull(bo.getSn())){

        }
        MesProcess add = MapstructUtils.convert(bo, MesProcess.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改⑧工艺管理
     *
     * @param bo ⑧工艺管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesProcessBo bo) {
        MesProcess update = MapstructUtils.convert(bo, MesProcess.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesProcess entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除⑧工艺管理信息
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
