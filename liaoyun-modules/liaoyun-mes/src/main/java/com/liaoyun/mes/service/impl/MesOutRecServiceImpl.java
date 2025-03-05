package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.domain.MesToolUse;
import com.liaoyun.mes.mapper.MesOutsourcingMapper;
import com.liaoyun.mes.mapper.MesProductMapper;
import com.liaoyun.mes.mapper.MesTaskMapper;
import com.liaoyun.system.domain.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesOutRecBo;
import com.liaoyun.mes.domain.vo.MesOutRecVo;
import com.liaoyun.mes.domain.MesOutRec;
import com.liaoyun.mes.mapper.MesOutRecMapper;
import com.liaoyun.mes.service.IMesOutRecService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 外协记录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-26
 */
@RequiredArgsConstructor
@Service
public class MesOutRecServiceImpl implements IMesOutRecService {

    private final MesOutRecMapper baseMapper;

    private final MesProductMapper productMapper;

    private final MesOutsourcingMapper outsourcingMapper;

    private final MesTaskMapper mesTaskMapper;


    /**
     * 查询外协记录
     *
     * @param id 主键
     * @return 外协记录
     */
    @Override
    public MesOutRecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询外协记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 外协记录分页列表
     */
    @Override
    public TableDataInfo<MesOutRecVo> queryPageList(MesOutRecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesOutRec> lqw = buildQueryWrapper(bo);
        if(StringUtils.isNotBlank(bo.getProname())){
            LambdaQueryWrapper<MesProduct> wrapper3=new LambdaQueryWrapper<>();
            wrapper3.like(MesProduct::getTitle,bo.getProname());
            List<MesProduct> list3=productMapper.selectList(wrapper3);
            List<Long> ids=new ArrayList<Long>();
            if(list3.size()>0){
                for(MesProduct mesProduct:list3){
                    ids.add(mesProduct.getId());
                }
                lqw.in(MesOutRec::getProId,ids);
            }
        }
        Page<MesOutRecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesOutRecVo vo : result.getRecords()) {
            vo.setProductVo(productMapper.selectVoById(vo.getProId()));
            vo.setOutsourcingVo(outsourcingMapper.selectVoById(vo.getOutId()));
            vo.setTaskVo(mesTaskMapper.selectVoById(vo.getTaskId()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的外协记录列表
     *
     * @param bo 查询条件
     * @return 外协记录列表
     */
    @Override
    public List<MesOutRecVo> queryList(MesOutRecBo bo) {
        LambdaQueryWrapper<MesOutRec> lqw = buildQueryWrapper(bo);
        List<MesOutRecVo> list=baseMapper.selectVoList(lqw);
        for(MesOutRecVo vo:list){
            vo.setProductVo(productMapper.selectVoById(vo.getProId()));
            vo.setOutsourcingVo(outsourcingMapper.selectVoById(vo.getOutId()));
            vo.setTaskVo(mesTaskMapper.selectVoById(vo.getTaskId()));
        }
        return list;
    }

    private LambdaQueryWrapper<MesOutRec> buildQueryWrapper(MesOutRecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesOutRec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOutId() != null, MesOutRec::getOutId, bo.getOutId());
        lqw.eq(bo.getProId() != null, MesOutRec::getProId, bo.getProId());
        lqw.eq(bo.getCount() != null, MesOutRec::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesOutRec::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增外协记录
     *
     * @param bo 外协记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesOutRecBo bo) {
        MesOutRec add = MapstructUtils.convert(bo, MesOutRec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改外协记录
     *
     * @param bo 外协记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesOutRecBo bo) {
        MesOutRec update = MapstructUtils.convert(bo, MesOutRec.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dates=sdf.format(System.currentTimeMillis());
        update.setRemark(dates);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesOutRec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除外协记录信息
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
