package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesEquipmentTypeBo;
import com.liaoyun.mes.domain.vo.MesEquipmentTypeVo;
import com.liaoyun.mes.domain.MesEquipmentType;
import com.liaoyun.mes.mapper.MesEquipmentTypeMapper;
import com.liaoyun.mes.service.IMesEquipmentTypeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * ⑤设备类别Service业务层处理
 *
 * @author Kylin
 * @date 2024-06-03
 */
@RequiredArgsConstructor
@Service
public class MesEquipmentTypeServiceImpl implements IMesEquipmentTypeService {

    private final MesEquipmentTypeMapper baseMapper;

    /**
     * 查询⑤设备类别
     *
     * @param id 主键
     * @return ⑤设备类别
     */
    @Override
    public MesEquipmentTypeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询⑤设备类别列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ⑤设备类别分页列表
     */
    @Override
    public TableDataInfo<MesEquipmentTypeVo> queryPageList(MesEquipmentTypeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesEquipmentType> lqw = buildQueryWrapper(bo);
        Page<MesEquipmentTypeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的⑤设备类别列表
     *
     * @param bo 查询条件
     * @return ⑤设备类别列表
     */
    @Override
    public List<MesEquipmentTypeVo> queryList(MesEquipmentTypeBo bo) {
        LambdaQueryWrapper<MesEquipmentType> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesEquipmentType> buildQueryWrapper(MesEquipmentTypeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesEquipmentType> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesEquipmentType::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesEquipmentType::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesEquipmentType::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增⑤设备类别
     *
     * @param bo ⑤设备类别
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesEquipmentTypeBo bo) {
        MesEquipmentType add = MapstructUtils.convert(bo, MesEquipmentType.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改⑤设备类别
     *
     * @param bo ⑤设备类别
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesEquipmentTypeBo bo) {
        MesEquipmentType update = MapstructUtils.convert(bo, MesEquipmentType.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesEquipmentType entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除⑤设备类别信息
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
