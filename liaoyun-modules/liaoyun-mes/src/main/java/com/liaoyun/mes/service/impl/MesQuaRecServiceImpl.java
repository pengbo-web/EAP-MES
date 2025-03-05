package com.liaoyun.mes.service.impl;

import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liaoyun.mes.domain.MesWork;
import com.liaoyun.mes.mapper.MesQualityMainMapper;
import com.liaoyun.mes.mapper.MesTaskMapper;
import com.liaoyun.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesQuaRecBo;
import com.liaoyun.mes.domain.vo.MesQuaRecVo;
import com.liaoyun.mes.domain.MesQuaRec;
import com.liaoyun.mes.mapper.MesQuaRecMapper;
import com.liaoyun.mes.service.IMesQuaRecService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 质检列表Service业务层处理
 *
 * @author Liao Liao
 * @date 2024-07-24
 */
@RequiredArgsConstructor
@Service
public class MesQuaRecServiceImpl implements IMesQuaRecService {

    private final MesQuaRecMapper baseMapper;
    private final MesTaskServiceImpl taskService;
    private final MesQualityMainServiceImpl mainService;
    private final SysUserMapper sysUserMapper;

    /**
     * 查询质检列表
     *
     * @param id 主键
     * @return 质检列表
     */
    @Override
    public MesQuaRecVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询质检列表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 质检列表分页列表
     */
    @Override
    public TableDataInfo<MesQuaRecVo> queryPageList(MesQuaRecBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesQuaRec> lqw = buildQueryWrapper(bo);
        Page<MesQuaRecVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (MesQuaRecVo vo : result.getRecords()) {
            vo.setQuaname(baseMapper.selectEquTitleById(vo.getQuaMian()));
            vo.setTaskinfo(taskService.queryById(vo.getTaskId()));
            vo.setMaininfo(mainService.queryById(vo.getQuaMian()));
            vo.setCreateByName(sysUserMapper.selectById(vo.getCreateBy()).getNickName());
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的质检列表列表
     *
     * @param bo 查询条件
     * @return 质检列表列表
     */
    @Override
    public List<MesQuaRecVo> queryList(MesQuaRecBo bo) {
        LambdaQueryWrapper<MesQuaRec> lqw = buildQueryWrapper(bo);
        List<MesQuaRecVo> list = baseMapper.selectVoList(lqw);
        for (MesQuaRecVo vo : list) {
            vo.setQuaname(baseMapper.selectEquTitleById(vo.getQuaMian()));
            vo.setTaskinfo(taskService.queryById(vo.getTaskId()));
            vo.setMaininfo(mainService.queryById(vo.getQuaMian()));
        }
        return list;
    }

    private LambdaQueryWrapper<MesQuaRec> buildQueryWrapper(MesQuaRecBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesQuaRec> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getQuaMian() != null, MesQuaRec::getQuaMian, bo.getQuaMian());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesQuaRec::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), MesQuaRec::getTitle, bo.getTitle());
        lqw.eq(bo.getTaskId() != null, MesQuaRec::getTaskId, bo.getTaskId());
        lqw.eq(StringUtils.isNotBlank(bo.getValuest()), MesQuaRec::getValuest, bo.getValuest());
        lqw.eq(StringUtils.isNotBlank(bo.getRes()), MesQuaRec::getRes, bo.getRes());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesQuaRec::getStatus, bo.getStatus());
        lqw.eq(bo.getMustbe() != null, MesQuaRec::getMustbe, bo.getMustbe());
        lqw.between(params.get("beginEndtime") != null && params.get("endEndtime") != null,
            MesQuaRec::getCreateTime ,params.get("beginEndtime"), params.get("endEndtime"));
        return lqw;
    }

    /**
     * 新增质检列表
     *
     * @param bo 质检列表
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(MesQuaRecBo bo) {
        MesQuaRec add = MapstructUtils.convert(bo, MesQuaRec.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改质检列表
     *
     * @param bo 质检列表
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesQuaRecBo bo) {
        MesQuaRec update = MapstructUtils.convert(bo, MesQuaRec.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesQuaRec entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除质检列表信息
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
