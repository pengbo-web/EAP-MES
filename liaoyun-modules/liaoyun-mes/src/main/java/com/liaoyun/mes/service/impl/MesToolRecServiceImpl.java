package com.liaoyun.mes.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.MesEquipmentVo;
import com.liaoyun.mes.mapper.MesEtoolMapper;
import com.liaoyun.mes.mapper.MesToolUseMapper;
import com.liaoyun.mes.mapper.MesWorkplacesMapper;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesToolRecBo;
import com.liaoyun.mes.domain.vo.MesToolRecVo;
import com.liaoyun.mes.mapper.MesToolRecMapper;
import com.liaoyun.mes.service.IMesToolRecService;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 夹具记录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@RequiredArgsConstructor
@Service
public class MesToolRecServiceImpl implements IMesToolRecService {

    private final MesToolRecMapper baseMapper;
    private final MesEtoolMapper etoolMapper;

    private final MesToolUseMapper toolUseMapper;
    private final SysUserMapper sysUserMapper;
    private final MesWorkplacesMapper workplacesMapper;
    /**
     * 查询夹具记录
     *
     * @param id 主键
     * @return 夹具记录
     */
    @Override
    public MesToolRecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询夹具记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 夹具记录分页列表
     */
    @Override
    public TableDataInfo<MesToolRecVo> queryPageList(MesToolRecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesToolRec> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<SysUser> wrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(bo.getUsername())){
            wrapper.like(SysUser::getNickName,bo.getUsername());
            List<SysUser> list = sysUserMapper.selectList(wrapper);
            List<Long> ids=new ArrayList<>();
            for(SysUser sysUser:list){
                ids.add(sysUser.getUserId());
            }
            if(ids.size()>0){
                lqw.in(MesToolRec::getUserId,ids);
            }

        }
        Page<MesToolRecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesToolRecVo vo : result.getRecords()) {
            vo.setToolname(baseMapper.selectmainTitleById(vo.getToolid()));
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的夹具记录列表
     *
     * @param bo 查询条件
     * @return 夹具记录列表
     */
    @Override
    public List<MesToolRecVo> queryList(MesToolRecBo bo) {
        LambdaQueryWrapper<MesToolRec> lqw = buildQueryWrapper(bo);
        List<MesToolRecVo> list= baseMapper.selectVoList(lqw);
        for (MesToolRecVo vo : list) {
            vo.setToolname(baseMapper.selectmainTitleById(vo.getToolid()));
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
        }
        return list;
    }

    private LambdaQueryWrapper<MesToolRec> buildQueryWrapper(MesToolRecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesToolRec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getToolid() != null, MesToolRec::getToolid, bo.getToolid());
        lqw.eq(bo.getCount() != null, MesToolRec::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getImgs()), MesToolRec::getImgs, bo.getImgs());
        lqw.eq(StringUtils.isNotBlank(bo.getRebak()), MesToolRec::getRebak, bo.getRebak());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), MesToolRec::getType, bo.getType());
        lqw.eq(bo.getUserId() != null, MesToolRec::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesToolRec::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增夹具记录
     *
     * @param bo 夹具记录
     * @return 是否新增成功
     */
    @Override
    @DSTransactional
    public Boolean insertByBo(MesToolRecBo bo) {
        MesToolRec add = MapstructUtils.convert(bo, MesToolRec.class);
        validEntityBeforeSave(add);

        // 构建工作地点查询条件
        Long deptId = LoginHelper.getDeptId();
        LambdaQueryWrapper<MesWorkplaces> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(MesWorkplaces::getTeams, deptId);
        List<MesWorkplaces> list = workplacesMapper.selectList(wrapper);

        StringJoiner joiner = new StringJoiner(",");
        list.forEach(workplace -> joiner.add(workplace.getId().toString()));

        String typeName = "";
        switch (add.getType()) {
            case "0": typeName = "领用"; break;
            case "1": typeName = "报废"; break;
            case "2": typeName = "退还"; break;
            case "3": typeName = "更换"; break;
            default: break;
        }
        add.setRebak(typeName);
        add.setWorkId(joiner.toString());

        LambdaQueryWrapper<MesToolUse> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(MesToolUse::getToolid, add.getToolid())
            .like(MesToolUse::getWorkId, list.get(0).getId());
        List<MesToolUse> list1 = toolUseMapper.selectList(wrapper1);
        MesToolUse mesToolUse;
        if (!list1.isEmpty()) {
             mesToolUse = list1.get(0);
            mesToolUse.setCount(mesToolUse.getCount() + add.getCount());
            toolUseMapper.updateById(mesToolUse);
        } else {
            mesToolUse = new MesToolUse();
            mesToolUse.setToolid(add.getToolid());
            mesToolUse.setCount(add.getCount());
            mesToolUse.setUserId(LoginHelper.getUserId());
            mesToolUse.setWorkId(list.get(0).getId().toString());
            toolUseMapper.insert(mesToolUse);
        }
        add.setUseId(mesToolUse.getId());
        if (!"0".equals(add.getType())) {

            int ht=0;
            try {
                ht=Integer.parseInt(mesToolUse.getImgs());
            }catch (Exception e){}
            mesToolUse.setImgs(String.valueOf(ht+1));
            mesToolUse.setCount(mesToolUse.getCount() - ht);
            toolUseMapper.updateById(mesToolUse);
        }
        return baseMapper.insert(add) > 0;
    }

    private boolean updateStockAndUseStock(MesToolRec toolUse, Long count, boolean increment) {
        MesEtool etool = etoolMapper.selectById(toolUse.getToolid());
        Long now = increment ? etool.getStocknum() - count : etool.getStocknum() + count;
        MesToolUse toolUse1=toolUseMapper.selectById(toolUse.getUseId());
        if(!increment){
            int strtoint= Integer.parseInt(toolUse1.getImgs())-toolUse.getCount().intValue();
            toolUse1.setImgs(String.valueOf(strtoint));
            toolUseMapper.updateById(toolUse1);
        }else{
            SysUser sysUser=sysUserMapper.selectById(toolUse1.getCreateBy());
            Long deptId = sysUser.getDeptId();
            LambdaQueryWrapper<MesWorkplaces> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(MesWorkplaces::getTeams, deptId);
            List<MesWorkplaces> list = workplacesMapper.selectList(wrapper);
            for (MesWorkplaces workplace : list) {
                workplace.setTongsIds(workplace.getTongsIds()+","+toolUse.getId());
                workplacesMapper.updateById(workplace);
            }

        }

        if (toolUse1.getCount().intValue() == 0 && Integer.parseInt(toolUse1.getImgs()) == 0) {
            toolUseMapper.deleteById(toolUse1.getId());
        }

        if (now >= 0) {
            Long usecount = toolUseMapper.selectCount(new LambdaQueryWrapper<MesToolUse>().eq(MesToolUse::getToolid, toolUse.getToolid()));

            etool.setStocknum(now);
            etool.setUsestock(usecount);
            etoolMapper.updateById(etool);

            return true;
        }

        return false;
    }

    private boolean updateStockAndOtStock(MesToolUse toolUse, Long count) {
        MesEtool etool = etoolMapper.selectById(toolUse.getToolid());


        if (toolUse.getCount().intValue() == 0 && Convert.toInt(toolUse.getImgs()) == 0) {
            toolUseMapper.deleteById(toolUse.getId());
        }
        Long now = etool.getStocknum() - count;
        etool.setStocknum(now);
        etool.setOtstock(etool.getOtstock() + count);
        etoolMapper.updateById(etool);
        return true;
    }

    /**
     * 修改夹具记录
     *
     * @param bo 夹具记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesToolRecBo bo) {
        MesToolRec update = MapstructUtils.convert(bo, MesToolRec.class);
        validEntityBeforeSave(update);
        boolean flag = false;
        if(StringUtils.equals(update.getStatus(),"1")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dates=sdf.format(System.currentTimeMillis());
            update.setRemark(dates);
            MesToolUse toolUse=toolUseMapper.selectById(update.getUseId());
            Long te = LoginHelper.getDeptId();
            LambdaQueryWrapper<MesWorkplaces> lqwt = new LambdaQueryWrapper<>();
            lqwt.like(MesWorkplaces::getTeams, te);
            List<MesWorkplaces> list = workplacesMapper.selectList(lqwt);
            switch (update.getType()) {
                case "0":
                    flag = updateStockAndUseStock(update, update.getCount(), true);
                    break;
                case "1"://报废
                    flag = updateStockAndOtStock(toolUse, update.getCount());
                    break;
                case "2"://退还
                case "3"://更换
                    flag = updateStockAndUseStock(update, update.getCount(), false);
                    break;
                default:
                    break;
            }

            if (flag) {
                for (MesWorkplaces mesWorkplaces : list) {
                    if (ObjectUtils.isNotNull(mesWorkplaces.getTongsIds())) {
                        String[] ids = mesWorkplaces.getTongsIds().split(",");
                        List<String> idList = new ArrayList<>(Arrays.asList(ids));
                        idList.remove(update.getToolid().toString());
                        String updatedIds = String.join(",", idList);
                        mesWorkplaces.setTongsIds(updatedIds);
                        workplacesMapper.updateById(mesWorkplaces);
                    }
                }
            }
        }
        flag = baseMapper.updateById(update) > 0;
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesToolRec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除夹具记录信息
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
