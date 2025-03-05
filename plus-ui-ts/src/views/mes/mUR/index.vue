<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="套餐名" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入套餐名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="套餐内容" prop="ids">
              <el-input v-model="queryParams.ids" placeholder="请输入套餐内容" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:mUR:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['mes:mUR:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:mUR:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['mes:mUR:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="mURList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="套餐名" align="center" prop="title" />
        <el-table-column label="套餐内容" align="center" prop="idsname" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['mes:mUR:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:mUR:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改移动套餐对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="mURFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="套餐名" prop="title">
          <el-input v-model="form.title" placeholder="请输入套餐名" />
        </el-form-item>
        <el-form-item label="套餐内容" prop="idsarray">
          <el-select v-model="form.idsarray" multiple placeholder="模块选择" style="width: 240px">
            <el-option v-for="item in mobileMenuList" :key="item.id" :label="item.title"
              :value="item.id" />
          </el-select>
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

<script setup name="MUR" lang="ts">
import { listMUR, getMUR, delMUR, addMUR, updateMUR } from '@/api/mes/mUR';
import { MURVO, MURQuery, MURForm } from '@/api/mes/mUR/types';
import { listMobileMenu, getMobileMenu, delMobileMenu, addMobileMenu, updateMobileMenu } from '@/api/mes/mobileMenu';
import { MobileMenuVO, MobileMenuQuery, MobileMenuForm } from '@/api/mes/mobileMenu/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const mobileMenuList = ref<MobileMenuVO[]>([]);


const mURList = ref<MURVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const mURFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: MURForm = {
  id: undefined,
  title: undefined,
  ids: undefined,
  status: undefined,
  remark: undefined,
  idsarray: undefined,
}
const data = reactive<PageData<MURForm, MURQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 20,
    title: undefined,
    ids: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询移动套餐列表 */
const getList = async () => {
  loading.value = true;
  const res = await listMUR(queryParams.value);
  mURList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}
const getListmenu = async () => {
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
  form.value = { ...initFormData };
  mURFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: MURVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加移动套餐";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: MURVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getMUR(_id);
  Object.assign(form.value, res.data);
  console.log(form.value)
  dialog.visible = true;
  dialog.title = "修改移动套餐";
}

/** 提交按钮 */
const submitForm = () => {
  form.value.ids = form.value.idsarray.join(',')
  console.log(form.value)
  mURFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateMUR(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addMUR(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: MURVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除移动套餐编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delMUR(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/mUR/export', {
    ...queryParams.value
  }, `mUR_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getListmenu();
});
</script>
