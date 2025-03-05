<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="类型" prop="type">
              <el-select v-model="queryParams.type" placeholder="请选择类型" clearable >
                <el-option v-for="dict in customtype" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
            <el-form-item label="简称" prop="descp">
              <el-input v-model="queryParams.descp" placeholder="请输入简称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="银行账户" prop="cardnum">
              <el-input v-model="queryParams.cardnum" placeholder="请输入银行账户" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="全称" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入全称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="联系方式" prop="phone">
              <el-input v-model="queryParams.phone" placeholder="请输入联系方式" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="社会编码" prop="shnum">
              <el-input v-model="queryParams.shnum" placeholder="请输入社会编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="收货地址" prop="address">
              <el-input v-model="queryParams.address" placeholder="请输入收货地址" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="邮寄地址" prop="address1">
              <el-input v-model="queryParams.address1" placeholder="请输入邮寄地址" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:custom:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:custom:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:custom:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:custom:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="customList" @selection-change="handleSelectionChange"
      style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center','font-size':'12px' }"
          :row-style="{ height: '50px' }"
          lazy border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编码" align="center" prop="id" v-if="false" />
        <el-table-column label="类型" align="center" prop="type">
          <template #default="scope">
            <dict-tag :options="customtype" :value="scope.row.type"/>
          </template>
        </el-table-column>
        <el-table-column label="全称" align="center" prop="title" />
        <el-table-column label="简称" align="center" prop="descp" />
        <el-table-column label="联系人" align="center" prop="cname" />

        <el-table-column label="联系方式" align="center" prop="phone" />
        <el-table-column label="社会编码" align="center" prop="shnum" />
        <el-table-column label="收货地址" align="center" prop="address" />
        <el-table-column label="邮寄地址" align="center" prop="address1" />
        <el-table-column label="银行账户" align="center" prop="cardnum" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:custom:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:custom:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改客户供应商对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="customFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option
                v-for="dict in customtype"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="全称" prop="title">
          <el-input v-model="form.title" placeholder="请输入全称" />
        </el-form-item>
        <el-form-item label="简称" prop="descp">
          <el-input v-model="form.descp" placeholder="请输入简称" />
        </el-form-item>
        <el-form-item label="联系人" prop="descp">
          <el-input v-model="form.cname" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系方式" />
        </el-form-item>
        
        <el-form-item label="银行账户" prop="cardnum">
          <el-input v-model="form.cardnum" placeholder="请输入银行账户" />
        </el-form-item>
        <el-form-item label="社会编码" prop="shnum">
          <el-input v-model="form.shnum" placeholder="请输入社会编码" />
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="邮寄地址" prop="address1">
          <el-input v-model="form.address1" placeholder="请输入邮寄地址" />
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

<script setup name="Custom" lang="ts">
import { listCustom, getCustom, delCustom, addCustom, updateCustom } from '@/api/mes/custom';
import { CustomVO, CustomQuery, CustomForm } from '@/api/mes/custom/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { customtype } = toRefs<any>(proxy?.useDict('customtype'));

const customList = ref<CustomVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const customFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: CustomForm = {
  id: undefined,
  type: undefined,
  descp: undefined,
  cardnum: undefined,
  title: undefined,
  phone: undefined,
  shnum: undefined,
  address: undefined,
  address1: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<CustomForm, CustomQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    type: undefined,
    descp: undefined,
    cardnum: undefined,
    title: undefined,
    phone: undefined,
    shnum: undefined,
    address: undefined,
    address1: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询客户供应商列表 */
const getList = async () => {
  loading.value = true;
  const res = await listCustom(queryParams.value);
  customList.value = res.rows;
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
  customFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: CustomVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加客户供应商";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: CustomVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getCustom(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改客户供应商";
}

/** 提交按钮 */
const submitForm = () => {
  customFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateCustom(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addCustom(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: CustomVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除客户供应商编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delCustom(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/custom/export', {
    ...queryParams.value
  }, `custom_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
