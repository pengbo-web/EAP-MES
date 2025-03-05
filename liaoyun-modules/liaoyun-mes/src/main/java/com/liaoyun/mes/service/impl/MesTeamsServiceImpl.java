package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.mes.domain.MesTask;
import com.liaoyun.mes.domain.MesWorkRec;
import com.liaoyun.mes.mapper.MesTaskMapper;
import com.liaoyun.mes.mapper.MesWorkRecMapper;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesTeamsBo;
import com.liaoyun.mes.domain.vo.MesTeamsVo;
import com.liaoyun.mes.domain.MesTeams;
import com.liaoyun.mes.mapper.MesTeamsMapper;
import com.liaoyun.mes.service.IMesTeamsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 班组管理Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-10-25
 */
@RequiredArgsConstructor
@Service
public class MesTeamsServiceImpl implements IMesTeamsService {

    private final MesTeamsMapper baseMapper;
    private final SysUserMapper sysUserMapper;
    private final MesTaskMapper taskMapper;
    private final MesWorkRecMapper workRecMapper;

    /**
     * 查询班组管理
     *
     * @param id 主键
     * @return 班组管理
     */
    @Override
    public MesTeamsVo queryById(Long id){

        MesTeamsVo vo=baseMapper.selectVoById(id);
        List<Long> a=new ArrayList<>();
        List<Long> b=new ArrayList<>();
        try{
            for(String id1:Arrays.stream(vo.getUseids().split(",")).toList()){
                a.add(Long.valueOf(id1));
            }
            for (String id2 : Arrays.stream(vo.getUpids().split(",")).toList()){
                b.add(Long.valueOf(id2));
            }
        }catch (Exception e){

        }
        vo.setUseidsList(a);
        vo.setUpidsList(b);
        return vo;
    }

    /**
     * 分页查询班组管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 班组管理分页列表
     */
    @Override
    public TableDataInfo<MesTeamsVo> queryPageList(MesTeamsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesTeams> lqw = buildQueryWrapper(bo);
        Page<MesTeamsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesTeamsVo vo : result.getRecords()) {
            String[] equlist= baseMapper.selectTitleById(vo.getUseids()).toArray(new String[0]);
            vo.setStname(String.join(StringUtils.SEPARATOR, equlist));
            List<Long> a=new ArrayList<>();
            List<Long> b=new ArrayList<>();
            try{
                for(String id:Arrays.stream(vo.getUseids().split(",")).toList()){
                    a.add(Long.valueOf(id));
                }
                for (String id2 : Arrays.stream(vo.getUpids().split(",")).toList()){
                    b.add(Long.valueOf(id2));
                }
            }catch (Exception e){

            }
            vo.setUseidsList(a);
            vo.setUpidsList(b);
            String[] upname=baseMapper.selectTitleById(vo.getUpids()).toArray(new String[0]);
            vo.setUpnames(String.join(StringUtils.SEPARATOR, upname));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的班组管理列表
     *
     * @param bo 查询条件
     * @return 班组管理列表
     */
    @Override
    public List<MesTeamsVo> queryList(MesTeamsBo bo) {
        LambdaQueryWrapper<MesTeams> lqw = buildQueryWrapper(bo);
        List<MesTeamsVo> list=baseMapper.selectVoList(lqw);
        List<MesTeamsVo> vos=new ArrayList<MesTeamsVo>();
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 获取本月的第一天和最后一天
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        //获取到UserId
        for (MesTeamsVo vo : list) {
            String[] equlist= vo.getUseids().split(",");
            for (String s : equlist) {
                //用户名
                MesTeamsVo vo1=new MesTeamsVo();
                SysUser user=sysUserMapper.selectById(Long.parseLong(s));
                vo1.setUsername(user.getNickName());

                //查询工作任务（
                List<MesTask> taskList= DataPermissionHelper.ignore(() -> {
                    LambdaQueryWrapper<MesTask> wrapper=new LambdaQueryWrapper<>();
                    // 添加 createTime 字段的条件：在本月内
                    wrapper.between(MesTask::getCreateTime, firstDayOfMonth.atStartOfDay(), lastDayOfMonth.atTime(23, 59, 59));
                    wrapper.eq(MesTask::getUserId,Long.parseLong(s));
                    return taskMapper.selectList(wrapper);
                });
                LambdaQueryWrapper<MesWorkRec> wrapper2=new LambdaQueryWrapper<>();
                wrapper2.eq(MesWorkRec::getUserId,Long.parseLong(s));
                wrapper2.eq(MesWorkRec::getStatus,"2");
                wrapper2.between(MesWorkRec::getCreateTime, firstDayOfMonth.atStartOfDay(), lastDayOfMonth.atTime(23, 59, 59));
                Long workcount=workRecMapper.selectCount(wrapper2);
                int noneeau=0;
                int notcheck=0;
                for (MesTask task : taskList) {
                    if(task.getChecknum().intValue()>0){
                        noneeau++;
                    }
                    if(task.getIschecknew()>0){
                        notcheck++;
                    }
                }
                //全部任务
                vo1.setMytask(String.valueOf(taskList.size()));
                //已质检列表
                vo1.setQualist(String.valueOf(noneeau));
                //查询工作数量
                vo1.setWorknum(String.valueOf(workcount));
                //用户id
                vo1.setUserid(s);
                //查询异常结果
                vo1.setYichang(String.valueOf(notcheck));
                vos.add(vo1);
            }
        }
        return vos;
    }

    private LambdaQueryWrapper<MesTeams> buildQueryWrapper(MesTeamsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesTeams> lqw = Wrappers.lambdaQuery();
        lqw.in(StringUtils.isNotBlank(bo.getUseids()), MesTeams::getUseids, bo.getUseids());
        lqw.in(StringUtils.isNotBlank(bo.getUpids()), MesTeams::getUpids, bo.getUpids());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesTeams::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增班组管理
     *
     * @param bo 班组管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesTeamsBo bo) {
        MesTeams add = MapstructUtils.convert(bo, MesTeams.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改班组管理
     *
     * @param bo 班组管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesTeamsBo bo) {
        MesTeams update = MapstructUtils.convert(bo, MesTeams.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesTeams entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除班组管理信息
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
