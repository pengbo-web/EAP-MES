<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="工作站" prop="workshopId">
              <el-input v-model="queryParams.workshopId" placeholder="请输入工作站" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="物料" prop="proId">
              <el-input v-model="queryParams.proId" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="需组装数量" prop="zpcount">
              <el-input v-model="queryParams.zpcount" placeholder="请输入需组装数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input v-model="queryParams.sort" placeholder="请输入排序" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否关键工序" prop="mustbe">
              <el-input v-model="queryParams.mustbe" placeholder="请输入是否关键工序" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否工艺流转" prop="istruec">
              <el-input v-model="queryParams.istruec" placeholder="请输入是否工艺流转" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:prosettingzp:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:prosettingzp:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:prosettingzp:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:prosettingzp:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="prosettingzpList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="编号" align="center" prop="id" v-if="true" />
        <el-table-column label="工作站" align="center" prop="workshopId" />
        <el-table-column label="物料" align="center" prop="proId" />
        <el-table-column label="需组装数量" align="center" prop="zpcount" />
        <el-table-column label="排序" align="center" prop="sort" />
        <el-table-column label="角色状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="是否关键工序" align="center" prop="mustbe" />
        <el-table-column label="是否工艺流转" align="center" prop="istruec" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:prosettingzp:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:prosettingzp:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改组装排程对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="prosettingzpFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工作站" prop="workshopId">
          <el-input v-model="form.workshopId" placeholder="请输入工作站" />
        </el-form-item>
        <el-form-item label="物料" prop="proId">
          <el-input v-model="form.proId" placeholder="请输入物料" />
        </el-form-item>
        <el-form-item label="需组装数量" prop="zpcount">
          <el-input v-model="form.zpcount" placeholder="请输入需组装数量" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="是否关键工序" prop="mustbe">
          <el-input v-model="form.mustbe" placeholder="请输入是否关键工序" />
        </el-form-item>
        <el-form-item label="是否工艺流转" prop="istruec">
          <el-input v-model="form.istruec" placeholder="请输入是否工艺流转" />
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

<script setup name="Prosettingzp" lang="ts">
import { listProsettingzp, getProsettingzp, delProsettingzp, addProsettingzp, updateProsettingzp } from '@/api/mes/prosettingzp';
import { ProsettingzpVO, ProsettingzpQuery, ProsettingzpForm } from '@/api/mes/prosettingzp/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const prosettingzpList = ref<ProsettingzpVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const prosettingzpFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: ProsettingzpForm = {
  id: undefined,
  workshopId: undefined,
  proId: undefined,
  zpcount: undefined,
  sort: undefined,
  status: undefined,
  remark: undefined,
  mustbe: undefined,
  istruec: undefined
}
const data = reactive<PageData<ProsettingzpForm, ProsettingzpQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    workshopId: undefined,
    proId: undefined,
    zpcount: undefined,
    sort: undefined,
    status: undefined,
    mustbe: undefined,
    istruec: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询组装排程列表 */
const getList = async () => {
  loading.value = true;
  const res = await listProsettingzp(queryParams.value);
  prosettingzpList.value = res.rows;
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
  prosettingzpFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: ProsettingzpVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加组装排程";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: ProsettingzpVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getProsettingzp(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改组装排程";
}

/** 提交按钮 */
const submitForm = () => {
  prosettingzpFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateProsettingzp(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addProsettingzp(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: ProsettingzpVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除组装排程编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delProsettingzp(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/prosettingzp/export', {
    ...queryParams.value
  }, `prosettingzp_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
