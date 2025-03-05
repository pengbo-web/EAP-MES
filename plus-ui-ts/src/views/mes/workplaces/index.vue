<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="编码" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="车间ID" prop="workshopId">
              <el-input v-model="queryParams.workshopId" placeholder="请输入车间ID" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="设备" prop="equIds">
              <el-input v-model="queryParams.equIds" placeholder="请输入设备" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="班组" prop="teams">
              <el-input v-model="queryParams.teams" placeholder="请输入班组" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:workplaces:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:workplaces:edit']"
              >修改</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:workplaces:remove']"
              >删除</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:workplaces:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="workplacesList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="编码" align="center" prop="sn" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="车间ID" align="center" prop="workshopName" />
        <el-table-column label="设备" align="center" prop="equIdnames" />
        <el-table-column label="班组" align="center" prop="teamnames" />
        <el-table-column label="夹具" align="center" prop="tongames" />
        <el-table-column label="工序" align="center" prop="processNames" />
        <el-table-column label="班次" align="center" prop="counts" >
          <template #default="scope">
            <el-tag type=""info>08:00-12:00</el-tag>
            <el-tag type=""info>12:00-18:00</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:workplaces:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:workplaces:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改工作站对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="workplacesFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编码" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="车间ID" prop="workshopId">
          <el-select v-model="form.workshopId" placeholder="请选择车间ID">
            <el-option v-for="item in workplaceList" :key="item.id" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-tabs type="border-card">
          <el-tab-pane label="选择班组">
            <el-text class="mx-1" type="success">请选择执行班组</el-text>
            <el-tree-select
                v-model="form.teamsarray"
                :data="teamtList"
                filterable
                :props="{ value: 'deptId', label: 'deptName', children: 'children' }"
                value-key="deptId"
                placeholder="选择上级部门"
                check-strictly
                multiple
              />
          </el-tab-pane>
          <el-tab-pane label="选择设备">选择设备
            <el-select v-model="form.equIdsarray" multiple filterable placeholder="请选择设备">
              <el-option
                v-for="item in equipmentList"
                :key="item.id"
                :label="item.title"
                :value="item.id"
                multiple
              />
            </el-select>
          </el-tab-pane>
          <el-tab-pane label="选择工序">选择工序
            <el-transfer v-model="form.processIdsarray" :data="datatra" filterable  :titles="['所有工序', '执行工序']"/>
          </el-tab-pane>
          <el-tab-pane label="选择夹具">选择夹具
            <el-button type="primary" @click="opendiag2">选择</el-button>

            <el-table v-loading="loading" :data="etoolListuse"  v-if="etoolListuse" border>
              <el-table-column label="主键" align="center" prop="id" v-if="false"/>
              <el-table-column label="名称" align="center" prop="title" />
              <el-table-column label="编码" align="center" prop="sn" />
              <el-table-column label="品牌" align="center" prop="brand" />
              <el-table-column label="类别" align="center" prop="otype">
                <template #default="scope">
                  <dict-tag :options="equusestatus" :value="scope.row.otype"/>
                </template>
              </el-table-column>
              <el-table-column label="使用数量" align="center" prop="count" width="220"/>
            </el-table>
            <!-- <el-divider v-if="form.toolUses"> 原数据 </el-divider>  -->
            <el-table v-loading="loading" :data="form.toolUses"  border v-if="form.toolUses">
              <el-table-column label="主键" align="center" prop="id" v-if="false" />
              <el-table-column label="名称" align="center" prop="title" />
              <el-table-column label="编码" align="center" prop="sn" />
              <el-table-column label="品牌" align="center" prop="brand" />
              <el-table-column label="类别" align="center" prop="otype">
                <template #default="scope">
                  <dict-tag :options="equusestatus" :value="scope.row.otype"/>
                </template>
              </el-table-column>
              <el-table-column label="使用数量" align="center" prop="count" width="220"/>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="执行班次">班次管理
            <el-button type="primary" @click="opendiag2">选择</el-button>

            <el-table v-loading="loading" :data="etoolListuse"  v-if="etoolListuse" border>
              <el-table-column label="主键" align="center" prop="id" v-if="false"/>
              <el-table-column label="名称" align="center" prop="title" />
              <el-table-column label="时间短" align="center" prop="sn" />
            
            </el-table>
            <!-- <el-divider v-if="form.toolUses"> 原数据 </el-divider>  -->
            <el-table v-loading="loading" :data="form.toolUses"  border v-if="form.toolUses">
              <el-table-column label="主键" align="center" prop="id" v-if="false" />
              <el-table-column label="名称" align="center" prop="title" />
              <el-table-column label="编码" align="center" prop="sn" />
              <el-table-column label="品牌" align="center" prop="brand" />
              <el-table-column label="类别" align="center" prop="otype">
                <template #default="scope">
                  <dict-tag :options="equusestatus" :value="scope.row.otype"/>
                </template>
              </el-table-column>
              <el-table-column label="使用数量" align="center" prop="count" width="220"/>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog title="选择车间"  fullscreen  append-to-body v-model="dialog2.visible" class="md">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover" style="font-size: 12px;">
          <el-form ref="queryFormRef" :model="param2" :inline="true" >
            <el-form-item label="名称" prop="title" >
              <el-input v-model="param2.title" placeholder="请输入名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="编码" prop="sn">
              <el-input v-model="param2.sn" placeholder="请输入编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="类别" prop="otype">
              <el-select v-model="param2.otype" placeholder="请选择类别" clearable >
                <el-option v-for="dict in equusestatus" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery2">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
      <div>
      <el-table v-loading="loading" :data="etoolList"  border style="font-size: 12px;">
        <el-table-column label="选择" min-width="120" align="center" >
          <template #default="scope">
            <el-button type="primary" size="small" @click="inserttool(scope.row)">
          选择
        </el-button>
          </template>
          
        </el-table-column>
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="编码" align="center" prop="sn" />
        <el-table-column label="品牌" align="center" prop="brand" />
        <el-table-column label="类别" align="center" prop="otype">
          <template #default="scope">
            <dict-tag :options="equusestatus" :value="scope.row.otype"/>
          </template>
        </el-table-column>
        <el-table-column label="历史库存" align="center" prop="stockrec" />
        <el-table-column label="当前库存" align="center" prop="stocknum" />
        <el-table-column label="使用中数量" align="center" prop="usestock" />
        <el-table-column label="可用库存量" align="center" prop="instock" />
        <el-table-column label="报废库存" align="center" prop="otstock" />
        <el-table-column label="保养周期" align="center" prop="otime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.otime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下次周期" align="center" prop="nexttime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.nexttime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="totaltool > 0" :total="totaltool" v-model:page="param2.pageNum" v-model:limit="param2.pageSize" @pagination="gettoolList" />
      </div>
      <div class="mt-12">
        <el-divider >添加内容</el-divider>
        <el-table v-loading="loading" :data="etoolListuse"  border>
        <el-table-column label="选择" min-width="120" align="center">
          <template #default="scope">
          <el-button type="warning" size="small" @click="todelect(scope.row)">
          删除
          </el-button>
          </template>
        </el-table-column>
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="编码" align="center" prop="sn" />
        <el-table-column label="品牌" align="center" prop="brand" />
        <el-table-column label="类别" align="center" prop="otype">
          <template #default="scope">
            <dict-tag :options="equusestatus" :value="scope.row.otype"/>
          </template>
        </el-table-column>
        <el-table-column label="使用数量" align="center" prop="usestock" width="220">
          <template #default="scope">
            
          <el-input-number v-model="scope.row.count" :min="1" :max="100" @change="handleChange($event, scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </div>
     <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitFormtb">提 交</el-button>
          <el-button @click="dialog2.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Workplaces" lang="ts">
