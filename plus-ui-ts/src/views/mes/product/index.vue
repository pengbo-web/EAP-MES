<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div class="search" v-show="showSearch">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
          <el-form-item label="编码" prop="sn">
            <el-input v-model="queryParams.sn" placeholder="请输入编码" clearable style="width: 240px"
              @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="标题" prop="title">
            <el-input v-model="queryParams.title" placeholder="请输入标题" clearable style="width: 240px"
              @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="需求数量" prop="needCount">
            <el-input v-model="queryParams.needCount" placeholder="请输入需求数量" clearable style="width: 240px"
              @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
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
      <div> <el-tag size="medium" type="error" class="mb1">第三部产品录入**</el-tag></div>
      <div> <el-tag size="medium" type="error" class="mb1">此处为重要数据，必填。自制件会生成生产任务，采购件根据MRP生成采购任务</el-tag></div>
    </el-card>
    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd()"
              v-hasPermi="['mes:product:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:workplace:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" plain icon="Sort" @click="handleToggleExpandAll">展开/折叠</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button icon="Download" @click="importTemplate">下载模板</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button icon="Top" @click="handleImport"> Bom导入</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button icon="Download" @click="handleExport"> 导出数据</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" :data="productList" row-key="id" :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" ref="productTableRef"
        @selection-change="handleSelectionChange" border style="width: 100%"
        :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'font-size': '12px' }"
        :row-style="{ height: '50px' }" lazy>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="名称" align="left" prop="title" width="150" />
        <el-table-column label="编码(图号)" prop="sn" width="60" />
        <el-table-column label="Bom" align="center" prop="tilte" >
            <template #default="scope">
              <el-button link type="primary" size="small" @click="showbom(scope.row)">查看Bom</el-button>
            </template>
          </el-table-column>
        <el-table-column label="单位" align="center" prop="unit" width="60" />
        <el-table-column label="品类" align="center" prop="typename" />
        <el-table-column label="组成件" align="center" prop="pidname" />
        <!-- <el-table-column label="最新价格" align="center" prop="bcount" >
          <template #default="scope">
            <el-text class="mx-1" type="danger">
            {{ scope.row.bcount ? scope.row.bcount + '元' : ''}}</el-text>
          </template>
        </el-table-column> -->
        <el-table-column label="类型" align="center" prop="mustbe">
          <template #default="scope">
            <dict-tag :options="markpro" :value="scope.row.mustbe" />
          </template>
        </el-table-column>
        <el-table-column label="版本" align="center" prop="ver" />
        <el-table-column label="单体重量" align="center" prop="weight" />
        <el-table-column label="需求数量" align="center" prop="needCount" />
        <el-table-column label="总需求" align="center" prop="ver">
          <template #default="scope">
            {{ scope.row.needCount ? (scope.row.needCount * scope.row.weight) : '' }}
          </template>
        </el-table-column>
        <el-table-column label="库存余量" align="center" prop="stock">
          <template #default="scope">
            {{ scope.row.stock ? scope.row.stock + scope.row.stockname : '' }}
          </template>
        </el-table-column>
        <el-table-column label="条形码" align="center" prop="id" width="273">
          <template #default="scope">
            <div class="flex items-center">
              <printstj :barcodeValue="scope.row.id" />
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['mes:product:edit']" />
            </el-tooltip>
            <el-tooltip content="新增" placement="top">
              <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                v-hasPermi="['mes:product:add']" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:product:remove']" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <!-- 添加或修改③产品管理对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="900px" append-to-body>
      <el-form ref="productFormRef" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="编码(图号)" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入编码">
            <template #append>
              <el-button icon="Search" @click="handleSearch">查询仓库</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="使用单位" prop="unit">
          <el-select v-model="form.unitId" placeholder="单位" clearable>
            <el-option v-for="dict in unilist" :key="dict.id" :label="dict.title" :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="单体重量" prop="unit">
          <el-input v-model="form.weight" placeholder="请输入重量" />
        </el-form-item>
        <el-form-item label="是否自制件" prop="mustbe">
          <el-select v-model="form.mustbe" placeholder="自制件" clearable>
            <el-option v-for="dict in markpro" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="组成件" prop="pid">
          <el-tree-select v-model="form.pid" :data="productOptions"
            :props="{ value: 'id', label: 'title', children: 'children' }" value-key="id" placeholder="请选择组成件"
            check-strictly />
        </el-form-item>
        <el-form-item label="版本" prop="ver">
          <el-input v-model="form.ver" placeholder="请输入版本" />
        </el-form-item>
        <el-form-item label="类别" prop="protypeId">
          <el-tree-select v-model="form.protypeId" :data="protypeList"
            :props="{ value: 'id', label: 'title', children: 'children' }" value-key="id" placeholder="请选择类别"
            check-strictly />
        </el-form-item>
        <el-form-item label="图片" prop="imgs">
          <image-upload v-model="form.imgs" />
        </el-form-item>
        <el-form-item label="需求数量" prop="needCount">
          <el-input v-model="form.needCount" placeholder="请输入需求数量" />
        </el-form-item>
        <el-form-item label="与主单位进率" prop="grow">
          <el-input v-model="form.grow" placeholder="与主单位进率" />
        </el-form-item>
        <el-form-item label="库存量" prop="stock">
          <el-input v-model="form.stock" placeholder="请输入需求数量" />
        </el-form-item>
        <el-form-item label="库存单位" prop="stockId">
          <el-select v-model="form.stockId" placeholder="库存单位" clearable>
            <el-option v-for="dict in unilist" :key="dict.id" :label="dict.title" :value="dict.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
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

    <!-- 用户导入对话框 -->
    <el-dialog v-model="upload.open" :title="upload.title" width="400px" append-to-body>
      <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <el-icon class="el-icon--upload">
          <i-ep-upload-filled />
        </el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="text-center el-upload__tip">
            <div class="el-upload__tip"><el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据</div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate">下载模板</el-link>
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
    <el-dialog :title="dialog2.title" v-model="dialog2.visible" append-to-body fullscreen style="width: 800px">
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
              <el-button type="primary" size="small" @click="submit2(scope.row)">确认</el-button>
            </template>
          </el-table-column>
          <el-table-column label="主键" align="center" prop="id" v-if="false" />
          <el-table-column label="货架号" align="center" prop="stockname" />
          <el-table-column label="编码" align="center" prop="sn" />
          <el-table-column label="名称" align="center" prop="tilte" />
   
          <el-table-column label="规格" align="center" prop="gk" />
          <el-table-column label="品类" align="center" prop="otype" />
          <el-table-column label="完成度" align="center" prop="type">
            <template #default="scope">
              <dict-tag :options="stocktype" :value="scope.row.type" />
            </template>
          </el-table-column>
          <el-table-column label="图号" align="center" prop="iid" />
          <el-table-column label="当前库存" align="center" prop="nowstock" />
          <el-table-column label="单位" align="center" prop="unitname" />
          <el-table-column label="报警库存" align="center" prop="warnnum" />
          <!-- <el-table-column label="库存信息" align="center" prop="stockid" /> -->
          <el-table-column label="是否在用" align="center" prop="status">
            <template #default="scope">
              <dict-tag :options="diyisno" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="入库批次" align="center" prop="pcount" />
        </el-table>

        <pagination v-show="total2 > 0" :total="total2" v-model:page="queryParams2.pageNum"
          v-model:limit="queryParams2.pageSize" @pagination="getList" />
      </el-card>
    </el-dialog>
    <el-dialog v-model="bomdialog.visible"  title="BOM" width="50%" append-to-body>
      <el-table v-loading="loading" :data="productList2" row-key="id" :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" ref="productTableRef"
        @selection-change="handleSelectionChange" border style="width: 100%"
        :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'font-size': '12px' }"
        :row-style="{ height: '50px' }" lazy>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="名称" align="left" prop="title" width="150" />
        <el-table-column label="编码(图号)" prop="sn" width="60" />
        <el-table-column label="单位" align="center" prop="unit" width="60" />
        <el-table-column label="品类" align="center" prop="typename" />
        <el-table-column label="组成件" align="center" prop="pidname" />
        <!-- <el-table-column label="最新价格" align="center" prop="bcount" >
          <template #default="scope">
            <el-text class="mx-1" type="danger">
            {{ scope.row.bcount ? scope.row.bcount + '元' : ''}}</el-text>
          </template>
        </el-table-column> -->
        <el-table-column label="类型" align="center" prop="mustbe">
          <template #default="scope">
            <dict-tag :options="markpro" :value="scope.row.mustbe" />
          </template>
        </el-table-column>
        <el-table-column label="版本" align="center" prop="ver" />
        <el-table-column label="单体重量" align="center" prop="weight" />
        <el-table-column label="需求数量" align="center" prop="needCount" />
        <el-table-column label="总需求" align="center" prop="ver">
          <template #default="scope">
            {{ scope.row.needCount ? (scope.row.needCount * scope.row.weight) : '' }}
          </template>
        </el-table-column>
        <el-table-column label="库存余量" align="center" prop="stock">
          <template #default="scope">
            {{ scope.row.stock ? scope.row.stock + scope.row.stockname : '' }}
          </template>
        </el-table-column>
        <el-table-column label="条形码" align="center" prop="id" width="273">
          <template #default="scope">
            <div class="flex items-center">
              <printstj :barcodeValue="scope.row.id" />
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['mes:product:edit']" />
            </el-tooltip>
            <el-tooltip content="新增" placement="top">
              <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                v-hasPermi="['mes:product:add']" />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:product:remove']" />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup name="Product" lang="ts">
