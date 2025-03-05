<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="商品" prop="proId">
              <el-input v-model="queryParams.proId" placeholder="请输入商品" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="当时数量" prop="count">
              <el-input v-model="queryParams.count" placeholder="请输入当时数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="价格" prop="prices">
              <el-input v-model="queryParams.prices" placeholder="请输入价格" clearable @keyup.enter="handleQuery" />
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
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:priceRec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="priceRecList" @selection-change="handleSelectionChange"
      style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center','font-size':'11px' }"
          :row-style="{ height: '50px' }"
          lazy border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="商品" align="center" prop="proname" />
        <el-table-column label="当时数量" align="center" prop="count" />
        <el-table-column label="价格" align="center" prop="prices" />
        <el-table-column label="时间" align="center" prop="createTime" />
        <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:priceRec:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:priceRec:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column> -->
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改价格波动对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="priceRecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品" prop="proId">
          <el-input v-model="form.proId" placeholder="请输入商品" />
        </el-form-item>
        <el-form-item label="当时数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入当时数量" />
        </el-form-item>
        <el-form-item label="价格" prop="prices">
          <el-input v-model="form.prices" placeholder="请输入价格" />
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

<script setup name="PriceRec" lang="ts">
import { listPriceRec, getPriceRec, delPriceRec, addPriceRec, updatePriceRec } from '@/api/mes/priceRec';
import { PriceRecVO, PriceRecQuery, PriceRecForm } from '@/api/mes/priceRec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const priceRecList = ref<PriceRecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const priceRecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: PriceRecForm = {
  id: undefined,
  proId: undefined,
  count: undefined,
  prices: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<PriceRecForm, PriceRecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    proId: undefined,
    count: undefined,
    prices: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询价格波动列表 */
const getList = async () => {
  loading.value = true;
  const res = await listPriceRec(queryParams.value);
  priceRecList.value = res.rows;
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
  priceRecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: PriceRecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加价格波动";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: PriceRecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getPriceRec(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改价格波动";
}

/** 提交按钮 */
const submitForm = () => {
  priceRecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updatePriceRec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addPriceRec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: PriceRecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除价格波动编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delPriceRec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/priceRec/export', {
    ...queryParams.value
  }, `priceRec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
