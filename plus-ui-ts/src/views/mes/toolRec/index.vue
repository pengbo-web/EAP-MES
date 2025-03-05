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
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:toolRec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="toolRecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="主表" align="center" prop="toolname" />
        <el-table-column label="数量" align="center" prop="count" />
        <el-table-column label="图片" align="center" prop="imgs" >
          <template #default="scope">
            <image-preview :src="scope.row.imgs" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column label="原因" align="center" prop="rebak" />
        <el-table-column label="提交时间" align="center" prop="createTime" />
        <el-table-column label="确认时间" align="center" prop="remark" />
        <el-table-column label="状态" align="center" prop="type" >
          <template #default="scope">
            <el-tag v-if="scope.row.type === '0'" type="success">领用</el-tag>
            <el-tag v-if="scope.row.type === '1'" type="success">报废</el-tag>
            <el-tag v-else-if="scope.row.type === '2'" type="danger">退还</el-tag>
            <el-tag v-else-if="scope.row.type === '3'" type="warning">更换</el-tag>
         </template>
        </el-table-column>

        <el-table-column label="用户" align="center" prop="username" />
        <el-table-column label="状态" align="center" prop="status" >
          <template #default="scope">
            <el-tag v-if="scope.row.status === '1'" type="success">通过</el-tag>
            <el-tag v-else-if="scope.row.status === '2'" type="danger">不通过</el-tag>
            <el-tag v-else-if="scope.row.status === '0'" type="warning">待审核</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope" >
              <el-button v-if="scope.row.status === '0'" link type="primary" icon="Edit" label="" @click="handleUpdate(scope.row)" v-hasPermi="['mes:toolRec:edit']">同意操作</el-button>
             <span v-else>无法操作</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改夹具记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="toolRecFormRef" :model="form" :rules="rules" label-width="80px">
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

<script setup name="ToolRec" lang="ts">
import { listToolRec, getToolRec, delToolRec, addToolRec, updateToolRec } from '@/api/mes/toolRec';
import { ToolRecVO, ToolRecQuery, ToolRecForm } from '@/api/mes/toolRec/types';
import { fa } from 'element-plus/es/locale/index.mjs';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const toolRecList = ref<ToolRecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const toolRecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: ToolRecForm = {
  id: undefined,
  toolid: undefined,
  count: undefined,
  imgs: undefined,
  rebak: undefined,
  type: undefined,
  userId: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<ToolRecForm, ToolRecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    toolid: undefined,
    count: undefined,
    imgs: undefined,
    rebak: undefined,
    type: undefined,
    userId: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询夹具记录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listToolRec(queryParams.value);
  toolRecList.value = res.rows;
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
  toolRecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: ToolRecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加夹具记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: ToolRecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getToolRec(_id);
  Object.assign(form.value, res.data);
  form.value.status = '1';
  await updateToolRec(form.value).finally(() =>  buttonLoading.value = false);
  getList();
}

/** 提交按钮 */
const submitForm = () => {
  toolRecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateToolRec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addToolRec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: ToolRecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除夹具记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delToolRec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/toolRec/export', {
    ...queryParams.value
  }, `toolRec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
