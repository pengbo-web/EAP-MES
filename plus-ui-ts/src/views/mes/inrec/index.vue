<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="物料" prop="stockid">
              <el-input v-model="queryParams.stockname" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="编码" prop="custId">
              <el-input v-model="queryParams.stockcode" placeholder="请输入编码" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:inrec:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:inrec:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:inrec:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:inrec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="inrecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="" align="center" prop="id" v-if="false" />
        <el-table-column label="物料" align="center" prop="stockVo.tilte" />
        <el-table-column label="编号" align="center" prop="stockVo.sn" />
        <!-- <el-table-column label="关联编码" align="center" prop="stockVo.sn" /> -->
        <!-- <el-table-column label="供应商" align="center" prop="custId" /> -->
        <el-table-column label="进货价" align="center" prop="nprice" />
        <el-table-column label="税点" align="center" prop="SHUIDIAN" />
        <el-table-column label="结算方式" align="center" prop="met" />
        <el-table-column label="数量" align="center" prop="count" />
        <el-table-column label="角色状态" align="center" prop="status" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:inrec:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:inrec:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改入库记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="inrecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料" prop="stockid">
          <el-input v-model="form.stockid" placeholder="请输入物料" />
        </el-form-item>
        <el-form-item label="供应商" prop="custId">
          <el-input v-model="form.custId" placeholder="请输入供应商" />
        </el-form-item>
        <el-form-item label="进货价" prop="nprice">
          <el-input v-model="form.nprice" placeholder="请输入进货价" />
        </el-form-item>
        <el-form-item label="税点" prop="SHUIDIAN">
          <el-input v-model="form.SHUIDIAN" placeholder="请输入税点" />
        </el-form-item>
        <el-form-item label="结算方式" prop="met">
          <el-input v-model="form.met" placeholder="请输入结算方式" />
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入数量" />
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

<script setup name="Inrec" lang="ts">
import { listInrec, getInrec, delInrec, addInrec, updateInrec } from '@/api/mes/inrec';
import { InrecVO, InrecQuery, InrecForm } from '@/api/mes/inrec/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const inrecList = ref<InrecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const inrecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: InrecForm = {
  id: undefined,
  stockid: undefined,
  custId: undefined,
  nprice: undefined,
  SHUIDIAN: undefined,
  met: undefined,
  count: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<InrecForm, InrecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    stockid: undefined,
    custId: undefined,
    nprice: undefined,
    SHUIDIAN: undefined,
    met: undefined,
    count: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询入库记录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listInrec(queryParams.value);
  inrecList.value = res.rows;
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
  inrecFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  queryParams.value = {};
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: InrecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加入库记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: InrecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getInrec(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改入库记录";
}

/** 提交按钮 */
const submitForm = () => {
  inrecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateInrec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addInrec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: InrecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除入库记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delInrec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/inrec/export', {
    ...queryParams.value
  }, `inrec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
