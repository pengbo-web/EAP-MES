<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="数量" prop="counts">
              <el-input v-model="queryParams.counts" placeholder="请输入数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="价格" prop="prices">
              <el-input v-model="queryParams.prices" placeholder="请输入价格" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="物料名称" prop="stockid">
              <el-input v-model="queryParams.stockname" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="物料编码" prop="stockid">
              <el-input v-model="queryParams.stockcode" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="供应商" prop="cname">
              <el-input v-model="queryParams.cname" placeholder="请输入供应商" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="创建时间" style="width: 308px">
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
            <el-form-item label="备注" prop="remark">
              <el-input v-model="queryParams.remark" placeholder="请输入备注" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:allrec:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:allrec:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:allrec:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:allrec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="allrecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编码" align="center" prop="id" v-if="false" />
        <el-table-column label="物料" align="center" prop="stockVo.tilte" />
        <el-table-column label="编码" align="center" prop="stockVo.sn" />
        <el-table-column label="规格" align="center" prop="stockVo.gk" />
        <el-table-column label="类型" align="center" prop="type" />
        <el-table-column label="来源" align="center" prop="fromtype" />
        <el-table-column label="数量" align="center" prop="counts" />
        <el-table-column label="单位" align="center" prop="unitid" />
        <el-table-column label="价格" align="center" prop="prices" />

        <el-table-column label="供应商" align="center" prop="cname" />
        <el-table-column label="操作时间" align="center" prop="createTime" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:allrec:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:allrec:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改出入库记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="allrecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="数量" prop="counts">
          <el-input v-model="form.counts" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="单位" prop="unitid">
          <el-input v-model="form.unitid" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="价格" prop="prices">
          <el-input v-model="form.prices" placeholder="请输入价格" />
        </el-form-item>
        <el-form-item label="物料" prop="stockid">
          <el-input v-model="form.stockid" placeholder="请输入物料" />
        </el-form-item>
        <el-form-item label="供应商" prop="cname">
          <el-input v-model="form.cname" placeholder="请输入供应商" />
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

<script setup name="Allrec" lang="ts">
import { listAllrec, getAllrec, delAllrec, addAllrec, updateAllrec } from '@/api/mes/allrec';
import { AllrecVO, AllrecQuery, AllrecForm } from '@/api/mes/allrec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const allrecList = ref<AllrecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRangeCreateTime = ref<[DateModelType, DateModelType]>(['', '']);

const queryFormRef = ref<ElFormInstance>();
const allrecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: AllrecForm = {
  id: undefined,
  type: undefined,
  fromtype: undefined,
  counts: undefined,
  unitid: undefined,
  prices: undefined,
  stockid: undefined,
  cname: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<AllrecForm, AllrecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    type: undefined,
    fromtype: undefined,
    counts: undefined,
    prices: undefined,
    stockid: undefined,
    cname: undefined,
    status: undefined,
    remark: undefined,
    params: {
      createTime: undefined,
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询出入库记录列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.params = {};
  proxy?.addDateRange(queryParams.value, dateRangeCreateTime.value, 'CreateTime');
  const res = await listAllrec(queryParams.value);
  allrecList.value = res.rows;
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
  allrecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: AllrecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加出入库记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: AllrecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getAllrec(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改出入库记录";
}

/** 提交按钮 */
const submitForm = () => {
  allrecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateAllrec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addAllrec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: AllrecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除出入库记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delAllrec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/allrec/export', {
    ...queryParams.value
  }, `allrec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