import { listWorkplaces, getWorkplaces, delWorkplaces, addWorkplaces, updateWorkplaces } from '@/api/mes/workplaces';
import { ToolUseVO } from '@/api/mes/toolUse';
import { WorkplacesVO, WorkplacesQuery, WorkplacesForm } from '@/api/mes/workplaces/types';
import { listProcess ,listByProcess} from '@/api/mes/process';
import { ProcessVO } from '@/api/mes/process/types';
import { listWorkplace, getWorkplace, delWorkplace, addWorkplace, updateWorkplace } from '@/api/mes/workplace';
import { listToolUse, getToolUse, delToolUse, addToolUse, updateToolUse } from '@/api/mes/toolUse';
import { WorkplaceVO, WorkplaceQuery, WorkplaceForm } from '@/api/mes/workplace/types';
import { listDept } from '@/api/system/dept';
import { DeptVO } from '@/api/system/dept/types';
import { listEquipment } from '@/api/mes/equipment';
import { EquipmentVO } from '@/api/mes/equipment/types';
import { listEtool, getEtool, delEtool, addEtool, updateEtool } from '@/api/mes/etool';
import { EtoolVO, EtoolQuery, EtoolForm } from '@/api/mes/etool/types';
import { fa } from 'element-plus/es/locale/index.mjs';
import { Console } from 'console';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable,protypes } = toRefs<any>(proxy?.useDict('sys_normal_disable','protypes'));
  const { diyisno, equusestatus } = toRefs<any>(proxy?.useDict('diyisno', 'equusestatus'));
const teamtList = ref<DeptVO[]>([]);
const etoolList = ref<EtoolVO[]>([]);
const etoolListuse = ref<EtoolVO[]>([]);
const workplaceList = ref<WorkplaceVO[]>([]);
const processList = ref<ProcessVO[]>([]);
const workplacesList = ref<WorkplacesVO[]>([]);
const equipmentList = ref<EquipmentVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const tooluse=ref<ToolUseVO[]>([]);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const totaltool=ref(0);

const queryFormRef = ref<ElFormInstance>();
const workplacesFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const dialog2 = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: WorkplacesForm = {
  id: undefined,
  sn: undefined,
  title: undefined,
  workshopId: undefined,
  equIdsarray: undefined,
  teamsarray: undefined,
  tongsIdsarray: undefined,
  processIdsarray: undefined,
  counts: undefined,
  status: undefined,
  remark: undefined,
  toolUses: undefined
};

