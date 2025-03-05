<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:outorderDe:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:outorderDe:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:outorderDe:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:outorderDe:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="outorderDeList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编号" align="center" prop="id" v-if="true" />
        <el-table-column label="物料" align="center" prop="stockid" />
        <el-table-column label="单位" align="center" prop="unid" />
        <el-table-column label="单价" align="center" prop="prices" />
        <el-table-column label="数量" align="center" prop="counts" />
        <el-table-column label="总价" align="center" prop="allprice" />
        <el-table-column label="交付时间" align="center" prop="ctime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.ctime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="已计划数量" align="center" prop="pcount" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:outorderDe:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:outorderDe:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改委外详情对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="outorderDeFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料" prop="stockid">
          <el-input v-model="form.stockid" placeholder="请输入物料" />
        </el-form-item>
        <el-form-item label="单位" prop="unid">
          <el-input v-model="form.unid" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="单价" prop="prices">
          <el-input v-model="form.prices" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="数量" prop="counts">
          <el-input v-model="form.counts" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="总价" prop="allprice">
          <el-input v-model="form.allprice" placeholder="请输入总价" />
        </el-form-item>
        <el-form-item label="交付时间" prop="ctime">
          <el-date-picker clearable
            v-model="form.ctime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择交付时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="已计划数量" prop="pcount">
          <el-input v-model="form.pcount" placeholder="请输入已计划数量" />
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

<script setup name="OutorderDe" lang="ts">
import { listOutorderDe, getOutorderDe, delOutorderDe, addOutorderDe, updateOutorderDe } from '@/api/mes/outorderDe';
import { OutorderDeVO, OutorderDeQuery, OutorderDeForm } from '@/api/mes/outorderDe/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const outorderDeList = ref<OutorderDeVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const outorderDeFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: OutorderDeForm = {
  id: undefined,
  stockid: undefined,
  unid: undefined,
  prices: undefined,
  counts: undefined,
  allprice: undefined,
  ctime: undefined,
  status: undefined,
  remark: undefined,
  pcount: undefined
}
const data = reactive<PageData<OutorderDeForm, OutorderDeQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询委外详情列表 */
const getList = async () => {
  loading.value = true;
  const res = await listOutorderDe(queryParams.value);
  outorderDeList.value = res.rows;
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
  outorderDeFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: OutorderDeVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加委外详情";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: OutorderDeVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOutorderDe(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改委外详情";
}

/** 提交按钮 */
const submitForm = () => {
  outorderDeFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateOutorderDe(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addOutorderDe(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: OutorderDeVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除委外详情编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delOutorderDe(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/outorderDe/export', {
    ...queryParams.value
  }, `outorderDe_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
