package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesProcess;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.vo.MesProcessVo;
import com.liaoyun.mes.mapper.MesProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesProsettingBo;
import com.liaoyun.mes.domain.vo.MesProsettingVo;
import com.liaoyun.mes.domain.MesProsetting;
import com.liaoyun.mes.mapper.MesProsettingMapper;
import com.liaoyun.mes.service.IMesProsettingService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 排程设置Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-06-09
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MesProsettingServiceImpl implements IMesProsettingService {

    private final MesProsettingMapper baseMapper;
    private final MesProductMapper productMapper;

    /**
     * 查询排程设置
     *
     * @param id 主键
     * @return 排程设置
     */
    @Override
    public MesProsettingVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询排程设置列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 排程设置分页列表
     */
    @Override
    public TableDataInfo<MesProsettingVo> queryPageList(MesProsettingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesProsetting> lqw = buildQueryWrapper(bo);
        Page<MesProsettingVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);

        for (MesProsettingVo vo:result.getRecords()){
            vo.setWorkshopname(baseMapper.selectwpTitleById(Long.valueOf(vo.getWorkshopId())));
            vo.setProcessname(baseMapper.selectpreTitleById(Long.valueOf(vo.getProcessId())));
            try{
                vo.setProname(productMapper.selectById(vo.getProId()).getTitle());
            }catch (Exception e){

            }


        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的排程设置列表
     *
     * @param bo 查询条件
     * @return 排程设置列表
     */
    @Override
    public List<MesProsettingVo> queryList(MesProsettingBo bo) {
        LambdaQueryWrapper<MesProsetting> lqw = buildQueryWrapper(bo);
        List<MesProsettingVo> list=baseMapper.selectVoList(lqw);
        for (MesProsettingVo vo:list){
                vo.setWorkshopname(baseMapper.selectwpTitleById(Long.valueOf(vo.getWorkshopId())));
                vo.setProcessname(baseMapper.selectpreTitleById(Long.valueOf(vo.getProcessId())));

        }
        log.info("kkkkkkkkkkkk:"+lqw.getSqlSelect());
        return list;
    }

    private LambdaQueryWrapper<MesProsetting> buildQueryWrapper(MesProsettingBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesProsetting> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getWorkshopId()), MesProsetting::getWorkshopId, bo.getWorkshopId());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessId()), MesProsetting::getProcessId, bo.getProcessId());
        lqw.eq(bo.getSort() != null, MesProsetting::getSort, bo.getSort());
        lqw.eq(bo.getProId() != null, MesProsetting::getProId, bo.getProId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesProsetting::getStatus, bo.getStatus());
        lqw.eq(bo.getMustbe() != null, MesProsetting::getMustbe, bo.getMustbe());
        return lqw;
    }

    /**
     * 新增排程设置
     *
     * @param bo 排程设置
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesProsettingBo bo) {

        MesProsetting add = MapstructUtils.convert(bo, MesProsetting.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }
    @Override
    public Boolean insertByBos(MesProsettingBo bo) {
        List<MesProduct> productList=bo.getProlist();
        List<MesProcessVo> processes=bo.getProcessList();
        //删除
        LambdaQueryWrapper<MesProsetting> lqw =new LambdaQueryWrapper<>();
        lqw.eq(MesProsetting::getProId, bo.getProId());
        baseMapper.delete(lqw);
        for (MesProduct product:productList){
            int sort=0;
            for (MesProcessVo process:processes){
                sort++;
                MesProsetting add=new MesProsetting();
                add.setWorkshopId(process.getWrid().toString());
                add.setProId(product.getId());
                add.setProcessId(process.getId().toString());
                add.setSort(sort);
                add.setStatus("1");
                add.setMustbe(process.getMustbe());
                baseMapper.insert(add);
            }
        }
        return true;
    }

    /**
     * 修改排程设置
     *
     * @param bo 排程设置
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesProsettingBo bo) {
        MesProsetting update = MapstructUtils.convert(bo, MesProsetting.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesProsetting entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除排程设置信息
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
