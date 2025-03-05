package com.liaoyun.system.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.system.domain.bo.MesInventoryLedgerBo;
import com.liaoyun.system.domain.vo.MesInventoryLedgerVo;
import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import com.liaoyun.system.service.IMesInventoryLedgerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 库存流水
Service业务层处理
 *
 * @author Liao Liao
 * @date 2025-02-28
 */
@RequiredArgsConstructor
@Service
public class MesInventoryLedgerServiceImpl implements IMesInventoryLedgerService {

    private final MesInventoryLedgerMapper baseMapper;

    /**
     * 查询库存流水

     *
     * @param sn 主键
     * @return 库存流水

     */
    @Override
    public MesInventoryLedgerVo queryById(Long sn){
        return baseMapper.selectVoById(sn);
    }

    /**
     * 分页查询库存流水
列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 库存流水
分页列表
     */
    @Override
    public TableDataInfo<MesInventoryLedgerVo> queryPageList(MesInventoryLedgerBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesInventoryLedger> lqw = buildQueryWrapper(bo);
        Page<MesInventoryLedgerVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的库存流水
列表
     *
     * @param bo 查询条件
     * @return 库存流水
列表
     */
    @Override
    public List<MesInventoryLedgerVo> queryList(MesInventoryLedgerBo bo) {
        LambdaQueryWrapper<MesInventoryLedger> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesInventoryLedger> buildQueryWrapper(MesInventoryLedgerBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesInventoryLedger> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(MesInventoryLedger::getSn);
        lqw.eq(bo.getStockid() != null, MesInventoryLedger::getStockid, bo.getStockid());
        lqw.eq(bo.getLocationId() != null, MesInventoryLedger::getLocationId, bo.getLocationId());
        lqw.eq(bo.getQuantity() != null, MesInventoryLedger::getQuantity, bo.getQuantity());
        lqw.eq(bo.getOperation() != null, MesInventoryLedger::getOperation, bo.getOperation());
        lqw.eq(StringUtils.isNotBlank(bo.getBatchNo()), MesInventoryLedger::getBatchNo, bo.getBatchNo());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesInventoryLedger::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增库存流水

     *
     * @param bo 库存流水

     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesInventoryLedgerBo bo) {
        MesInventoryLedger add = MapstructUtils.convert(bo, MesInventoryLedger.class);
        validEntityBeforeSave(add);
        add.setOperation(1);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setSn(add.getSn());
        }
        return flag;
    }

    @Override
    public Boolean outByBo(MesInventoryLedgerBo bo) {
        MesInventoryLedger add = MapstructUtils.convert(bo, MesInventoryLedger.class);
        int rec=0;
        try{


            if(bo.getLocationId()==0){
                bo.setLocationId(null);
            }
            Map<String,Object> map=new HashMap<>();
            map.put("stockid",bo.getStockid());
            map.put("count",bo.getQuantity());
            map.put("kwid",add.getLocationId());
            map.put("tenantid", LoginHelper.getTenantId());
            map.put("msg",0);
            baseMapper.callInOutProcedure(map);
//
            if (map.get("msg").equals(1)) {
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    /**
     * 修改库存流水

     *
     * @param bo 库存流水

     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesInventoryLedgerBo bo) {
        MesInventoryLedger update = MapstructUtils.convert(bo, MesInventoryLedger.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesInventoryLedger entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除库存流水
信息
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
        return baseMapper.deleteByIds(ids) > 0;
    }
}
