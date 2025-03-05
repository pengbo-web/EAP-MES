<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="使用地址" prop="wname">
              <el-input v-model="queryParams.wname" placeholder="请输入使用地址" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="外发名称" prop="cname">
              <el-input v-model="queryParams.cname" placeholder="请输入外发名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="queryParams.phone" placeholder="请输入联系电话" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="地址" prop="address">
              <el-input v-model="queryParams.address" placeholder="请输入地址" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="归还日期" prop="endtime">
              <el-date-picker clearable
                v-model="queryParams.endtime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择归还日期"
              />
            </el-form-item>
            <el-form-item label="备注2" prop="remark1">
              <el-input v-model="queryParams.remark1" placeholder="请输入备注2" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:modelrec:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:modelrec:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:modelrec:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:modelrec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="modelrecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编号" align="center" prop="id" v-if="true" />
        <el-table-column label="磨具编号" align="center" prop="modelid" />
        <el-table-column label="使用地址" align="center" prop="wname" />
        <el-table-column label="类型" align="center" prop="type" />
        <el-table-column label="外发名称" align="center" prop="cname" />
        <el-table-column label="联系电话" align="center" prop="phone" />
        <el-table-column label="地址" align="center" prop="address" />
        <el-table-column label="归还日期" align="center" prop="endtime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.endtime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注2" align="center" prop="remark1" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:modelrec:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:modelrec:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改模具使用记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="modelrecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="磨具编号" prop="modelid">
          <el-input v-model="form.modelid" placeholder="请输入磨具编号" />
        </el-form-item>
        <el-form-item label="使用地址" prop="wname">
          <el-input v-model="form.wname" placeholder="请输入使用地址" />
        </el-form-item>
        <el-form-item label="外发名称" prop="cname">
          <el-input v-model="form.cname" placeholder="请输入外发名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="归还日期" prop="endtime">
          <el-date-picker clearable
            v-model="form.endtime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择归还日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注2" prop="remark1">
            <el-input v-model="form.remark1" type="textarea" placeholder="请输入内容" />
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

<script setup name="Modelrec" lang="ts">
import { listModelrec, getModelrec, delModelrec, addModelrec, updateModelrec } from '@/api/mes/modelrec';
import { ModelrecVO, ModelrecQuery, ModelrecForm } from '@/api/mes/modelrec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const modelrecList = ref<ModelrecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const modelrecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: ModelrecForm = {
  id: undefined,
  modelid: undefined,
  wname: undefined,
  type: undefined,
  cname: undefined,
  phone: undefined,
  address: undefined,
  endtime: undefined,
  remark1: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<ModelrecForm, ModelrecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    wname: undefined,
    type: undefined,
    cname: undefined,
    phone: undefined,
    address: undefined,
    endtime: undefined,
    remark1: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询模具使用记录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listModelrec(queryParams.value);
  modelrecList.value = res.rows;
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
  modelrecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: ModelrecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加模具使用记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: ModelrecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getModelrec(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改模具使用记录";
}

/** 提交按钮 */
const submitForm = () => {
  modelrecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateModelrec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addModelrec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: ModelrecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除模具使用记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delModelrec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/modelrec/export', {
    ...queryParams.value
  }, `modelrec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
