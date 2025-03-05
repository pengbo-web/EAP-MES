<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="主表" prop="toolid">
              <el-input v-model="queryParams.toolid" placeholder="请输入主表" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="数量" prop="count">
              <el-input v-model="queryParams.count" placeholder="请输入数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="图片" prop="imgs">
              <el-input v-model="queryParams.imgs" placeholder="请输入图片" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="原因" prop="rebak">
              <el-input v-model="queryParams.rebak" placeholder="请输入原因" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="用户" prop="userId">
              <el-input v-model="queryParams.userId" placeholder="请输入用户" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="工作中" prop="workId">
              <el-input v-model="queryParams.workId" placeholder="请输入工作中" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="" prop="useId">
              <el-input v-model="queryParams.useId" placeholder="请输入" clearable @keyup.enter="handleQuery" />
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

      <el-table v-loading="loading" :data="cuttingRecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="编码" align="center" prop="cuttingname" />
        <el-table-column label="图号" align="center" prop="mesCuttingVo.sn" />
        <el-table-column label="数量" align="center" prop="count" />
        <el-table-column label="图片" align="center" prop="imgs" >
          <template #default="scope">
            <image-preview :src="scope.row.imgs" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column label="原因" align="center" prop="rebak" />
        <el-table-column label="提交人" align="center" prop="username" />
        <el-table-column label="提交时间" align="center" prop="createTime" />
       
        <el-table-column label="确认时间" align="center" prop="remark" />
        <el-table-column label="状态" align="center" prop="type" >
          <template #default="scope">
            <el-tag v-if="scope.row.type === '0'" type="success">领用</el-tag>
            <el-tag v-if="scope.row.type === '1'" type="success">报废</el-tag>
            <el-tag v-else-if="scope.row.type === '2'" type="danger">退还</el-tag>
            <el-tag v-else-if="scope.row.type === '3'" type="warning">更换</el-tag>
         </template>
        </el-table-column>

        <el-table-column label="用户" align="center" prop="username" />
        <el-table-column label="状态" align="center" prop="status" >
          <template #default="scope">
            <el-tag v-if="scope.row.status === '1'" type="success">通过</el-tag>
            <el-tag v-else-if="scope.row.status === '2'" type="danger">不通过</el-tag>
            <el-tag v-else-if="scope.row.status === '0'" type="warning">待审核</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope" >
              <el-button v-if="scope.row.status === '0'" link type="primary" icon="Edit" label="" @click="handleUpdate(scope.row)" v-hasPermi="['mes:toolRec:edit']">同意操作</el-button>
             <span v-else>无法操作</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改刀具记录审核对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="cuttingRecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主表" prop="toolid">
          <el-input v-model="form.toolid" placeholder="请输入主表" />
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="图片" prop="imgs">
          <el-input v-model="form.imgs" placeholder="请输入图片" />
        </el-form-item>
        <el-form-item label="原因" prop="rebak">
          <el-input v-model="form.rebak" placeholder="请输入原因" />
        </el-form-item>
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="工作中" prop="workId">
            <el-input v-model="form.workId" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="" prop="useId">
          <el-input v-model="form.useId" placeholder="请输入" />
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

<script setup name="CuttingRec" lang="ts">
import { listCuttingRec, getCuttingRec, delCuttingRec, addCuttingRec, updateCuttingRec } from '@/api/mes/cuttingRec';
import { CuttingRecVO, CuttingRecQuery, CuttingRecForm } from '@/api/mes/cuttingRec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const cuttingRecList = ref<CuttingRecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const cuttingRecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: CuttingRecForm = {
  id: undefined,
  toolid: undefined,
  count: undefined,
  imgs: undefined,
  rebak: undefined,
  type: undefined,
  userId: undefined,
  status: undefined,
  remark: undefined,
  workId: undefined,
  useId: undefined
}
const data = reactive<PageData<CuttingRecForm, CuttingRecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    toolid: undefined,
    count: undefined,
    imgs: undefined,
    rebak: undefined,
    type: undefined,
    userId: undefined,
    status: undefined,
    workId: undefined,
    useId: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询刀具记录审核列表 */
const getList = async () => {
  loading.value = true;
  const res = await listCuttingRec(queryParams.value);
  cuttingRecList.value = res.rows;
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
  cuttingRecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: CuttingRecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加刀具记录审核";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: CuttingRecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getCuttingRec(_id);
  Object.assign(form.value, res.data);
  form.value.status = '1';
  await updateCuttingRec(form.value).finally(() =>  buttonLoading.value = false);
  getList();
}

/** 提交按钮 */
const submitForm = () => {
  cuttingRecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateCuttingRec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addCuttingRec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: CuttingRecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除刀具记录审核编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delCuttingRec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/cuttingRec/export', {
    ...queryParams.value
  }, `cuttingRec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  console.log('123123')
  getList();
});
</script>
