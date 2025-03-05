package com.liaoyun.mes.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.common.sse.dto.SseMessageDto;
import com.liaoyun.common.sse.utils.SseMessageUtils;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesWorkBo;
import com.liaoyun.mes.service.IMesWorkService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 生产派单Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-03
 */
@RequiredArgsConstructor
@Service
public class MesWorkServiceImpl implements IMesWorkService {

    private final MesWorkMapper baseMapper;
    private final MesProductServiceImpl mesProductService;
    private final MesProductMapper productMapper;
    private final MesProsettingMapper mesProsettingMapper;
    private final MesOrderMapper orderMapper;
    private final MesStockMapper stockMapper;
    private final MesCustomMapper customMapperl;
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * 查询生产派单
     *
     * @param id 主键
     * @return 生产派单
     */
    @Override
    public MesWorkVo queryById(Long id){
        MesWorkVo vo=baseMapper.selectVoById(id);
//        vo.setProinfo(mesProductService.queryById(vo.getProId()));
        MesProductVo productVo=mesProductService.queryById(vo.getProId());
        vo.setProinfo(productVo);
        if(null!=vo.getOrderid()){
            MesOrderVo vo1=orderMapper.selectVoById(vo.getOrderid());
            vo.setOrderVo(vo1);
        }
        MesStockVo voc=stockMapper.selectVoById(productVo.getStocks());
        productVo.setStockname(voc.getGk());
        return vo;
    }

