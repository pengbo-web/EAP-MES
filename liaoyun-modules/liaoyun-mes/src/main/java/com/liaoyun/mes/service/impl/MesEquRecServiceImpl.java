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
import com.liaoyun.mes.domain.bo.MesEquRecBo;
import com.liaoyun.mes.domain.vo.MesEquRecVo;
import com.liaoyun.mes.domain.MesEquRec;
import com.liaoyun.mes.mapper.MesEquRecMapper;
import com.liaoyun.mes.service.IMesEquRecService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设备点检记录Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-28
 */
@RequiredArgsConstructor
@Service
public class MesEquRecServiceImpl implements IMesEquRecService {

    private final MesEquRecMapper baseMapper;

    /**
     * 查询设备点检记录
     *
     * @param id 主键
     * @return 设备点检记录
     */
    @Override
    public MesEquRecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询设备点检记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 设备点检记录分页列表
     */
    @Override
    public TableDataInfo<MesEquRecVo> queryPageList(MesEquRecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesEquRec> lqw = buildQueryWrapper(bo);
        Page<MesEquRecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesEquRecVo vo : result.getRecords()) {
            vo.setEquname(baseMapper.selectequmian(vo.getEquid()));
            vo.setMainname(baseMapper.selecttilte(vo.getEqumian()));
            vo.setUsername(baseMapper.getusername(vo.getCreateBy()));
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的设备点检记录列表
     *
     * @param bo 查询条件
     * @return 设备点检记录列表
     */
    @Override
    public List<MesEquRecVo> queryList(MesEquRecBo bo) {
        LambdaQueryWrapper<MesEquRec> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesEquRec> buildQueryWrapper(MesEquRecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesEquRec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEqumian() != null, MesEquRec::getEqumian, bo.getEqumian());
        lqw.eq(bo.getListid() != null, MesEquRec::getListid, bo.getListid());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesEquRec::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getConts()), MesEquRec::getConts, bo.getConts());
        lqw.eq(StringUtils.isNotBlank(bo.getRest()), MesEquRec::getRest, bo.getRest());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesEquRec::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增设备点检记录
     *
     * @param bo 设备点检记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesEquRecBo bo) {
        MesEquRec add = MapstructUtils.convert(bo, MesEquRec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备点检记录
     *
     * @param bo 设备点检记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesEquRecBo bo) {
        MesEquRec update = MapstructUtils.convert(bo, MesEquRec.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesEquRec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除设备点检记录信息
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
