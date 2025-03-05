<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="工作id" prop="workid">
              <el-input v-model="queryParams.workid" placeholder="请输入工作id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="订单号" prop="orderid">
              <el-input v-model="queryParams.orderid" placeholder="请输入订单号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="交货日期" prop="subtime">
              <el-input v-model="queryParams.subtime" placeholder="请输入交货日期" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="流转码" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入流转码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="物料码" prop="stockid">
              <el-input v-model="queryParams.stockid" placeholder="请输入物料码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="版次" prop="banci">
              <el-input v-model="queryParams.banci" placeholder="请输入版次" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="数量" prop="counts">
              <el-input v-model="queryParams.counts" placeholder="请输入数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="提交时间" style="width: 308px">
              <el-date-picker
                v-model="dateRangeCreateTime"
                value-format="YYYY-MM-DD HH:mm:ss"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
              />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:primark:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:primark:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:primark:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:primark:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="primarkList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编号" align="center" prop="id" v-if="true" />
        <el-table-column label="工作id" align="center" prop="workid" />
        <el-table-column label="订单号" align="center" prop="orderid" />
        <el-table-column label="交货日期" align="center" prop="subtime" />
        <el-table-column label="流转码" align="center" prop="sn" />
        <el-table-column label="物料码" align="center" prop="stockid" />
        <el-table-column label="版次" align="center" prop="banci" />
        <el-table-column label="数量" align="center" prop="counts" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:primark:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:primark:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改打单列表对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="primarkFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工作id" prop="workid">
          <el-input v-model="form.workid" placeholder="请输入工作id" />
        </el-form-item>
        <el-form-item label="订单号" prop="orderid">
          <el-input v-model="form.orderid" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="交货日期" prop="subtime">
          <el-input v-model="form.subtime" placeholder="请输入交货日期" />
        </el-form-item>
        <el-form-item label="流转码" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入流转码" />
        </el-form-item>
        <el-form-item label="物料码" prop="stockid">
          <el-input v-model="form.stockid" placeholder="请输入物料码" />
        </el-form-item>
        <el-form-item label="版次" prop="banci">
          <el-input v-model="form.banci" placeholder="请输入版次" />
        </el-form-item>
        <el-form-item label="数量" prop="counts">
          <el-input v-model="form.counts" placeholder="请输入数量" />
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

<script setup name="Primark" lang="ts">
import { listPrimark, getPrimark, delPrimark, addPrimark, updatePrimark } from '@/api/mes/primark';
import { PrimarkVO, PrimarkQuery, PrimarkForm } from '@/api/mes/primark/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const primarkList = ref<PrimarkVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRangeCreateTime = ref<[DateModelType, DateModelType]>(['', '']);

const queryFormRef = ref<ElFormInstance>();
const primarkFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: PrimarkForm = {
  id: undefined,
  workid: undefined,
  orderid: undefined,
  subtime: undefined,
  sn: undefined,
  stockid: undefined,
  banci: undefined,
  counts: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<PrimarkForm, PrimarkQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workid: undefined,
    orderid: undefined,
    subtime: undefined,
    sn: undefined,
    stockid: undefined,
    banci: undefined,
    counts: undefined,
    status: undefined,
    params: {
      createTime: undefined,
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询打单列表列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.params = {};
  proxy?.addDateRange(queryParams.value, dateRangeCreateTime.value, 'CreateTime');
  const res = await listPrimark(queryParams.value);
  primarkList.value = res.rows;
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
  primarkFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  dateRangeCreateTime.value = ['', ''];
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: PrimarkVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加打单列表";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: PrimarkVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getPrimark(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改打单列表";
}

/** 提交按钮 */
const submitForm = () => {
  primarkFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updatePrimark(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addPrimark(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: PrimarkVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除打单列表编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delPrimark(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/primark/export', {
    ...queryParams.value
  }, `primark_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