const data = reactive<PageData<WorkplacesForm, WorkplacesQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    title: undefined,
    workshopId: undefined,
    equIds: undefined,
    teams: undefined,
    tongsIds: undefined,
    processIds: undefined,
    counts: undefined,
    status: undefined,
    params: {}
  },
  rules: {
    title: [{ required: true, message: '标题不能为空', trigger: 'blur' }]
  }
});
const param=ref<any>({
  pageNum: 1,
    pageSize: 1000,
});
const param2=ref<any>({
  pageNum: 1,
    pageSize: 5,
    title: undefined,
    sn: undefined,
    brand: undefined,
    otype: undefined,
    stocknum: undefined,
    stockrec: undefined,
    otstock: undefined,
    usestock: undefined,
    otime: undefined,
    nexttime: undefined,
    instock: undefined,
    status: undefined,
    params: {
    }
});
const { queryParams, form, rules } = toRefs(data);
/** 查询工装夹具列表 */
const gettoolList = async () => {
  loading.value = true;
  const res = await listEtool(param2.value);
  console.log(res)
  etoolList.value = res.rows;
  totaltool.value = res.total;
  loading.value = false;
}
/** 查询工作站列表 */
const getList = async () => {
  loading.value = true;
  const res = await listWorkplaces(queryParams.value);
  workplacesList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};
/** 查询设备列表列表 */
const getequList = async () => {
  const res = await listEquipment(param.value);
  equipmentList.value = res.rows;
}
const inserttool=async(item:any)=>{
  
  item.count=1;
  etoolListuse.value.push(item)
  
}
const submitFormtb=async()=>{
  form.value.tongsIdsarray=[]
  etoolListuse.value.forEach(item => {
    tooluse.value.push({toolid:item.id,count:item.count})
  })
  let res=await addToolUse(tooluse.value).finally(() =>  buttonLoading.value = false);
  let arr=res.msg.split(',')
  form.value.tongsIdsarray=arr
  dialog2.visible = false;
}
const todelect=async(item:any)=>{
  etoolListuse.value.splice(etoolListuse.value.indexOf(item), 1)
}
const handleChange = (value: number, op: any) => {

  if(value>op.stocknum){
    etoolListuse.value.forEach(item => {if(item.id==op.id)item.count=op.stocknum})
  }
}
/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
};
/** 查询⑧工艺管理列表 */
const getproList = async () => {
  const res = await listProcess(param.value);
  processList.value = res.rows;
  processList.value.forEach(item => {
    if(item.remark=='1'){
      item.title = item.typename
    }
    
  })
}
const opendiag2=()=>{
  dialog2.visible=true
  etoolListuse.value=[]
  tooluse.value=[]
}
/** 查询④车间设置列表 */
const getworkList = async () => {
  const res = await listWorkplace(param.value);
  workplaceList.value = res.rows;
}

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  workplacesFormRef.value?.resetFields();
};
interface Option {
  key: number|string
  label: string
  disabled: boolean
}

const generateData = () => {
  const data: Option[] = []
  processList.value.forEach(item => {
    let statusname=''
    protypes.value.forEach(item2 => {
      if(item2.value==item.typeId){
        statusname= item2.label
      }
    })
    data.push({
      key: item.id,
      label: item.title+'('+statusname+')',
      disabled: false,
    })
  })
  datatra.value=data
}
const datatra = ref<Option[]>()
/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};
/** 搜索工具操作 */
const handleQuery2 = () => {
  param2.value.pageNum = 1;
  gettoolList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
};

/** 多选框选中数据 */
const handleSelectionChange = (selection: WorkplacesVO[]) => {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  generateData();
  dialog.visible = true;
  dialog.title = '添加工作站';
};

/** 修改按钮操作 */
const handleUpdate = async (row?: WorkplacesVO) => {
  reset();
  generateData();
  form.value.toolUses=[]
  const _id = row?.id || ids.value[0];
  const res = await getWorkplaces(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改工作站';
};
/** 查询菜单列表 */
const getteamList = async () => {
  const res = await listDept(queryParams.value);
  const data = proxy?.handleTree<DeptVO>(res.data, 'deptId');
  if (data) {
    teamtList.value = data;
  }
};
/** 提交按钮 */
const submitForm = () => {
  workplacesFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateWorkplaces(form.value).finally(() => (buttonLoading.value = false));
      } else {
        await addWorkplaces(form.value).finally(() => (buttonLoading.value = false));
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
const handleDelete = async (row?: WorkplacesVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除工作站编号为"' + _ids + '"的数据项？').finally(() => (loading.value = false));
  await delWorkplaces(_ids);
  proxy?.$modal.msgSuccess('删除成功');
  await getList();
};

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'mes/workplaces/export',
    {
      ...queryParams.value
    },
    `workplaces_${new Date().getTime()}.xlsx`
  );
};

onMounted(() => {
  getList();
  getteamList();
  getequList();
  getproList();
  getworkList();
  gettoolList();
});
</script>
<style scoped lang="scss">
  .md{
    margin: 0 auto !important;
    border-radius: 10px;
    width: 95%;
    line-height: 2.5rem;
  }
</style>