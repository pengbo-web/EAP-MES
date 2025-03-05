<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="编码" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="类别" prop="typeId">
              <el-input v-model="queryParams.typeId" placeholder="请输入类别" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标准" prop="standards">
              <el-input v-model="queryParams.standards" placeholder="请输入标准" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="浮动" prop="refvalue">
              <el-input v-model="queryParams.refvalue" placeholder="请输入浮动" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
                <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
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
      <div> <el-tag size="medium" type="primary" class="mb1">Mes使用第8步，具体添加的示例，点击【新增】后可看到介绍</el-tag></div>
    </el-card>
    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd"
              v-hasPermi="['mes:process:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['mes:process:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:process:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['mes:process:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="processList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="类别" align="center" prop="typeId">

          <template #default="scope">
            <dict-tag :options="protypes" :value="scope.row.typeId" />
          </template>
        </el-table-column>
        <!-- <el-table-column label="编码" align="center" prop="sn" /> -->
        <el-table-column label="薪资类型" align="center" prop="">
          <template #default="scope">
            <dict-tag :options="protypeofm" :value="scope.row.moneytype" />
          </template>
        </el-table-column>
        <el-table-column label="单价" align="center" prop="">
          <template #default="scope">
            <dict-tag :options="protypeofm" :value="scope.row.moneyprice+'￥'" />
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="SAP" align="center" prop="typename" />
        <el-table-column label="工时(分钟）" align="center" prop="refvalue" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['mes:process:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:process:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改⑧工艺管理对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="900px" append-to-body>
      <el-form ref="processFormRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="类型" prop="moneytype">
          <el-select v-model="form.moneytype" placeholder="选择类型" clearable>
            <el-option v-for="dict in protypeofm" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="单价" prop="moneyprice">
          <el-input-number v-model="form.moneyprice" :precision="2" :step="0.1" :max="10" />
        </el-form-item>
        <el-form-item label="标准工时" prop="remark">
          <el-input v-model="form.refvalue" placeholder="请输入分钟">
            <template #append>分钟</template>
          </el-input>
        </el-form-item>
        <el-form-item label="类别" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择类别" @change="typechecked">
            <el-option v-for="dict in protypes" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="质检" prop="standards" v-if="ttype == 1" >
          <el-select v-model="form.standards" placeholder="请选择">
            <el-option v-for="dict in qualityList" :key="dict.id" :label="dict.title" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="外协" prop="standards" v-if="ttype == 3" >
          <el-select v-model="form.standards" placeholder="请选择">
            <el-option v-for="dict in outsourcingList" :key="dict.id" :label="dict.title" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="人员" prop="userids" >
          <el-select v-model="form.userids" placeholder="请选择" multiple filterable>
            <el-option v-for="item in userList" :key="item.userId" :label="item.userName+'..班组：'+item.deptName" :value="item.userId" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="作业指导书（选填">
          <editor v-model="form.remark" :min-height="132" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Process" lang="ts">
import { listProcess, getProcess, delProcess, addProcess, updateProcess } from '@/api/mes/process';
import { ProcessVO, ProcessQuery, ProcessForm } from '@/api/mes/process/types';
import { listQualityMain } from '@/api/qualityMain/qualityMain';
import { QualityMainVO } from '@/api/qualityMain/qualityMain/types';
import { listOutsourcing, getOutsourcing, delOutsourcing, addOutsourcing, updateOutsourcing } from '@/api/mes/outsourcing';
import { OutsourcingVO, OutsourcingQuery, OutsourcingForm } from '@/api/mes/outsourcing/types';
import { UserForm, UserQuery, UserVO } from '@/api/system/user/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable, diyisno, protypeofm, protypes } = toRefs<any>(proxy?.useDict('sys_normal_disable', 'diyisno', 'protypeofm', 'protypes'));
const processList = ref<ProcessVO[]>([]);
const userList = ref<UserVO[]>();
const qualityList = ref<QualityMainVO[]>([]);
const outsourcingList = ref<OutsourcingVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const ttype = ref(0);
const typecheck = ref(0);
import api from '@/api/system/user';
const queryFormRef = ref<ElFormInstance>();
const processFormRef = ref<ElFormInstance>();
const dateRange = ref<[DateModelType, DateModelType]>(['', '']);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});
const param2=ref<any>({
  pageNum: 1,
    pageSize: 1000,
});
const initFormData: ProcessForm = {
  id: undefined,
  sn: undefined,
  typeId: undefined,
  standards: undefined,
  refvalue: undefined,
  title: undefined,
  status: '0',
  remark: undefined,
  userids: undefined,
  moneyprice:0,
  mustbe: '1'
}
const data = reactive<PageData<ProcessForm, ProcessQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    typeId: undefined,
    standards: undefined,
    refvalue: undefined,
    title: undefined,
    status: undefined,
    mustbe: undefined,
    params: {
    }
  },
  rules: {
    title: [
      { required: true, message: "标题不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "角色状态不能为空", trigger: "change" }
    ],
  }
});
const getListuser = async () => {
  loading.value = true;
  const res = await api.listUser(proxy?.addDateRange(param2.value, dateRange.value));
  loading.value = false;
  userList.value = res.rows;
  total.value = res.total;
};
const { queryParams, form, rules } = toRefs(data);
/** 查询⑦工艺类别列表 */
const getListtype = async () => {
  const res = await listOutsourcing(queryParams.value);
  outsourcingList.value = res.rows;
}
/** 查询⑧工艺管理列表 */
const getList = async () => {
  loading.value = true;
  const res = await listProcess(queryParams.value);
  processList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  processFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: ProcessVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  getListtype();
  dialog.visible = true;
  dialog.title = "添加⑧工艺管理";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: ProcessVO) => {
  reset();
  getListtype();
  const _id = row?.id || ids.value[0]
  const res = await getProcess(_id);
  Object.assign(form.value, res.data);
  if(form.value.userids){
    form.value.userids = form.value.userids.split(',').map(Number);
  }
  dialog.visible = true;
  dialog.title = "修改⑧工艺管理";
}

const typechecked = async (value: any) => {
  ttype.value=value;
}

/** 提交按钮 */
const submitForm = () => {
  processFormRef.value?.validate(async (valid: boolean) => {
    if(form.value.userids){
      form.value.userids = form.value.userids.join(',');
    }
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateProcess(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addProcess(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: ProcessVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除⑧工艺管理编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delProcess(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/process/export', {
    ...queryParams.value
  }, `process_${new Date().getTime()}.xlsx`)
}

const getqualityList = async () => {
  loading.value = true;
  const res = await listQualityMain();
  qualityList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}
const ruleslist = reactive({
  title: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  standards: [{ required: true, message: '请输入标准', trigger: 'blur' }]
})
onMounted(() => {
  getList();
  getqualityList();
  getListuser();
});
</script>
