<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="物料" prop="stockid">
              <el-input v-model="queryParams.stockid" placeholder="请输入物料" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="操作">
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:ourec:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['mes:ourec:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:ourec:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['mes:ourec:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="ourecList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="" align="center" prop="id" v-if="false" />
        <el-table-column label="物料" align="center" prop="stockname" />
        <el-table-column label="数量" align="center" prop="count" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <el-tag :type="scope.row.status == '1' ? 'primary' : 'danger'" v-if="scope.row.status == '1'">同意</el-tag>
            <el-tag :type="scope.row.status == '0' ? 'warning' : 'success'" v-if="scope.row.status == '0'">待确认</el-tag>
            <el-tag :type="scope.row.status == '2' ? 'danger' : 'success'" v-if="scope.row.status == '2'">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="订单号" align="center" prop="orderid" />
        <el-table-column label="操作" align="center" >
          <template #default="scope">
            <el-text v-if="scope.row.status == '1'">已同意</el-text>
            <el-tooltip content="同意" placement="top" v-if="scope.row.status == '0'">
              <el-button link type="primary" icon="Select" @click="handleUpdate(scope.row)"
                v-hasPermi="['mes:instock:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="拒绝" placement="top" v-if="scope.row.status == '0'">
              <el-button link type="primary" icon="CloseBold" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:instock:remove']"></el-button>
            </el-tooltip>
            <div class="block">
              <el-button plain type="danger" size="small" 
                @click="handleDelete3(scope.row)">回退</el-button>
                <el-button plain type="info" size="small" 
                @click="handleUpdate2(scope.row)">修改</el-button>
            </div>
            <el-text v-if="scope.row.status == '2'">无法操作</el-text>

          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改出库记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="800px" append-to-body>
      <el-button :icon="Search" @click="choosePro" type="primary">查询</el-button>
      <el-table :data="productlist" style="width: 100%" tooltip-effect="light" scrollbar-always-on>
        <el-table-column prop="tilte" label="名称" width="180" />
        <el-table-column prop="sn" label="图号" />
        <el-table-column prop="unitname" label="单位" />
        <el-table-column prop="gk" label="规格" />
        <el-table-column prop="price" label="单价" width="100">
          <template #default="scope">
            <el-input v-model="scope.row.price" placeholder="请输入单价" />
          </template>
        </el-table-column>
        <el-table-column prop="count" label="库存" width="100">
          <template #default="scope">
            <el-input v-model="scope.row.count" placeholder="请输入数量" />
          </template>
        </el-table-column>
        <el-table-column prop="count" label="订单号" width="100">
          <template #default="scope">
            <el-input v-model="scope.row.orderid" placeholder="请输入数量" />
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" width="100">
          <template #default="scope">
            <el-input v-model="scope.row.remark" placeholder="请输入备注" />
          </template>
        </el-table-column>
        <el-table-column prop="count" label="操作">
          <template #default="scope">
            <el-button type="danger" plain icon="Delete" @click="handleDelete2(scope.row)"
              v-hasPermi="['mes:ourec:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="dialog5.visible"  style="width: 80%;" :title="dialog5.title">  
      <el-input v-model="torder" >
        <template #prepend>订单号：</template>
      </el-input>
      <el-button type="primary"  @click="change">修改</el-button>
    </el-dialog>
    <el-dialog v-model="prolistadd" title="物料列表" style="width: 80%;">
      <el-card shadow="never">
        <template #header>
          <el-row :gutter="10" class="mb8">
            <el-form ref="queryFormRef" :model="queryParams2" :inline="true">
              <el-form-item label="名称" prop="tilte">
                <el-input v-model="queryParams2.tilte" placeholder="请输入名称" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item label="编号" prop="iid">
                <el-input v-model="queryParams2.sn" placeholder="请输入图号" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item label="图号" prop="iid">
                <el-input v-model="queryParams2.iid" placeholder="请输入图号" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery2">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery2">重置</el-button>
              </el-form-item>
            </el-form>
          </el-row>
        </template>
        <el-table v-loading="loading" :data="stockList" highlight-current-row border style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center', 'font-size': '12px' }"
          :row-style="{ height: '50px' }" lazy>
          <el-table-column label="选择" width="" align="center">
            <template #default="scope">
              <el-button type="primary" size="small" @click="submit2(scope.row)">选择</el-button>
            </template>
          </el-table-column>
          <el-table-column label="主键" align="center" prop="id" v-if="false" />
          <el-table-column label="货架号" align="center" prop="stockname" />
          <el-table-column label="编码" align="center" prop="sn" />
          <el-table-column label="名称" align="center" prop="tilte" />
          <el-table-column label="规格" align="center" prop="gk" />
          <el-table-column label="品类" align="center" prop="otype" />
          <el-table-column label="图号" align="center" prop="iid" />
          <el-table-column label="当前库存" align="center" prop="nowstock" />
          <el-table-column label="单位" align="center" prop="unitname" />

          <el-table-column label="报警库存" align="center" prop="warnnum" />
          <!-- <el-table-column label="库存信息" align="center" prop="stockid" /> -->
          <el-table-column label="入库批次" align="center" prop="pcount" />
        </el-table>
        <pagination v-show="total2 > 0" :total="total2" v-model:page="queryParams2.pageNum"
          v-model:limit="queryParams2.pageSize" @pagination="getList2" />
      </el-card>
    </el-dialog>
  </div>
</template>

<script setup name="Ourec" lang="ts">
import { listOurec, getOurec, delOurec, addOurec, updateOurec } from '@/api/mes/ourec';
import { OurecVO, OurecQuery, OurecForm } from '@/api/mes/ourec/types';
import { listStock, getStock, delStock, addStock, updateStock } from '@/api/mes/stock';
import { StockVO, StockQuery, StockForm } from '@/api/mes/stock/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
import { Search } from '@element-plus/icons-vue'
const ourecList = ref<OurecVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const prolistadd = ref(false);
const stockList = ref<StockVO[]>([]);
const stid = ref(0)
const showtitle = ref('');
const torder=ref('');
const queryFormRef = ref<ElFormInstance>();
const ourecFormRef = ref<ElFormInstance>();
const queryParams2 = ref<any>({
  pageNum: 1,
  pageSize: 10,
  tilte: undefined,
  warnnum: undefined,
  type: undefined,
  iid: undefined,
  sn: undefined,
  stockid: undefined,
  instock: undefined,
  outstock: undefined,
  nowstock: undefined,
  unitid: undefined,
  params: {
  }
})
const total2 = ref(0);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});
const dialog5 = reactive<DialogOption>({
  visible: false,
  title: ''
});
const handleQuery2 = () => {
  queryParams2.value.pageNum = 1;
  getList2();
}
const resetQuery2 = () => {
  queryFormRef.value?.resetFields();
  handleQuery2();
}
const initFormData: OurecForm = {
  id: undefined,
  stockid: undefined,
  sn: undefined,
  custId: undefined,
  nprice: undefined,
  stocktypeid: undefined,
  shuidian: undefined,
  met: undefined,
  count: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<OurecForm, OurecQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    stockid: undefined,
    sn: undefined,
    custId: undefined,
    nprice: undefined,
    stocktypeid: undefined,
    shuidian: undefined,
    met: undefined,
    count: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
  }
});
interface Product {
  id: string
  name: string
  amount1: string
  amount2: string
  amount3: number
  custids: number
}
const choosePro = () => {
  prolistadd.value = !prolistadd.value
}
const productlist = ref<any[]>([])
const submit2 = (row: any) => {
  stid.value = row.id
  productlist.value.push(row)
  showtitle.value = row.tilte
  prolistadd.value = !prolistadd.value
}
const { queryParams, form, rules } = toRefs(data);

