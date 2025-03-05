<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>搜索</span>
            </div>
          </template>
          <el-form ref="queryFormRef" :model="queryParams" :inline="true" size="small">

            <el-form-item label="" prop="custid">
              <el-input v-model="queryParams.custid" placeholder="请输入客户" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="" prop="subtime">
              <el-date-picker clearable v-model="queryParams.subtime" type="date" value-format="YYYY-MM-DD"
                placeholder="请选择交货时间" />
            </el-form-item>
            <el-form-item label="" prop="custno">
              <el-input v-model="queryParams.custno" placeholder="请输入客户订单号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="订单时间" prop="otime">
              <el-date-picker clearable v-model="queryParams.otime" type="date" value-format="YYYY-MM-DD"
                placeholder="请选择订单时间" />
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
      <el-table v-loading="loading" :data="orderList"  style="width: 100%"
        :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center', 'font-size': '12px' }"
        :row-style="{ height: '50px' }" lazy border>
        <el-table-column type="expand" label="出库记录" width="80">
          <template #default="props">
            <div m="4">
              <p m="t-0 b-2">客户名称: {{ props.row.customVo.descp }}</p>
              <p m="t-0 b-2">联系人: {{ props.row.customVo.cname }}</p>
              <p m="t-0 b-2">联系电话: {{ props.row.customVo.phone }}</p>
              <p m="t-0 b-2">发货地址: {{ props.row.customVo.address }}</p>

              <h3>需求列表</h3>
              <el-table :data="props.row.saleoutList" :border="childBorder" show-summary stripe>
                <el-table-column label="物料" prop="stockVo.tilte" />
                <el-table-column label="编号" prop="stockVo.sn" />
                <el-table-column label="规格" prop="stockVo.gk" />
                <el-table-column label="规格" prop="stockVo.gk" />
                <el-table-column label="数量" prop="counts" />
                <el-table-column label="时间" prop="createTime" />
                <el-table-column label="操作"  >
                  <template #default="scope">
                    <el-button type="text" size="small" @click="gaddprint(props.row,scope.row)">补打单据</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" prop="orders" width="90">
          <template #default="scope">
            <div class="block mt-2" v-if="scope.row.tsta == 1">
              <div class="mt-2">
                <el-button type="primary" plain size="small" @click="toprint(scope.row)">出库</el-button>
              </div>
            </div>
            <div class="block mt-2" v-else>
              <el-text type="danger" class="text-blod">已完成</el-text>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="主键" align="center" prop="id" v-if="false" />

        <el-table-column label="客户" align="center" prop="custname">
          <template #default="scope">
            <span class="">{{ scope.row.custname }}</span>
          </template>
        </el-table-column>
        <el-table-column label="需求清单" align="center" prop="custname" width="300px">
          <template #default="scope">
            <div v-for="(item, index) in scope.row.chulist">
              <el-tag type="info" class="p-1">{{ item }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="当前库存" align="center" prop="custname" width="200px">
          <template #default="scope">
            <div v-for="(item, index) in scope.row.chulist2">
              <el-tag type="info" class="p-1">{{ item }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <!-- <el-table-column label="税点" align="center" prop="shui" /> -->
        <el-table-column label="交货时间" align="center" prop="subtime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.subtime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户订单号" align="center" prop="custno" />
        <el-table-column label="付款周期" align="center" prop="cycle">
          <template #default="scope">
            <span>{{ scope.row.cycle }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="物料信息" align="center" prop="stids" /> -->
        <el-table-column label="订单时间" align="center" prop="otime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.otime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="备注" align="center" prop="remark" /> -->
      </el-table>
      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <el-dialog v-model="showprint" :show-close="false" width="800px">
      <div id="printView" style="width: 100%;padding-left: 5px;padding-right: 5px;">
        <table class="table" border="0pm" cellspacing="0" align="left" width="100%"
          style="font-size: 12px;font-family: SimSun; table-layout:fixed;word-wrap:break-word;word-break:break-all;">

          <tr>
            <td style="font-size: 12px;" align="center" colspan="3">地址：宁波市海曙区洞桥镇三李村
            </td>
          </tr>
          <tr>
            <td style="font-size: 12px;line-height: 0px" align="center" colspan="3">电话：0571-88201360 13486640387
            </td>
          </tr>
          <tr>
            <td align="left"> </td>
            <td style="font-weight: bolder; font-size: 15px;" align="right" colspan="2">单据编号：{{ ww.oid }}
            </td>
          </tr>
          <tr style="line-height: 0px;">
            <td align="left">购货单位:{{ ww.customtitle }}</td>
            <td align="left"></td>
            <!-- <td style="padding:5px;" align="left">制单人：{{ ww }}</td> -->
          </tr>
          <tr>
            <td align="left">地址:{{ ww.address }}</td>
            <td align="left">联系人:{{ ww.phone }}</td>
            <td align="left" v-if="ww.times">出库时间:{{ parseTime(ww.times, '{y}-{m}-{d}') }}</td>
            <td align="left" v-else>出库时间:{{ new Date().toLocaleDateString() }}</td>
          </tr>
        </table>
        <table class="yk-table" border="1pm" cellspacing="0" align="center" width="95%"
          style="font-size: 10px;font-family: SimSun; table-layout:fixed;word-wrap:break-word;word-break:break-all">
          <tr class="yk-table">

            <th width="55px">序号</th>
            <th>物料</th>
            <th>编号</th>
            <th>规格</th>
            <th>合同号</th>
            <th>单位</th>
            <th>数量</th>
            <th>单价</th>
            <th>金额</th>
            <th>备注</th>
          </tr>
          <tr v-for="(item, index) in ww.orders" :key="index" align="center" class="yk-table">
            <td>{{ index + 1 }}</td>
            <td>{{ item.stockVo.tilte }}</td>
            <td>{{ item.stockVo.sn }}</td>
            <td>{{ item.stockVo.gk }}</td>
            <td>{{ ww.custno }}</td>
            <td>{{ item.stockVo.unitname }}</td>
            <td>
              <el-input v-model="item.counts" size="small" @change="deleteitem(item)" v-if="ww.showc"></el-input>
              <span v-else>{{ item.counts }}</span>
            </td>
            <td>
              <el-input v-model="item.prices" size="small" @change="changePrice(item)"></el-input>
            </td>
            <td>
              <el-input v-model="item.allprices" size="small"></el-input>
            </td>
            <td>
              <el-input v-model="item.remark" size="small"></el-input>
            </td>
          </tr>
          <tr align="center">
            <td colspan="2">合计：</td>
            <td colspan="5"><span align="left">整</span></td>
            <!-- <td>{{ adAmount }}</td>
            <td /> -->
          </tr>
        </table>
        <div style="width: 98%;text-align: center;margin: auto;" class="ccen">客户签收并盖章：_____________ 送货：____________
        </div>
      </div>

      <div class="drawer-bottom-bar" v-show="showprint">
        <el-button @click="prints" type="primary" v-if="ww.showc">打印并出库</el-button>
        <el-button @click="toc" type="primary" v-else>打印</el-button>
        <el-button @click="showprint = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="Order" lang="ts">
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from '@/api/mes/order';
import { OrderVO, OrderQuery, OrderForm } from '@/api/mes/order/types';
import { listSaleout, getSaleout, delSaleout, addSaleout, updateSaleout } from '@/api/mes/saleout';
import { SaleoutVO, SaleoutQuery, SaleoutForm } from '@/api/mes/saleout/types';
import { listCustom, getCustom, delCustom, addCustom2, updateCustom } from '@/api/mes/custom';
import { CustomVO, CustomQuery, CustomForm } from '@/api/mes/custom/types';
import { listStock, getStock, delStock, addStock, updateStock } from '@/api/mes/stock';
import { StockVO, StockQuery, StockForm } from '@/api/mes/stock/types';
import { listOrderDe, getOrderDe, delOrderDe, addOrderDe, updateOrderDe } from '@/api/mes/orderDe';
import { OrderDeVO, OrderDeQuery, OrderDeForm } from '@/api/mes/orderDe/types';
import { Search } from '@element-plus/icons-vue';
import printJs from 'print-js';
import { split } from 'postcss/lib/list';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { customtype, diyisno, stocktype } = toRefs<any>(proxy?.useDict('customtype', 'diyisno', 'stocktype'));
const stockList = ref<StockVO[]>([]);
const customList = ref<CustomVO[]>([]);
const ww = ref<any>({})
const orderDeList = ref<OrderDeVO[]>([]);
const showprint = ref(false);
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
const orderList = ref<OrderVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const total2 = ref(0);
const total3 = ref(0);
const custname = ref('');
const cuid = ref(0);
const queryFormRef = ref<ElFormInstance>();
const orderFormRef = ref<ElFormInstance>();
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
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});
const dialog2 = reactive<DialogOption>({
  visible: false,
  title: ''
});
const dialog3 = reactive<DialogOption>({
  visible: false,
  title: ''
});
const saout = ref<any>({
  id: undefined,
  orderid: undefined,
  stockid: undefined,
  counts: undefined,
  files: undefined,
  wuliu: undefined,
  status: undefined,
  remark: undefined,
})
const prints = () => {
  showprint.value = false
  let cbool=false

  ww.value.orders.forEach((oc:any) =>{
    if(parseInt(oc.counts)>parseInt(oc.stockVo.nowstock)){
      console.log(1)
      proxy?.$modal.msgError(`库存不足：${oc.stockVo.tilte}`);
      cbool=true
    }
  })
 if(cbool)return
  
  ww.value.orders.forEach((item: any) => {
    item.remark = ''
    saout.value.orderid = ww.value.oid
    saout.value.stockid = item.stockid
    saout.value.counts = item.counts
    saout.value.remark = item.remark
    saout.value.status = '1'
    addSaleout(saout.value).then(() => {
      proxy?.$modal.msgSuccess('出库成功')
      toc()
    }).catch(() => {
      console.log('出库失败')
    })
    getList()
  })
}
const toc=() => {
  printJs({
    printable: 'printView',
    type: 'html',
    style: '.print-css { font-size: 10px; width: 100%;} input{border:none;} .ccen{width: 100%;margin:auto;text-align:center;padding-top:10px;font-size:16px;} .yk-table{font-size: 7px;}',
    margin: 1,
    header: '宁波市海曙博远塑胶厂',
    headerStyle: 'font-size:20px; text-align: center;',
  })
}

const toprint = (row: any) => {
  ww.value={}
  ww.value.orders = []
  ww.value.custno = row.custno
  ww.value.customtitle = row.customVo.title
  ww.value.oid = row.id
  ww.value.customname = row.customVo.cname
  ww.value.phone = row.customVo.cname
  ww.value.address = row.customVo.address
  ww.value.times = row.createTime
  ww.value.showc=true;
  row.orderList.forEach((item: any) => {
    item.remark = ''
  })
  ww.value.orders = row.orderList
  showprint.value = true

}
const gaddprint=(row:any,row1:any)=>{
  console.log(row1)
  ww.value={}
  ww.value.orders = []
  ww.value.custno = row.custno
  ww.value.customtitle = row.customVo.title
  ww.value.oid = row.id
  ww.value.customname = row.customVo.cname
  ww.value.phone = row.customVo.cname
  ww.value.address = row.customVo.address
  ww.value.times = row.otime
  ww.value.showc=false;
  ww.value.orders.push(row1)
  showprint.value = true
}

const findstock = () => {
  dialog3.visible = true
  dialog3.title = '选择产品';
}
const initFormData: OrderForm = {
  id: undefined,
  shui: 13,
  custid: undefined,
  subtime: undefined,
  custno: undefined,
  cycle: undefined,
  stids: undefined,
  fp: undefined,
  otime: undefined,
  ismrp: undefined,
  otfile: undefined,
  ctfile: undefined,
  status: undefined,
  remark: undefined,
}
const data = reactive<PageData<OrderForm, OrderQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    shui: undefined,
    custid: undefined,
    subtime: undefined,
    custno: undefined,
    cycle: undefined,
    stids: undefined,
    fp: undefined,
    otime: undefined,
    ismrp: undefined,
    otfile: undefined,
    ctfile: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "主键不能为空", trigger: "blur" }
    ],
  }
});
const addcustom2 = () => {
  dialog2.title = '添加客户';
  dialog2.visible = true;
}
const changePrice = (item: any) => {
  item.allprices = item.prices * item.counts
}
const deleteitem = (items: any) => {
  if (items.counts == 0) {
    const index = ww.value.orders.findIndex(item => item === items);
    if (index !== -1) {
      ww.value.orders.splice(index, 1);
    }
  }


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
const { queryParams, form, rules } = toRefs(data);
const findcustom = () => {
  dialog.visible = true;
  dialog.title = '选择客户';
  getList2();
}
const chulist = ref<any[]>([]);
const chulist2 = ref<any[]>([]);
const chulist3 = ref<any[]>([]);
/** 查询销售订单列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.tsta = 0;
  chulist.value=[]
  chulist2.value=[]
  const res = await listOrder(queryParams.value);
  orderList.value = res.rows;
  
  orderList.value.forEach(item => {
    item.orderList.forEach((items: any) => {
      chulist.value.push("[编号" + items.stockVo.sn + "]" + items.stockVo.tilte + ",需求量：" + items.counts + ",已出库:" + items.ckcounts)
      chulist2.value.push(items.stockVo.tilte + ",当前库存量：" + items.stockVo.nowstock)
      
      
    })
    item.chulist=chulist.value
    item.chulist2=chulist2.value
      chulist.value=[]
      chulist2.value=[]
  }
  )
  console.log(orderList.value)
  total.value = res.total;
  loading.value = false;
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
/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  orderDeList.value = []
  cuid.value = 0
  custname.value = undefined
  orderFormRef.value?.resetFields();
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
  queryFormRef.value?.resetFields();
  custname.value = undefined
  cuid.value = undefined
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: OrderVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加销售订单";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: OrderVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getOrder(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改销售订单";
}
const changeFp = async (row?: OrderVO) => {
  reset();
  console.log(row)
  const _id = row?.id || ids.value[0]
  const res = await getOrder(_id);
  Object.assign(form.value, res.data);
  form.value.fp = row.fp
  await updateOrder(form.value).finally(() => buttonLoading.value = false);
  proxy?.$modal.msgSuccess("修改成功");
}
/** 提交按钮 */
const submitForm = async () => {
  if (cuid.value == 0) {
    proxy?.$modal.msgError("请选择客户");
    return
  }
  let ids = []
  let concatenatedDataArray = []; // 在循环外部定义一个数组来存储每次循环中的数据

  let ss = (form.value.shui / 100) + 1

  await Promise.all(orderDeList.value.map(async (item) => {
    item.allprice = (item.prices * item.counts * ss).toFixed(2)
    item.status = '0'
    let rec = await addOrderDe(item).finally(() => buttonLoading.value = false);
    let dataArray = rec.data;
    concatenatedDataArray.push(dataArray);
  }))
  let concatenatedData = concatenatedDataArray.join(',');
  form.value.stids = concatenatedData
  form.value.custid = cuid.value
  orderFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateOrder(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addOrder(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      reset();
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: OrderVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除销售订单编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delOrder(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('mes/order/export', {
    ...queryParams.value
  }, `order_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
  getList3();
});
</script>
