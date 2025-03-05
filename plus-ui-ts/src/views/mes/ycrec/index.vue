<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="流转" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入流转" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="正常数量" prop="count">
              <el-input v-model="queryParams.count" placeholder="请输入正常数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="图片" prop="imgs">
              <el-input v-model="queryParams.imgs" placeholder="请输入图片" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="异常数量" prop="yccount">
              <el-input v-model="queryParams.yccount" placeholder="请输入异常数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="描述" prop="ycdesc">
              <el-input v-model="queryParams.ycdesc" placeholder="请输入描述" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="用户" prop="userId">
              <el-input v-model="queryParams.userId" placeholder="请输入用户" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:ycrec:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:ycrec:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:ycrec:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:ycrec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="ycrecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        
        <el-table-column label="产品" align="center" prop="productVo.title" />
        <el-table-column label="图号" align="center" prop="productVo.sn" />
        <el-table-column label="流转码" align="center" prop="sn" />
        <el-table-column label="排程名" align="center" prop="workVo.title" />
        <el-table-column label="正常数量" align="center" prop="count" />
        <el-table-column label="异常数量" align="center" prop="yccount" />
        <el-table-column label="异常原因" align="center" prop="remark" >
          <template #default="scope">
            <dict-tag :options="user_task_type" :value="scope.row.remark" />
          </template>
          </el-table-column>
        <el-table-column label="异常图片" align="center" prop="imgs" >
          <template #default="scope">
            <image-preview :src="scope.row.imgs" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column label="描述" align="center" prop="ycdesc" />
        <el-table-column label="提交用户" align="center" prop="username" />
        
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:ycrec:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:ycrec:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改异常报工审核对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="ycrecFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流转" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入流转" />
        </el-form-item>
        <el-form-item label="正常数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入正常数量" />
        </el-form-item>
        <el-form-item label="图片" prop="imgs">
          <el-input v-model="form.imgs" placeholder="请输入图片" />
        </el-form-item>
        <el-form-item label="异常数量" prop="yccount">
          <el-input v-model="form.yccount" placeholder="请输入异常数量" />
        </el-form-item>
        <el-form-item label="描述" prop="ycdesc">
          <el-input v-model="form.ycdesc" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户" />
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

<script setup name="Ycrec" lang="ts">
import { listYcrec, getYcrec, delYcrec, addYcrec, updateYcrec } from '@/api/mes/ycrec';
import { YcrecVO, YcrecQuery, YcrecForm } from '@/api/mes/ycrec/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { makestatus, user_task_type,isturnon } = toRefs<any>(proxy?.useDict('makestatus', 'user_task_type','isturnon'));

const ycrecList = ref<YcrecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const ycrecFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: YcrecForm = {
  id: undefined,
  sn: undefined,
  count: undefined,
  imgs: undefined,
  yccount: undefined,
  ycdesc: undefined,
  userId: undefined,
  status: undefined,
  remark: undefined
}
const data = reactive<PageData<YcrecForm, YcrecQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    count: undefined,
    imgs: undefined,
    yccount: undefined,
    ycdesc: undefined,
    userId: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询异常报工审核列表 */
const getList = async () => {
  loading.value = true;
  const res = await listYcrec(queryParams.value);
  ycrecList.value = res.rows;
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
  ycrecFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: YcrecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加异常报工审核";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: YcrecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getYcrec(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改异常报工审核";
}

/** 提交按钮 */
const submitForm = () => {
  ycrecFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateYcrec(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addYcrec(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: YcrecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除异常报工审核编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delYcrec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/ycrec/export', {
    ...queryParams.value
  }, `ycrec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
