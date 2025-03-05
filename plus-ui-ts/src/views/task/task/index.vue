<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="流转号" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入流转号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="所需数量" prop="needcount">
              <el-input v-model="queryParams.needcount" placeholder="请输入所需数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="生产数量" prop="markcount">
              <el-input v-model="queryParams.markcount" placeholder="请输入生产数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="报废数量" prop="scrapcount">
              <el-input v-model="queryParams.scrapcount" placeholder="请输入报废数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="流程号" prop="currprocess">
              <el-input v-model="queryParams.currprocess" placeholder="请输入当前流程" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否加急" prop="isTop">
              <el-input v-model="queryParams.isTop" placeholder="请输入是否加急" clearable @keyup.enter="handleQuery" />
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
          <!-- <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['task:task:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['task:task:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['task:task:remove']">删除</el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['task:task:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" 
          border
          style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center' }"
          row-key="id"
          :row-style="{ height: '50px' }"
          lazy
      :data="taskList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" v-if="false" />
        <el-table-column label="流转批次号" align="center" prop="sn" >
          <template #default="scope">
            <span class="text-blue-600">{{ scope.row.sn }}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品" align="center" prop="proId" >
          <template #default="scope" >
            <span class="text-green-500"> {{ scope.row.productVo.title}}</span>
           </template>
        </el-table-column>
        <el-table-column label="工作站" align="center" prop="workplaceId" >
          <template #default="scope">{{ scope.row.workplacesVo.title}}</template>
        </el-table-column>
        <el-table-column label="用户" align="center" prop="username" >
          <template #default="scope"><span class=" text-pink-500">{{ scope.row.username}}</span></template>
        </el-table-column>
        <el-table-column label="所需数量" align="center" prop="needcount" />
        <el-table-column label="生产数量" align="center" prop="markcount" />
        <el-table-column label="报废数量" align="center" prop="scrapcount" />
        <el-table-column label="是否质检" align="center" prop="isCheck" />
        <el-table-column label="当前流程" align="center" prop="currprocess" />
        <el-table-column label="进度" align="center"  >
          <template #default="scope">
            <el-progress :text-inside="true" status="success" :stroke-width="26" :percentage="(scope.row.okprocess/scope.row.allprocess)*100" />
            </template>
        </el-table-column>
        <el-table-column label="返工数量" align="center" prop="redoingcount" />
        <el-table-column label="返工id" align="center" prop="redoingid" />
        <el-table-column label="是否加急" align="center" prop="isTop" />
        <el-table-column label="主表id" align="center" prop="workId" v-if="false"/>
        <!-- <el-table-column label="类型" align="center" prop="type" /> -->
        <!-- <el-table-column label="排序" align="center" prop="sort" /> -->
        <el-table-column label="状态" align="center" prop="status" >
          <template #default="scope">
            <dict-tag :options="makestatus" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" >
          <template #default="scope">
            <dict-tag :options="user_task_type" :value="scope.row.remark" />
          </template>
        </el-table-column>
        <!-- <el-table-column label="是否关键工序" align="center" prop="mustbe" /> -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['task:task:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改任务流转对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="taskFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流转批次号" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入流转批次号" />
        </el-form-item>
        <el-form-item label="产品" prop="proId">
          <el-input v-model="form.proId" placeholder="请输入产品" />
        </el-form-item>
        <el-form-item label="工作站" prop="workplaceId">
          <el-input v-model="form.workplaceId" placeholder="请输入工作站" />
        </el-form-item>
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户" />
        </el-form-item>
        <el-form-item label="所需数量" prop="needcount">
          <el-input v-model="form.needcount" placeholder="请输入所需数量" />
        </el-form-item>
        <el-form-item label="生产数量" prop="markcount">
          <el-input v-model="form.markcount" placeholder="请输入生产数量" />
        </el-form-item>
        <el-form-item label="报废数量" prop="scrapcount">
          <el-input v-model="form.scrapcount" placeholder="请输入报废数量" />
        </el-form-item>
        <el-form-item label="是否质检" prop="isCheck">
          <el-input v-model="form.isCheck" placeholder="请输入是否质检" />
        </el-form-item>
        <el-form-item label="当前流程" prop="currprocess">
          <el-input v-model="form.currprocess" placeholder="请输入当前流程" />
        </el-form-item>
        <el-form-item label="返工数量" prop="redoingcount">
          <el-input v-model="form.redoingcount" placeholder="请输入返工数量" />
        </el-form-item>
        <el-form-item label="返工id" prop="redoingid">
          <el-input v-model="form.redoingid" placeholder="请输入返工id" />
        </el-form-item>
        <el-form-item label="是否加急" prop="isTop">
          <el-input v-model="form.isTop" placeholder="请输入是否加急" />
        </el-form-item>
        <el-form-item label="主表id" prop="workId">
          <el-input v-model="form.workId" placeholder="请输入主表id" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="是否关键工序" prop="mustbe">
          <el-input v-model="form.mustbe" placeholder="请输入是否关键工序" />
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

<script setup name="Task" lang="ts">
import { listTask, getTask, delTask, addTask, updateTask } from '@/api/task/task';
import { TaskVO, TaskQuery, TaskForm } from '@/api/task/task/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { makestatus, user_task_type,isturnon } = toRefs<any>(proxy?.useDict('makestatus', 'user_task_type','isturnon'));

const taskList = ref<TaskVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const taskFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: TaskForm = {
  id: undefined,
  sn: undefined,
  proId: undefined,
  workplaceId: undefined,
  userId: undefined,
  needcount: undefined,
  markcount: undefined,
  scrapcount: undefined,
  isCheck: undefined,
  currprocess: undefined,
  redoingcount: undefined,
  redoingid: undefined,
  isTop: undefined,
  workId: undefined,
  type: undefined,
  sort: undefined,
  status: undefined,
  remark: undefined,
  mustbe: undefined
}
const data = reactive<PageData<TaskForm, TaskQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    proId: undefined,
    workplaceId: undefined,
    userId: undefined,
    needcount: undefined,
    markcount: undefined,
    scrapcount: undefined,
    isCheck: undefined,
    currprocess: undefined,
    redoingcount: undefined,
    redoingid: undefined,
    isTop: undefined,
    workId: undefined,
    type: undefined,
    sort: undefined,
    status: undefined,
    mustbe: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询任务流转列表 */
const getList = async () => {
  loading.value = true;
  const res = await listTask(queryParams.value);
  taskList.value = res.rows;
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
  taskFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: TaskVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加任务流转";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: TaskVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getTask(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改任务流转";
}

/** 提交按钮 */
const submitForm = () => {
  taskFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateTask(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addTask(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: TaskVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除任务流转编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delTask(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('task/task/export', {
    ...queryParams.value
  }, `task_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
