package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesStockTypeBo;
import com.liaoyun.mes.domain.vo.MesStockTypeVo;
import com.liaoyun.mes.domain.MesStockType;
import com.liaoyun.mes.mapper.MesStockTypeMapper;
import com.liaoyun.mes.service.IMesStockTypeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 库存分类Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-17
 */
@RequiredArgsConstructor
@Service
public class MesStockTypeServiceImpl implements IMesStockTypeService {

    private final MesStockTypeMapper baseMapper;

    /**
     * 查询库存分类
     *
     * @param id 主键
     * @return 库存分类
     */
    @Override
    public MesStockTypeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询符合条件的库存分类列表
     *
     * @param bo 查询条件
     * @return 库存分类列表
     */
    @Override
    public List<MesStockTypeVo> queryList(MesStockTypeBo bo) {
        LambdaQueryWrapper<MesStockType> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesStockType> buildQueryWrapper(MesStockTypeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesStockType> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesStockType::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getSn()), MesStockType::getSn, bo.getSn());

        lqw.eq(bo.getPid() != null, MesStockType::getPid, bo.getPid());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesStockType::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增库存分类
     *
     * @param bo 库存分类
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesStockTypeBo bo) {
        MesStockType add = MapstructUtils.convert(bo, MesStockType.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改库存分类
     *
     * @param bo 库存分类
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesStockTypeBo bo) {
        MesStockType update = MapstructUtils.convert(bo, MesStockType.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesStockType entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除库存分类信息
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
