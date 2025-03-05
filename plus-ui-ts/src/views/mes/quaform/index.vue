<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="产品" prop="proid">
              <el-input v-model="queryParams.proid" placeholder="请输入产品" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="批次" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入批次" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="结果" prop="resu">
              <el-input v-model="queryParams.resu" placeholder="请输入结果" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否入库" prop="instock">
              <el-input v-model="queryParams.instock" placeholder="请输入是否入库" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input v-model="queryParams.sort" placeholder="请输入排序" clearable @keyup.enter="handleQuery" />
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
          <!-- <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:quaform:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:quaform:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:quaform:remove']">删除</el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:quaform:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="quaformList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编码" align="center" prop="id" v-if="false" />
        <el-table-column label="产品" align="center" prop="productVo.title" />
        <el-table-column label="批次" align="center" prop="sn" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="结果" align="center" prop="resu" />
        <el-table-column label="是否入库" align="center" prop="instock" />
        <el-table-column label="检测时间" align="center" prop="createTime" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:quaform:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:quaform:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改来料检对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="quaformFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品" prop="proid">
          <el-input v-model="form.proid" placeholder="请输入产品" />
        </el-form-item>
        <el-form-item label="批次" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入批次" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="结果" prop="resu">
          <el-input v-model="form.resu" placeholder="请输入结果" />
        </el-form-item>
        <el-form-item label="是否入库" prop="instock">
          <el-input v-model="form.instock" placeholder="请输入是否入库" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
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

<script setup name="Quaform" lang="ts">
import { listQuaform, getQuaform, delQuaform, addQuaform, updateQuaform } from '@/api/mes/quaform';
import { QuaformVO, QuaformQuery, QuaformForm } from '@/api/mes/quaform/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const quaformList = ref<QuaformVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const quaformFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: QuaformForm = {
  id: undefined,
  proid: undefined,
  sn: undefined,
  title: undefined,
  resu: undefined,
  instock: undefined,
  sort: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<QuaformForm, QuaformQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    proid: undefined,
    sn: undefined,
    title: undefined,
    resu: undefined,
    instock: undefined,
    sort: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询来料检列表 */
const getList = async () => {
  loading.value = true;
  const res = await listQuaform(queryParams.value);
  quaformList.value = res.rows;
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
  quaformFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: QuaformVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加来料检";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: QuaformVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getQuaform(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改来料检";
}

/** 提交按钮 */
const submitForm = () => {
  quaformFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateQuaform(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addQuaform(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: QuaformVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除来料检编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delQuaform(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/quaform/export', {
    ...queryParams.value
  }, `quaform_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
