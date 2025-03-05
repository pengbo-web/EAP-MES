<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div class="search" v-show="showSearch">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
          
          <el-form-item label="标题" prop="title">
            <el-input v-model="queryParams.title" placeholder="请输入标题" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="编码" prop="sn">
            <el-input v-model="queryParams.sn" placeholder="请输入编码" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="上级" prop="pid">
            <el-input v-model="queryParams.pid" placeholder="请输入上级" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="角色状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择角色状态" clearable>
              <el-option
                  v-for="dict in diyisno"
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
 

     <div> <el-tag size="medium" type="primary" class="mb1">第二部，录入产品类型，如：成品，原材料，自制件，采购件，等……</el-tag></div>
    </el-card>
    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd()" v-hasPermi="['mes:protype:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Sort" @click="handleToggleExpandAll">展开/折叠</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table
        v-loading="loading"
        :data="protypeList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        ref="protypeTableRef"
      >
    
        <el-table-column label="名称" align="center" prop="title" />
        <el-table-column label="库位" prop="sn" />
        <el-table-column label="上级" align="center" prop="upname" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:protype:edit']" />
            </el-tooltip>
            <el-tooltip content="新增" placement="top">
              <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)" v-hasPermi="['mes:protype:add']" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:protype:remove']" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 添加或修改②产品类别对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="protypeFormRef" :model="form" :rules="rules" label-width="80px">
     
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="库位" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="上级" prop="pid">
          <el-tree-select
            v-model="form.pid"
            :data="protypeOptions"
            :props="{ value: 'id', label: 'title', children: 'children' }"
            value-key="id"
            placeholder="请选择上级"
            check-strictly
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
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

<script setup name="Protype" lang="ts">
import { listProtype, getProtype, delProtype, addProtype, updateProtype } from "@/api/mes/protype";
import { ProtypeVO, ProtypeQuery, ProtypeForm } from '@/api/mes/protype/types';

type ProtypeOption = {
  id: number;
  title: string;
  children?: ProtypeOption[];
}

const { proxy } = getCurrentInstance() as ComponentInternalInstance;;

const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));

const protypeList = ref<ProtypeVO[]>([]);
const protypeOptions = ref<ProtypeOption[]>([]);
const buttonLoading = ref(false);
const showSearch = ref(true);
const isExpandAll = ref(true);
const loading = ref(false);

const queryFormRef = ref<ElFormInstance>();
const protypeFormRef = ref<ElFormInstance>();
const protypeTableRef = ref<ElTableInstance>()

const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
});


const initFormData: ProtypeForm = {
    id: undefined,
    sn: undefined,
    title: undefined,
    pid: undefined,
    status: "0",
    remark: undefined,
}

const data = reactive<PageData<ProtypeForm, ProtypeQuery>>({
  form: {...initFormData},
  queryParams: {
    sn: undefined,
    title: undefined,
    pid: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    title: [
      { required: true, message: "标题不能为空", trigger: "blur" }
    ],
    pid: [
      { required: true, message: "上级不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询②产品类别列表 */
const getList = async () => {
  loading.value = true;
  const res = await listProtype(queryParams.value);
  const data = proxy?.handleTree<ProtypeVO>(res.data, "id", "pid");
  if (data) {
    protypeList.value = data;
    loading.value = false;
  }
}

/** 查询②产品类别下拉树结构 */
const getTreeselect = async () => {
  const res = await listProtype();
  protypeOptions.value = [];
  const data: ProtypeOption = { id: 0, title: '无上级', children: [] };
  data.children = proxy?.handleTree<ProtypeOption>(res.data, "id", "pid");
  protypeOptions.value.push(data);
}

// 取消按钮
const cancel = () => {
  reset();
  dialog.visible = false;
}

// 表单重置
const reset = () => {
  form.value = {...initFormData}
  protypeFormRef.value?.resetFields();
}

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
const handleAdd = (row?: ProtypeVO) => {
  reset();
  getTreeselect();
  if (row != null && row.id) {
      form.value.pid = row.id;
  } else {
      form.value.pid = 0;
  }
  dialog.visible = true;
  dialog.title = "添加②产品类别";
}

/** 展开/折叠操作 */
const handleToggleExpandAll = () => {
  isExpandAll.value = !isExpandAll.value;
  toggleExpandAll(protypeList.value, isExpandAll.value)
}

/** 展开/折叠操作 */
const toggleExpandAll = (data: ProtypeVO[], status: boolean) => {
  data.forEach((item) => {
    protypeTableRef.value?.toggleRowExpansion(item, status)
    if (item.children && item.children.length > 0) toggleExpandAll(item.children, status)
  })
}

/** 修改按钮操作 */
const handleUpdate = async (row: ProtypeVO) => {
  reset();
  await getTreeselect();
  if (row != null) {
    form.value.pid = row.pid;
  }
  const res = await getProtype(row.id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改②产品类别";
}

/** 提交按钮 */
const submitForm = () => {
  protypeFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateProtype(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addProtype(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row: ProtypeVO) => {
  await proxy?.$modal.confirm('是否确认删除②产品类别编号为"' + row.id + '"的数据项？');
  loading.value = true;
  await delProtype(row.id).finally(() => loading.value = false);
  await getList();
  proxy?.$modal.msgSuccess("删除成功");
}

onMounted(() => {
  getList();
});
</script>
