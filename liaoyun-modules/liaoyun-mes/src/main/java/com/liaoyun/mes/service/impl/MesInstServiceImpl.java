package com.liaoyun.mes.service.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRecordRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesInstBo;
import com.liaoyun.mes.service.IMesInstService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 其他入库Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-12
 */
@RequiredArgsConstructor
@Service
public class MesInstServiceImpl implements IMesInstService {

    private final MesInstMapper baseMapper;

    private final MesStockMapper stockMapper;

    private final MesInrecMapper inrecMapper;

    private final SysUserMapper sysUserMapper;

    private final MesUnitmeasureMapper unitmeasureMapper;

    private final MesAllrecMapper allrecMapper;

    private final MesCustomMapper mesCustomMapper;

    /**
     * 查询其他入库
     *
     * @param id 主键
     * @return 其他入库
     */
    @Override
    public MesInstVo queryById(Long id){

//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
//        OapiAttendanceListRecordRequest req = new OapiAttendanceListRecordRequest();
//        req.setUserIds("12");
//        req.setCheckDateFrom("21");
//        req.setCheckDateTo("21");
//        OapiUserGetResponse rsp = client.execute(req, accessToken);
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询其他入库列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 其他入库分页列表
     */
    @Override
    public TableDataInfo<MesInstVo> queryPageList(MesInstBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesInst> lqw = buildQueryWrapper(bo);
        Page<MesInstVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesInstVo vo : result.getRecords()) {
            List<Long> ids=baseMapper.selectTeamTitleById(vo.getRecids());
            LambdaQueryWrapper<MesStock> wrapper=new LambdaQueryWrapper<>();
            wrapper.in(MesStock::getId, ids);
            List<MesStock> stocks=stockMapper.selectList(wrapper);
            List<String> names=new ArrayList<>();
            if(!stocks.isEmpty()){
                for(MesStock stock:stocks){
                    names.add(stock.getTilte());
                }
            }
            try{
                vo.setUsername(sysUserMapper.selectById(vo.getCreateBy()).getNickName());
            }catch (Exception e){
                System.out.println(vo);
            }


            vo.setStockname(String.join(",",names));
            // 按逗号拆分字符串
            List<Long> longList=new ArrayList<>();
            List<MesInrecVo> list=new ArrayList<>();
            if (null!=vo.getRecids()){
                String[] numberArray = vo.getRecids().split(",");

                for (String numStr : numberArray) {
                    longList.add(Long.parseLong(numStr));
                }
                for (Long id : longList) {
                    MesInrecVo inrec=inrecMapper.selectVoById(id);
                    MesStockVo nstock=new MesStockVo();
                    try{
                        nstock=stockMapper.selectVoById(inrec.getStockid());
                        nstock.setUnitname(unitmeasureMapper.selectVoById(nstock.getUnitid()).getTitle());
                        MesCustom cc=mesCustomMapper.selectById(inrec.getCustId());
                        inrec.setCustom(cc);
                    }catch (Exception e){

                    }

                    inrec.setStockVo(nstock);
                    list.add(inrec);
                }
            }

            vo.setInrecVo(list);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的其他入库列表
     *
     * @param bo 查询条件
     * @return 其他入库列表
     */
    @Override
    public List<MesInstVo> queryList(MesInstBo bo) {
        LambdaQueryWrapper<MesInst> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesInst> buildQueryWrapper(MesInstBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesInst> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getRecids()), MesInst::getRecids, bo.getRecids());
        lqw.eq(bo.getStocktype() != null, MesInst::getStocktype, bo.getStocktype());
        lqw.eq(bo.getAllprice() != null, MesInst::getAllprice, bo.getAllprice());
        lqw.eq(StringUtils.isNotBlank(bo.getMet()), MesInst::getMet, bo.getMet());
        lqw.eq(StringUtils.isNotBlank(bo.getShui()), MesInst::getShui, bo.getShui());
        lqw.eq(StringUtils.isNotBlank(bo.getFromtype()), MesInst::getFromtype, bo.getFromtype());
        lqw.eq(StringUtils.isNotBlank(bo.getFiles()), MesInst::getFiles, bo.getFiles());
        lqw.eq(StringUtils.isNotBlank(bo.getRemark()), MesInst::getRemark, bo.getRemark());
        lqw.eq(bo.getIspri()!=null, MesInst::getIspri, bo.getIspri());
        lqw.eq(bo.getCtime() != null, MesInst::getCtime, bo.getCtime());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesInst::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增其他入库
     *
     * @param bo 其他入库
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesInstBo bo) {
        MesInst add = MapstructUtils.convert(bo, MesInst.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            List<Long> longList = new ArrayList<>();

            // 按逗号拆分字符串
            String[] numberArray = bo.getRecids().split(",");

            for (String numStr : numberArray) {
                longList.add(Long.parseLong(numStr));
            }
            for (Long id : longList) {
                MesInrec vo=inrecMapper.selectById(id);
                vo.setRemark2(add.getId().toString());
                inrecMapper.updateById(vo);
            }
        }
        return flag;
    }

    /**
     * 修改其他入库
     *
     * @param bo 其他入库
     * @return 是否修改成功
     */
    @Override
    @Transactional
    public Boolean updateByBo(MesInstBo bo) {
        MesInst update = MapstructUtils.convert(bo, MesInst.class);
        validEntityBeforeSave(update);
        if(StringUtils.equals(bo.getStatus(),"1")){
            List<Long> longList = new ArrayList<>();

            // 按逗号拆分字符串
            String[] numberArray = bo.getRecids().split(",");

            for (String numStr : numberArray) {
                longList.add(Long.parseLong(numStr));
            }
            for(Long id:longList){
                MesInrec mesInrec = inrecMapper.selectById(id);
                MesStock stock = stockMapper.selectById(mesInrec.getStockid());
                BigDecimal nes = mesInrec.getCount();
                BigDecimal newStock = nes.add(stock.getNowstock());
                stock.setNowstock(newStock);
                stockMapper.updateById(stock);
                MesAllrec allrec=new MesAllrec();
                allrec.setFromtype(mesInrec.getFromtype());
                allrec.setRemark(mesInrec.getRemark());
                allrec.setCounts(mesInrec.getCount());
                allrec.setType("入库");
                allrec.setPrices(mesInrec.getNprice());
                allrec.setStockid(mesInrec.getStockid());
                String cname="";
                try{
                    MesCustomVo customVo=mesCustomMapper.selectVoById(mesInrec.getCustId());
                    cname=customVo.getTitle();
                }catch (Exception e){

                }

                allrec.setCname(cname);
                allrecMapper.insert(allrec);
            }
        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesInst entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除其他入库信息
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
        for (Long id : ids) {
            MesInst inst = baseMapper.selectById(id);
            inst.setRemark2("手动删除");
            baseMapper.updateById(inst);
            List<String> tearms = Arrays.asList(inst.getRecids().split(","));
            List<Long> longList = tearms.stream()
                .map(Long::parseLong)
                .toList();
            for (Long idst:longList){
                MesInrec mesInrec = inrecMapper.selectById(idst);
                MesStock stock = stockMapper.selectById(mesInrec.getStockid());
                BigDecimal nes = mesInrec.getCount();
                BigDecimal newStock = stock.getNowstock().subtract(mesInrec.getCount());
                stock.setNowstock(newStock);
                stockMapper.updateById(stock);
                mesInrec.setRemark2("手动删除");
                inrecMapper.updateById(mesInrec);
                inrecMapper.deleteById(mesInrec);
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
