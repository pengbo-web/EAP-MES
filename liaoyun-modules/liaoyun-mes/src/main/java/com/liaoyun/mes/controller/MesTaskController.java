package com.liaoyun.mes.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.liaoyun.common.core.utils.MapstructUtils;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.mes.domain.MesTask;
import com.liaoyun.mes.domain.MesWorkRec;
import com.liaoyun.mes.domain.bo.MesWorkRecBo;
import com.liaoyun.mes.domain.vo.MesWorkRecVo;
import com.liaoyun.mes.mapper.MesTaskMapper;
import com.liaoyun.mes.service.IMesWorkRecService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.liaoyun.common.idempotent.annotation.RepeatSubmit;
import com.liaoyun.common.log.annotation.Log;
import com.liaoyun.common.web.core.BaseController;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.core.validate.AddGroup;
import com.liaoyun.common.core.validate.EditGroup;
import com.liaoyun.common.log.enums.BusinessType;
import com.liaoyun.common.excel.utils.ExcelUtil;
import com.liaoyun.mes.domain.vo.MesTaskVo;
import com.liaoyun.mes.domain.bo.MesTaskBo;
import com.liaoyun.mes.service.IMesTaskService;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;

/**
 * 任务流转
 *
 * @author Liao Liao
 * @date 2024-07-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/task/task")
public class MesTaskController extends BaseController {

    private final IMesTaskService mesTaskService;
    private final IMesWorkRecService mesWorkRecService;
    private final MesTaskMapper taskMapper;

    /**
     * 查询任务流转列表
     */
    @GetMapping("/lists")
    public TableDataInfo<MesTaskVo> lists(MesTaskBo bo, PageQuery pageQuery) {
        return mesTaskService.queryPageListstatus(bo, pageQuery,"7",0);
    }
    @GetMapping("/listsbywt")
    public TableDataInfo<MesTaskVo> listworks(MesTaskBo bo, PageQuery pageQuery) {
        return mesTaskService.queryPageListstatus(bo, pageQuery,"2",1);
    }
    /**
     * 查询任务流转列表
     */
    @SaCheckPermission("task:task:list")
    @GetMapping("/list")
    public TableDataInfo<MesTaskVo> list(MesTaskBo bo, PageQuery pageQuery) {
        return mesTaskService.queryPageList(bo, pageQuery);
    }

    @GetMapping("/listsbyid/{uid}/{typ}")
    public TableDataInfo<MesTaskVo> listbyuid(@PathVariable Long uid,@PathVariable Integer typ,MesTaskBo bo, PageQuery pageQuery) {
        return mesTaskService.queryPageListbtidd(uid,typ,bo, pageQuery);
    }
    /**
     * 查询任务流转列表
     */
    @GetMapping("/liststatus")
    public TableDataInfo<MesTaskVo> liststatus(MesTaskBo bo, PageQuery pageQuery) {
        bo.setStatus("3");
        return mesTaskService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询任务流转列表
     */
    @PostMapping("/tasktodo/{id}")
    public R<Long> tasktodo(@PathVariable Long id) {
        MesTaskVo mesTaskVo=mesTaskService.queryById(id);
        if(null==mesTaskVo){
            return R.fail();
        }
        List<MesTask> listc= DataPermissionHelper.ignore(() -> {
            LambdaQueryWrapper<MesTask> otherwarp = new LambdaQueryWrapper<>();
            otherwarp.eq(MesTask::getSn, mesTaskVo.getSn());
            otherwarp.eq(MesTask::getProId, mesTaskVo.getProId());
            otherwarp.eq(MesTask::getWorkplaceId, mesTaskVo.getWorkplaceId());
            otherwarp.eq(MesTask::getNeedcount, mesTaskVo.getNeedcount());
            otherwarp.eq(MesTask::getCurrprocess, mesTaskVo.getCurrprocess());
            otherwarp.eq(MesTask::getStatus,"3");
            return taskMapper.selectList(otherwarp);
        });
        if(listc.size()>0){
            return R.fail();
        }
        MesTaskBo update = (MesTaskBo) BeanMapHelper.Bean2Bean(mesTaskVo, MesTaskBo.class);
        update.setStatus("2");
        mesTaskService.updateByBo(update);
        MesWorkRecBo recBo=new MesWorkRecBo();
        recBo.setUserId(update.getUserId());
        recBo.setTaskId(update.getId());
        recBo.setSort(update.getCurrprocess());
        recBo.setProid(update.getProId());
        recBo.setLastcount(update.getMarkcount());
        mesWorkRecService.insertByBo(recBo);
        return R.ok(recBo.getId());
    }

    /**
     * 导出任务流转列表
     */
    @SaCheckPermission("task:task:export")
    @Log(title = "任务流转", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MesTaskBo bo, HttpServletResponse response) {
        List<MesTaskVo> list = mesTaskService.queryList(bo);
        ExcelUtil.exportExcel(list, "任务流转", MesTaskVo.class, response);
    }

    @PostMapping("/taskrec/{id}")
    public R<Void> taskrec(@PathVariable Long id,@RequestBody MesWorkRecBo bo){
        if(bo.getId()==0){
            MesWorkRecBo recBo=new MesWorkRecBo();
            MesTaskVo taskinfo=mesTaskService.queryById(id);
            recBo.setUserId(LoginHelper.getUserId());
            recBo.setTaskId(id);
            recBo.setStatus("0");
            try {
                recBo.setSort(taskinfo.getCurrprocess());
                recBo.setProid(taskinfo.getProId());
            }catch (Exception e){

            }
            List<MesWorkRecVo> vo=mesWorkRecService.queryList(recBo);
            bo.setId(vo.get(0).getId());
        }
        MesWorkRecVo mesWorkRecVo=mesWorkRecService.queryById(bo.getId());
        MesWorkRecBo update = (MesWorkRecBo) BeanMapHelper.Bean2Bean(mesWorkRecVo, MesWorkRecBo.class);
        update.setCount(bo.getCount());
        update.setRemark(bo.getRemark());
        update.setLastcount(bo.getLastcount());
        update.setStatus("3");
        return toAjax(mesWorkRecService.updateByBo(update));
    }
    /**
     * 获取任务流转详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<MesTaskVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(mesTaskService.queryById(id));
    }

    /**
     * 新增任务流转
     */
    @Log(title = "任务流转", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MesTaskBo bo) {
        return toAjax(mesTaskService.insertByBo(bo));
    }

    /**
     * 修改任务流转
     */
    @Log(title = "任务流转", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit( @RequestBody MesTaskBo bo) {

        return toAjax(mesTaskService.updateByBo(bo));
    }

    /**
     * 删除任务流转
     *
     * @param ids 主键串
     */
    @SaCheckPermission("task:task:remove")
    @Log(title = "任务流转", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(mesTaskService.deleteWithValidByIds(List.of(ids), true));
    }
}
