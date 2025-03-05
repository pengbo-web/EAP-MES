<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="用户" prop="userId">
              <el-input v-model="queryParams.userId" placeholder="请输入用户" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="任务id" prop="taskId">
              <el-input v-model="queryParams.taskId" placeholder="请输入任务id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="已完成数量" prop="lastcount">
              <el-input v-model="queryParams.lastcount" placeholder="请输入已完成数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="报工数量" prop="count">
              <el-input v-model="queryParams.count" placeholder="请输入报工数量" clearable @keyup.enter="handleQuery" />
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
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['workRec:workRec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="workRecList" @selection-change="handleSelectionChange" border lazy>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="报工人" align="center" prop="username" />
        <el-table-column label="报工物料" align="center" prop="productVo.title" >
          <template #default="scope">
            <!-- {{ scope.row.productVo }} -->
            <el-text v-if="scope.row.productVo!=null">{{ scope.row.productVo.title }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="工序" align="center" prop="processVo.title" >
          <template #default="scope">
            <el-text v-if="scope.row.processVo!=null">{{ scope.row.processVo.title }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="酬劳" align="center" prop="processVo.title" >
          <template #default="scope">
            <el-text v-if="scope.row.processVo!=null">{{ scope.row.count*scope.row.processVo.moneyprice }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="关联任务" align="center" prop="taskId" />
        <el-table-column label="总工作数量" align="center" prop="lastcount" />
        <el-table-column label="本次报工数量" align="center" prop="count" />
        <el-table-column label="不良品数量" align="center" prop="defective" />
        <el-table-column label="状态" align="center" prop="status" >
          <template #default="scope">
            <el-tag v-if="scope.row.status === '2'" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.status === '1'" type="warning">进行中</el-tag>
            <el-tag v-else-if="scope.row.status === '3'" type="danger">异常停工</el-tag>
            <el-tag v-else type="info">未开始</el-tag>
          </template>
        </el-table-column> 
        <el-table-column label="创建时间" align="center" prop="createTime" />
        <!-- <el-table-column label="是否关键工序" align="center" prop="mustbe" /> -->
        <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['workRec:workRec:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['workRec:workRec:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column> -->
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改工作记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="workRecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户" />
        </el-form-item>
        <el-form-item label="任务id" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入任务id" />
        </el-form-item>
        <el-form-item label="已完成数量" prop="lastcount">
          <el-input v-model="form.lastcount" placeholder="请输入已完成数量" />
        </el-form-item>
        <el-form-item label="报工数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入报工数量" />
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

<script setup name="WorkRec" lang="ts">
import { listWorkRec, getWorkRec, delWorkRec, addWorkRec, updateWorkRec } from '@/api/workRec/workRec';
import { WorkRecVO, WorkRecQuery, WorkRecForm } from '@/api/workRec/workRec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const workRecList = ref<WorkRecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const workRecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: WorkRecForm = {
  id: undefined,
  userId: undefined,
  taskId: undefined,
  lastcount: undefined,
  count: undefined,
  sort: undefined,
  status: undefined,
  remark: undefined,
  mustbe: undefined
}
const data = reactive<PageData<WorkRecForm, WorkRecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: undefined,
    taskId: undefined,
    lastcount: undefined,
    count: undefined,
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

/** 查询工作记录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listWorkRec(queryParams.value);
  workRecList.value = res.rows;
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
  workRecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: WorkRecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加工作记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: WorkRecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getWorkRec(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改工作记录";
}

/** 提交按钮 */
const submitForm = () => {
  workRecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateWorkRec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addWorkRec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: WorkRecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除工作记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delWorkRec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('workRec/workRec/export', {
    ...queryParams.value
  }, `workRec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
