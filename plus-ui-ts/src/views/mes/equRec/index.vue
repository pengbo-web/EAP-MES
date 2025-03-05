<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="主表关联" prop="equmian">
              <el-input v-model="queryParams.equmian" placeholder="请输入主表关联" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="列表关联" prop="listid">
              <el-input v-model="queryParams.listid" placeholder="请输入列表关联" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="异常内容" prop="conts">
              <el-input v-model="queryParams.conts" placeholder="请输入异常内容" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="结果" prop="rest">
              <el-input v-model="queryParams.rest" placeholder="请输入结果" clearable @keyup.enter="handleQuery" />
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
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:equRec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="equRecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="设备名称" align="center" prop="equname" />
        <el-table-column label="点检类别" align="center" prop="mainname" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="异常内容" align="center" prop="conts" />
        <el-table-column label="结果" align="center" prop="rest" />
        <el-table-column label="异常图片" align="center" prop="remark" >
          <template #default="scope">
            <image-preview :src="scope.row.remark" :width="50" :height="50" v-if="scope.row.rest=='异常'" />
            <span v-else>无异常</span>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" align="center" prop="createTime" >
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" >
        <template #default="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">待复核</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="danger">完成</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="warning">疑问</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope" >
              <el-button v-if="scope.row.status === '0'" link type="primary" icon="Edit" label="" @click="handleUpdate(scope.row)" v-hasPermi="['mes:toolRec:edit']">通过</el-button>
             <span v-else>无法操作</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改设备点检记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="equRecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主表关联" prop="equmian">
          <el-input v-model="form.equmian" placeholder="请输入主表关联" />
        </el-form-item>
        <el-form-item label="列表关联" prop="listid">
          <el-input v-model="form.listid" placeholder="请输入列表关联" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="异常内容" prop="conts">
          <el-input v-model="form.conts" placeholder="请输入异常内容" />
        </el-form-item>
        <el-form-item label="结果" prop="rest">
          <el-input v-model="form.rest" placeholder="请输入结果" />
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

<script setup name="EquRec" lang="ts">
import { listEquRec, getEquRec, delEquRec, addEquRec, updateEquRec } from '@/api/mes/equRec';
import { EquRecVO, EquRecQuery, EquRecForm } from '@/api/mes/equRec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const equRecList = ref<EquRecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const equRecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: EquRecForm = {
  id: undefined,
  equmian: undefined,
  listid: undefined,
  title: undefined,
  conts: undefined,
  rest: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<EquRecForm, EquRecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    equmian: undefined,
    listid: undefined,
    title: undefined,
    conts: undefined,
    rest: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询设备点检记录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listEquRec(queryParams.value);
  equRecList.value = res.rows;
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
  equRecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: EquRecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加设备点检记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: EquRecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getEquRec(_id);
  Object.assign(form.value, res.data);
  form.value.status = '1';
  await updateEquRec(form.value).finally(() =>  buttonLoading.value = false);
  await getList();
}

/** 提交按钮 */
const submitForm = () => {
  equRecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateEquRec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addEquRec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: EquRecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除设备点检记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delEquRec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/equRec/export', {
    ...queryParams.value
  }, `equRec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
