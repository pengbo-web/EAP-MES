<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="组员" prop="useids">
              <el-input v-model="queryParams.useids" placeholder="请输入组员" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="班组长" prop="upids">
              <el-input v-model="queryParams.upids" placeholder="请输入班组长" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:teams:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:teams:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:teams:remove']">删除</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="teamsList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="标识" align="center" prop="remark" />
        <el-table-column label="班组长" align="center" prop="upnames" />
        <el-table-column label="组员" align="center" prop="stname" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:teams:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:teams:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改班组管理对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="teamsFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标识" prop="remark">
            <el-input v-model="form.remark"  placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="组员" prop="useids">
          <el-select v-model="form.useids" multiple placeholder="组员" style="width: 240px" filterable>
            <el-option v-for="item in userList" :key="item.userId" :label="item.userName+'..班组：'+item.deptName" :value="item.userId" />
          </el-select>
        </el-form-item>
        <el-form-item label="班组长" prop="upids">
          <el-select v-model="form.upids" multiple placeholder="组员" style="width: 240px" filterable>
            <el-option v-for="item in userList" :key="item.userId" :label="item.userName+'..班组：'+item.deptName" :value="item.userId" />
          </el-select>
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

<script setup name="Teams" lang="ts">
import { listTeams, getTeams, delTeams, addTeams, updateTeams } from '@/api/mes/teams';
import { TeamsVO, TeamsQuery, TeamsForm } from '@/api/mes/teams/types';
import { UserForm, UserQuery, UserVO } from '@/api/system/user/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const teamsList = ref<TeamsVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const userList = ref<UserVO[]>();
const queryFormRef = ref<ElFormInstance>();
const teamsFormRef = ref<ElFormInstance>();
  import api from '@/api/system/user';
  const dateRange = ref<[DateModelType, DateModelType]>(['', '']);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: TeamsForm = {
  id: undefined,
  useids: undefined,
  upids: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<TeamsForm, TeamsQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 1000,
    useids: undefined,
    upids: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);
const getListuser = async () => {
  loading.value = true;
  const res = await api.listUser(proxy?.addDateRange(queryParams.value, dateRange.value));
  loading.value = false;
  userList.value = res.rows;
  total.value = res.total;
};
/** 查询班组管理列表 */
const getList = async () => {
  loading.value = true;
  const res = await listTeams(queryParams.value);
  teamsList.value = res.rows;
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
  form.value = {...initFormData};
  teamsFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: TeamsVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加班组管理";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: TeamsVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getTeams(_id);
  Object.assign(form.value, res.data);
  form.value.useids = form.value.useids.split(',').map(Number);
  form.value.upids = form.value.upids.split(',').map(Number);
  dialog.visible = true;
  dialog.title = "修改班组管理";
}

/** 提交按钮 */
const submitForm = () => {
  teamsFormRef.value?.validate(async (valid: boolean) => {
    form.value.useids = form.value.useids.join(',');
    form.value.upids = form.value.upids.join(',');
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateTeams(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addTeams(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: TeamsVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除班组管理编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delTeams(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/teams/export', {
    ...queryParams.value
  }, `teams_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getListuser();
});
</script>
