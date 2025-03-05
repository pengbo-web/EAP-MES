package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesOutRec;
import com.liaoyun.mes.domain.MesProduct;
import com.liaoyun.mes.mapper.MesProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesQuaformBo;
import com.liaoyun.mes.domain.vo.MesQuaformVo;
import com.liaoyun.mes.domain.MesQuaform;
import com.liaoyun.mes.mapper.MesQuaformMapper;
import com.liaoyun.mes.service.IMesQuaformService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 来料检Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-10-12
 */
@RequiredArgsConstructor
@Service
public class MesQuaformServiceImpl implements IMesQuaformService {

    private final MesQuaformMapper baseMapper;
    private final MesProductMapper productMapper;

    /**
     * 查询来料检
     *
     * @param id 主键
     * @return 来料检
     */
    @Override
    public MesQuaformVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询来料检列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 来料检分页列表
     */
    @Override
    public TableDataInfo<MesQuaformVo> queryPageList(MesQuaformBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesQuaform> lqw = buildQueryWrapper(bo);
        if(StringUtils.isNotBlank(bo.getProname())){
            LambdaQueryWrapper<MesProduct> wrapper3=new LambdaQueryWrapper<>();
            wrapper3.like(MesProduct::getTitle,bo.getProname());
            List<MesProduct> list3=productMapper.selectList(wrapper3);
            List<Long> ids=new ArrayList<Long>();
            if(list3.size()>0){
                for(MesProduct mesProduct:list3){
                    ids.add(mesProduct.getId());
                }
                lqw.in(MesQuaform::getProid,ids);
            }
        }
        Page<MesQuaformVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesQuaformVo vo : result.getRecords()) {
            vo.setProductVo(productMapper.selectVoById(vo.getProid()));
            vo.setUsername(baseMapper.getusername(vo.getCreateBy()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的来料检列表
     *
     * @param bo 查询条件
     * @return 来料检列表
     */
    @Override
    public List<MesQuaformVo> queryList(MesQuaformBo bo) {
        LambdaQueryWrapper<MesQuaform> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesQuaform> buildQueryWrapper(MesQuaformBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesQuaform> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProid() != null, MesQuaform::getProid, bo.getProid());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesQuaform::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesQuaform::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getResu()), MesQuaform::getResu, bo.getResu());
        lqw.eq(StringUtils.isNotBlank(bo.getInstock()), MesQuaform::getInstock, bo.getInstock());
        lqw.eq(bo.getSort() != null, MesQuaform::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesQuaform::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增来料检
     *
     * @param bo 来料检
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesQuaformBo bo) {
        MesQuaform add = MapstructUtils.convert(bo, MesQuaform.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改来料检
     *
     * @param bo 来料检
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesQuaformBo bo) {
        MesQuaform update = MapstructUtils.convert(bo, MesQuaform.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesQuaform entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除来料检信息
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
