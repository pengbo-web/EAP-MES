<template>
  <div class="app-container home">
    <!--块类统计-->
    <!-- <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <h3>商品本月销量</h3>
          <div>
            <i class="el-icon-s-shop" style="color: purple"></i>
            <span>2424132</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <h3>累计用户数</h3>
          <div>
            <i class="el-icon-user-solid" style="color: green"></i>
            <span>12763</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <h3>消费增长率</h3>
          <div>
            <i class="el-icon-s-flag" style="color: red"></i>
            <span>28%</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <h3>入驻商家数</h3>
          <div>
            <i class="el-icon-star-on" style="color: blue"></i>
            <span>358</span>
          </div>
        </el-card>
      </el-col>
    </el-row> -->
    <el-row :gutter="70">
     
      <el-col :span="6" :sm="24" :lg="24" style="padding-left: 20px" class=" pt-1">
        <el-card>
          <template #header>
            <div class="p-2">
              <span class="text-3">快捷入口</span>
            </div>
          </template>
          <div class="grid gap-4 grid-cols-4 h-20">
            <div class="bg-[#38bdf8] flex items-center justify-center rounded-1 shadow-sm cursor-pointer"
              @click="tourl('mestask/work')">
              <div class="text-white">
                生产排产
              </div>
              <div class="pl-1 pt-1 ml-8 bg-white rounded-full w-6 h-6 ">
                <el-icon>
                  <ArrowRightBold />
                </el-icon>
              </div>
            </div>
            <div class="bg-[#0ea5e9] flex items-center justify-center rounded-1 shadow-sm cursor-pointer"
              @click="tourl('reportmain/quaRec')">
              <div class="text-white">
                质检审批
              </div>
              <div class="pl-1 pt-1 ml-8 bg-white rounded-full w-6 h-6 ">
                <el-icon>
                  <ArrowRightBold />
                </el-icon>
              </div>
            </div>
            <div class="bg-[#0284c7] flex items-center justify-center rounded-1 shadow-sm cursor-pointer"
              @click="tourl('mestask/task')">
              <div class="text-white">
                任务列表
              </div>
              <div class="pl-1 pt-1 ml-8 bg-white rounded-full w-6 h-6 ">
                <el-icon>
                  <ArrowRightBold />
                </el-icon>
              </div>
            </div>
            <div class="bg-[#0c4a6e] flex items-center justify-center rounded-1 shadow-sm cursor-pointer"
              @click="tourl('reportmain/toolRec')">
              <div class="text-white">
                夹具复核
              </div>
              <div class="pl-1 pt-1 ml-8 bg-white rounded-full w-6 h-6 ">
                <el-icon>
                  <ArrowRightBold />
                </el-icon>
              </div>
            </div>
          </div>

        </el-card>
      </el-col>
      <el-col :sm="24" :lg="12" style="padding-left: 20px" class=" pt-1">
        <el-card>
          <template #header>
            <div class="p-2">
              <span class="text-3">今日数据</span>
            </div>
          </template>
          <div class="grid gap-3 grid-cols-3 h-24">
            <div class="bg-[#0284c7] flex items-center justify-between rounded-1 shadow-sm">
              <div class="text-white">
                <img src="http://images.llcoms.com/base/日报工时.png" width="70">
              </div>
              <div class="text-white pr-6 flex flex-col items-end ">
                <span class="text-white text-8 font-bold">{{ worknum }}</span>
                <span class="text-white text-3">今日报工数量</span>
              </div>
            </div>
            <div class="bg-[#0369a1] flex items-center justify-between rounded-1 shadow-sm">
              <div class="text-white">
                <img src="http://images.llcoms.com/base/che1.png" width="70">
              </div>
              <div class="text-white pr-6 flex flex-col items-end ">
                <span class="text-white text-8 font-bold">{{ quanum }}</span>
                <span class="text-white text-3">今日质检次数</span>
              </div>
            </div>
            <div class="bg-[#0c4a6e] flex items-center justify-between rounded-1 shadow-sm">
              <div class="text-white">
                <img src="http://images.llcoms.com/base/che2.png" width="70">
              </div>
              <div class="text-white pr-6 flex flex-col items-end ">
                <span class="text-white text-8 font-bold">{{ warnnum }}</span>
                <span class="text-white text-3">今日报警次数</span>
              </div>
            </div>
          </div>
          <el-divider content-position="left">当前质检列表</el-divider>
          <div class="pt-5">
            <el-table :data="quaRecList">
              <el-table-column label="质检主键" align="center" prop="quaname" />
              <el-table-column label="流转号" align="center" prop="sn" />
              <el-table-column label="标题" align="center" prop="title" />
              <el-table-column label="结果" align="center" prop="res" >
                <template #default="scope">
                  <span v-if="scope.row.res!=null"  >
                  {{ scope.row.res }}
                  </span>
                  <span v-else class="text-red-500" >
                 暂未完成
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
      <el-col :sm="24" :lg="12" style="padding-left: 3px" class=" pt-1">
        <el-card>
          <template #header>
            <div class="p-2">
              <span class="text-3">进行中任务</span>
            </div>
          </template>
          <div class="grid gap-2 grid-cols-2 h-24">
            <div class="bg-[#0284c7] flex items-center justify-between rounded-1 shadow-sm">
              <div class="text-white">
                <img src="http://images.llcoms.com/base/mking.png" width="60">
              </div>
              <div class="text-white pr-6 flex flex-col items-end ">
                <span class="text-white text-8 font-bold">{{ worknum }}</span>
                <span class="text-white text-3">本月在流转</span>
              </div>
            </div>
            <div class="bg-[#0369a1] flex items-center justify-between rounded-1 shadow-sm">
              <div class="text-white">
                <img src="http://images.llcoms.com/base/mkdone.png" width="60">
              </div>
              <div class="text-white pr-6 flex flex-col items-end ">
                <span class="text-white text-8 font-bold">{{ wkdone }}</span>
                <span class="text-white text-3">本月已完成数量</span>
              </div>
            </div>
          </div>
          <el-table v-loading="loading" :data="workList">
            <el-table-column label="编码" align="center" prop="sn" />
            <el-table-column label="标题" align="center" prop="title" />
            <el-table-column label="来源类型" align="center" prop="fromtype">
              <template #default="scope">
                <dict-tag :options="diy_mark" :value="scope.row.fromtype" />
              </template>
            </el-table-column>
            <el-table-column label="产品关联" align="center" prop="proId">
              <template #default="scope">
                {{ scope.row.proinfo.title }}
              </template>
            </el-table-column>
            <el-table-column label="所需数量" align="center" prop="neednum" />
            <el-table-column label="当前数量" align="center" prop="currnum" />
            <el-table-column label="所有工序" align="center" prop="processcount" />
            <el-table-column label="当前工序" align="center" prop="currprocess" />
            <el-table-column label="进度" align="center">
              <template #default="scope">
                <el-progress :text-inside="true" status="success" :stroke-width="26"
                  :percentage="(scope.row.okprocess / scope.row.processcount) * 100" />
              </template>
            </el-table-column>
            <el-table-column label="质检次数" align="center" prop="checkcount" />
            <el-table-column label="是否加急" align="center" prop="istop">
              <template #default="scope">
                <dict-tag :options="diyisno" :value="scope.row.istop" />
              </template>
            </el-table-column>
            <el-table-column label="需求日期" align="center" prop="endtime" width="180">
              <template #default="scope">
                <span>{{ parseTime(scope.row.endtime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>

            <el-table-column label="关联订单号" align="center" prop="orderid" />
            <el-table-column label="排产状态" align="center" prop="status">
              <template #default="scope">
                <dict-tag :options="worktype" :value="scope.row.status" />
              </template>
            </el-table-column>
            <!-- <el-table-column label="备注" align="center" prop="remark" /> -->

          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <el-divider />
  </div>
</template>

<script setup name="Index" lang="ts">
import User from '@/store/modules/user';
import { login as loginApi, logout as logoutApi, getInfo as getUserInfo } from '@/api/login';
import { listWork, getWork, delWork, addWork, updateWork } from '@/api/mes/work';
import { listQuaRec, getQuaRec, delQuaRec, addQuaRec, updateQuaRec,getindexlist } from '@/api/mes/quaRec';
import { QuaRecVO, QuaRecQuery, QuaRecForm } from '@/api/mes/quaRec/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
import { WorkVO, WorkQuery, WorkForm } from '@/api/mes/work/types';
const dateRangeEndtime = ref<[DateModelType, DateModelType]>(['', '']);
import router from '@/router/index';
const { diyisno, diy_mark, isturnon, worktype } = toRefs<any>(proxy?.useDict('diyisno', 'diy_mark', 'isturnon', 'worktype'));
const workList = ref<WorkVO[]>([]);
const quaRecList = ref<QuaRecVO[]>([]);
const userStore = ref<User>({});
const total = ref(0);
const nickName = ref('');
const banzu = ref('');
const latime = ref('');
const loading = ref(true);
const worknum=ref(0);
const quanum=ref(0);
const warnnum=ref(0);
const wkdone=ref(0);
const wking=ref(0);
const goTarget = (url: string) => {
  window.open(url, '__blank');
};
const tourl = (item: any) => {
  router.push(item);
};
onMounted(async () => {
  getList();
  getquaList();
  getindex();
  const res = await getUserInfo();
  nickName.value = res.data.user.nickName;
  banzu.value = res.data.user.deptName;
  latime.value = res.data.user.loginDate;
  userStore.value = res.data;
});
const getindex=async()=>{
  let res=await getindexlist();
  worknum.value=res.data.work
  quanum.value=res.data.qua
  warnnum.value=res.data.warn
  wking.value=res.data.workdone
  wkdone.value=res.data.markingdone
  console.log(res);
}
const newf=ref({
    pageNum: 1,
    pageSize: 4,
    sn: undefined,
    title: undefined,
    fromtype: undefined,
    proId: undefined,
    neednum: undefined,
    istop: undefined,
    status: undefined,
    params: {
      beginEndtime:undefined,
      endEndtime: undefined,
    }
  });
const getquaList = async () => {
  let be=new Date().getFullYear()+'-'+(new Date().getMonth()+1)+'-'+new Date().getDate()+" 00:00:00"
  let en=new Date().getFullYear()+'-'+(new Date().getMonth()+1)+'-'+new Date().getDate()+" 23:59:59"
 newf.value.params.beginEndtime=be
 newf.value.params.endEndtime=en
  const res = await listQuaRec(newf.value);
  quaRecList.value = res.rows;
}

const initFormData: WorkForm = {
  id: undefined,
  sn: undefined,
  title: undefined,
  fromtype: 0,
  proId: undefined,
  neednum: 1,
  currnum: undefined,
  processcount: undefined,
  checkcount: undefined,
  istop: 0,
  endtime: undefined,
  currprocess: undefined,
  orderid: undefined,
  remark: undefined,
  grow: 1,
  ismrp: 0,
  isturn: 0,
  status: '1'
}
const data = reactive<PageData<WorkForm, WorkQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    title: undefined,
    fromtype: undefined,
    proId: undefined,
    neednum: undefined,
    istop: undefined,
    status: undefined,
    params: {
      endtime: undefined,
    }
  },
  rules: {
    title: [
      { required: true, message: "标题不能为空", trigger: "blur" }
    ],
    neednum: [
      { required: true, message: "所需数量不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);
const getList = async () => {
  loading.value = true;
  queryParams.value.params = {};
  queryParams.value.status = 1
  proxy?.addDateRange(queryParams.value, dateRangeEndtime.value, 'Endtime');
  const res = await listWork(queryParams.value);
  workList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: 'open sans',
  'Helvetica Neue',
  Helvetica,
  Arial,
  sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
