<template>
    <div class="p-2">
      <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
        :leave-active-class="proxy?.animate.searchAnimate.leave">
        <div v-show="showSearch" class="mb-[10px]">
          <el-card shadow="hover">
            <el-form ref="queryFormRef" :model="queryParams" :inline="true">
              <el-form-item label="委外名称" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入委外名称" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="订单号" prop="order">
                <el-input v-model="queryParams.order" placeholder="请输入订单号" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="外部编号" prop="sn">
                <el-input v-model="queryParams.sn" placeholder="请输入外部编号" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="备注" prop="stockids">
                <el-input v-model="queryParams.remark" placeholder="请输入备注" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="价格" prop="prices">
                <el-input v-model="queryParams.prices" placeholder="请输入价格" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="要求完成日期" style="width: 308px">
                <el-date-picker v-model="dateRangeSubtime" value-format="YYYY-MM-DD HH:mm:ss" type="daterange"
                  range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                  :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]" />
              </el-form-item>
              <el-form-item label="出库时间" style="width: 308px">
                <el-date-picker v-model="dateRangeOuttime" value-format="YYYY-MM-DD HH:mm:ss" type="daterange"
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
              <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
                v-hasPermi="['mes:outorder:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
                v-hasPermi="['mes:outorder:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="Download" @click="handleExport"
                v-hasPermi="['mes:outorder:export']">导出</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>
        </template>
  
        <el-table v-loading="loading" :data="outorderList" >
          <el-table-column label="入库" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="入库" placement="top">
                <el-button plain type="primary" icon="Edit" size="small" style="font-size: 10px;" @click="handleUpdate(scope.row)"
                  v-hasPermi="['mes:outorder:edit']" v-if="scope.row.status === '0'">外协入库</el-button>
                  <el-text type="danger" class="mr-8" v-if="scope.row.status === '1'">已入库</el-text>
              </el-tooltip>
              <!-- <el-tooltip content="删除" placement="top">
                <el-button  type="danger" icon="Delete" size="small" style="font-size: 10px;" @click="handleDelete(scope.row)"
                  v-hasPermi="['mes:outorder:remove']">删除</el-button>
              </el-tooltip> -->
            </template>
          </el-table-column>
          <el-table-column label="委外名称" align="center" prop="title" />
          <el-table-column label="委外商家" align="center" prop="cname" />
          <el-table-column label="订单号" align="center" prop="orders" />
          <el-table-column label="外部编号" align="center" prop="sn" />
          <el-table-column label="委外物料清单" align="center" prop="stonames" />
          <el-table-column label="出库库位" align="center" prop="stocktype" />
          <el-table-column label="价格" align="center" prop="prices" />
          <el-table-column label="要求完成日期" align="center" prop="subtime" width="180">
            <template #default="scope">
              <span>{{ parseTime(scope.row.subtime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="出库时间" align="center" prop="outtime" width="180">
            <template #default="scope">
              <span>{{ parseTime(scope.row.outtime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="是否带料" align="center" prop="status">
            <template #default="scope">
              <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">{{ scope.row.status === 0 ? '是' : '否'
                }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="入库物料" align="center" prop="stoname" />
          <el-table-column label="入库数量" align="center" prop="counts" />
          <el-table-column label="备注" align="center" prop="remark" />
          
        </el-table>
  
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize" @pagination="getList" />
      </el-card>
      <el-dialog :title="dialog2.title" v-model="dialog2.visible" width="500px" append-to-body>
        <el-form ref="orderFormRef" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="物料" prop="title">
                  <el-input v-model="runames" placeholder="请输入委外名称" /> <el-button type="primary" icon="Search" size="small" @click="findstock2">选择</el-button>
                </el-form-item>
                <el-form-item label="数量" prop="orders">
                  <el-input-number v-model="form.counts" placeholder="请输入数量" />
                </el-form-item>
                </el-form>
                <template #footer>
              <div class="dialog-footer">
                <el-button :loading="buttonLoading" type="primary" @click="submitForm3">录 入</el-button>
              </div>
            </template>
      </el-dialog>
      <!-- 添加或修改委外订单对话框 -->
      <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
        <el-card shadow="never">
          <template #header>
            <el-form ref="queryFormRef" :model="params2" :inline="true">
              <el-form-item label="类型" prop="type">
                <el-select v-model="params2.type" placeholder="请选择类型" clearable>
                  <el-option v-for="dict in customtype" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
              </el-form-item>
              <el-form-item label="简称" prop="descp">
                <el-input v-model="params2.descp" placeholder="请输入简称" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item label="银行账户" prop="cardnum">
                <el-input v-model="params2.cardnum" placeholder="请输入银行账户" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item label="全称" prop="title">
                <el-input v-model="params2.title" placeholder="请输入全称" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item label="联系方式" prop="phone">
                <el-input v-model="params2.phone" placeholder="请输入联系方式" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item label="社会编码" prop="shnum">
                <el-input v-model="params2.shnum" placeholder="请输入社会编码" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item label="收货地址" prop="address">
                <el-input v-model="params2.address" placeholder="请输入收货地址" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item label="邮寄地址" prop="address1">
                <el-input v-model="params2.address1" placeholder="请输入邮寄地址" clearable @keyup.enter="handleQuery2" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery2">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-form>
          </template>
  
          <el-table v-loading="loading" :data="customList" style="width: 100%"
            :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center', 'font-size': '12px' }"
            :row-style="{ height: '50px' }" lazy border>
            <el-table-column label="选择" width="90" align="center">
              <template #default="scope">
                <el-button plain type="success" @click="tocheck(scope.row)">选择</el-button>
              </template>
            </el-table-column>
            <el-table-column label="编码" align="center" prop="id" v-if="false" />
            <el-table-column label="类型" align="center" prop="type">
              <template #default="scope">
                <dict-tag :options="customtype" :value="scope.row.type" />
              </template>
            </el-table-column>
            <el-table-column label="简称" align="center" prop="descp" />
            <el-table-column label="银行账户" align="center" prop="cardnum" />
            <el-table-column label="全称" align="center" prop="title" />
            <el-table-column label="联系方式" align="center" prop="phone" />
            <el-table-column label="社会编码" align="center" prop="shnum" />
            <el-table-column label="收货地址" align="center" prop="address" />
            <el-table-column label="邮寄地址" align="center" prop="address1" />
          </el-table>
  
          <pagination v-show="total2 > 0" :total="total2" v-model:page="params2.pageNum" v-model:limit="params2.pageSize"
            @pagination="getList2" />
        </el-card>
      </el-dialog>
      <el-dialog :title="dialog3.title" v-model="dialog3.visible" append-to-body style="width: 800px">
        <el-card shadow="never">
          <template #header>
            <el-row :gutter="10" class="mb8">
              <el-form ref="queryFormRef" :model="paramsstock" :inline="true">
                <el-form-item label="名称" prop="tilte">
                  <el-input v-model="paramsstock.tilte" placeholder="请输入名称" clearable @keyup.enter="handleQuery3" />
                </el-form-item>
                <el-form-item label="编号" prop="iid">
                  <el-input v-model="paramsstock.sn" placeholder="请输入图号" clearable @keyup.enter="handleQuery3" />
                </el-form-item>
                <el-form-item label="图号" prop="iid">
                  <el-input v-model="paramsstock.iid" placeholder="请输入图号" clearable @keyup.enter="handleQuery3" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="Search" @click="handleQuery3">搜索</el-button>
                  <el-button icon="Refresh" @click="resetQuery">重置</el-button>
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
            <el-table-column label="品类" align="center" prop="otype" />
            <el-table-column label="完成度" align="center" prop="type">
              <template #default="scope">
                <dict-tag :options="stocktype" :value="scope.row.type" />
              </template>
            </el-table-column>
            <el-table-column label="图号" align="center" prop="iid" />
            <el-table-column label="当前库存" align="center" prop="nowstock" />
            <el-table-column label="单位" align="center" prop="unitname" />
            <el-table-column label="规格" align="center" prop="gk" />
            <el-table-column label="报警库存" align="center" prop="warnnum" />
            <!-- <el-table-column label="库存信息" align="center" prop="stockid" /> -->
            <el-table-column label="是否在用" align="center" prop="status">
              <template #default="scope">
                <dict-tag :options="diyisno" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column label="入库批次" align="center" prop="pcount" />
          </el-table>
  
          <pagination v-show="total3 > 0" :total="total3" v-model:page="paramsstock.pageNum"
            v-model:limit="paramsstock.pageSize" @pagination="getList3" />
        </el-card>
      </el-dialog>
      <el-dialog v-model="showprint" :show-close="false" width="800px">
        <div id="printView" style="padding-left: 90px; padding-right: 20px;width: 100%;">
          <table class="table" border="0pm" cellspacing="0" align="left" width="100%"
            style="font-size: 12px;font-family: SimSun;margin-bottom: 10px; table-layout:fixed;word-wrap:break-word;word-break:break-all">
            <tr>
              <td style="padding:5px;" align="left"> </td>
              <td style="padding:5px; font-weight: bolder; font-size: 15px;" align="right">单据编号：{{ ww.orders }}</td>
            </tr>
            <tr>
              <td style="padding:5px;" align="left">委外供应商：{{ ww.cname }}</td>
              <td style="padding:5px;" align="left">委外物料名称：{{ ww.title }}</td>
              <!-- <td style="padding:5px;" align="left">制单人：{{ ww }}</td> -->
            </tr>
          </table>
  
          <div style="font-size: 12px;font-family: SimSun;font-weight: bold;margin: 0 0 10px 5px;">明细</div>
          <table class="yk-table" border="1pm" cellspacing="0" align="center" width="100%"
            style="font-size: 12px;font-family: SimSun; table-layout:fixed;word-wrap:break-word;word-break:break-all">
            <tr>
              <th width="55px">序号</th>
              <th>物料</th>
              <th>编号</th>
              <th>规格</th>
              <th>单位</th>
              <th>数量</th>
              <th>备注</th>
            </tr>
            <tr v-for="(item, index) in ww.delist" :key="index" align="center">
              <td>{{ index + 1 }}</td>
              <td>{{ item.stockVo.tilte }}</td>
              <td>{{ item.stockVo.sn }}</td>
              <td>{{ item.stockVo.gk }}</td>
              <td>{{ item.stockVo.unitname }}</td>
              <td>{{ item.counts }}</td>
              <td>{{ item.remark }}</td>
            </tr>
            <tr align="center">
              <td>签单：</td>
              <!-- <td colspan="2">{{ adAmountUpper }}</td>
              <td>{{ adAmount }}</td>
              <td /> -->
            </tr>
          </table>
        </div>
  
        <div class="drawer-bottom-bar" v-show="showprint">
          <el-button @click="prints" type="primary">打印</el-button>
          <el-button @click="showprint = false">取消</el-button>
        </div>
      </el-dialog>
      <el-dialog :title="dialog4.title" v-model="dialog4.visible" append-to-body style="width: 800px">
        <el-card shadow="never">
          <template #header>
            <el-row :gutter="10" class="mb8">
              <el-form ref="queryFormRef" :model="paramsstock" :inline="true">
                <el-form-item label="名称" prop="tilte">
                  <el-input v-model="paramsstock.tilte" placeholder="请输入名称" clearable @keyup.enter="handleQuery3" />
                </el-form-item>
                <el-form-item label="编号" prop="iid">
                  <el-input v-model="paramsstock.sn" placeholder="请输入图号" clearable @keyup.enter="handleQuery3" />
                </el-form-item>
                <el-form-item label="图号" prop="iid">
                  <el-input v-model="paramsstock.iid" placeholder="请输入图号" clearable @keyup.enter="handleQuery3" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="Search" @click="handleQuery3">搜索</el-button>
                  <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
              </el-form>
            </el-row>
          </template>
  
          <el-table v-loading="loading" :data="stockList" highlight-current-row border style="width: 100%"
            :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center', 'font-size': '12px' }"
            :row-style="{ height: '50px' }" lazy>
            <el-table-column label="选择" width="" align="center">
              <template #default="scope">
                <el-button type="primary" size="small" @click="submit3(scope.row)">确认</el-button>
              </template>
            </el-table-column>
            <el-table-column label="主键" align="center" prop="id" v-if="false" />
            <el-table-column label="货架号" align="center" prop="stockname" />
            <el-table-column label="编码" align="center" prop="sn" />
            <el-table-column label="名称" align="center" prop="tilte" />
            <el-table-column label="品类" align="center" prop="otype" />
            <el-table-column label="完成度" align="center" prop="type">
              <template #default="scope">
                <dict-tag :options="stocktype" :value="scope.row.type" />
              </template>
            </el-table-column>
            <el-table-column label="图号" align="center" prop="iid" />
            <el-table-column label="当前库存" align="center" prop="nowstock" />
            <el-table-column label="单位" align="center" prop="unitname" />
            <el-table-column label="规格" align="center" prop="gk" />
            <el-table-column label="报警库存" align="center" prop="warnnum" />
            <!-- <el-table-column label="库存信息" align="center" prop="stockid" /> -->
            <el-table-column label="是否在用" align="center" prop="status">
              <template #default="scope">
                <dict-tag :options="diyisno" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column label="入库批次" align="center" prop="pcount" />
          </el-table>
  
          <pagination v-show="total3 > 0" :total="total3" v-model:page="paramsstock.pageNum"
            v-model:limit="paramsstock.pageSize" @pagination="getList3" />
        </el-card>
      </el-dialog>
    </div>
  </template>
  
  <script setup name="Outorder" lang="ts">
  import { listOutorder, getOutorder, delOutorder, addOutorder, updateOutorder } from '@/api/mes/outorder';
  import { OutorderVO, OutorderQuery, OutorderForm } from '@/api/mes/outorder/types';
  import { Search } from '@element-plus/icons-vue';
  import { listCustom, getCustom, delCustom, addCustom2, updateCustom } from '@/api/mes/custom';
  import { CustomVO, CustomQuery, CustomForm } from '@/api/mes/custom/types';
  import { listStock, getStock, delStock, addStock, updateStock } from '@/api/mes/stock';
  import { StockVO, StockQuery, StockForm } from '@/api/mes/stock/types';
  import { OutorderDeVO, OrderDeQuery, OutorderDeForm } from '@/api/mes/outorderDe/types';
  import { listOutorderDe, getOutorderDe, delOutorderDe, addOutorderDe, updateOutorderDe } from '@/api/mes/outorderDe';
  import { listUnitmeasure, getUnitmeasure, delUnitmeasure, addUnitmeasure, updateUnitmeasure } from '@/api/mes/unitmeasure';
  import { UnitmeasureVO, UnitmeasureQuery, UnitmeasureForm } from '@/api/mes/unitmeasure/types';
  import { addOrderDe } from '@/api/mes/orderDe/index';
  const unitmeasureList = ref<UnitmeasureVO[]>([]);
  
  import printJs from 'print-js';
  const { proxy } = getCurrentInstance() as ComponentInternalInstance;
  const { customtype, diyisno, stocktype } = toRefs<any>(proxy?.useDict('customtype', 'diyisno', 'stocktype'));
  const outorderList = ref<OutorderVO[]>([]);
  const buttonLoading = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref<Array<string | number>>([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);
  const total2 = ref(0);
  const total3 = ref(0);
  const showprint = ref(false);
  const custname = ref('');
  const dateRangeSubtime = ref<[DateModelType, DateModelType]>(['', '']);
  const dateRangeOuttime = ref<[DateModelType, DateModelType]>(['', '']);
  const cuid = ref(0);
  const queryFormRef = ref<ElFormInstance>();
  const outorderFormRef = ref<ElFormInstance>();
  const stockList = ref<StockVO[]>([]);
  const customList = ref<CustomVO[]>([]);
  const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
  });
  const runames=ref('');
  const orderDe = ref<OrderDeVO1>({
    stockVO: [],
    id: '',
    stockid: '',
    unid: '',
    prices: undefined,
    counts: 0,
    allprice: 0,
    ctime: '',
    status: '',
    remark: ''
  });
  interface OrderDeVO1 {
    stockVO: any[];
    id: string | number;
    stockid: string | number;
    unid: string | number;
    prices: number;
    counts: number;
    allprice: number;
    ctime: string;
    status: string;
    remark: string;
  }
  const ww = ref<any>({})
  const orderDeList = ref<OutorderDeVO[]>([]);
  const initFormData: OutorderForm = {
    id: undefined,
    title: undefined,
    custid: undefined,
    order: undefined,
    sn: undefined,
    stockids: undefined,
    stocktype: undefined,
    prices: undefined,
    subtime: undefined,
    outtime: undefined,
    status: undefined,
    remark: undefined,
  }
  const initFormData2 = ref<any>({
    id: undefined,
    type: undefined,
    descp: undefined,
    cardnum: undefined,
    title: undefined,
    phone: undefined,
    shnum: undefined,
    address: undefined,
    address1: undefined,
    status: undefined,
    remark: undefined,
  })
  const dialog3 = reactive<DialogOption>({
    visible: false,
    title: ''
  });
  const dialog4 = reactive<DialogOption>({
    visible: false,
    title: ''
  });
  const toprint = (row: any) => {
    ww.value.title = row.title;
    ww.value.cname = row.cname;
    ww.value.orders = row.orders;
    ww.value.remark = row.remark;
    ww.value.delist = row.devo
    showprint.value = true
  
  }
  const submitForm3=async ()=>{
    form.value.status='1'
    await updateOutorder(form.value).finally(() => buttonLoading.value = false);
    proxy?.$modal.msgSuccess("操作成功");
    dialog2.visible = false;
    getList();
  }
  /**
  /*************  ✨ Codeium Command ⭐  *************/
  /******  9206f2ef-3092-485b-a192-6430c066ce55  *******/
  const findstock = () => {
    dialog3.visible = true
    dialog3.title = '选择产品';
  }
  const findstock2 = () => {
    dialog4.visible = true
    dialog4.title = '选择产品';
  }
  const paramsstock = ref<any>({
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
  const params2 = ref<any>({
    pageNum: 1,
    pageSize: 10,
    type: undefined,
    descp: undefined,
    cardnum: undefined,
    title: undefined,
    phone: undefined,
    shnum: undefined,
    address: undefined,
    address1: undefined,
    status: undefined,
    params: {
    }
  })
  const prints = () => {
    showprint.value = false
    printJs({
      printable: 'printView',
      type: 'html',
      style: '.print-css { font-size: 12px; }',
      margin: 1,
      header:'委外领料单',
      headerStyle: 'font-size:20px; text-align: center;',
    })
  }
  const findcustom = () => {
    dialog.visible = true;
    dialog.title = '选择委外供应商';
    getList2();
  }
  const tocheck = (row: any) => {
    cuid.value = row.id
    custname.value = row.title
    dialog.visible = false;
  }
  const getsn = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    const min = today.getMinutes()+Math.random().toFixed(4)*10000;
    form.value.sn = year + month + day + min
  }
  
  const submitForm2 = async () => {
    let res = await addCustom2(initFormData2.value).finally(() => buttonLoading.value = false);
    custname.value = res.data.title
    cuid.value = res.data.id
    dialog2.visible = false;
  }
  const data = reactive<PageData<OutorderForm, OutorderQuery>>({
    form: { ...initFormData },
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      title: undefined,
      custid: undefined,
      order: undefined,
      sn: undefined,
      stockids: undefined,
      stocktype: undefined,
      prices: undefined,
      fromtype:'订单委外',
      status: '0',
      params: {
        subtime: undefined,
        outtime: undefined,
      }
    },
    rules: {
      id: [
        { required: true, message: "编码不能为空", trigger: "blur" }
      ],
    }
  });
  const q4 = ref<any>({
    pageNum: 1,
    pageSize: 1000
  })
  const { queryParams, form, rules } = toRefs(data);
  
  /** 查询委外订单列表 */
  const getList = async () => {
    loading.value = true;
    queryParams.value.params = {};
    proxy?.addDateRange(queryParams.value, dateRangeSubtime.value, 'Subtime');
    proxy?.addDateRange(queryParams.value, dateRangeOuttime.value, 'Outtime');
    // queryParams.value.params.status = '0'
    const res = await listOutorder(queryParams.value);
    outorderList.value = res.rows;
    total.value = res.total;
    loading.value = false;
  }
  
  const submit2 = (row: any) => {
    orderDe.value.stockVO = row;
    orderDe.value.stockid = row.id;
    orderDe.value.counts = 1;
    orderDe.value.status = '0'
    orderDe.value.prices = 0.0000;
    orderDeList.value.push(orderDe.value);
    orderDe.value = {};
    console.log(orderDeList.value);
  }
  
  const submit3 = (row: any) => {
    form.value.stoid=row.id;
    runames.value=row.tilte
    dialog4.visible = false
  }
  const dialog2 = reactive<DialogOption>({
    visible: false,
    title: ''
  });
  /** 取消按钮 */
  const cancel = () => {
    reset();
    dialog.visible = false;
  }
  
  /** 表单重置 */
  const reset = () => {
    form.value = { ...initFormData };
    orderDeList.value = [];
    outorderFormRef.value?.resetFields();
  }
  
  /** 搜索按钮操作 */
  const handleQuery = () => {
    queryParams.value.pageNum = 1;
    getList();
  }
  const handleQuery2 = () => {
    params2.value.pageNum = 1;
    getList2();
  }
  const handleQuery3 = () => {
    paramsstock.value.pageNum = 1;
    getList3();
  }
  /** 重置按钮操作 */
  const resetQuery = () => {
    dateRangeSubtime.value = ['', ''];
    dateRangeOuttime.value = ['', ''];
    queryFormRef.value?.resetFields();
    handleQuery();
  }
  
  /** 多选框选中数据 */
  const handleSelectionChange = (selection: OutorderVO[]) => {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }
  const getList4 = async () => {
    const res = await listUnitmeasure(q4.value);
    unitmeasureList.value = res.rows;
  
  }
  /** 新增按钮操作 */
  const handleAdd = () => {
    reset();
    dialog.visible = true;
    dialog.title = "添加委外订单";
  }
  const getList2 = async () => {
    const res = await listCustom(params2.value);
    customList.value = res.rows;
    total2.value = res.total;
  }
  
  const getList3 = async () => {
    const res = await listStock(paramsstock.value);
    stockList.value = res.rows;
    total3.value = res.total;
  }
  /** 修改按钮操作 */
  const handleUpdate = async (row?: OutorderVO) => {
    reset();
    const _id = row?.id || ids.value[0]
    const res = await getOutorder(_id);
    Object.assign(form.value, res.data);
    dialog2.visible = true;
    dialog2.title = "修改委外订单";
    
  }
  
  /** 提交按钮 */
  const submitForm = async () => {
    if (cuid.value == 0) {
      proxy?.$modal.msgError("请选择供应商");
      return
    }
    let ids = []
    let concatenatedDataArray = []; // 在循环外部定义一个数组来存储每次循环中的数据
  
    await Promise.all(orderDeList.value.map(async (item) => {
      item.allprice = (item.prices * item.counts).toFixed(4)
      item.status = '0'
      let rec = await addOutorderDe(item).finally(() => buttonLoading.value = false);
      let dataArray = rec.data;
      concatenatedDataArray.push(dataArray);
    }))
    let concatenatedData = concatenatedDataArray.join(',');
    form.value.stockids = concatenatedData
    form.value.custid = cuid.value
  
    await addOutorder(form.value).finally(() => buttonLoading.value = false);
  
    proxy?.$modal.msgSuccess("操作成功");
    dialog.visible = false;
    await getList();
  
  
  }
  
  /** 删除按钮操作 */
  const handleDelete = async (row?: OutorderVO) => {
    const _ids = row?.id || ids.value;
    await proxy?.$modal.confirm('是否确认删除委外订单编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
    await delOutorder(_ids);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  }
  
  /** 导出按钮操作 */
  const handleExport = () => {
    proxy?.download('mes/outorder/export', {
      ...queryParams.value
    }, `outorder_${new Date().getTime()}.xlsx`)
  }
  
  onMounted(() => {
    getList();
    getList3();
    getList4();
  });
  </script>
  
  <style media="print">
  @page {
    size: auto;
    margin: 3mm;
  }
  
  html {
    background-color: #ffffff;
    margin: 0px;
  }
  
  body {
    border: solid 1px #ffffff;
  }
  .table {
    font-size: 10px;
  }
  </style>
  <style lang="scss" scoped>
  @media print {
    #printView {
      display: block;
      width: 100%;
      overflow: hidden;
    }
  }
  </style>
  