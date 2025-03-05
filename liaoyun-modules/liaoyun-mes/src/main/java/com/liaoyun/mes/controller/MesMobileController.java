package com.liaoyun.mes.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.liaoyun.common.core.domain.R;
import com.liaoyun.common.mybatis.core.page.PageQuery;
import com.liaoyun.common.mybatis.core.page.TableDataInfo;
import com.liaoyun.common.mybatis.helper.DataPermissionHelper;
import com.liaoyun.common.satoken.utils.LoginHelper;
import com.liaoyun.common.web.core.BaseController;
import com.liaoyun.mes.domain.*;
import com.liaoyun.mes.domain.bo.*;
import com.liaoyun.mes.domain.vo.*;
import com.liaoyun.mes.mapper.MesTaskMapper;
import com.liaoyun.mes.service.*;
import com.liaoyun.system.domain.SysUser;
import com.liaoyun.system.domain.bo.SysUserBo;
import com.liaoyun.system.domain.vo.SysOssUploadVo;
import com.liaoyun.system.domain.vo.SysOssVo;
import com.liaoyun.system.domain.vo.SysUserVo;
import com.liaoyun.system.mapper.SysUserMapper;
import com.liaoyun.system.service.ISysOssService;
import com.liaoyun.system.service.ISysUserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mobile")
public class MesMobileController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MesMobileController.class);
    private final IMesOutstockService mesOutstockService;
    private final IMesProductService productService;
    private final SysUserMapper sysUserMapper;
    private final IMesTaskService taskService;
    private final MesTaskMapper mesTaskMapper;
    private final IMesProcurementplanService procurementplanService;
    private final IMesQualityMainService qualityMainService;
    private final IMesProsettingService prosettingService;
    private final IMesQuaRecService mesQuaRecService;
    private final IMesQualityListService qualityListService;

    private final ISysUserService userService;
    private final IMesWorkService mesWorkService;
    private final IMesInstockService mesInstockService;
    private final IMesOutRecService mesOutRecService;
    private final IMesWorkplacesService workplacesService;
    private final IMesEquCheckService equCheckService;
    private final IMesEqucheckListService equcheckListService;
    private final ISysOssService ossService;
    private final IMesEquRecService mesEquRecService;
    private final IMesEtoolService etoolService;
    private final IMesCuttingService cuttingService;
    private final IMesCuttingRecService cuttingRecService;
    private final IMesCuttingUseService cuttingUseService;
    private final IMesToolRecService toolRecService;
    private final IMesWorkRecService workRecService;
    private final IMesMobileMenuService mesMobileMenuService;
    private final IMesTaskcService taskcService;
    private final IMesQuaformService quaformService;
    private final IMesTeamsService mesTeamsService;
    private final IMesYcrecService ycrecService;
    private final IMesStockService stockService;
    private final IMesAllrecService allrecService;
    private final IMesOrderService orderService;
    /**
     * 自制件需求库存量
     * @param id
     * @return
     */
    @GetMapping("/outstock/list/{id}")
    public R<List<MesProductVo>> list(@PathVariable Long id) {
        return R.ok(productService.getallstock(id,"1"));
    }

    /**
     * 采购件需求数量
     * @return
     */
    @GetMapping("/taskqualist")
    public TableDataInfo<MesTaskVo> zzlist(MesTaskBo bo, PageQuery pageQuery) {

        return taskService.queryListbyqua(bo,pageQuery);
    }
    /**
     * 未质检列表
     * @param id
     * @return
     */
    @GetMapping("/outstock/zzlist/{id}")
    public R<List<MesProductVo>> zzlist(@PathVariable Long id) {
        return R.ok(productService.getallstock(id,"0"));
    }

    /**
     * 根据质检主键获取质检列表
     * @param id
     * @return
     */
    @GetMapping("/getquality/list/{id}")
    public R<List<MesQualityListVo>> qualitys(@PathVariable Long id) {
        return R.ok(qualityMainService.getqualityList(id));
    }
    @GetMapping("/getquality/listlai")
    public R<List<MesQualityListVo>> qualityslailiao() {
        MesQualityMainBo bo=new MesQualityMainBo();
        bo.setOther("来料检");
        List<MesQualityListVo> list=new ArrayList<>();
        try {
            list=qualityMainService.queryList(bo).get(0).getQualityListVoList();
        }catch (Exception e){}
        return R.ok(list);
    }

    /**
     * 正常更新task任务
     * @param id
     * @return
     */
    @PutMapping("/updatetask")
    public R<Boolean> updatetask(@RequestBody MesTaskBo id) {
        return R.ok(taskService.updatetask(id));
    }

    /**
     * 获取工序信息
     * @param id
     * @return
     */
    @GetMapping("/prosetting/{id}")
    public R<List<MesProsettingVo>> prosettinglist(@PathVariable Long id) {
        MesProsettingBo bo=new MesProsettingBo();
        bo.setProId(id);
        return R.ok(prosettingService.queryList(bo));
    }

    /**
     * 采购计划
     * @param bo
     * @return
     */
    @PostMapping("/procurement/")
    public R<Boolean> zzlist(@RequestBody MesProcurementplanBo bo) {
        try{
            MesTaskVo vo=taskService.queryById(bo.getTaskId());
            vo.setProcure(1);
            MesTaskBo tobo=(MesTaskBo) BeanMapHelper.Bean2Bean(vo, MesTaskBo.class);
            taskService.updateByBo(tobo);
        }catch (Exception e){

        }


        return R.ok(procurementplanService.insertByBo(bo));
    }

    /**
     * 组装列表
     * @return
     */
    @GetMapping("/taskc/list")
    public TableDataInfo<MesTaskcVo> gettaskcInfo(MesTaskcBo bo, PageQuery pageQuery) {
        bo.setUserId(LoginHelper.getUserId());
        return taskcService.queryPagecList(bo, pageQuery);
    }
    /**
     * 组装详情
     * @return
     */
    @GetMapping("/taskc/detail/{id}")
    public R<MesTaskcVo> gettaskcInfo(@PathVariable Long id) {
        return R.ok(taskcService.queryById(id));
    }

    /**
     * 所有的质检主键
     * @param id
     * @return
     */
    @GetMapping("/quamain/{id}")
    public R<MesQualityMainVo> getInfo(@PathVariable Long id) {
        return R.ok(qualityMainService.queryById(id));
    }

    /**
     * 采购出库
     * @param bo
     * @return
     */
    @PostMapping("/stockstl")
    public R<Boolean> stockup1(@RequestBody MesTaskBo bo) {
        if(null!=bo.getWorkId()){
            return R.ok(taskService.stockupstatus(bo));
        }else{
            MesTaskVo taskVo=taskService.queryById(bo.getId());
            MesTaskBo update1 = (MesTaskBo) BeanMapHelper.Bean2Bean(taskVo, MesTaskBo.class);
            update1.setProId(bo.getProId());
            return R.ok(taskService.stockupstatus(update1));
        }

    }

    /**
     * 出库
     * @param bo
     * @return
     */
    @PostMapping("/stockupdate")
    public R<Boolean> stockup(@RequestBody Long bo) {
        MesTaskVo taskVo=taskService.queryById(bo);
        MesTaskBo update1 = (MesTaskBo) BeanMapHelper.Bean2Bean(taskVo, MesTaskBo.class);
        return R.ok(taskService.stockupdate(update1));
    }

    /**
     * 获取质检列表
     * @return
     */
    @GetMapping("/getquamianlist")
    public R<List<MesQualityMainVo>> GetQualityMainList() {
        MesQualityMainBo bo =new MesQualityMainBo();
        return R.ok(qualityMainService.queryList(bo));
    }
    @GetMapping("/getkaoqin")
    public R<List<MesQualityMainVo>> getkaoqin() {
        try{
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listid");
            OapiUserListidRequest req = new OapiUserListidRequest();
//            for(Long id2=472128124L;id2<472128130L;id2++){
                req.setDeptId(476999300L);
                DingTalkAccessToken tk = new DingTalkAccessToken();
                String ac=tk.getAccessToken();
                OapiUserListidResponse rsp = client.execute(req, ac);
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JSONObject jsonObj = new JSONObject(rsp.getBody());
                JSONObject dataArray = jsonObj.getJSONObject("result");
                if (dataArray != null) {

                    JSONArray userIdList = dataArray.getJSONArray("userid_list");
                    for (Object id : userIdList) {
                        try {
                            DingTalkClient client2 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
                            OapiV2UserGetRequest req2 = new OapiV2UserGetRequest();
                            req2.setUserid(id.toString());
                            OapiV2UserGetResponse rsp2 = client2.execute(req2, ac);
                            JSONObject jsonObj2 = new JSONObject(rsp2.getBody());
                            JSONObject dataArray2 = jsonObj2.getJSONObject("result");
//                            System.out.println(dataArray2.get("name"));
                            LambdaQueryWrapper<SysUser> wrapper=new LambdaQueryWrapper<>();
                            wrapper.eq(SysUser::getNickName,dataArray2.get("name"));
                            wrapper.isNull(SysUser::getDdid);
                            List<SysUser> users=sysUserMapper.selectList(wrapper);
                            for (SysUser user : users) {
                                user.setDdid(id.toString());
                                sysUserMapper.updateById(user);
                            }
//                        JSONObject dataArray3 = dataArray2.getJSONObject("name");
//                        JSONObject dataArray4 = dataArray3.getJSONObject("name");
//                        System.out.println(dataArray3.toString());
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing user ID: " + id);
                        }
                    }
                }
                System.out.println(rsp.getBody());
//            }



//
//
//            OapiV2UserGetRequest req2 = new OapiV2UserGetRequest();
//            req2.setUserid("001");
//            req.setLanguage("en_US");
//            OapiV2UserGetResponse rsp = client.execute(req, access_token);
//            System.out.println(rsp.getBody());

        }catch (Exception e){

        }

        return R.ok();
    }

    /**
     * 扫码开始报工
     * @param bo
     * @return
     */
    @PostMapping("/scando")
    public R<Boolean> scando(@RequestBody MesTaskBo bo) {
       return R.ok(taskService.insertbo(bo));
    }
    @GetMapping("/user/list")
    public R<List<SysUserVo>> list(SysUserBo user, PageQuery pageQuery) {
        TableDataInfo<SysUserVo> list=userService.selectPageUserList(user, pageQuery);

        List<String> dataList=new ArrayList<>();
        List<SysUserVo> dataList3=new ArrayList<>();
        for (SysUserVo vo:list.getRows()){
           if(null!=vo.getDdid()){
                dataList.add(vo.getDdid());
                dataList3.add(vo);
           }
        }
        List<SysUserVo> dataList2=new ArrayList<>();
        try {
            DingTalkAccessToken tk = new DingTalkAccessToken();
            String ac=tk.getAccessToken();
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dates=sdf.format(System.currentTimeMillis());

            String beg=dates+" 00:00:00";
            String end=dates+" 23:59:59";
            OapiAttendanceListRequest req = new OapiAttendanceListRequest();
            req.setWorkDateFrom(beg);
            req.setWorkDateTo(end);
            req.setUserIdList(dataList);
            req.setOffset(0L);
            req.setLimit(50L);
            OapiAttendanceListResponse rsp = client.execute(req, ac);
            JSONObject jsonObj2 = new JSONObject(rsp.getBody());
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONArray userIdList = jsonObj2.getJSONArray("recordresult");
            for (Object id : userIdList) {
                JSONObject jsonObj3 = new JSONObject(id);
                SysUserVo vo=new SysUserVo();
                vo=dataList3.stream().filter(sd->sd.getDdid().equals(jsonObj3.get("userId"))).toList().get(0);
                vo.setChetime(sdf2.format(jsonObj3.get("userCheckTime")));
                dataList2.add(vo);
            }
        } catch (Exception e) {
            log.error("eeeeeeeeeeeeeeeeeeee"+e.getMessage());
            e.printStackTrace();
        }
        return R.ok(dataList2);
    }

    @GetMapping("/user/list/{id}")
    public R<List<SysUserVo>> lists(@PathVariable Long id) {
        SysUserVo sysUser=sysUserMapper.selectVoById(id);
        List<SysUserVo> dataList2=new ArrayList<>();
        try{
            DingTalkAccessToken tk = new DingTalkAccessToken();
            String ac=tk.getAccessToken();
            List<String> st=new ArrayList<>();
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/listRecord");
            OapiAttendanceListRecordRequest req = new OapiAttendanceListRecordRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dates=sdf.format(System.currentTimeMillis());

            // 将当前日期转换为 Calendar 对象
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));

            // 将日期减去4天
            cal.add(Calendar.DAY_OF_MONTH, -4);

            String newDate = sdf.format(cal.getTime());
            st.add(sysUser.getDdid());
            req.setUserIds(st);

            String beg=newDate+" 00:00:00";
            String end=dates+" 23:59:59";
            req.setCheckDateFrom(beg);
            req.setCheckDateTo(end);
            OapiAttendanceListRecordResponse rsp = client.execute(req, ac);
            JSONObject jsonObj2 = new JSONObject(rsp.getBody());
            JSONArray userIdList = jsonObj2.getJSONArray("recordresult");
            for (Object id2 : userIdList) {
                JSONObject jsonObj3 = new JSONObject(id2);

                for (int i=0;i<userIdList.size();i++){
                    SysUserVo vo=new SysUserVo();
                    vo=sysUserMapper.selectVoById(id);
                    vo.setChetime(sdf2.format(jsonObj3.get("userCheckTime")));
                    String lar=jsonObj3.get("timeResult").toString();
                    dataList2.add(vo);
                }
            }
        }catch (Exception e){

        }

    return R.ok(dataList2);
    }
    /**
     * 扫码报工
     * @param bo
     * @return
     */

    @PostMapping("/scandorep")
    public R<Boolean> scandorep(@RequestBody MesTaskBo bo) {
        return R.ok(taskService.scandorep(bo));
    }
    /**
     * 质检完工
     * @param bo
     * @return
     */
    @PostMapping("/quaidone")
    @DSTransactional
    public R<Boolean> quaidone(@RequestBody MesTaskBo bo) {
        MesTaskVo vo=taskService.queryById(bo.getId());
        MesTaskBo boifo=(MesTaskBo) BeanMapHelper.Bean2Bean(vo, MesTaskBo.class);
        MesQualityMainVo mesQualityMainVo=qualityMainService.queryById(bo.getQualityListList().get(0).getQualitymainId());
        String names=vo.getRemark()==null?"":vo.getRemark()+",";
        boifo.setRemark(names+mesQualityMainVo.getTitle()+"：结果正常");
        for (MesQualityList mesQualityList : bo.getQualityListList()) {
            MesQuaRecBo bo1=new MesQuaRecBo();
            bo1.setTaskId(bo.getId());
            bo1.setSn(bo.getSn());
            bo1.setQuaMian(mesQualityList.getQualitymainId());
            bo1.setTitle(mesQualityList.getTitle());
            bo1.setValuest(mesQualityList.getStandards());
            bo1.setStatus("!");
            if(bo.getIschecknew()==0){//通过自动自动判断
                if(ObjectUtils.isNotNull(mesQualityList.getRefvalue())){
                    try {
                        MesQualityListVo mesQualityListVo=qualityListService.queryById(mesQualityList.getId());
                        BigDecimal be=new BigDecimal(mesQualityList.getRefvalue());
                        BigDecimal stand=new BigDecimal(mesQualityListVo.getStandards());
                        BigDecimal now=new BigDecimal(mesQualityList.getStandards());
                        BigDecimal begin=ArithHelper.add(be,stand);
                        BigDecimal end=ArithHelper.sub(stand,be);
                        int res=now.compareTo(end);
                        int tes=now.compareTo(begin);
                        bo1.setRes("结果为：正常");
                        if(tes==1||res==-1){
                            bo1.setRes("结果为：异常");
                            boifo.setRemark(vo.getRemark()+","+mesQualityMainVo.getTitle()+"：结果异常");
                        }else{
                            boifo.setStatus("3");
                        }


                    }catch (Exception e){

                    }
                }
            }else{
                //未通过
                boifo.setRemark(names+mesQualityMainVo.getTitle()+"：结果异常");
                bo1.setRes("结果为：异常");
                if(bo.getScrapcount()>0){
                    boifo.setScrapcount(bo.getScrapcount());
                    boifo.setMarkcount(bo.getMarkcount()-bo.getScrapcount());
                    boifo.setStatus("1");
                }
                if(bo.getRedoingcount()>0){
                    boifo.setRedoingcount(bo.getRedoingcount());
                    boifo.setRedoingid(bo.getRedoingid());
                    boifo.setMarkcount(bo.getMarkcount()-bo.getRedoingcount());
                    boifo.setStatus("1");
                }
            }

            mesQuaRecService.insertByBo(bo1);
        }
        MesWorkVo workVo=mesWorkService.queryById(boifo.getWorkId());
        MesWorkBo workinfo=(MesWorkBo)BeanMapHelper.Bean2Bean(workVo, MesWorkBo.class);
        workinfo.setCheckcount(workVo.getCheckcount()+1);
        mesWorkService.updateByBo(workinfo);
        boifo.setIsCheck(boifo.getIsCheck()+1);
        boifo.setChecknum(boifo.getChecknum()+1);//对当前质检数量
        boifo.setIschecknew(bo.getIschecknew());//任务质检结果，0正常1报废2返工
        List<MesTaskVo> voList= DataPermissionHelper.ignore(()->{
            MesTaskBo newbowr=new MesTaskBo();
            newbowr.setSn(boifo.getSn());
            return taskService.queryList(newbowr);
        });
        for (MesTaskVo vo1:voList){
            vo1.setIsCheck(vo1.getIsCheck()+1);//全部质检数量
            MesTask boifot=(MesTask) BeanMapHelper.Bean2Bean(vo1, MesTask.class);
            DataPermissionHelper.ignore(()->{
                LambdaQueryWrapper<MesTask> queryWrapper=new LambdaQueryWrapper<>();
                queryWrapper.eq(MesTask::getId,vo1.getId());
                boifot.setSn(boifo.getSn());
                return mesTaskMapper.update(boifot,queryWrapper);
            });
        }
        return R.ok(taskService.updatetask(boifo));
    }

    @PostMapping("/quaform/add/")
    public R<String> insertquaform(@RequestBody List<MesQuaformBo> bo){
        String uuid= IdUtil.simpleUUID();
        for (MesQuaformBo bo1:bo){
            bo1.setSn(uuid);
            quaformService.insertByBo(bo1);
        }
        return R.ok1(uuid);
    }

    /**
     * 采购计划
     * @return
     */
    @GetMapping("/procurement/list")
    public R<List<MesProcurementplanVo>> listProcurementplan() {
        MesProcurementplanBo bo=new MesProcurementplanBo();
        bo.setStatus("0");
        return R.ok(procurementplanService.queryList(bo));
    }

    /**
     * 采购计划更新
     * @param bo
     * @return
     */

    @PutMapping("/procurement/update")
    public R<Boolean> proupdate(@RequestBody MesProcurementplanBo bo){
        return R.ok(procurementplanService.updateByBo(bo));
    }


    /**
     * 商品列表
     * @param bo
     * @return
     */
    @PostMapping("/product/list")
    public R<List<MesProductVo>> Productlist(@RequestBody MesProductBo bo) {
        return R.ok(productService.queryList(bo));
    }
    @PostMapping("/product/lists")
    public R<List<MesStockVo>> Productlists(@RequestBody MesStockBo bo) {
        return R.ok(stockService.queryList(bo));
    }
    /**
     * 入库
     * @param bo
     * @return
     */
    @PostMapping("/instock/insert")
    public R<Boolean> instockInsert(@RequestBody MesInstockBo bo) {
        return R.ok(mesInstockService.insertByBo(bo,0));
    }

    /**
     * 入库
     * @param bo
     * @return
     */
    @PostMapping("/instock/insertsn")
    public R<Boolean> instockInsertsn(@RequestBody MesInstockBo bo) {
        return R.ok(mesInstockService.insertByBo(bo,1));
    }

    /**
     *规格列表
     * @param id
     * @return
     */
    @GetMapping("/unitlist/{id}")
    public R<List<MesUnitmeasureVo>> unitlist(@PathVariable Long id) {
        MesOutstockBo bo=new MesOutstockBo();
        bo.setUnitId(id);
        return R.ok(mesOutstockService.getUnitList(bo));
    }

    /**
     *
     * @param bo
     * @return
     */
    @PostMapping("/outstock/insert")
    public R<Boolean> outstockInsert(@RequestBody MesOutstockBo bo) {
        return R.ok(mesOutstockService.insertByBo(bo));
    }

    /**
     * 外协
     * @param bo
     * @return
     */
    @PostMapping("/outrec/insert")
    public R<Boolean> ourrecinsert(@RequestBody MesOutRecBo bo) {
        return R.ok(mesOutRecService.insertByBo(bo));
    }

    /**
     *外协列表
     * @return
     */
    @GetMapping("/outrec/list")
    public R<List<MesOutRecVo>> outreclist() {
        MesOutRecBo bo=new MesOutRecBo();
        return R.ok(mesOutRecService.queryList(bo));
    }


    /**
     *外协列表
     * @return
     */
    @PostMapping("/outrec/update")
    public R<Boolean> outrecupdate(@RequestBody MesOutRecBo bo) {
        bo.setStatus("2");
        return R.ok(mesOutRecService.updateByBo(bo));
    }

    /**
     *外协列表
     * @return
     */
    @GetMapping("/equ/list")
    public R<List<MesEquipmentVo>> getmyeuq() {

        return R.ok(workplacesService.quertallgetlist());
    }
    @GetMapping("/equcheck/info/{id}/{status}")
    public R<List<MesEquCheckVo>> equcheckinfo(@PathVariable Long id,@PathVariable Integer status) {
        MesEquCheckBo bo=new MesEquCheckBo();
        bo.setId(id);
        bo.setTypes(status.toString());
        return R.ok(equCheckService.queryList(bo));
    }

    @GetMapping("/equ/{id}/{st}")
    public R<List<MesEqucheckListVo>> equchecklist(@PathVariable Long id,@PathVariable Long st) {
        MesEquCheckBo  checkBo=new MesEquCheckBo();
        checkBo.setEquIds(st.toString());
        List<MesEquCheckVo> list=equCheckService.queryList(checkBo);
        MesEqucheckListBo bo=new MesEqucheckListBo();
       if(list.isEmpty()){
           bo.setEquMain(id);
       }else{
           MesEquCheckVo vo=list.get(0);
           bo.setEquMain(vo.getId());
       }
        return R.ok(equcheckListService.queryList(bo));
    }

    @SaIgnore
    @PostMapping("/upload")
    public String upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            return null;
        }
        SysOssVo oss = ossService.upload(file);
        SysOssUploadVo uploadVo = new SysOssUploadVo();
        uploadVo.setUrl(oss.getUrl());
        uploadVo.setFileName(oss.getOriginalName());
        uploadVo.setOssId(oss.getOssId().toString());
        return oss.getUrl();
    }
    @PostMapping("/euqlist/insert")
    public R<Boolean> euqlistinsert(@RequestBody MesEquRecBo bo) {
        for (MesEqucheckListBo o: bo.getList()) {
            MesEquRecBo recBo=new MesEquRecBo();
            recBo.setEqumian(o.getEquMain());
            recBo.setListid(o.getId());
            recBo.setTitle(o.getTitle());
            recBo.setConts(o.getRebak());
            recBo.setEquid(bo.getEquid());
            String rk="异常";
            if(null==o.getRemark()){
                rk="正常";
            }else {
                if(o.getRemark().equals("0")){
                    rk="正常";
                }
            }
            recBo.setRest(rk);
            recBo.setRemark(o.getUpimg());
            mesEquRecService.insertByBo(recBo);
        }
        return R.ok();
    }

    @GetMapping("/tong/list")
    public R<List<MesToolUse>> tonglist() {
        return R.ok(workplacesService.gettonglist());
    }

    @GetMapping("/cutting/list")
    public R<List<MesCuttingUseVo>> cuttinglist() {
        MesCuttingUseBo bo=new MesCuttingUseBo();
        bo.setStatus("1");
        return R.ok(cuttingUseService.queryList(bo));
    }

    @PostMapping("/tong/listby")
    public R<List<MesEtoolVo>> tonglistby(@RequestBody MesEtoolBo bo) {
        return R.ok(etoolService.queryList(bo));
    }
    @PostMapping("/cutting/listby")
    public R<List<MesCuttingVo>> cuttinglistby(@RequestBody MesCuttingBo bo) {
        return R.ok(cuttingService.queryList(bo));
    }

    @PostMapping("/tongrec/insert")
    public R<Boolean> tongrecinsert(@RequestBody MesToolRecBo bo) {
        bo.setUserId(LoginHelper.getUserId());
        return R.ok(toolRecService.insertByBo(bo));
    }
    @PostMapping("/cuttingrec/insert")
    public R<Boolean> cuttinginsert(@RequestBody MesCuttingRecBo bo) {
        bo.setUserId(LoginHelper.getUserId());
        return R.ok(cuttingRecService.insertByBo(bo));
    }
    @PostMapping("/ycrec/insertycrec")
    public R<Boolean> insertycrec(@RequestBody MesYcrecBo bo) {
        bo.setUserId(LoginHelper.getUserId());
        return R.ok(ycrecService.insertByBo(bo));
    }
    @PostMapping("/tongrec/update")
    public R<Boolean> tongrecupdate(@RequestBody MesToolRecBo bo) {
        return R.ok(toolRecService.updateByBo(bo));
    }


    @GetMapping("/work/recs")
    public R<Map<String,String>> getinfo(){
        return R.ok(workRecService.getinfo());
    }
    @GetMapping("/workrec/list")
    public TableDataInfo<MesWorkRecVo> getreclist(PageQuery pageQuery){

        MesWorkRecBo bo=new MesWorkRecBo();
        bo.setUserId(LoginHelper.getUserId());
        return workRecService.queryPageList(bo,pageQuery);
    }
    @GetMapping("/menulist")
    public R<List<MesMobileMenuVo>> menulist(){


        return R.ok(mesMobileMenuService.getmylist());
    }
    @GetMapping("/userlist/list")
    public R<List<MesTeamsVo>> userlist(){
        MesTeamsBo bo=new MesTeamsBo();
        bo.setUpids(LoginHelper.getUserId().toString());
        return R.ok(mesTeamsService.queryList(bo));
    }
    @GetMapping("/stock/allred")
    private R<List<MesAllrecVo>> stockallred(@RequestBody MesAllrecBo bo){
        return R.ok(allrecService.queryList(bo));
    }


}
