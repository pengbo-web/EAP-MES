package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.MesStock;
import com.liaoyun.mes.domain.MesWork;
import com.liaoyun.mes.domain.vo.MesProductVo;
import com.liaoyun.mes.domain.vo.MesWorkVo;
import com.liaoyun.mes.mapper.*;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesYcrecBo;
import com.liaoyun.mes.domain.vo.MesYcrecVo;
import com.liaoyun.mes.domain.MesYcrec;
import com.liaoyun.mes.service.IMesYcrecService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 异常报工审核Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-11-08
 */
@RequiredArgsConstructor
@Service
public class MesYcrecServiceImpl implements IMesYcrecService {

    private final MesYcrecMapper baseMapper;
    private final MesWorkMapper workMapper;
    private final MesProductMapper productMapper;
    private final SysUserMapper sysUserMapper;
    private final MesStockMapper stockMapper;

    /**
     * 查询异常报工审核
     *
     * @param id 主键
     * @return 异常报工审核
     */
    @Override
    public MesYcrecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询异常报工审核列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 异常报工审核分页列表
     */
    @Override
    public TableDataInfo<MesYcrecVo> queryPageList(MesYcrecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesYcrec> lqw = buildQueryWrapper(bo);
        if(StringUtils.isNotBlank(bo.getUsername())){
            LambdaQueryWrapper<SysUser> wrapper3=new LambdaQueryWrapper<>();
            wrapper3.like(SysUser::getNickName,bo.getUsername());
            List<SysUser> list3=sysUserMapper.selectList(wrapper3);
            List<Long> ids=new ArrayList<Long>();
            if(list3.size()>0){
                for(SysUser mesProduct:list3){
                    ids.add(mesProduct.getUserId());
                }
                lqw.in(MesYcrec::getUserId,ids);
            }
        }
        Page<MesYcrecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesYcrecVo vo : result.getRecords()) {
            LambdaQueryWrapper<MesWork> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(MesWork::getSn, vo.getSn());
            MesWorkVo workVo=workMapper.selectVoOne(wrapper);
            vo.setWorkVo(workVo);
            vo.setUsername(sysUserMapper.selectById(vo.getUserId()).getNickName());
            try {
                vo.setProductVo(productMapper.selectVoById(workVo.getProId()));
            }catch (Exception e){
                try {
                    LambdaQueryWrapper<MesProduct> wrapper1=new LambdaQueryWrapper<>();
                    wrapper1.eq(MesProduct::getStocks, workVo.getProId());
                    MesProductVo product=productMapper.selectVoList(wrapper1).get(0);
                    vo.setProductVo(product);
                }catch (Exception e1){

                }
            }

        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的异常报工审核列表
     *
     * @param bo 查询条件
     * @return 异常报工审核列表
     */
    @Override
    public List<MesYcrecVo> queryList(MesYcrecBo bo) {
        LambdaQueryWrapper<MesYcrec> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesYcrec> buildQueryWrapper(MesYcrecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesYcrec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSn() != null, MesYcrec::getSn, bo.getSn());
        lqw.eq(bo.getCount() != null, MesYcrec::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getImgs()), MesYcrec::getImgs, bo.getImgs());
        lqw.eq(bo.getYccount() != null, MesYcrec::getYccount, bo.getYccount());
        lqw.eq(StringUtils.isNotBlank(bo.getYcdesc()), MesYcrec::getYcdesc, bo.getYcdesc());
        lqw.eq(bo.getUserId() != null, MesYcrec::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesYcrec::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增异常报工审核
     *
     * @param bo 异常报工审核
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesYcrecBo bo) {
        MesYcrec add = MapstructUtils.convert(bo, MesYcrec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改异常报工审核
     *
     * @param bo 异常报工审核
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesYcrecBo bo) {
        MesYcrec update = MapstructUtils.convert(bo, MesYcrec.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesYcrec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除异常报工审核信息
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
