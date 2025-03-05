package com.liaoyun.mes.service.impl;

import cn.hutool.core.convert.Convert;
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
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesWorkRecBo;
import com.liaoyun.mes.service.IMesWorkRecService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工作记录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-09
 */
@RequiredArgsConstructor
@Service
public class MesWorkRecServiceImpl implements IMesWorkRecService {

    private final MesWorkRecMapper baseMapper;
    private final MesProsettingMapper prosettingMapper;
    private final MesProcessMapper processMapper;
    private final MesProductMapper productMapper;
    private final MesTaskMapper taskMapper;
    private final MesWorkMapper workMapper;
    private final MesStockMapper stockMapper;
    private final SysUserMapper sysUserMapper;
    /**
     * 查询工作记录
     *
     * @param id 主键
     * @return 工作记录
     */
    @Override
    public MesWorkRecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询工作记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 工作记录分页列表
     */
    @Override
    public TableDataInfo<MesWorkRecVo> queryPageList(MesWorkRecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesWorkRec> lqw = buildQueryWrapper(bo);
        if(StringUtils.isNotBlank(bo.getRemark())){
            lqw.isNotNull(MesWorkRec::getRemark);
            lqw.eq(MesWorkRec::getStatus,'0');
        }
        LambdaQueryWrapper<SysUser> wrapper=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(bo.getUsername())){
            wrapper.like(SysUser::getNickName,bo.getUsername());
            List<SysUser> list = sysUserMapper.selectList(wrapper);
            List<Long> ids=new ArrayList<>();
            for(SysUser sysUser:list){
                ids.add(sysUser.getUserId());
            }
            if(ids.size()>0){
                lqw.in(MesWorkRec::getUserId,ids);
            }
        }
        LambdaQueryWrapper<MesProduct> wrappe=new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(bo.getProname())){
            wrappe.like(MesProduct::getTitle,bo.getProname());
            List<MesProduct> stockList = productMapper.selectList(wrappe);
            if(stockList.size()>0){
                List<Long> ids=new ArrayList<>();
                for(MesProduct stock:stockList){
                    ids.add(stock.getId());
                }
                lqw.in(MesWorkRec::getProid,ids);
            }
        }
        Page<MesWorkRecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesWorkRecVo vo : result.getRecords()) {
            vo.setUsername(baseMapper.selecttteById(vo.getUserId()));
            vo.setProname(baseMapper.selecproname(vo.getProid()));
            if(null!=vo.getProid()){
                MesProductVo productVo=productMapper.selectVoById(vo.getProid());
                vo.setProductVo(productVo);
            }
            if(null!=vo.getRemark()){
                MesWorkVo workVo=workMapper.selectVoById(vo.getRemark());
                MesStockVo stockVo=stockMapper.selectVoById(vo.getProid());
                vo.setStockVo(stockVo);
                vo.setWorkVo(workVo);
            }
            if(null!=vo.getTaskId()){
                try{
                    MesTaskVo taskVo=taskMapper.selectVoById(vo.getTaskId());
                    vo.setTaskVo(taskVo);
                    MesProcessVo processVo=processMapper.selectVoById(taskVo.getCurrproid());
                    vo.setProcessVo(processVo);
                }catch (Exception e){

                }

            }
        }
        return TableDataInfo.build(result);
    }

    @Override
    public Map<String,String> getinfo(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dates = sdf.format(System.currentTimeMillis());
        String beg = dates + " 00:00:00";
        String end = dates + " 23:59:59";

        // 查询当日是否完成
        LambdaQueryWrapper<MesWorkRec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MesWorkRec::getCreateBy, LoginHelper.getUserId());
        wrapper.eq(MesWorkRec::getStatus,"2");
        wrapper.between(MesWorkRec::getCreateTime, beg, end);

        List<MesWorkRecVo> list1 = baseMapper.selectVoList(wrapper);
        int todayCount = 0;
        // 查询所有任务
        LambdaQueryWrapper<MesWorkRec> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(MesWorkRec::getCreateBy, LoginHelper.getUserId());
        wrapper2.eq(MesWorkRec::getStatus,"2");
        List<MesWorkRecVo> list2 = baseMapper.selectVoList(wrapper2);
        int allcount=0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalPrice2 = BigDecimal.ZERO;
        // 查询价格并计算总价
        for (MesWorkRecVo vo : list2) {
            if(null==vo.getCount()){
                vo.setCount(Long.valueOf(0));
            }
            allcount+=vo.getCount();
            LambdaQueryWrapper<MesProsetting> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(MesProsetting::getSort, vo.getSort());
            wrapper1.eq(MesProsetting::getProId, vo.getProid());
            List<MesProsettingVo> list = prosettingMapper.selectVoList(wrapper1);

            if (!list.isEmpty()) {
                MesProsettingVo proinfo = list.get(0);
                MesProcessVo proceinfo = processMapper.selectVoById(proinfo.getProcessId());

                try {
                    BigDecimal price = proceinfo.getMoneyprice().multiply(BigDecimal.valueOf(vo.getCount()));
                    totalPrice = totalPrice.add(price); // 累加总价
                } catch (Exception e) {
                    // 异常处理
                }
            }
        }
        // 当天酬劳
        for (MesWorkRecVo vo : list1) {
            if(null==vo.getCount()){
                vo.setCount(Long.valueOf(0));
            }
            todayCount+=vo.getCount();
            LambdaQueryWrapper<MesProsetting> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(MesProsetting::getSort, vo.getSort());
            wrapper1.eq(MesProsetting::getProId, vo.getProid());
            List<MesProsettingVo> list = prosettingMapper.selectVoList(wrapper1);

            if (!list.isEmpty()) {
                MesProsettingVo proinfo = list.get(0);
                MesProcessVo proceinfo = processMapper.selectVoById(proinfo.getProcessId());

                try {
                    BigDecimal price = proceinfo.getMoneyprice().multiply(BigDecimal.valueOf(vo.getCount()));
                    totalPrice2 = totalPrice2.add(price); // 累加总价
                } catch (Exception e) {
                    // 异常处理
                }
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("today", String.valueOf(todayCount));
        map.put("all", String.valueOf(allcount));
        map.put("priceall", totalPrice.toString());
        map.put("npriceall", totalPrice2.toString());

        return map;
    }

    /**
     * 查询符合条件的工作记录列表
     *
     * @param bo 查询条件
     * @return 工作记录列表
     */
    @Override
    public List<MesWorkRecVo> queryList(MesWorkRecBo bo) {
        LambdaQueryWrapper<MesWorkRec> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesWorkRec> buildQueryWrapper(MesWorkRecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesWorkRec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, MesWorkRec::getUserId, bo.getUserId());
        lqw.eq(bo.getTaskId() != null, MesWorkRec::getTaskId, bo.getTaskId());
        lqw.eq(bo.getLastcount() != null, MesWorkRec::getLastcount, bo.getLastcount());
        lqw.eq(bo.getCount() != null, MesWorkRec::getCount, bo.getCount());
        lqw.eq(bo.getSort() != null, MesWorkRec::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesWorkRec::getStatus, bo.getStatus());
        lqw.eq(bo.getMustbe() != null, MesWorkRec::getMustbe, bo.getMustbe());
        lqw.between(params.get("beginEndtime") != null && params.get("endEndtime") != null,
            MesWorkRec::getUpdateTime ,params.get("beginEndtime"), params.get("endEndtime"));
        return lqw;
    }

    /**
     * 新增工作记录
     *
     * @param bo 工作记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesWorkRecBo bo) {
        MesWorkRec add = MapstructUtils.convert(bo, MesWorkRec.class);
        add.setUserId(LoginHelper.getUserId());
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改工作记录
     *
     * @param bo 工作记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesWorkRecBo bo) {
        MesWorkRec update = MapstructUtils.convert(bo, MesWorkRec.class);
        validEntityBeforeSave(update);
        if(update.getStatus().equals("3")){
            if(null!=update.getRemark()){
                MesWork workVo = workMapper.selectById(update.getRemark());
                workVo.setCurrnum(workVo.getCurrnum()+bo.getCount().intValue());
                workMapper.updateById(workVo);
            }

        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesWorkRec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除工作记录信息
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
