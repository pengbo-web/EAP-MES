<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="关联主表" prop="equMain">
              <el-input v-model="queryParams.equMain" placeholder="请输入关联主表" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="名称" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标准" prop="standards">
              <el-input v-model="queryParams.standards" placeholder="请输入标准" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable >
                <el-option v-for="dict in equstatus" :key="dict.value" :label="dict.label" :value="dict.value"/>
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
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:equcheckList:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:equcheckList:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:equcheckList:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:equcheckList:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="equcheckListList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="true" />
        <el-table-column label="关联主表" align="center" prop="equMain" />
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="标准" align="center" prop="standards" />
        <el-table-column label="图片" align="center" prop="imgs" width="100">
          <template #default="scope">
            <image-preview :src="scope.row.imgs" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="equstatus" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:equcheckList:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:equcheckList:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改设备点检列对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="equcheckListFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联主表" prop="equMain">
          <el-input v-model="form.equMain" placeholder="请输入关联主表" />
        </el-form-item>
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="标准" prop="standards">
          <el-input v-model="form.standards" placeholder="请输入标准" />
        </el-form-item>
        <el-form-item label="图片" prop="imgs">
          <image-upload v-model="form.imgs"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in equstatus"
              :key="dict.value"
              :value="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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

<script setup name="EqucheckList" lang="ts">
import { listEqucheckList, getEqucheckList, delEqucheckList, addEqucheckList, updateEqucheckList } from '@/api/mes/equcheckList';
import { EqucheckListVO, EqucheckListQuery, EqucheckListForm } from '@/api/mes/equcheckList/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { equstatus } = toRefs<any>(proxy?.useDict('equstatus'));

const equcheckListList = ref<EqucheckListVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const equcheckListFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: EqucheckListForm = {
  id: undefined,
  equMain: undefined,
  title: undefined,
  standards: undefined,
  imgs: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<EqucheckListForm, EqucheckListQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    equMain: undefined,
    title: undefined,
    standards: undefined,
    imgs: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询设备点检列列表 */
const getList = async () => {
  loading.value = true;
  const res = await listEqucheckList(queryParams.value);
  equcheckListList.value = res.rows;
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
  equcheckListFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: EqucheckListVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加设备点检列";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: EqucheckListVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getEqucheckList(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改设备点检列";
}

/** 提交按钮 */
const submitForm = () => {
  equcheckListFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateEqucheckList(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addEqucheckList(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: EqucheckListVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除设备点检列编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delEqucheckList(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/equcheckList/export', {
    ...queryParams.value
  }, `equcheckList_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
