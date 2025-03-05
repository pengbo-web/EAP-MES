<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="名称" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入名称" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:mobileMenu:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:mobileMenu:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:mobileMenu:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:mobileMenu:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="mobileMenuList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="true" />
        <el-table-column label="图标" align="center" prop="icoimg" >
          <template #default="scope">
            <img :src="scope.row.icoimg" alt="" style="width: 50px;height: 50px;">
          </template>
        </el-table-column>
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="类别" align="center" prop="type" />
        <el-table-column label="连接" align="center" prop="urls" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:mobileMenu:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:mobileMenu:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改移动端目录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="mobileMenuFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="图标" prop="icoimg">
          <el-input v-model="form.icoimg" placeholder="请输入图标" />
        </el-form-item>
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="连接" prop="urls">
          <el-input v-model="form.urls" placeholder="请输入连接" />
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

<script setup name="MobileMenu" lang="ts">
import { listMobileMenu, getMobileMenu, delMobileMenu, addMobileMenu, updateMobileMenu } from '@/api/mes/mobileMenu';
import { MobileMenuVO, MobileMenuQuery, MobileMenuForm } from '@/api/mes/mobileMenu/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const mobileMenuList = ref<MobileMenuVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const mobileMenuFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: MobileMenuForm = {
  id: undefined,
  icoimg: undefined,
  title: undefined,
  type: undefined,
  urls: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<MobileMenuForm, MobileMenuQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    type: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询移动端目录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listMobileMenu(queryParams.value);
  mobileMenuList.value = res.rows;
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
  mobileMenuFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: MobileMenuVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加移动端目录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: MobileMenuVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getMobileMenu(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改移动端目录";
}

/** 提交按钮 */
const submitForm = () => {
  mobileMenuFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateMobileMenu(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addMobileMenu(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: MobileMenuVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除移动端目录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delMobileMenu(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/mobileMenu/export', {
    ...queryParams.value
  }, `mobileMenu_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
