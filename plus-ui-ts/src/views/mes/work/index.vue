<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="编码" prop="sn">
              <el-input v-model="queryParams.sn" placeholder="请输入编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="产品关联" prop="proId">
              <el-input v-model="queryParams.proId" placeholder="请输入产品关联" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="所需数量" prop="neednum">
              <el-input v-model="queryParams.neednum" placeholder="请输入所需数量" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否加急" prop="istop">
              <el-select v-model="queryParams.istop" placeholder="请选择是否加急" clearable>
                <el-option v-for="dict in diyisno" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="需求日期" style="width: 308px">
              <el-date-picker v-model="dateRangeEndtime" value-format="YYYY-MM-DD HH:mm:ss" type="daterange"
                range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['mes:work:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['mes:work:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['mes:work:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['mes:work:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="workList" @selection-change="handleSelectionChange"
      style="width: 100%"
          :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center','font-size':'12px' }"
          :row-style="{ height: '50px' }"
          lazy border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="打印" align="center" prop="id" >
          <template #default="scope">
            <el-button type="text" @click="printhi(scope.row)">打印</el-button>
          </template>
        </el-table-column>
        <el-table-column label="打印" align="center" prop="id" v-if="false" />
        <el-table-column label="流转码" align="center" prop="sn" />
        <el-table-column label="计划名称" align="center" prop="title" />
        <el-table-column label="面向类型" align="center" prop="fromtype" >
          <template #default="scope">
            <dict-tag :options="diy_mark" :value="scope.row.fromtype" />
          </template>
        </el-table-column>
        <el-table-column label="物料" align="center" prop="proId" >
          <template #default="scope">
           {{ scope.row.proinfo?scope.row.proinfo.sn+scope.row.proinfo.title:'' }}
          </template>
        </el-table-column>
        <el-table-column label="所需数量" align="center" prop="neednum" />
        <el-table-column label="当前数量" align="center" prop="currnum" />
        <el-table-column label="所有工序" align="center" prop="processcount" />
        <el-table-column label="当前工序" align="center" prop="currprocess" />
        <el-table-column label="进度" align="center"  >
          <template #default="scope">
            <el-progress :text-inside="true" status="success" :stroke-width="26" :percentage="(scope.row.okprocess/scope.row.processcount)*100" />
            </template>
        </el-table-column>
        <el-table-column label="质检次数" align="center" prop="checkcount" />
        <el-table-column label="是否加急" align="center" prop="istop">
          <template #default="scope">
            <dict-tag :options="diyisno" :value="scope.row.istop" />
          </template>
        </el-table-column>
        <el-table-column label="需求日期" align="center" prop="endtime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.endtime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="排程日期" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
       
        <el-table-column label="关联订单号" align="center" prop="orderid" />
        <el-table-column label="排产状态" align="center" prop="status" >
          <template #default="scope">
            <dict-tag :options="worktype" :value="scope.row.status" />
            </template>
            </el-table-column>
        <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['mes:work:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="暂停" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:work:remove']"></el-button>
            </el-tooltip>
            <el-tooltip content="加急" placement="top">
              <el-button link type="primary" icon="Top" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:work:remove']"></el-button>
            </el-tooltip>
            <el-tooltip content="暂停" placement="top">
              <el-button link type="primary" icon="VideoPause" @click="handleDelete(scope.row)"
                v-hasPermi="['mes:work:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改生产派单对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="980px" append-to-body>
      <el-form :ref="workFormRef" :model="form" :rules="rules" label-width="140px">
      
        <el-form-item label="排程名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item required label="加工物料" prop="proId">
          <el-input placeholder="请输入内容" v-model="protitle" class="input-with-select">
            <template #append>
              <el-button :icon="Search" @click="choosePro" />
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="面向类型" prop="neednum">
          <el-radio-group v-model="form.fromtype" disabled>
            <el-radio v-for="dict in diy_mark" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否流转" prop="neednum">
          <el-radio-group v-model="form.isturn" >
            <el-radio v-for="dict in isturnon" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所需数量" prop="neednum">

          <el-input-number v-model="form.neednum" placeholder="请输入所需数量" style="width: 200px!important;" />
        </el-form-item>
        <el-form-item label="是否加急" prop="istop">
          <el-radio-group v-model="form.istop">
            <el-radio v-for="dict in diyisno" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否与库存对比计算" prop="ismrp">
          <el-radio-group v-model="form.ismrp">
            <el-radio v-for="dict in diyisno" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item label="报工模式" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in worktype" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="需求日期" prop="endtime">
          <el-date-picker clearable v-model="form.endtime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择需求日期">
          </el-date-picker>
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
    <el-dialog v-model="prolistadd" title="产品列表" style="width: 80%;">
      <div class="p-2">
        <el-row :gutter="24">
          <!-- 部门树 -->
          <el-col :lg="8" :xs="24" style="" ref="ref1">
            <el-card shadow="hover">
              <el-tree ref="deptTreeRef" class="mt-2" node-key="id" :data="protypeOptions"
                :props="{ label: 'title', children: 'children' }" :expand-on-click-node="false" highlight-current
                default-expand-all @node-click="handleNodeClick" />
            </el-card>
          </el-col>
          <el-col :lg="16" :xs="24">
            <el-table v-loading="loading" :data="productList" row-key="id" :default-expand-all="true"
              :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" ref="productTableRef"
              @row-click="singleElection" highlight-current-row>
              <el-table-column label="">
                <template #default="scope">
                  <el-radio class="radio" v-model="templateSelection" :label="scope.row.id">&nbsp;</el-radio>
                </template>
              </el-table-column>
              <el-table-column label="编码" prop="sn" />
              <el-table-column label="标题" align="center" prop="title" />
              <el-table-column label="单位" align="center" prop="unit" />
              <el-table-column label="类别" align="center" prop="typename" />
              <el-table-column label="组成件" align="center" prop="pidname" />
              <el-table-column label="组成件需要本产品数量" align="center" prop="bcount" />
              <el-table-column label="版本" align="center" prop="ver" />
              <el-table-column label="单体重量" align="center" prop="weight" />
              <el-table-column label="总重量核算" align="center" prop="ver">
                <template #default="scope">
                  {{ scope.row.needCount ? (scope.row.needCount * scope.row.weight) + 'kg' : '' }}
                </template>
              </el-table-column>
              <el-table-column label="需求数量" align="center" prop="needCount" />
              <el-table-column label="备注" align="center" prop="remark" />
            </el-table>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="Work" lang="ts">
import { listWork, getWork, delWork, addWork, updateWork } from '@/api/mes/work';
import { WorkVO, WorkQuery, WorkForm } from '@/api/mes/work/types';
import { Search } from '@element-plus/icons-vue'
import '@/assets/public/print-lock.css'  
import { listProtype, getProtype, delProtype, addProtype, updateProtype } from "@/api/mes/protype";
import { ProtypeVO, ProtypeQuery, ProtypeForm } from '@/api/mes/protype/types';
import { listProduct, getProduct, delProduct, addProduct, updateProduct } from "@/api/mes/product";
import { ProductVO, ProductQuery, ProductForm } from '@/api/mes/product/types';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { diyisno, diy_mark,isturnon,worktype } = toRefs<any>(proxy?.useDict('diyisno', 'diy_mark','isturnon','worktype'));
type ProtypeOption = {
  id: number;
  title: string;
  children?: ProtypeOption[];
}
const workList = ref<WorkVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const prolistadd = ref(false);
const productList = ref<ProductVO[]>([]);
const dateRangeEndtime = ref<[DateModelType, DateModelType]>(['', '']);
const protypeOptions = ref<ProtypeOption[]>([]);
const queryFormRef = ref<ElFormInstance>();
const workFormRef = ref<ElFormInstance>();
const protitle = ref('');
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: WorkForm = {
  id: undefined,
  sn: undefined,
  title: undefined,
  fromtype: 0,
  proId: undefined,
  neednum: 1,
  currnum: undefined,
  processcount: undefined,
  checkcount: undefined,
  istop: 0,
  endtime: undefined,
  currprocess: undefined,
  orderid: undefined,
  remark: undefined,
  grow:1,
  ismrp:0,
  isturn:0,
  status: 0
}
const data = reactive<PageData<WorkForm, WorkQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sn: undefined,
    title: undefined,
    fromtype: undefined,
    proId: undefined,
    neednum: undefined,
    istop: undefined,
    params: {
      endtime: undefined,
    }
  },
  rules: {
    title: [
      { required: true, message: "标题不能为空", trigger: "blur" }
    ],
    neednum: [
      { required: true, message: "所需数量不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询生产派单列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.params = {};
  proxy?.addDateRange(queryParams.value, dateRangeEndtime.value, 'Endtime');
  const res = await listWork(queryParams.value);
  workList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}
/**
 *  Init hiPrint
 */
const printhi=(item)=>{
  hiprint.init;
  var hiprintTemplate = new hiprint.PrintTemplate();
  var panel = hiprintTemplate.addPrintPanel({ width: 100, height: 130, paperFooter: 340, paperHeader: 10 });
  //文本
  panel.addPrintText({ options: { width: 140, height: 15, top: 20, left: 20, title: '制造工艺流转卡', textAlign: 'center' } });
  //条形码
  panel.addPrintText({ options: { width: 210, height: 40, top: 40, left: 20, title: item.sn, textType: 'barcode' } });
  //二维码
  //Html
  panel.addPrintHtml({ options: { width: 140, height: 35, top: 87, left: 20, content:'<Hr>' } });
  // panel.addPrintText({ options: { width: 35, height: 35, top: 40, left: 165, title: '123456', textType: 'qrcode' } });
  //长文本
  panel.addPrintLongText({ options: { width: 180, height: 35, top: 100, left: 20,fontSize:10.5,color:'#ff0000', title: '请在终端进行扫码报工和质检工艺' } });
  panel.addPrintHtml({ options: { width: 140, height: 35, top: 110, left: 20, content:'<Hr>' } });
  panel.addPrintLongText({ options: { width: 180, height: 35, top: 130, left: 20,fontSize:10.5,color:'#ff0000', title: '产品名称：'+item.title } });
  panel.addPrintHtml({ options: { width: 140, height: 35, top: 150, left: 20, content:'<Hr>' } });
  panel.addPrintLongText({ options: { width: 180, height: 35, top: 170, left: 20,fontSize:10.5,color:'#ff0000', title: '产品图号：'+item.proinfo.sn } });
  panel.addPrintHtml({ options: { width: 140, height: 35, top: 190, left: 20, content:'<Hr>' } });
  panel.addPrintLongText({ options: { width: 180, height: 35, top: 210, left: 20,fontSize:10.5,color:'#ff0000', title: '产品数量：'+item.neednum } });
  panel.addPrintHtml({ options: { width: 140, height: 35, top: 230, left: 20, content:'<Hr>' } });
  panel.addPrintLongText({ options: { width: 180, height: 35, top: 250, left: 20,fontSize:10.5,color:'#ff0000', title: '排程时间：'+item.createTime } });
  //表格
  // panel.addPrintTable({ options: { width: 252, height: 35, top: 130, left: 20, content: $('#testTable').html() } });
  
  // //竖线//不设置宽度
  // panel.addPrintVline({ options: { height: 35, top: 230, left: 20 } });
  // //横线 //不设置高度
  // panel.addPrintHline({ options: { width: 140, top: 245, left: 120 } });
  // panel.addPrintHline({ options: { width: 140, top: 245, left: 130 } });
  // panel.addPrintHline({ options: { width: 140, top: 245, left: 140 } });
  // panel.addPrintHline({ options: { width: 140, top: 245, left: 150 } });
  // panel.addPrintHline({ options: { width: 140, top: 245, left: 160 } });
  // panel.addPrintHline({ options: { width: 135, top: 30, left: 20,borderColor:'red',borderWidth:0.75 } });
  // panel.addPrintHline({ options: { width: 135, top: 30, left: 120,borderColor:'#000',borderWidth:0.75 } });
  // //矩形
  // panel.addPrintRect({ options: { width: 35, height: 35, top: 230, left: 60 } });
  //打印设计
  hiprintTemplate.print({});
}
/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}
const templateSelection = ref('');
const checkList = ref([]);
const proid = ref(0);
const singleElection = (row: any) => {
  templateSelection.value = row.id
  checkList.value = productList.value.filter((item) => item.id === row.id)
  proid.value = row.id;
  form.value.proId = row.id
  protitle.value = row.title
  prolistadd.value = !prolistadd.value
};

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  workFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  dateRangeEndtime.value = ['', ''];
  queryFormRef.value?.resetFields();
  handleQuery();
}

const getTreeselect = async () => {
  const res = await listProtype();
  protypeOptions.value = [];
  const data: ProtypeOption = { id: 0, title: '全部', children: [] };
  data.children = proxy?.handleTree<ProtypeOption>(res.data, "id", "pid");
  protypeOptions.value.push(data);
}
/** 多选框选中数据 */
const handleSelectionChange = (selection: WorkVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  getListpro();
  reset();
  dialog.visible = true;
  dialog.title = "添加生产派单";
}
const choosePro = () => {
  prolistadd.value = !prolistadd.value
}
/** 修改按钮操作 */
const handleUpdate = async (row?: WorkVO) => {
  reset();
  getListpro();
  const _id = row?.id || ids.value[0]
  const res = await getWork(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改生产派单";
}

/** 提交按钮 */
const submitForm = async () => {
  console.log(form.value);
  if (form.value.proId == undefined) {
    proxy?.$modal.msgError("请选择产品");
    return;
  }
  if (form.value.title == undefined) {
    proxy?.$modal.msgError("请输入标题");
    return;
  }

  buttonLoading.value = true;
  if (form.value.id) {
    await updateWork(form.value).finally(() => buttonLoading.value = false);
  } else {
    await addWork(form.value).finally(() => buttonLoading.value = false);
  }
  proxy?.$modal.msgSuccess("操作成功");
  dialog.visible = false;
  await getList();

}
const proqueryParams = reactive({
  sn: undefined,
  title: undefined,
  needCount: undefined,
  status: undefined,
  protypeId: undefined,
  params: {
  }
})
const handleNodeClick = (data: ProductVO) => {
  proqueryParams.protypeId = data.id;
  getListpro();
}
const getListpro = async () => {
  loading.value = true;
  const res = await listProduct(proqueryParams);
  if (data) {
    productList.value = res.data;
    loading.value = false;
  }
}
/** 删除按钮操作 */
const handleDelete = async (row?: WorkVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除生产派单编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delWork(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/work/export', {
    ...queryParams.value
  }, `work_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getTreeselect();
});
</script>
