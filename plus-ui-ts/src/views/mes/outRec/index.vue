<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="外协" prop="outId">
              <el-input v-model="queryParams.outId" placeholder="请输入外协" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="产品" prop="proId">
              <el-input v-model="queryParams.proId" placeholder="请输入产品" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="数量" prop="count">
              <el-input v-model="queryParams.count" placeholder="请输入数量" clearable @keyup.enter="handleQuery" />
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
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:outRec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="outRecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="外协" align="center" prop="outsourcingVo.title" />
        <el-table-column label="产品" align="center" prop="productVo.title" />
        <el-table-column label="数量" align="center" prop="count" />
        <el-table-column label="状态" align="center" prop="status" >
         <template #default="scope">
          <el-tag v-if="scope.row.status === '2'" type="success">完成</el-tag>
          <el-tag v-if="scope.row.status === '1'" type="danger">等待中</el-tag>
        </template>
        </el-table-column>
        <el-table-column label="开始时间" align="center" prop="createTime" />
        <el-table-column label="结束时间" align="center" prop="remark" />
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改外协记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="outRecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="外协" prop="outId">
          <el-input v-model="form.outId" placeholder="请输入外协" />
        </el-form-item>
        <el-form-item label="产品" prop="proId">
          <el-input v-model="form.proId" placeholder="请输入产品" />
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

<script setup name="OutRec" lang="ts">
import { listOutRec, getOutRec, delOutRec, addOutRec, updateOutRec } from '@/api/mes/outRec';
import { OutRecVO, OutRecQuery, OutRecForm } from '@/api/mes/outRec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const outRecList = ref<OutRecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const outRecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: OutRecForm = {
  id: undefined,
  outId: undefined,
  proId: undefined,
  count: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<OutRecForm, OutRecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    outId: undefined,
    proId: undefined,
    count: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询外协记录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listOutRec(queryParams.value);
  outRecList.value = res.rows;
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
  outRecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: OutRecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加外协记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: OutRecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOutRec(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改外协记录";
}

/** 提交按钮 */
const submitForm = () => {
  outRecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateOutRec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addOutRec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: OutRecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除外协记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delOutRec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/outRec/export', {
    ...queryParams.value
  }, `outRec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