    /**
     * 分页查询生产派单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 生产派单分页列表
     */
    @Override
    public TableDataInfo<MesWorkVo> queryPageList(MesWorkBo bo, PageQuery pageQuery) throws ParseException {
        LambdaQueryWrapper<MesWork> lqw = buildQueryWrapper(bo);
        LambdaQueryWrapper<MesProduct> stockwra=new LambdaQueryWrapper<>();
        List<Long> ids=new ArrayList<>();
        if(StringUtils.isNotBlank(bo.getStockname())){
            stockwra.like(MesProduct::getTitle,bo.getStockname());
            List<MesProduct> list = productMapper.selectList(stockwra);
            if(!list.isEmpty()){
                ids=list.stream().map(MesProduct::getId).toList();
            }
        }
        if(!ids.isEmpty()){
            lqw.in(MesWork::getProId, ids);
        }
        Page<MesWorkVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesWorkVo vo:result.getRecords()){

//            if(vo.getTenantId().equals("802333")){
//                MesOrderVo vo1=orderMapper.selectVoById(vo.getOrderid());
//                vo.setOrderVo(vo1);
//                if(null!=vo1){
//                    MesProductVo productVo=mesProductService.queryById(vo.getProId());
//                    vo.setProinfo(productVo);
//                    if(null!=vo.getOrderid()){
//
//                    }
//                    if (null!=productVo){
//                        MesStockVo voc=stockMapper.selectVoById(productVo.getStocks());
//                        productVo.setStockname(voc.getGk());
//                    }else{
//                        baseMapper.updateFlag(vo.getId());
//                        break;
//                    }
//                    int prc=0;
//                    int ruc=0;
//                    try{
//                        prc=baseMapper.getpriCount(vo.getId());
//                        ruc=baseMapper.getincount(vo.getId());
//                    }catch (Exception e){
//                        System.out.println(e.getMessage());
//                    }
//
//                    vo.setPricount(String.valueOf(prc));
//                    vo.setInallcount(String.valueOf(ruc));
//                }else{
//
//                    baseMapper.updateFlag(vo.getId());
//                }

//            }else{
                MesOrderVo vo1=orderMapper.selectVoById(vo.getOrderid());
                vo.setOrderVo(vo1);
                if(null!=vo1){

                }
                MesProductVo productVo=mesProductService.queryById(vo.getProId());
                vo.setProinfo(productVo);
                if(null!=vo.getOrderid()){

                }
                if (null!=productVo){
                    MesStockVo voc=stockMapper.selectVoById(productVo.getStocks());
                    productVo.setStockname(voc.getGk());
                }
                int prc=0;
                int ruc=0;
                try{
                    prc=baseMapper.getpriCount(vo.getId());
                    ruc=baseMapper.getincount(vo.getId());
                }catch (Exception e){

                }

                vo.setPricount(String.valueOf(prc));
                vo.setInallcount(String.valueOf(ruc));
//            }
            try{
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));  // 设置输入时间为UTC
                Date date = inputFormat.parse(vo.getEndtime());
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                vo.setEndtime(outputFormat.format(date));
            }catch (Exception e){

            }


        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的生产派单列表
     *
     * @param bo 查询条件
     * @return 生产派单列表
     */
    @Override
    public List<MesWorkVo> queryList(MesWorkBo bo) {
        LambdaQueryWrapper<MesWork> lqw = buildQueryWrapper(bo);
        List<MesWorkVo> list=baseMapper.selectVoList(lqw);
        for (MesWorkVo vo:list){
            vo.setProinfo(mesProductService.queryById(vo.getProId()));
        }
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesWork> buildQueryWrapper(MesWorkBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesWork> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesWork::getSn, bo.getSn());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), MesWork::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getStatus()), MesWork::getStatus, bo.getStatus());
        lqw.eq(bo.getFromtype() != null, MesWork::getFromtype, bo.getFromtype());
        lqw.eq(bo.getProId() != null, MesWork::getProId, bo.getProId());
        lqw.eq(bo.getNeednum() != null, MesWork::getNeednum, bo.getNeednum());
        lqw.eq(bo.getIstop() != null, MesWork::getIstop, bo.getIstop());
        lqw.between(params.get("beginEndtime") != null && params.get("endEndtime") != null,
            MesWork::getEndtime ,params.get("beginEndtime"), params.get("endEndtime"));
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            MesWork::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增生产派单
     *
     * @param bo 生产派单
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesWorkBo bo) {
        MesWork add = MapstructUtils.convert(bo, MesWork.class);
        if (null==bo.getSn()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String datePart = sdf.format(new Date());
            Random random = new Random();
            int randomNumber = random.nextInt(9999999); // 生成六位随机数
            String orderNumber = "LLT" + datePart + String.format("%06d", randomNumber);
            add.setSn(orderNumber);
        }
        try{
            if(bo.getFromtype()==1){
                LambdaQueryWrapper<MesProduct> lqw = Wrappers.lambdaQuery();
                lqw.eq(MesProduct::getStocks,bo.getProId());
                MesProduct product=productMapper.selectList(lqw).get(0);
                add.setProId(product.getId());
                bo.setProId(product.getId());
                if(null!=bo.getOrderid()){
                    MesOrder order=orderMapper.selectById(bo.getOrderid());
                    order.setPricount(order.getPricount()+bo.getNeednum());
                    orderMapper.updateById(order);
                }
            }
            //递归
            LambdaQueryWrapper<MesProsetting> wrapper=new LambdaQueryWrapper<>();
            List<Long> listids=new ArrayList<>();
            List<Long> childids=getAllChild(bo.getProId(),listids);
            wrapper.in(MesProsetting::getProId,childids);
            List<MesProsettingVo> vo=mesProsettingMapper.selectVoList(wrapper);
            add.setProcesscount(Long.valueOf(vo.size()));
        }catch (Exception e){

        }



        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        scheduledExecutorService.schedule(() -> {
            SseMessageDto dto = new SseMessageDto();
            dto.setMessage("任务下达成功");
            dto.setUserIds(List.of(LoginHelper.getUserId()));
            SseMessageUtils.publishMessage(dto);
        }, 5, TimeUnit.SECONDS);
        return flag;
    }

    /**
     * 递归查询
     * @param pid
     * @param childIds
     * @return
     */
    public List<Long> getAllChild(Long pid,List<Long> childIds){
        // 判断当前节点是否已经处理过
        if (!childIds.contains(pid)) {
            childIds.add(pid);
            LambdaQueryWrapper<MesProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MesProduct::getPid, pid);
            List<MesProductVo> list = productMapper.selectVoList(wrapper);
            if (ObjectUtils.isNotEmpty(list)) {
                for (MesProductVo vo : list) {
                    if (vo.getMustbe().equals("0")) {
                        getAllChild(vo.getId(), childIds);
                    }
                }
            }
        }
        return childIds;
    }

    /**
     * 修改生产派单
     *
     * @param bo 生产派单
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesWorkBo bo) {
        LambdaQueryWrapper<MesProsetting> wrapper=new LambdaQueryWrapper<>();
        List<Long> listids=new ArrayList<>();
        List<Long> childids=getAllChild(bo.getProId(),listids);
        wrapper.in(MesProsetting::getProId,childids);
        List<MesProsettingVo> vo=mesProsettingMapper.selectVoList(wrapper);
        MesWork update = MapstructUtils.convert(bo, MesWork.class);
        update.setProcesscount(Long.valueOf(vo.size()));
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesWork entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除生产派单信息
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
