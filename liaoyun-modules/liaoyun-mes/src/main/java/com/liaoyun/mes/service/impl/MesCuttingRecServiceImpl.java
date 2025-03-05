package com.liaoyun.mes.service.impl;

import cn.hutool.core.convert.Convert;
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
import com.liaoyun.mes.domain.vo.MesToolRecVo;
import com.liaoyun.mes.mapper.MesCuttingMapper;
import com.liaoyun.mes.mapper.MesCuttingUseMapper;
import com.liaoyun.mes.mapper.MesWorkplacesMapper;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesCuttingRecBo;
import com.liaoyun.mes.domain.vo.MesCuttingRecVo;
import com.liaoyun.mes.mapper.MesCuttingRecMapper;
import com.liaoyun.mes.service.IMesCuttingRecService;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 刀具记录审核Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-10-15
 */
@RequiredArgsConstructor
@Service
public class MesCuttingRecServiceImpl implements IMesCuttingRecService {

    private final MesCuttingRecMapper baseMapper;
    private final SysUserMapper sysUserMapper;
    private final MesWorkplacesMapper workplacesMapper;
    private final MesCuttingUseMapper cuttingUseMapper;
    private final MesCuttingMapper cuttingMapper;

    /**
     * 查询刀具记录审核
     *
     * @param id 主键
     * @return 刀具记录审核
     */
    @Override
    public MesCuttingRecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询刀具记录审核列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 刀具记录审核分页列表
     */
    @Override
    public TableDataInfo<MesCuttingRecVo> queryPageList(MesCuttingRecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesCuttingRec> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<SysUser> wrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(bo.getUsername())){
            wrapper.like(SysUser::getNickName,bo.getUsername());
            List<SysUser> list = sysUserMapper.selectList(wrapper);
            List<Long> ids=new ArrayList<>();
            for(SysUser sysUser:list){
                ids.add(sysUser.getUserId());
            }
            if(ids.size()>0){
                lqw.in(MesCuttingRec::getUserId,ids);
            }

        }
        Page<MesCuttingRecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesCuttingRecVo vo : result.getRecords()) {
            vo.setCuttingname(baseMapper.selectmainTitleById(vo.getToolid()));
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
            vo.setMesCuttingVo(cuttingMapper.selectVoById(vo.getToolid()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的刀具记录审核列表
     *
     * @param bo 查询条件
     * @return 刀具记录审核列表
     */
    @Override
    public List<MesCuttingRecVo> queryList(MesCuttingRecBo bo) {
        LambdaQueryWrapper<MesCuttingRec> lqw = buildQueryWrapper(bo);
        List<MesCuttingRecVo> list= baseMapper.selectVoList(lqw);
        for (MesCuttingRecVo vo : list) {
            vo.setCuttingname(baseMapper.selectmainTitleById(vo.getToolid()));
            vo.setUsername(baseMapper.getusername(vo.getUserId()));
        }
        return list;
    }

    private LambdaQueryWrapper<MesCuttingRec> buildQueryWrapper(MesCuttingRecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesCuttingRec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getToolid() != null, MesCuttingRec::getToolid, bo.getToolid());
        lqw.eq(bo.getCount() != null, MesCuttingRec::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getImgs()), MesCuttingRec::getImgs, bo.getImgs());
        lqw.eq(StringUtils.isNotBlank(bo.getRebak()), MesCuttingRec::getRebak, bo.getRebak());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), MesCuttingRec::getType, bo.getType());
        lqw.eq(bo.getUserId() != null, MesCuttingRec::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesCuttingRec::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getWorkId()), MesCuttingRec::getWorkId, bo.getWorkId());
        lqw.eq(bo.getUseId() != null, MesCuttingRec::getUseId, bo.getUseId());
        return lqw;
    }

    /**
     * 新增刀具记录审核
     *
     * @param bo 刀具记录审核
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesCuttingRecBo bo) {
        MesCuttingRec add = MapstructUtils.convert(bo, MesCuttingRec.class);
        if(null!=bo.getRebak()){
            LambdaQueryWrapper<MesCuttingUse> lqw = Wrappers.lambdaQuery();
            lqw.eq(MesCuttingUse::getToolid, add.getToolid());
            lqw.eq(MesCuttingUse::getUserId, add.getUserId());
            List<MesCuttingUse> list = cuttingUseMapper.selectList(lqw);
            MesCuttingUse cuttingUse=list.get(0);
            int res=Integer.parseInt(bo.getRebak())+Integer.parseInt(cuttingUse.getRebak());
            cuttingUse.setRebak(String.valueOf(res));
            return cuttingUseMapper.insert(cuttingUse)>0;
        }
        validEntityBeforeSave(add);
        String typeName = "";
        switch (add.getType()) {
            case "0": typeName = "领用"; break;
            case "1": typeName = "报废"; break;
            case "2": typeName = "退还"; break;
            case "3": typeName = "更换"; break;
            default: break;
        }
        add.setRebak(typeName);
        LambdaQueryWrapper<MesCuttingUse> wrapper1 = new LambdaQueryWrapper<>();
        Long logid=LoginHelper.getUserId();
        wrapper1.eq(MesCuttingUse::getToolid, add.getToolid())
            .eq(MesCuttingUse::getUserId,logid);
        List<MesCuttingUse> list1 = cuttingUseMapper.selectList(wrapper1);
        MesCuttingUse mesToolUse;
        if (!list1.isEmpty()) {
            mesToolUse = list1.get(0);
            if("0".equals(mesToolUse.getType())){
                mesToolUse.setCount(mesToolUse.getCount() + add.getCount());
            }
            cuttingUseMapper.updateById(mesToolUse);
        } else {
            mesToolUse = new MesCuttingUse();
            mesToolUse.setToolid(add.getToolid());
            mesToolUse.setCount(add.getCount());
            mesToolUse.setUserId(LoginHelper.getUserId());
            mesToolUse.setStatus("2");
            cuttingUseMapper.insert(mesToolUse);
        }
        add.setUseId(mesToolUse.getId());
        if (!"0".equals(add.getType())) {
            int ht=0;
            try {
                ht=mesToolUse.getCount().intValue();
            }catch (Exception e){}
            mesToolUse.setImgs(String.valueOf(Integer.parseInt(mesToolUse.getImgs())+ht));
            mesToolUse.setCount(mesToolUse.getCount() - ht);
            cuttingUseMapper.updateById(mesToolUse);
        }
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改刀具记录审核
     *
     * @param bo 刀具记录审核
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesCuttingRecBo bo) {
        MesCuttingRec update = MapstructUtils.convert(bo, MesCuttingRec.class);
        validEntityBeforeSave(update);
        boolean flag = false;
        if(StringUtils.equals(update.getStatus(),"1")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dates=sdf.format(System.currentTimeMillis());
            update.setRemark(dates);
            MesCuttingUse toolUse=cuttingUseMapper.selectById(update.getUseId());
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
        }
        flag = baseMapper.updateById(update) > 0;
        return flag;
    }
    private boolean updateStockAndUseStock(MesCuttingRec toolUse, Long count, boolean increment) {
        MesCutting etool = cuttingMapper.selectById(toolUse.getToolid());
        Long now = increment ? etool.getStocknum() - count : etool.getStocknum() + count;
        MesCuttingUse toolUse1=cuttingUseMapper.selectById(toolUse.getUseId());
        if(!increment){
            int strtoint= Integer.parseInt(toolUse1.getImgs())-toolUse.getCount().intValue();
            toolUse1.setImgs(String.valueOf(strtoint));

        }
        toolUse1.setStatus("1");
        cuttingUseMapper.updateById(toolUse1);
        if (toolUse1.getCount().intValue() == 0 && Integer.parseInt(toolUse1.getImgs()) == 0) {
            cuttingUseMapper.deleteById(toolUse1.getId());
        }

        if (now >= 0) {
            Long usecount = cuttingUseMapper.selectCount(new LambdaQueryWrapper<MesCuttingUse>().eq(MesCuttingUse::getToolid, toolUse.getToolid()));

            etool.setStocknum(now);
            etool.setUsestock(usecount);
            cuttingMapper.updateById(etool);

            return true;
        }

        return false;
    }

    private boolean updateStockAndOtStock(MesCuttingUse toolUse, Long count) {
        MesCutting etool = cuttingMapper.selectById(toolUse.getToolid());


        if (toolUse.getCount().intValue() == 0 && Convert.toInt(toolUse.getImgs()) == 0) {
            cuttingUseMapper.deleteById(toolUse.getId());
        }
        Long now = etool.getStocknum() - count;
        etool.setStocknum(now);
        etool.setOtstock(etool.getOtstock() + count);
        cuttingMapper.updateById(etool);
        return true;
    }
    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesCuttingRec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除刀具记录审核信息
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
