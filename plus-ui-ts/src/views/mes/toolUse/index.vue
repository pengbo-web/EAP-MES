<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="主表" prop="toolid">
              <el-input v-model="queryParams.toolid" placeholder="请输入主表" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="数量" prop="count">
              <el-input v-model="queryParams.count" placeholder="请输入数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="图片" prop="imgs">
              <el-input v-model="queryParams.imgs" placeholder="请输入图片" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="原因" prop="rebak">
              <el-input v-model="queryParams.rebak" placeholder="请输入原因" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="用户" prop="userId">
              <el-input v-model="queryParams.userId" placeholder="请输入用户" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="工作中" prop="workId">
              <el-input v-model="queryParams.workId" placeholder="请输入工作中" clearable @keyup.enter="handleQuery" />
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

      <el-table v-loading="loading" :data="toolUseList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="名称" align="center" prop="mesEtoolVo.title" />
        <el-table-column label="数量" align="center" prop="count" />
        <el-table-column label="图片" align="center" prop="imgs" />
        <el-table-column label="原因" align="center" prop="rebak" />
        <el-table-column label="用户" align="center" prop="userId" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="工作中" align="center" prop="workId" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:toolUse:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:toolUse:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改夹具使用对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="toolUseFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主表" prop="toolid">
          <el-input v-model="form.toolid" placeholder="请输入主表" />
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="图片" prop="imgs">
          <el-input v-model="form.imgs" placeholder="请输入图片" />
        </el-form-item>
        <el-form-item label="原因" prop="rebak">
          <el-input v-model="form.rebak" placeholder="请输入原因" />
        </el-form-item>
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="工作中" prop="workId">
            <el-input v-model="form.workId" type="textarea" placeholder="请输入内容" />
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

<script setup name="ToolUse" lang="ts">
import { listToolUse, getToolUse, delToolUse, addToolUse, updateToolUse } from '@/api/mes/toolUse';
import { ToolUseVO, ToolUseQuery, ToolUseForm } from '@/api/mes/toolUse/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const toolUseList = ref<ToolUseVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const toolUseFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: ToolUseForm = {
  id: undefined,
  toolid: undefined,
  count: undefined,
  imgs: undefined,
  rebak: undefined,
  userId: undefined,
  remark: undefined,
  workId: undefined
}
const data = reactive<PageData<ToolUseForm, ToolUseQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    toolid: undefined,
    count: undefined,
    imgs: undefined,
    rebak: undefined,
    userId: undefined,
    workId: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询夹具使用列表 */
const getList = async () => {
  loading.value = true;
  const res = await listToolUse(queryParams.value);
  toolUseList.value = res.rows;
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
  toolUseFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: ToolUseVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加夹具使用";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: ToolUseVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getToolUse(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改夹具使用";
}

/** 提交按钮 */
const submitForm = () => {
  toolUseFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateToolUse(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addToolUse(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: ToolUseVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除夹具使用编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delToolUse(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/toolUse/export', {
    ...queryParams.value
  }, `toolUse_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
