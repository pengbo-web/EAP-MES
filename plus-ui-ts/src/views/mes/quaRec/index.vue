<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="质检主键" prop="quaMian">
              <el-input v-model="queryParams.quaMian" placeholder="请输入质检主键" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="流转号" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入流转号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="任务列表" prop="taskId">
              <el-input v-model="queryParams.taskId" placeholder="请输入任务列表" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="值" prop="values">
              <el-input v-model="queryParams.values" placeholder="请输入值" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="结果" prop="res">
              <el-input v-model="queryParams.res" placeholder="请输入结果" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否关键工序" prop="mustbe">
              <el-input v-model="queryParams.mustbe" placeholder="请输入是否关键工序" clearable @keyup.enter="handleQuery" />
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
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:quaRec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="quaRecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="质检人" align="center" prop="createByName" />
        <el-table-column label="被质检人" align="center" prop="taskinfo.username" />
        <el-table-column label="质检类型" align="center" prop="quaname" />
        <el-table-column label="流转号" align="center" prop="sn" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="订单名称" align="center" prop="taskinfo.workVo.title" />
        <el-table-column label="值" align="center" prop="values" />
        <el-table-column label="结果" align="center" prop="res" />
        <el-table-column label="符合结果" align="center" prop="status" >
          <template #default="scope">
            <el-tag v-if="scope.row.status === '1'" type="success">符合</el-tag>
            <el-tag v-if="scope.row.status === '0'" type="danger">等待审核</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="质检时间" align="center" prop="createTime" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-if="scope.row.status === '0'" link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:quaRec:edit']">确认结果</el-button>
              <span v-else>无法操作</span>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改质检列表对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="quaRecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="质检主键" prop="quaMian">
          <el-input v-model="form.quaMian" placeholder="请输入质检主键" />
        </el-form-item>
        <el-form-item label="流转号" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入流转号" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="任务列表" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入任务列表" />
        </el-form-item>
        <el-form-item label="值" prop="values">
          <el-input v-model="form.values" placeholder="请输入值" />
        </el-form-item>
        <el-form-item label="结果" prop="res">
          <el-input v-model="form.res" placeholder="请输入结果" />
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

<script setup name="QuaRec" lang="ts">
import { listQuaRec, getQuaRec, delQuaRec, addQuaRec, updateQuaRec } from '@/api/mes/quaRec';
import { QuaRecVO, QuaRecQuery, QuaRecForm } from '@/api/mes/quaRec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const quaRecList = ref<QuaRecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const quaRecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: QuaRecForm = {
  id: undefined,
  quaMian: undefined,
  sn: undefined,
  title: undefined,
  taskId: undefined,
  values: undefined,
  res: undefined,
  status: undefined,
  remark: undefined,
  mustbe: undefined
}
const data = reactive<PageData<QuaRecForm, QuaRecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    quaMian: undefined,
    sn: undefined,
    title: undefined,
    taskId: undefined,
    values: undefined,
    res: undefined,
    status: undefined,
    mustbe: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询质检列表列表 */
const getList = async () => {
  loading.value = true;
  const res = await listQuaRec(queryParams.value);
  quaRecList.value = res.rows;
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
  quaRecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: QuaRecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加质检列表";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: QuaRecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getQuaRec(_id);
  Object.assign(form.value, res.data);
  form.value.status = '1';
  await updateQuaRec(form.value).finally(() =>  buttonLoading.value = false);
  await getList();
}

/** 提交按钮 */
const submitForm = () => {
  quaRecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateQuaRec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addQuaRec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: QuaRecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除质检列表编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delQuaRec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/quaRec/export', {
    ...queryParams.value
  }, `quaRec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