import { listProduct, listProducts, getProduct, delProduct, addProduct, updateProduct,getbom} from "@/api/mes/product";
import { listProtype } from "@/api/mes/protype";
import { listUnitmeasure } from '@/api/mes/unitmeasure';
import { UnitmeasureVO } from '@/api/mes/unitmeasure/types';
import { ProductVO, ProductQuery, ProductForm } from '@/api/mes/product/types';
import { ProtypeVO } from '@/api/mes/protype/types';
import Printstj from './pri.vue';
type ProductOption = {
  id: number;
  title: string;
  children?: ProductOption[];
}
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
import { listStock, getStock, delStock, addStock, updateStock } from '@/api/mes/stock';
import { StockVO, StockQuery, StockForm } from '@/api/mes/stock/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;;
const total = ref(0);
const total2 = ref(0);
const { sys_normal_disable, diyisno, markpro, stocktype } = toRefs<any>(proxy?.useDict('sys_normal_disable', 'diyisno', 'markpro', 'stocktype'));
const protypeList = ref<ProtypeVO[]>([]);
const productList = ref<ProductVO[]>([]);
const unilist = ref<UnitmeasureVO[]>([]);
const productOptions = ref<ProductOption[]>([]);
const buttonLoading = ref(false);
const showSearch = ref(true);
const isExpandAll = ref(false);
const loading = ref(false);
import { globalHeaders } from '@/utils/request';
const queryFormRef = ref<ElFormInstance>();
const productFormRef = ref<ElFormInstance>();
const stockList = ref<StockVO[]>([]);
const productTableRef = ref<ElTableInstance>()
const uploadRef = ref<ElUploadInstance>();
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});
const dialog2 = reactive<DialogOption>({
  visible: false,
  title: ''
});
const bomdialog = reactive<DialogOption>({
  visible: false,
  title: ''
});
const productList2 = ref<ProductVO[]>([]);
const showbom=async(item:any)=>{
  bomdialog.title = '查看Bom';
  bomdialog.visible = true;
  const res = await getbom(item.id);
  
  const data = proxy?.handleTree<ProtypeVO>(res.data, "id", "pid");
    productList2.value = data;
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
  url: import.meta.env.VITE_APP_BASE_API + '/mes/product/importData'
});
const initFormData: ProductForm = {
  id: undefined,
  sn: undefined,
  title: undefined,
  unit: undefined,
  protypeId: undefined,
  bcount: 1,
  pid: undefined,
  ver: 0,
  needCount: 1,
  status: "0",
  remark: undefined,
  mustbe: '1',
  grow: 1
}
const handleSearch = () => {
  getList2();
  dialog2.visible = true;
}
const data = reactive<PageData<ProductForm, ProductQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    title: undefined,
    needCount: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    title: [
      { required: true, message: "标题不能为空", trigger: "blur" }
    ],
    protypeId: [
      { required: true, message: "类别不能为空", trigger: "change" }
    ],
    pid: [
      { required: true, message: "组成件不能为空", trigger: "blur" }
    ],
    ver: [
      { required: true, message: "版本不能为空", trigger: "blur" }
    ],
    needCount: [
      { required: true, message: "需求数量不能为空", trigger: "blur" }
    ],
  }
});
const getList2 = async () => {
  const res = await listStock(queryParams2.value);
  stockList.value = res.rows;
  total2.value = res.total;
}
const { queryParams, form, rules } = toRefs(data);

