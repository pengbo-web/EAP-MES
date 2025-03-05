package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesEquipmentType;
import com.liaoyun.mes.mapper.MesEquipmentTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesEquipmentBo;
import com.liaoyun.mes.domain.vo.MesEquipmentVo;
import com.liaoyun.mes.domain.MesEquipment;
import com.liaoyun.mes.mapper.MesEquipmentMapper;
import com.liaoyun.mes.service.IMesEquipmentService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设备列表Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-06-04
 */
@RequiredArgsConstructor
@Service
public class MesEquipmentServiceImpl implements IMesEquipmentService {

    private final MesEquipmentMapper baseMapper;
    private final MesEquipmentTypeMapper equipmentTypeMapper;
    /**
     * 查询设备列表
     *
     * @param id 主键
     * @return 设备列表
     */
    @Override
    public MesEquipmentVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询设备列表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 设备列表分页列表
     */
    @Override
    public TableDataInfo<MesEquipmentVo> queryPageList(MesEquipmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesEquipment> lqw = buildQueryWrapper(bo);
        Page<MesEquipmentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        // 关联查询 typename 字段
        for (MesEquipmentVo vo : result.getRecords()) {
            String typename = baseMapper.selectTypeTitleById(vo.getTypeId());
            vo.setTypename(typename);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的设备列表列表
     *
     * @param bo 查询条件
     * @return 设备列表列表
     */
    @Override
    public List<MesEquipmentVo> queryList(MesEquipmentBo bo) {
        LambdaQueryWrapper<MesEquipment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesEquipment> buildQueryWrapper(MesEquipmentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesEquipment> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesEquipment::getSn, bo.getSn());
        lqw.eq(bo.getTypeId() != null, MesEquipment::getTypeId, bo.getTypeId());
        lqw.eq(StringUtils.isNotBlank(bo.getBrand()), MesEquipment::getBrand, bo.getBrand());
        lqw.eq(bo.getCount() != null, MesEquipment::getCount, bo.getCount());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesEquipment::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesEquipment::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增设备列表
     *
     * @param bo 设备列表
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesEquipmentBo bo) {
        MesEquipment add = MapstructUtils.convert(bo, MesEquipment.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备列表
     *
     * @param bo 设备列表
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesEquipmentBo bo) {
        MesEquipment update = MapstructUtils.convert(bo, MesEquipment.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesEquipment entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除设备列表信息
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
