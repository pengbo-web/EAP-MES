<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="名称" prop="tilte">
              <el-input v-model="queryParams.tilte" placeholder="请输入名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="报警" prop="warnnum">
              <el-input v-model="queryParams.warnnum" placeholder="请输入报警" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="类型" prop="type">
              <el-select v-model="queryParams.type" placeholder="请选择类型" clearable >
                <el-option v-for="dict in stocktype" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
            <el-form-item label="图号" prop="iid">
              <el-input v-model="queryParams.iid" placeholder="请输入图号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="库存编码" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入库存编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="库存信息" prop="stockid">
              <el-input v-model="queryParams.stockid" placeholder="请输入库存信息" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="总入库" prop="instock">
              <el-input v-model="queryParams.instock" placeholder="请输入总入库" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="总出库" prop="outstock">
              <el-input v-model="queryParams.outstock" placeholder="请输入总出库" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="当前库存" prop="nowstock">
              <el-input v-model="queryParams.nowstock" placeholder="请输入当前库存" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="规格" prop="unitid">
              <el-input v-model="queryParams.unitid" placeholder="请输入规格" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:stock:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['mes:stock:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['mes:stock:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['mes:stock:export']">导出</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Download" @click="handleImport" v-hasPermi="['mes:cutting:export']">批量导入</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="stockList" @selection-change="handleSelectionChange"
      border
          style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center','font-size':'12px' }"
          :row-style="{ height: '50px' }"
          lazy
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="货架号" align="center" prop="stockname" />
        <el-table-column label="编码" align="center" prop="sn" >
          <template #default="scope">
            <el-text @click="handleDetail(scope.row)" type="primary">{{ scope.row.sn }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="名称" align="center" prop="tilte" />
        <el-table-column label="规格" align="center" prop="gk" />
        <el-table-column label="品类" align="center" prop="otype" />
        <el-table-column label="类型" align="center" prop="type">
          <template #default="scope">
            <dict-tag :options="stocktype" :value="scope.row.type"/>
          </template>
        </el-table-column>
        <el-table-column label="图号" align="center" prop="iid" />
        <el-table-column label="当前库存" align="center" prop="nowstock"  />
        <el-table-column label="单位" align="center" prop="unitname" />
        <el-table-column label="报警库存" align="center" prop="warnnum" />
        <!-- <el-table-column label="库存信息" align="center" prop="stockid" /> -->
        <el-table-column label="是否在用" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="diyisno" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="总入库" align="center" prop="instock" />
        <el-table-column label="总出库" align="center" prop="outstock" />
        <el-table-column label="入库批次" align="center" prop="pcount" />
        <el-table-column label="供应商" align="center" prop="custname" />
        <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:stock:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:stock:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <el-dialog v-model="upload.open" :title="upload.title" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload">
          <i-ep-upload-filled />
        </el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="text-center el-upload__tip">
            <div class="el-upload__tip"><el-checkbox v-model="upload.updateSupport" />是否更新已经存在的图号产品数据</div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 添加或修改仓库信息对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="stockFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编码" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="名称" prop="tilte">
          <el-input v-model="form.tilte" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="图号" prop="iid">
          <el-input v-model="form.iid" placeholder="请输入图号" />
        </el-form-item>
        <el-form-item label="品类" prop="otype">
          <el-input v-model="form.otype" placeholder="请输入品类" />
          <el-text type="danger">此品类关乎MRP计算，可填写为：委外、成品、原材料</el-text>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio
              v-for="dict in stocktype"
              :key="dict.value"
              :value="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="货架码" prop="stockid">
          <el-tree-select v-model="form.stockid" :data="stockTypeList" :props="{ value: 'id', label: 'sn' }" clearable />
        </el-form-item>
        <el-form-item label="当前库存" prop="nowstock">
          <el-input v-model="form.nowstock" placeholder="请输入当前库存" type="number"/>
        </el-form-item>
        <el-form-item label="规格" prop="gk">
          <el-input v-model="form.gk" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="单位" prop="unitid">
          <el-select v-model="form.unitid" placeholder="单位" clearable >
                <el-option v-for="dict in unilist" :key="dict.id" :label="dict.title" :value="dict.id" />
              </el-select>
        </el-form-item>
        <el-form-item label="报警库存" prop="warnnum">
          <el-input v-model="form.warnnum" placeholder="请输入报警" type="number"/>
        </el-form-item>
        <el-form-item label="供应商" prop="custid">
          <el-select v-model="form.custid" placeholder="供应商" clearable multiple  filterable>
            <el-option v-for="(item,index) in customList" :key="index" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否在用" prop="status">
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
    <el-dialog title="出入库信息" v-model="detailshow" width="500px" append-to-body>
      <el-table :data="chulist1" style="width: 100%" border show-summary>
        <el-table-column prop="remark" label="出库信息" width="180" />
        <el-table-column prop="instock" label="出库数量" width="180" />
        <el-table-column prop="createtime" label="出库时间" width="180" />
      </el-table>
      <el-divider />
      <el-table :data="rulist1" style="width: 100%" border show-summary>
        <el-table-column prop="remark" label="入库信息" width="180" />
        <el-table-column prop="instock" label="入库数量" width="180" />
        <el-table-column prop="createtime" label="入库时间" width="180" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup name="Stock" lang="ts">
import { listStock, getStock, delStock, addStock, updateStock,chulist,rulist } from '@/api/mes/stock';
import { StockVO, StockQuery, StockForm } from '@/api/mes/stock/types';
import { listStockType, getStockType, delStockType, addStockType, updateStockType } from "@/api/mes/stockType";
import { StockTypeVO, StockTypeQuery, StockTypeForm } from '@/api/mes/stockType/types';
import { listCustom, getCustom, delCustom, addCustom, updateCustom } from '@/api/mes/custom';
import { CustomVO, CustomQuery, CustomForm } from '@/api/mes/custom/types';
import { globalHeaders } from '@/utils/request';
import { listUnitmeasure } from '@/api/mes/unitmeasure';
import { UnitmeasureVO } from '@/api/mes/unitmeasure/types';

const uploadRef = ref<ElUploadInstance>();
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { diyisno, stocktype } = toRefs<any>(proxy?.useDict('diyisno', 'stocktype'));
const stockTypeList = ref<StockTypeVO[]>([]);
const stockList = ref<StockVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const detailshow = ref(false);
const total = ref(0);
const chulist1 = ref<StockVO[]>([]);
const rulist1 = ref<StockVO[]>([]);
const unilist = ref<UnitmeasureVO[]>([]);
const queryFormRef = ref<ElFormInstance>();
const stockFormRef = ref<ElFormInstance>();
  const customList = ref<CustomVO[]>([]);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: StockForm = {
  id: undefined,
  tilte: undefined,
  warnnum: undefined,
  type: undefined,
  iid: undefined,
  sn: undefined,
  stockid: undefined,
  instock: 0,
  outstock: 0,
  nowstock: 0,
  unitid: undefined,
  proid: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<StockForm, StockQuery>>({
  form: {...initFormData},
  queryParams: {
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
  },
  rules: {
    unitid: [
      { required: true, message: "单位不能为空", trigger: "blur" }
    ],
    tilte: [
      { required: true, message: "名称不能为空", trigger: "blur" }
    ],
    sn: [
      { required: true, message: "编号不能为空", trigger: "blur" }
    ],
    otype: [
      { required: true, message: "类型不能为空", trigger: "blur" }
    ]
  }
});
const handleDetail=async (row:any)=>{
  let params={
    'pageSize': 100,
    'pageNum': 1,
  }
  let res=await chulist(row.id,params)
  detailshow.value=true
  chulist1.value=res
  let res1=await rulist(row.id,params)
  rulist1.value=res1
}
const handleFileUploadProgress = () => {
  upload.isUploading = true;
};
/** 文件上传成功处理 */
const handleFileSuccess = (response: any, file: UploadFile) => {
  upload.open = false;
  upload.isUploading = false;
  uploadRef.value?.handleRemove(file);
  ElMessageBox.alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + '</div>', '导入结果', {
    dangerouslyUseHTMLString: true
  });
  getList();
};
const queryParams2=ref<any>({
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
  const queryParams3=ref<any>({
    pageNum: 1,
    pageSize: 500,
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
const { queryParams, form, rules } = toRefs(data);

/** 查询仓库信息列表 */
const getList = async () => {
  loading.value = true;
  const res = await listStock(queryParams.value);
  stockList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}
/** 查询单位列表 */
const getuniList = async () => {
  const res = await listUnitmeasure(null);
  if (data) {
    unilist.value = res.rows;

  }
}
/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  stockFormRef.value?.resetFields();
}
/*** 用户导入参数 */
const upload = reactive<ImportOption>({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: '',
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: globalHeaders(),
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + '/mes/stock/importData'
});
/** 导入按钮操作 */
const handleImport = () => {
  upload.title = '批量导入';
  upload.open = true;
};
/** 下载模板操作 */
const importTemplate = () => {
  proxy?.download('mes/stock/importTemplate', {}, `stock_template_${new Date().getTime()}.xlsx`);
};
/** 提交上传文件 */
function submitFileForm() {
  uploadRef.value?.submit();
}


const getList3 = async () => {
  const res = await listCustom(queryParams3.value);
  customList.value = res.rows;
  total.value = res.total;
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}
/** 查询库存分类列表 */
const getList2 = async () => {
  const res = await listStockType(queryParams.value);
  const data = proxy?.handleTree<StockTypeVO>(res.data, "id", "pid");
  if (data) {
    stockTypeList.value = data;
  }
}
/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: StockVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加仓库信息";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: StockVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getStock(_id);
  Object.assign(form.value, res.data);
  if(form.value.custid){
    form.value.custid=res.data.custid.split(",");
  }
  dialog.visible = true;
  dialog.title = "修改仓库信息";
  
}

/** 提交按钮 */
const submitForm = () => {
  stockFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if(form.value.custid){
        form.value.custid=form.value.custid.join(",");
      }
      console.log('kkkkk')
      if (form.value.id) {
        await updateStock(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addStock(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: StockVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除数据项？').finally(() => loading.value = false);
  await delStock(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/stock/export', {
    ...queryParams.value
  }, `stock_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getuniList();
  getList2();
  getList3();
});
</script>
