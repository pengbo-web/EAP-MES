<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div class="search" v-show="showSearch">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
          <el-form-item label="单位编码" prop="unitsn">
            <el-input v-model="queryParams.unitsn" placeholder="请输入单位编码" clearable style="width: 240px"
              @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="名称" prop="title">
            <el-input v-model="queryParams.title" placeholder="请输入名称" clearable style="width: 240px"
              @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="角色状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择角色状态" clearable>
              <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
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
     <div> <el-tag size="medium" type="primary" class="mb1">Mes使用第一步，先录入公司产品和零部件的单位</el-tag></div>
      <el-tag size="medium" type="primary" class="mb1">可以使用捆与箱，比如主单位为箱，次单位为件。10件装一箱</el-tag>
    </el-card>
    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd"
              v-hasPermi="['mes:unitmeasure:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['mes:unitmeasure:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:unitmeasure:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['mes:unitmeasure:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="unitmeasureList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="单位编码" align="center" prop="unitsn" />
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="是否主单位" align="center" prop="isMain">
          <template #default="scope">
            <dict-tag :options="diyisno" :value="scope.row.isMain" />
          </template>
        </el-table-column>
        <el-table-column label="上级单位" align="center" prop="upname">
          <template #default="scope">
            <!-- <dict-tag :value="scope.row.upname" /> -->
          </template>
        </el-table-column>
        <el-table-column label="进率" align="center" prop="grow" >
          <template #default="scope">
            <el-tag  type="success"  v-if="scope.row.grow!=null">{{scope.row.grow}}</el-tag>
            <div v-else>无</div>
          </template>
        </el-table-column>
        <el-table-column label="角色状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
   
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['mes:unitmeasure:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:unitmeasure:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改①计量单位对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="900px" append-to-body>
      <el-form ref="unitmeasureFormRef" :model="form" :rules="rules" label-width="auto">
        <el-form-item label="单位编码" prop="unitsn">
          <el-input v-model="form.unitsn" placeholder="请输入单位编码" />
        </el-form-item>
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="是否主单位" prop="isMain">
          <el-radio-group v-model="form.isMain">
            <el-radio v-for="dict in diyisno" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上级单位" prop="remark" v-if="form.isMain=='0'">
          <el-select v-model="form.remark" placeholder="请选择上级单位">
            <el-option v-for="dict in unitMainList" :key="dict.id" :label="dict.title"
              :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
          
          <el-form-item label="进率" prop="grow" v-if="form.isMain=='0'">
            <el-input v-model="form.grow" placeholder="请输入进率" />
            <p>与上级[主]单位的进率比，例如1:100,此处填写100</p>
          </el-form-item>

        <el-form-item label="角色状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
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

<script setup name="Unitmeasure" lang="ts">
import { listUnitmeasure, getUnitmeasure, delUnitmeasure, addUnitmeasure, updateUnitmeasure } from '@/api/mes/unitmeasure';
import { UnitmeasureVO, UnitmeasureQuery, UnitmeasureForm } from '@/api/mes/unitmeasure/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_device_type, diyisno, sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_device_type', 'diyisno', 'sys_normal_disable'));

const unitmeasureList = ref<UnitmeasureVO[]>([]);
const unitMainList = ref<UnitmeasureVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const unitmeasureFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: UnitmeasureForm = {
  unitsn: undefined,
  title: undefined,
  isMain: "0",
  grow: undefined,
  status: "0",
  remark: undefined,
}
const data = reactive<PageData<UnitmeasureForm, UnitmeasureQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    id: undefined,
    unitsn: undefined,
    title: undefined,
    isMain: undefined,
    grow: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    unitsn: [
      { required: true, message: "单位编码不能为空", trigger: "blur" }
    ],
    title: [
      { required: true, message: "名称不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询①计量单位列表 */
const getList = async () => {
  loading.value = true;
  const res = await listUnitmeasure(queryParams.value);
  unitmeasureList.value = res.rows;
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
  form.value = { ...initFormData };
  unitmeasureFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: UnitmeasureVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
const getMainList = async () => {
  const res = await listUnitmeasure(queryParams.value);
  queryParams.value.isMain = undefined;
  unitMainList.value = res.rows;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加①计量单位";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: UnitmeasureVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getUnitmeasure(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改①计量单位";
}

/** 提交按钮 */
const submitForm = () => {
  unitmeasureFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateUnitmeasure(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addUnitmeasure(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("修改成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: UnitmeasureVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除①计量单位编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delUnitmeasure(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/unitmeasure/export', {
    ...queryParams.value
  }, `unitmeasure_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getMainList();
});
</script>
