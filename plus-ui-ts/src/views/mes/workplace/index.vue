<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div class="search" v-show="showSearch">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
          <el-form-item label="车间编码" prop="sn">
            <el-input v-model="queryParams.sn" placeholder="请输入车间编码" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="车间名称" prop="title">
            <el-input v-model="queryParams.title" placeholder="请输入车间名称" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="面积" prop="area">
            <el-input v-model="queryParams.area" placeholder="请输入面积" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="负责人" prop="curator">
            <el-input v-model="queryParams.curator" placeholder="请输入负责人" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="角色状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择角色状态" clearable>
              <el-option
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </transition>
    <el-card shadow="never">
     <div> <el-tag size="medium" type="primary" class="mb1">第四部，车间设置。此处流程为：车间—工作站—班组，负责什么工序，工序可以多工艺流程也可以单流程</el-tag></div>
    </el-card>
    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:workplace:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:workplace:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:workplace:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:workplace:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="workplaceList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="车间编码" align="center" prop="sn" />
        <el-table-column label="车间名称" align="center" prop="title" />
        <el-table-column label="面积" align="center" prop="area" />
        <el-table-column label="负责人" align="center" prop="curator" />
        <el-table-column label="角色状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:workplace:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:workplace:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </el-card>
    <!-- 添加或修改④车间设置对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="workplaceFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车间编码" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入车间编码" />
        </el-form-item>
        <el-form-item label="车间名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入车间名称" />
        </el-form-item>
        <el-form-item label="面积" prop="area">
          <el-input v-model="form.area" placeholder="请输入面积" />
        </el-form-item>
        <el-form-item label="负责人" prop="curator">
          <el-input v-model="form.curator" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="角色状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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

<script setup name="Workplace" lang="ts">
import { listWorkplace, getWorkplace, delWorkplace, addWorkplace, updateWorkplace } from '@/api/mes/workplace';
import { WorkplaceVO, WorkplaceQuery, WorkplaceForm } from '@/api/mes/workplace/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));

const workplaceList = ref<WorkplaceVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const workplaceFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: WorkplaceForm = {
  id: undefined,
  sn: undefined,
  title: undefined,
  area: undefined,
  curator: undefined,
  status: "0",
  remark: undefined,
}
const data = reactive<PageData<WorkplaceForm, WorkplaceQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    title: undefined,
    area: undefined,
    curator: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    title: [
      { required: true, message: "车间名称不能为空", trigger: "blur" }
    ],
    curator: [
      { required: true, message: "负责人不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询④车间设置列表 */
const getList = async () => {
  loading.value = true;
  const res = await listWorkplace(queryParams.value);
  workplaceList.value = res.rows;
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
  workplaceFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: WorkplaceVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加④车间设置";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: WorkplaceVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getWorkplace(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改④车间设置";
}

/** 提交按钮 */
const submitForm = () => {
  workplaceFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateWorkplace(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addWorkplace(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("修改成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: WorkplaceVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除④车间设置编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delWorkplace(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/workplace/export', {
    ...queryParams.value
  }, `workplace_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
