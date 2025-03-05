<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="物料" prop="proId">
              <el-input v-model="queryParams.proId" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="出库单位" prop="unitId">
              <el-input v-model="queryParams.unitId" placeholder="请输入出库单位" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="数量" prop="count">
              <el-input v-model="queryParams.count" placeholder="请输入数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="流转码" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入流转码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="任务关联" prop="taskId">
              <el-input v-model="queryParams.taskId" placeholder="请输入任务关联" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable >
                <el-option v-for="dict in diyisno" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:outstock:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:outstock:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:outstock:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:outstock:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="outstockList" @selection-change="handleSelectionChange"
      style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center','font-size':'11px' }"
          :row-style="{ height: '50px' }"
          lazy border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="物料" align="center" prop="stockVo.tilte" />
        <el-table-column label="编码" align="center" prop="stockVo.sn" />
        <el-table-column label="规格" align="center" prop="stockVo.gk" />
        <el-table-column label="出库单位" align="center" prop="uniname" />
        <el-table-column label="来源" align="center" prop="fromtype" >
          <template #default="scope">
            <el-text v-if="scope.row.remark=='1'">其他出库记录</el-text>
            <el-text v-else>{{scope.row.fromtype}}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="数量" align="center" prop="count" />
        <el-table-column label="流转码" align="center" prop="sn" />
        <el-table-column label="任务关联" align="center" prop="taskId" />
        <el-table-column label="是否完成审核" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="diyisno" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="上次结存数量" align="center" prop="laststock" />
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top" v-if="scope.row.status === '0'">
              <el-button  type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:outstock:edit']">确认出库</el-button>
            </el-tooltip>
            <el-text v-if="scope.row.status === '1'">已出库</el-text>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改出库管理对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="outstockFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品" prop="proId">
          <el-select v-model="form.proId" filterable placeholder="请输入产品" style="width: 440px" @change="chooseuni">
            <el-option v-for="item in productList" :key="item.id" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="出库单位" prop="unitId">
          <el-select v-model="form.unitId"  placeholder="请输入产品" style="width: 440px" >
            <el-option v-for="item in unilist" :key="item.id" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="流转码" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入流转码" />
        </el-form-item>
        <el-form-item label="任务关联" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入任务关联" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in diyisno"
              :key="dict.value"
              :value="dict.value"
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

<script setup name="Outstock" lang="ts">
import { listOutstock, getOutstock, delOutstock, addOutstock, updateOutstock,getunitlist } from '@/api/mes/outstock';
import { OutstockVO, OutstockQuery, OutstockForm } from '@/api/mes/outstock/types';
import { listProduct} from "@/api/mes/product";
import { ProductVO } from '@/api/mes/product/types';
import { UnitmeasureVO, UnitmeasureForm, UnitmeasureQuery } from '@/api/mes/unitmeasure/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { diyisno } = toRefs<any>(proxy?.useDict('diyisno'));

const outstockList = ref<OutstockVO[]>([]);
  const productList = ref<ProductVO[]>([]);
    const unilist = ref<UnitmeasureVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const outstockFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: OutstockForm = {
  id: undefined,
  proId: undefined,
  unitId: undefined,
  fromtype: undefined,
  count: undefined,
  sn: undefined,
  taskId: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<OutstockForm, OutstockQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    proId: undefined,
    unitId: undefined,
    fromtype: undefined,
    count: undefined,
    sn: undefined,
    taskId: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询出库管理列表 */
const getList = async () => {
  loading.value = true;
  const res = await listOutstock(queryParams.value);
  outstockList.value = res.rows;
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
  outstockFormRef.value?.resetFields();
}

const chooseuni=async(value:any)=>{
  let unit_id=0;
  productList.value.forEach((item) => {
    if(item.id==value){
      unit_id=item.unitId;
    }
  })
  const res=await getunitlist(unit_id);
  unilist.value=res;
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
const handleSelectionChange = (selection: OutstockVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加出库管理";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: OutstockVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOutstock(_id);
  Object.assign(form.value, res.data);
  form.value.status='1';
  await updateOutstock(form.value);
  proxy?.$modal.msgSuccess("操作成功");
  getList();
}

/** 提交按钮 */
const submitForm = () => {
  outstockFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateOutstock(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addOutstock(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: OutstockVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除出库管理编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delOutstock(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/outstock/export', {
    ...queryParams.value
  }, `outstock_${new Date().getTime()}.xlsx`)
}
const getProList = async () => {
  loading.value = true;
  const res = await listProduct(queryParams.value);
  productList.value = res.data;
}
onMounted(() => {
  getList();
  getProList();
});
</script>
