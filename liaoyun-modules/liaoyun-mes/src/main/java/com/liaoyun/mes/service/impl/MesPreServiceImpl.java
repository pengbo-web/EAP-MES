package com.liaoyun.mes.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.liaoyun.common.core.domain.event.ProcessDeleteEvent;
import com.liaoyun.common.core.domain.event.ProcessEvent;
import com.liaoyun.common.core.domain.event.ProcessTaskEvent;
import com.liaoyun.common.core.enums.BusinessStatusEnum;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.core.utils.StringUtils;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import com.liaoyun.mes.domain.bo.MesPreBo;
import com.liaoyun.mes.domain.vo.MesPreVo;
import com.liaoyun.mes.domain.MesPre;
import com.liaoyun.mes.mapper.MesPreMapper;
import com.liaoyun.mes.service.IMesPreService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 采购审核Service业务层处理
 *
 * @author Liao Liao
 * @date 2025-03-13
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MesPreServiceImpl implements IMesPreService {

    private final MesPreMapper baseMapper;

    /**
     * 查询采购审核
     *
     * @param id 主键
     * @return 采购审核
     */
    @Override
    public MesPreVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询采购审核列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 采购审核分页列表
     */
    @Override
    public TableDataInfo<MesPreVo> queryPageList(MesPreBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MesPre> lqw = buildQueryWrapper(bo);
        Page<MesPreVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的采购审核列表
     *
     * @param bo 查询条件
     * @return 采购审核列表
     */
    @Override
    public List<MesPreVo> queryList(MesPreBo bo) {
        LambdaQueryWrapper<MesPre> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MesPre> buildQueryWrapper(MesPreBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MesPre> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(MesPre::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getLeaveType()), MesPre::getLeaveType, bo.getLeaveType());
        lqw.eq(StringUtils.isNotBlank(bo.getTilte()), MesPre::getTilte, bo.getTilte());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), MesPre::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getGk()), MesPre::getGk, bo.getGk());
        lqw.eq(bo.getStartDate() != null, MesPre::getStartDate, bo.getStartDate());
        lqw.eq(bo.getEndDate() != null, MesPre::getEndDate, bo.getEndDate());
        lqw.eq(bo.getLeaveDays() != null, MesPre::getLeaveDays, bo.getLeaveDays());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), MesPre::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增采购审核
     *
     * @param bo 采购审核
     * @return 是否新增成功
     */
    @Override
    public MesPreVo insertByBo(MesPreBo bo) {
        MesPre add = MapstructUtils.convert(bo, MesPre.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
         return MapstructUtils.convert(add, MesPreVo.class);
    }

    /**
     * 修改采购审核
     *
     * @param bo 采购审核
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(MesPreBo bo) {
        MesPre update = MapstructUtils.convert(bo, MesPre.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MesPre entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除采购审核信息
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
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 总体流程监听(例如: 草稿，撤销，退回，作废，终止，已完成等)
     * 正常使用只需#processEvent.flowCode=='leave1'
     * 示例为了方便则使用startsWith匹配了全部示例key
     *
     * @param processEvent 参数
     */
    @EventListener(condition = "#processEvent.flowCode=='leave2'")
    public void processHandler(ProcessEvent processEvent) {
        log.info("当前任务执行了{}", processEvent.toString());
        MesPre testLeave = baseMapper.selectById(Long.valueOf(processEvent.getBusinessId()));
        testLeave.setStatus(processEvent.getStatus());
        // 用于例如审批附件 审批意见等 存储到业务表内 自行根据业务实现存储流程
        Map<String, Object> params = processEvent.getParams();
        if (MapUtil.isNotEmpty(params)) {
            // 历史任务扩展(通常为附件)
            String hisTaskExt = Convert.toStr(params.get("hisTaskExt"));
            // 办理人
            String handler = Convert.toStr(params.get("handler"));
            // 办理意见
            String message = Convert.toStr(params.get("message"));
            testLeave.setRem(testLeave.getRem()+","+message);
        }
        if (processEvent.isSubmit()) {
            testLeave.setStatus(BusinessStatusEnum.WAITING.getStatus());
        }
        if(processEvent.getStatus().equals(BusinessStatusEnum.FINISH.getStatus())){
            if(testLeave.getLeaveType().equals("")){

            }
        }

        baseMapper.updateById(testLeave);
    }

    /**
     * 执行办理任务监听
     * 示例：也可通过  @EventListener(condition = "#processTaskEvent.flowCode=='leave1'")进行判断
     * 在方法中判断流程节点key
     * if ("xxx".equals(processTaskEvent.getNodeCode())) {
     * //执行业务逻辑
     * }
     *
     * @param processTaskEvent 参数
     */
    @EventListener(condition = "#processTaskEvent.flowCode=='leave2'")
    public void processTaskHandler(ProcessTaskEvent processTaskEvent) {
        log.info("当前任务执行了{}", processTaskEvent.toString());
        MesPre testLeave = baseMapper.selectById(Long.valueOf(processTaskEvent.getBusinessId()));
        testLeave.setStatus(BusinessStatusEnum.WAITING.getStatus());
        baseMapper.updateById(testLeave);
    }

    /**
     * 监听删除流程事件
     * 正常使用只需#processDeleteEvent.flowCode=='leave1'
     * 示例为了方便则使用startsWith匹配了全部示例key
     *
     * @param processDeleteEvent 参数
     */
    @EventListener(condition = "#processDeleteEvent.flowCode==('leave2')")
    public void processDeleteHandler(ProcessDeleteEvent processDeleteEvent) {
        log.info("监听删除流程事件，当前任务执行了{}", processDeleteEvent.toString());
        MesPre testLeave = baseMapper.selectById(Long.valueOf(processDeleteEvent.getBusinessId()));
        if (ObjectUtil.isNull(testLeave)) {
            return;
        }
        baseMapper.deleteById(testLeave.getId());
    }
}
