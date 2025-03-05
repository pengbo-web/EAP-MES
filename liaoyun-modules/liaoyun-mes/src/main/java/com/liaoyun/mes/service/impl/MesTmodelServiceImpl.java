package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesModelrec;
import com.liaoyun.mes.domain.MesOutorder;
import com.liaoyun.mes.mapper.MesModelrecMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesTmodelBo;
import com.liaoyun.mes.domain.vo.MesTmodelVo;
import com.liaoyun.mes.domain.MesTmodel;
import com.liaoyun.mes.mapper.MesTmodelMapper;
import com.liaoyun.mes.service.IMesTmodelService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 模具管理Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-12-08
 */
@RequiredArgsConstructor
@Service
public class MesTmodelServiceImpl implements IMesTmodelService {

    private final MesTmodelMapper baseMapper;

    private final MesModelrecMapper mesModelrecMapper;

    /**
     * 查询模具管理
     *
     * @param id 主键
     * @return 模具管理
     */
    @Override
    public MesTmodelVo queryById(Long id){
        MesTmodelVo vo = baseMapper.selectVoById(id);
        LambdaQueryWrapper<MesModelrec> lqw1 =new LambdaQueryWrapper<>();
        lqw1.eq(MesModelrec::getModelid,vo.getId());
        List<MesModelrec> list = mesModelrecMapper.selectList(lqw1);
        int tc=0;
        for (MesModelrec mesModelrec : list) {
            if(StringUtils.equals(mesModelrec.getStatus(),"0")){
                tc++;
            }
        }
        vo.setUsestock(Long.valueOf(tc));
        vo.setReclist(list);
        return vo;
    }

    /**
     * 分页查询模具管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 模具管理分页列表
     */
    @Override
    public TableDataInfo<MesTmodelVo> queryPageList(MesTmodelBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesTmodel> lqw = buildQueryWrapper(bo);
        Page<MesTmodelVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesTmodelVo vo : result.getRecords()) {
            LambdaQueryWrapper<MesModelrec> lqw1 =new LambdaQueryWrapper<>();
            lqw1.eq(MesModelrec::getModelid,vo.getId());
            List<MesModelrec> list = mesModelrecMapper.selectList(lqw1);
            int tc=0;
            for (MesModelrec mesModelrec : list) {
                if(StringUtils.equals(mesModelrec.getStatus(),"0")){
                    tc++;
                }
            }
            vo.setUsestock(Long.valueOf(tc));
            vo.setReclist(list);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的模具管理列表
     *
     * @param bo 查询条件
     * @return 模具管理列表
     */
    @Override
    public List<MesTmodelVo> queryList(MesTmodelBo bo) {
        LambdaQueryWrapper<MesTmodel> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesTmodel> buildQueryWrapper(MesTmodelBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesTmodel> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesTmodel::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesTmodel::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getBrand()), MesTmodel::getBrand, bo.getBrand());
        lqw.eq(StringUtils.isNotBlank(bo.getOtype()), MesTmodel::getOtype, bo.getOtype());
        lqw.eq(bo.getStocknum() != null, MesTmodel::getStocknum, bo.getStocknum());
        lqw.eq(bo.getUsestock() != null, MesTmodel::getUsestock, bo.getUsestock());
        lqw.eq(bo.getOtime() != null, MesTmodel::getOtime, bo.getOtime());
        lqw.eq(bo.getNexttime() != null, MesTmodel::getNexttime, bo.getNexttime());
        lqw.eq(bo.getInstock() != null, MesTmodel::getInstock, bo.getInstock());
        lqw.between(params.get("beginnexttime") != null && params.get("endnexttime") != null,
            MesTmodel::getNexttime ,params.get("beginnexttime"), params.get("endnexttime"));
        lqw.between(params.get("beginotime") != null && params.get("endotime") != null,
            MesTmodel::getOtime ,params.get("beginotime"), params.get("endotime"));
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesTmodel::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增模具管理
     *
     * @param bo 模具管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesTmodelBo bo) {
        MesTmodel add = MapstructUtils.convert(bo, MesTmodel.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改模具管理
     *
     * @param bo 模具管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesTmodelBo bo) {
        MesTmodel update = MapstructUtils.convert(bo, MesTmodel.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesTmodel entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除模具管理信息
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
