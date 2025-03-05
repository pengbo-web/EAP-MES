<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="物料" prop="stockid">
              <el-input v-model="queryParams.stockid" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="库位" prop="stocktypeid">
              <el-input v-model="queryParams.stocktypeid" placeholder="请输入库位" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="盘查数量" prop="counts">
              <el-input v-model="queryParams.counts" placeholder="请输入盘查数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="异常数量" prop="yccounts">
              <el-input v-model="queryParams.yccounts" placeholder="请输入异常数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="异常原因" prop="ycrec">
              <el-input v-model="queryParams.ycrec" placeholder="请输入异常原因" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="结存量" prop="jc">
              <el-input v-model="queryParams.jc" placeholder="请输入结存量" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:stocktaking:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:stocktaking:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:stocktaking:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:stocktaking:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="stocktakingList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编号" align="center" prop="id" v-if="false" />
        <el-table-column label="物料" align="center" prop="stockVo.tilte" />
        <el-table-column label="库位" align="center" prop="stypeinfo.sn" />
        <el-table-column label="账面数量" align="center" prop="stockVo.nowstock" />
        <el-table-column label="盘查数量" align="center" prop="counts" />
        <el-table-column label="异常数量" align="center" prop="yccounts" />
        <el-table-column label="异常说明" align="center" prop="ycrec" />
        <el-table-column label="结存量" align="center" prop="jc" />
        <el-table-column label="状态" align="center" prop="status" >
          <template #default="scope">
            <el-tag v-if="scope.row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="danger">异常</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
              <el-button plain type="primary" v-if="scope.row.status === '0'" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:stocktaking:edit']">执行更新</el-button>
              <el-text v-else>无法操作</el-text>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改库位盘点对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="stocktakingFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料" prop="stockid">
          <el-input v-model="form.stockid" placeholder="请输入物料" />
        </el-form-item>
        <el-form-item label="库位" prop="stocktypeid">
          <el-input v-model="form.stocktypeid" placeholder="请输入库位" />
        </el-form-item>
        <el-form-item label="盘查数量" prop="counts">
          <el-input v-model="form.counts" placeholder="请输入盘查数量" />
        </el-form-item>
        <el-form-item label="异常数量" prop="yccounts">
          <el-input v-model="form.yccounts" placeholder="请输入异常数量" />
        </el-form-item>
        <el-form-item label="异常原因" prop="ycrec">
          <el-input v-model="form.ycrec" placeholder="请输入异常原因" />
        </el-form-item>
        <el-form-item label="结存量" prop="jc">
          <el-input v-model="form.jc" placeholder="请输入结存量" />
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

<script setup name="Stocktaking" lang="ts">
import { listStocktaking, getStocktaking, delStocktaking, addStocktaking, updateStocktaking } from '@/api/mes/stocktaking';
import { StocktakingVO, StocktakingQuery, StocktakingForm } from '@/api/mes/stocktaking/types';
import { get } from 'http';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const stocktakingList = ref<StocktakingVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const stocktakingFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: StocktakingForm = {
  id: undefined,
  stockid: undefined,
  stocktypeid: undefined,
  counts: undefined,
  yccounts: undefined,
  ycrec: undefined,
  jc: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<StocktakingForm, StocktakingQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    stockid: undefined,
    stocktypeid: undefined,
    counts: undefined,
    yccounts: undefined,
    ycrec: undefined,
    jc: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "编号不能为空", trigger: "blur" }
    ],
    stockid: [
      { required: true, message: "物料不能为空", trigger: "blur" }
    ],
    stocktypeid: [
      { required: true, message: "库位不能为空", trigger: "blur" }
    ],
    counts: [
      { required: true, message: "盘查数量不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询库位盘点列表 */
const getList = async () => {
  loading.value = true;
  const res = await listStocktaking(queryParams.value);
  stocktakingList.value = res.rows;
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
  stocktakingFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: StocktakingVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加库位盘点";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: StocktakingVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getStocktaking(_id);
  Object.assign(form.value, res.data);
  form.value.status="1"
  await updateStocktaking(form.value).finally(() =>  buttonLoading.value = false);
  proxy?.$modal.msgSuccess("操作成功");
  getList();
}

/** 提交按钮 */
const submitForm = () => {
  stocktakingFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateStocktaking(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addStocktaking(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: StocktakingVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除库位盘点编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delStocktaking(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/stocktaking/export', {
    ...queryParams.value
  }, `stocktaking_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
