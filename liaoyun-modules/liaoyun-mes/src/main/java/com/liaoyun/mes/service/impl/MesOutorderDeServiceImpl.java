package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.domain.MesOutorder;
import com.liaoyun.mes.domain.MesUnitmeasure;
import com.liaoyun.mes.domain.vo.MesStockVo;
import com.liaoyun.mes.domain.vo.MesUnitmeasureVo;
import com.liaoyun.mes.mapper.MesStockMapper;
import com.liaoyun.mes.mapper.MesUnitmeasureMapper;
import com.liaoyun.system.domain.MesInventoryLedger;
import com.liaoyun.system.mapper.MesInventoryLedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOutorderDeBo;
import com.liaoyun.mes.domain.vo.MesOutorderDeVo;
import com.liaoyun.mes.domain.MesOutorderDe;
import com.liaoyun.mes.mapper.MesOutorderDeMapper;
import com.liaoyun.mes.service.IMesOutorderDeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 委外详情Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-22
 */
@RequiredArgsConstructor
@Service
public class MesOutorderDeServiceImpl implements IMesOutorderDeService {

    private final MesOutorderDeMapper baseMapper;

    private final MesStockMapper stockMapper;

    private final MesUnitmeasureMapper unitmeasureMapper;

    private final MesInventoryLedgerMapper inventoryLedgerMapper;

    /**
     * 查询委外详情
     *
     * @param id 主键
     * @return 委外详情
     */
    @Override
    public MesOutorderDeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询委外详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 委外详情分页列表
     */
    @Override
    public TableDataInfo<MesOutorderDeVo> queryPageList(MesOutorderDeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOutorderDe> lqw = buildQueryWrapper(bo);
        Page<MesOutorderDeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesOutorderDeVo vo : result.getRecords()) {
            MesStockVo stockVo=stockMapper.selectVoById(vo.getStockid());
            MesUnitmeasureVo unmeasured=unitmeasureMapper.selectVoById(stockVo.getUnitid());
            stockVo.setUnitname(unmeasured.getTitle());
            vo.setStockVo(stockVo);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的委外详情列表
     *
     * @param bo 查询条件
     * @return 委外详情列表
     */
    @Override
    public List<MesOutorderDeVo> queryList(MesOutorderDeBo bo) {
        LambdaQueryWrapper<MesOutorderDe> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesOutorderDe> buildQueryWrapper(MesOutorderDeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOutorderDe> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFromtype()), MesOutorderDe::getFromtype, bo.getFromtype());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), MesOutorderDe::getRemark, bo.getRemark());
        return lqw;
    }

    /**
     * 新增委外详情
     *
     * @param bo 委外详情
     * @return 是否新增成功
     */
    @Override
    public Long insertByBo(MesOutorderDeBo bo) {
        MesOutorderDe add = MapstructUtils.convert(bo, MesOutorderDe.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return bo.getId();
    }

    /**
     * 修改委外详情
     *
     * @param bo 委外详情
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesOutorderDeBo bo) {
        MesOutorderDe update = MapstructUtils.convert(bo, MesOutorderDe.class);
//        validEntityBeforeSave(update);
//        AtomicInteger rec = new AtomicInteger(0);
        Map<String,Object> map=new HashMap<>();
        if(bo.getStatus().equals("2")){
            if(bo.getKwid()==0){
                bo.setKwid(null);
            }
            map.put("stockid",bo.getStockid());
            map.put("count",bo.getCounts().intValue());
            map.put("kwid",bo.getKwid());
            map.put("tenantid", LoginHelper.getTenantId());
            map.put("msg",0);
            inventoryLedgerMapper.callInOutProcedure(map);

        }
        if(map.get("msg").equals(1)){
            baseMapper.updateById(update);
            return true;
        }
        return  false;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOutorderDe entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除委外详情信息
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
