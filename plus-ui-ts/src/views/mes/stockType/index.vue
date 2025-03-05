<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="上级" prop="pid">
              <el-input v-model="queryParams.pid" placeholder="请输入上级" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd()" v-hasPermi="['mes:stockType:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Sort" @click="handleToggleExpandAll">展开/折叠</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table
        ref="stockTypeTableRef"
        v-loading="loading"
        :data="stockTypeList"
        row-key="id"
        :default-expand-all="false"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        border
        :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center' }"
        
      >
      <el-table-column label="货架码" align="center" prop="sn" />
        <el-table-column label="类型" align="center" prop="title">
          <template #default="scope">
            <dict-tag :options="stocktype" :value="scope.row.title"/>
          </template>
        </el-table-column>
        <el-table-column label="位置说明" align="center" prop="descs" />
        
        <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:stockType:edit']" />
            </el-tooltip>
            <el-tooltip content="新增" placement="top">
              <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)" v-hasPermi="['mes:stockType:add']" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:stockType:remove']" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 添加或修改库存分类对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="stockTypeFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="货架编码" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="类型" prop="title">
          <el-input v-model="form.title" placeholder="请输入类型" />
          <!-- <el-radio-group v-model="form.title">
            <el-radio
              v-for="dict in stocktype"
              :key="dict.value"
              :value="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group> -->
        </el-form-item>
        <el-form-item label="位置描述" prop="sn">
          <el-input v-model="form.descs" placeholder="请输入位置描述"  type="textarea"/>
        </el-form-item>
        <el-form-item label="上级" prop="pid">
          <el-tree-select
            ref="select"
            v-model="form.pid"
            :data="stockTypeOptions"
            :props="{ value: 'id', label: 'title', children: 'children' }"
            value-key="id"
            placeholder="请选择上级"
            check-strictly
            
          />
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

<script setup name="StockType" lang="ts">
import { listStockType, getStockType, delStockType, addStockType, updateStockType } from "@/api/mes/stockType";
import { StockTypeVO, StockTypeQuery, StockTypeForm } from '@/api/mes/stockType/types';

type StockTypeOption = {
  id: number;
  title: string;
  children?: StockTypeOption[];
}

const { proxy } = getCurrentInstance() as ComponentInternalInstance;;

const { diyisno, stocktype } = toRefs<any>(proxy?.useDict('diyisno', 'stocktype'));
const stockTypeList = ref<StockTypeVO[]>([]);
const stockTypeOptions = ref<StockTypeOption[]>([]);
const buttonLoading = ref(false);
const showSearch = ref(true);
const isExpandAll = ref(true);
const loading = ref(false);
const selected = ref(null);
const select = ref(null);
const queryFormRef = ref<ElFormInstance>();
const stockTypeFormRef = ref<ElFormInstance>();
const stockTypeTableRef = ref<ElTableInstance>()

const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
});


const initFormData: StockTypeForm = {
    id: undefined,
    title: undefined,
    pid: undefined,
    status: undefined,
    remark: undefined,
}

const data = reactive<PageData<StockTypeForm, StockTypeQuery>>({
  form: {...initFormData},
  queryParams: {
    title: undefined,
    pid: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询库存分类列表 */
const getList = async () => {
  loading.value = true;
  const res = await listStockType(queryParams.value);
  const data = proxy?.handleTree<StockTypeVO>(res.data, "id", "pid");
  if (data) {
    stockTypeList.value = data;
    loading.value = false;
  }
}

/** 查询库存分类下拉树结构 */
const getTreeselect = async () => {
  const res = await listStockType();
  stockTypeOptions.value = [];
  const data: StockTypeOption = { id: 0, title: '顶级节点', children: [] };
  data.children = proxy?.handleTree<StockTypeOption>(res.data, "id", "pid");
  stockTypeOptions.value.push(data);
}

// 取消按钮
const cancel = () => {
  reset();
  dialog.visible = false;
}

// 表单重置
const reset = () => {
  form.value = {...initFormData}
  stockTypeFormRef.value?.resetFields();
}
const toggleMenu = () => {
  console.log(123)
  select.value.toggleMenu();
};
/** 搜索按钮操作 */
const handleQuery = () => {
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 新增按钮操作 */
const handleAdd = (row?: StockTypeVO) => {
  reset();
  getTreeselect();
  if (row != null && row.id) {
    form.value.pid = row.id;
  } else {
    form.value.pid = 0;
  }
  dialog.visible = true;
  dialog.title = "添加库存分类";
}

/** 展开/折叠操作 */
const handleToggleExpandAll = () => {
  isExpandAll.value = !isExpandAll.value;
  toggleExpandAll(stockTypeList.value, isExpandAll.value)
}

/** 展开/折叠操作 */
const toggleExpandAll = (data: StockTypeVO[], status: boolean) => {
  data.forEach((item) => {
    stockTypeTableRef.value?.toggleRowExpansion(item, status)
    if (item.children && item.children.length > 0) toggleExpandAll(item.children, status)
  })
}

/** 修改按钮操作 */
const handleUpdate = async (row: StockTypeVO) => {
  reset();
  await getTreeselect();
  if (row != null) {
    form.value.pid = row.pid;
  }
  const res = await getStockType(row.id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改库存分类";
}

/** 提交按钮 */
const submitForm = () => {
  stockTypeFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateStockType(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addStockType(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row: StockTypeVO) => {
  await proxy?.$modal.confirm('是否确认删除库存分类编号为"' + row.id + '"的数据项？');
  loading.value = true;
  await delStockType(row.id).finally(() => loading.value = false);
  await getList();
  proxy?.$modal.msgSuccess("删除成功");
}

onMounted(() => {
  getList();
});
</script>
