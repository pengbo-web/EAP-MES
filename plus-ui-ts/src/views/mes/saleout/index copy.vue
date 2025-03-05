<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="订单" prop="orderid">
              <el-input v-model="queryParams.orderid" placeholder="请输入订单" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="物料" prop="stockid">
              <el-input v-model="queryParams.stockid" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:saleout:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:saleout:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:saleout:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:saleout:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="saleoutList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编号" align="center" prop="id" v-if="true" />
        <el-table-column label="订单" align="center" prop="orderid" />
        <el-table-column label="物料" align="center" prop="stockid" />
        <el-table-column label="数量" align="center" prop="counts" />
        <el-table-column label="文件" align="center" prop="files" />
        <el-table-column label="物流信息" align="center" prop="wuliu" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:saleout:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:saleout:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改销售出库对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="saleoutFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单" prop="orderid">
          <el-input v-model="form.orderid" placeholder="请输入订单" />
        </el-form-item>
        <el-form-item label="物料" prop="stockid">
          <el-input v-model="form.stockid" placeholder="请输入物料" />
        </el-form-item>
        <el-form-item label="数量" prop="counts">
          <el-input v-model="form.counts" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="文件" prop="files">
            <el-input v-model="form.files" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="物流信息" prop="wuliu">
          <el-input v-model="form.wuliu" placeholder="请输入物流信息" />
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

<script setup name="Saleout" lang="ts">
import { listSaleout, getSaleout, delSaleout, addSaleout, updateSaleout } from '@/api/mes/saleout';
import { SaleoutVO, SaleoutQuery, SaleoutForm } from '@/api/mes/saleout/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const saleoutList = ref<SaleoutVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const saleoutFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: SaleoutForm = {
  id: undefined,
  orderid: undefined,
  stockid: undefined,
  counts: undefined,
  files: undefined,
  wuliu: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<SaleoutForm, SaleoutQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderid: undefined,
    stockid: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "编号不能为空", trigger: "blur" }
    ],
    orderid: [
      { required: true, message: "订单不能为空", trigger: "blur" }
    ],
    stockid: [
      { required: true, message: "物料不能为空", trigger: "blur" }
    ],
    remark: [
      { required: true, message: "备注不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询销售出库列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSaleout(queryParams.value);
  saleoutList.value = res.rows;
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
  saleoutFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: SaleoutVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加销售出库";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: SaleoutVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getSaleout(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改销售出库";
}

/** 提交按钮 */
const submitForm = () => {
  saleoutFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateSaleout(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addSaleout(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: SaleoutVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除销售出库编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delSaleout(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/saleout/export', {
    ...queryParams.value
  }, `saleout_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