/** 查询出库记录列表 */
const getList = async () => {
  loading.value = true;
  const res = await listOurec(queryParams.value);
  ourecList.value = res.rows;
  total.value = res.total;
  loading.value = false;
  productlist.value = []
}
const handleDelete2 = (row: any) => {
  const index = productlist.value.findIndex(item => item === row);
  productlist.value.splice(index, 1)
}
/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  ourecFormRef.value?.resetFields();
  productlist.value = []
}
const getList2 = async () => {
  const res = await listStock(queryParams2.value);
  stockList.value = res.rows;
  total2.value = res.total;
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
const handleSelectionChange = (selection: OurecVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加出库记录";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: OurecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOurec(_id);
  Object.assign(form.value, res.data);
  form.value.status = '1';
  let rec = await updateOurec(form.value).finally(() => buttonLoading.value = false);
  console.log(rec)
  await getList();
}
const handleUpdate2 = async (row?: OurecVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOurec(_id);
  Object.assign(form.value, res.data);
  torder.value=form.value.orderid
  dialog5.visible=true;
}
const change=async()=> {
  form.value.orderid=torder.value
  let rec = await updateOurec(form.value).finally(() => buttonLoading.value = false);
  console.log(rec)
  await getList();
}
/** 提交按钮 */
const submitForm = async () => {
  form.value.stockid = stid.value
  form.value.status = '0'
  productlist.value.forEach(async (items) => {
    form.value.stockid = items.id
    form.value.status = '0'
    form.value.count = items.count
    form.value.remark = items.remark
    form.value.nprice = items.price
    form.value.orderid = items.orderid
    await addOurec(form.value).finally(() => buttonLoading.value = false);
  })
  proxy?.$modal.msgSuccess("操作成功");
  dialog.visible = false;
  await getList();
  // ourecFormRef.value?.validate(async (valid: boolean) => {
  //   if (valid) {
  //     buttonLoading.value = true;
  //     if (form.value.id) {

  //     } else {
  //       await addOurec(form.value).finally(() => buttonLoading.value = false);
  //     }

  //   }
  // });
}

/** 删除按钮操作 */
const handleDelete = async (row?: OurecVO) => {
  const _id = row?.id || ids.value[0]
  const res = await getOurec(_id);
  Object.assign(form.value, res.data);
  form.value.status = '2';
  await updateOurec(form.value).finally(() => buttonLoading.value = false);
  await getList();
}
const handleDelete3 = async (row?: OurecVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除销售订单编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delOurec(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/ourec/export', {
    ...queryParams.value
  }, `ourec_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getList2();
});
</script>