/** 查询③产品管理列表 */
const getList = async () => {
  loading.value = true;
  const res = await listProducts(queryParams.value);
  const data = proxy?.handleTree<ProductVO>(res.rows, "id", "pid");
  if (data) {
    productList.value = data;
    loading.value = false;
    total.value = res.total;
  }
}
/** 查询单位列表 */
const getuniList = async () => {
  const res = await listUnitmeasure(null);
  if (data) {
    unilist.value = res.rows;

  }
}
/** 导入按钮操作 */
const handleImport = () => {
  upload.title = '用户导入';
  upload.open = true;
};
const handleQuery2 = () => {
  // queryParams2.value.pageNum = 1;
  getList2();
}
const resetQuery2 = () => {
  queryFormRef2.value?.resetFields();
  handleQuery2();
}
/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'mes/product/export',
    {
      ...queryParams.value
    },
    `bom_${new Date().getTime()}.xlsx`
  );
};
/** 下载模板操作 */
const importTemplate = () => {
  proxy?.download('mes/product/importTemplate', {}, `bom_template_${new Date().getTime()}.xlsx`);
};
/**文件上传中处理 */
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
const submit2 = (row?: any) => {
  form.value.sn = row.sn;
  form.value.stocks = row.id;
  form.value.stockId = row.unitId;
  form.value.stock = row.nowstock;
  form.value.title = row.tilte;
  dialog2.visible = false
}
/** 多选框选中数据 */
const handleSelectionChange = (selection: ProductVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 提交上传文件 */
function submitFileForm() {
  uploadRef.value?.submit();
}
const para2=ref<any>({
  pageNum: 1,
  pageSize: 1000,
  sn: undefined,
  title: undefined,
  needCount: undefined,
  status: undefined,
})
/** 查询类别列表 */
const getTypeList = async () => {
  const res = await listProtype(para2.value);
  const data = proxy?.handleTree<ProtypeVO>(res.data, "id", "pid");
  if (data) {
    protypeList.value = data;
  }
}

/** 查询③产品管理下拉树结构 */
const getTreeselect = async () => {
  const res = await listProduct();
  productOptions.value = [];
  const data: ProductOption = { id: 0, title: '无', children: [] };
  data.children = proxy?.handleTree<ProductOption>(res.data, "id", "pid");
  productOptions.value.push(data);
}

// 取消按钮
const cancel = () => {
  reset();
  dialog.visible = false;
}

// 表单重置
const reset = () => {
  form.value = { ...initFormData }
  productFormRef.value?.resetFields();
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
const handleAdd = (row?: ProductVO) => {
  reset();
  getTreeselect();
  getTypeList();
  getuniList();
  if (row != null && row.id) {
    form.value.pid = row.id;
  } else {
    form.value.pid = 0;
  }
  dialog.visible = true;
  dialog.title = "添加③产品管理";
}

/** 展开/折叠操作 */
const handleToggleExpandAll = () => {
  isExpandAll.value = !isExpandAll.value;
  toggleExpandAll(productList.value, isExpandAll.value)
}

/** 展开/折叠操作 */
const toggleExpandAll = (data: ProductVO[], status: boolean) => {
  data.forEach((item) => {
    productTableRef.value?.toggleRowExpansion(item, status)
    if (item.children && item.children.length > 0) toggleExpandAll(item.children, status)
  })
}

/** 修改按钮操作 */
const handleUpdate = async (row: ProductVO) => {
  reset();
  getuniList();
  getTypeList();

  await getTreeselect();
  if (row != null) {
    form.value.pid = row.pid;
  }
  const res = await getProduct(row.id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改③产品管理";
}

/** 提交按钮 */
const submitForm = () => {
  productFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;

      if (form.value.id) {
        await updateProduct(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addProduct(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: ProductVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除产品编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  loading.value = true;
  await delProduct(_ids).finally(() => loading.value = false);
  await getList();

  proxy?.$modal.msgSuccess("删除成功");
}

onMounted(() => {
  getList();
});
</script>
<style lang="scss" >
tr.el-table__row.el-table__row--level-1 {
  color: #171819;
  background-color: #ccccccb0;
}
tr.el-table__row.el-table__row--level-2 {
    background-color: #e8e8e8;
}
</style>
