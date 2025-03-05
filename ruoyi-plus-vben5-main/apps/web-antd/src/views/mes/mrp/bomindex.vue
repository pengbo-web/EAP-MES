<script setup lang="ts">
import type { Recordable } from '@vben/types';
import { nextTick, watch, ref, onMounted, reactive } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';
import {
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps,
} from '#/adapter/vxe-table';
import {
  Modal,
  Popconfirm,
  Space,
  Tag,
  Button,
  Table,
  Form,
  FormItem,
  Input,
  RadioGroup,
  RadioButton,
  InputNumber,
  DatePicker,
  InputSearch,
} from 'ant-design-vue';

import { orderAdd, orderInfo, orderUpdate } from '#/api/mes/order';
const props = defineProps({
  rowinfo: Number,
});
watch(
  () => props.rowinfo,
  () => {
    console.log('123');
    tableApi.query();
  },
);
const gridOptions: VxeGridProps = {
  checkboxConfig: {
    // 高亮
    highlight: true,
    // 翻页时保留选中状态
    reserve: true,
  },
  columns: [
    { type: 'expand', width: 60, slots: { content: 'expand_content' } },
    {
      field: 'stockVo.tilte',
      title: '物料',
    },
    {
      field: 'stockVo.sn',
      title: '编号',
    },
    {
      field: 'stockVo.gk',
      title: '规格',
    },
    {
      field: 'stockVo.nowstock',
      title: '当前库存',
    },
    {
      field: 'counts',
      title: '需求量',
    },
    {
      field: 'pcount',
      title: '已排成数量',
    },
    {
      field: 'ckcounts',
      title: '已出库数量',
    },
    {
      field: 'stockVo.gk',
      title: '未出库数量',
    },
    {
      field: 'action',
      title: '操作',
      fixed: 'right',
      slots: { default: 'action' },
    },
  ],
  height: 'auto',
  keepSource: true,
  pagerConfig: {
    enabled: false,
  },
  toolbarConfig: {
    custom: false,
    zoom: false,
    refresh: false,
  },
  proxyConfig: {
    ajax: {
      query: async () => {
        return {
          rows: props.rowinfo.orderList,
        };
      },
    },
  },
  rowConfig: {
    isCurrent: false,
    keyField: 'id',
    height: 150,
  },
  id: 'profile-bind-table',
};
const [BasicTable, tableApi] = useVbenVxeGrid({
  gridOptions,
});
const childGridOptions = reactive<VxeGridProps<any>>({
  border: true,
  height: 200,
  columns: [
    { field: 'tilte', title: '物料' },
    { field: 'sn', title: '编号' },
    { field: 'gk', title: '规格' },
    { field: 'unitname', title: '单位' },
    { field: 'nowstock', title: '当前库存' },
    { field: 'ncout', title: '需求数量' },
    { field: 'action', title: '操作' },
  ],
});
const columns2 = [
  
  { dataIndex: 'tilte', title: '物料' },
  { dataIndex: 'sn', title: '编号' },
  { dataIndex: 'gk', title: '规格' },
  { dataIndex: 'unitname', title: '单位' },
  { dataIndex: 'nowstock', title: '当前库存' },
  { dataIndex: 'ncout', title: '需求数量' },
  { dataIndex: 'action', title: '操作' },
];
import { orderDeAdd, orderDeInfo, orderDeUpdate } from '#/api/mes/orderDe';
const otherupdate = async (row: any) => {
  row.status = '1';
  await orderDeUpdate(row);
};
const showpc = ref(false);

const formState = ref<any>({});
function markpc(row: any, type: number) {
  showpc.value = true;
  const today = new Date();
  const year = today.getFullYear();
  const month = (today.getMonth() + 1).toString().padStart(2, '0');
  const day = today.getDate().toString().padStart(2, '0');
  formState.value.title =
  props.rowinfo.custname + '(' + year + '-' + month + '-' + day + ')';
  formState.value.sn = row.remark;
  formState.value.orderid = props.rowinfo.id;
  formState.value.proId = row.proid;
  formState.value.endtime=props.rowinfo.subtime;
  if (type == 1) {
    formState.value.proname = row.stockVo.tilte;
  } else {
    formState.value.sn = props.rowinfo.remark;
    formState.value.proname = row.tilte;
  }
}
import { workAdd, workInfo, workUpdate } from '#/api/mes/work';
async function addpc() {
  await workAdd(formState.value);
  showpc.value = false;
}
const showww = ref(false);
const formww = ref<any>({});
async function addww(row: any, types: number) {
  console.log(row);
  showww.value = true;
  const today = new Date();
  const year = today.getFullYear();
  const month = (today.getMonth() + 1).toString().padStart(2, '0');
  const day = today.getDate().toString().padStart(2, '0');
  formww.value.title =
    props.rowinfo.custname + '(' + year + '-' + month + '-' + day + ')';
  formww.value.orders = props.rowinfo.id;
  if (types == 1) {
    formww.value.stockid = row.stockVo.id;
    formww.value.stockname = row.stockVo.tilte;
  } else {
    formww.value.stockid = row.id;
    formww.value.stockname = row.tilte;
  }
  formww.value.fromtype = '订单委外';
  formww.value.prices = 1;
}
import {
  outorderDeAdd,
  outorderDeInfo,
  outorderDeUpdate,
} from '#/api/mes/outorderDe';
import { outorderAdd, outorderInfo, outorderUpdate } from '#/api/mes/outorder';
async function addww0() {
  let pricenew = (formww.value.prices * formww.value.counts).toFixed(4);
  let par = {
    id: undefined,
    allprice: pricenew,
    fromtype: '订单委外',
    orderid: props.rowinfo.id,
    stockid: formww.value.stockid,
    prices: formww.value.prices,
    counts: formww.value.counts,
    status: formww.value.status,
    remark: formww.value.remark,
  };

  // 使用 Promise.all() 并等待所有 Promise 执行完成
  let re = await outorderDeAdd(par);
  formww.value.stockids = re;

  await outorderAdd(formww.value);
  showww.value = false;
}
import CustLists from '#/views/mes/custom/list.vue';
const custref = ref<any>({});
const showcustlist = ref(false);
function handleScust() {
  showcustlist.value = true;
}
function handleChildEvent() {
  const record = custref.value.rowinfo;
  formww.value.custname = record.title;
  formww.value.custid = record.id;
  showcustlist.value = false;
}
import {
  procurementplanAdd,
  procurementplanInfo,
  procurementplanUpdate,
} from '#/api/mes/procurementplan';
const showcg = ref(false);
const formcg = ref<any>({});
async function addcg(row: any, types: number) {
  formcg.value.sn = props.rowinfo.remark;
  showcg.value = true;
  if (types == 1) {
    formcg.value.proId = row.stockVo.id;
    formcg.value.stockname = row.stockVo.tilte;
  } else {
    formcg.value.proId = row.id;
    formcg.value.stockname = row.tilte;
  }
  formcg.value.workId = 100;
  formcg.value.status = '0';
  formcg.value.workname = '订单采购';
}
async function addcg0() {
  let re = await procurementplanAdd(formcg.value);
  showcg.value = false;
}
</script>

<template>
  <BasicTable class="col-span-2">
    <template #action="{ row }">
      <div v-if="row.status == '0'">
        <div class="mt-2" v-if="row.stockVo.nowstock >= row.counts">
         <Tag color="cyan">可申请出库</Tag> 
        </div>
        <div class="mt-2">
          <Button @click="otherupdate(row)" size="small">其他完成</Button>
        </div>
        <div class="mt-2" v-if="row.stockVo.nowstock < row.counts">
          <Button size="small" @click="addcg(row, 1)">采购申请</Button>
        </div>
        <div class="mt-2" v-if="row.stockVo.nowstock < row.counts">
          <Button @click="markpc(row, 1)" size="small">排程计划</Button>
        </div>
        <div class="mt-2">
          <Button @click="addww(row, 1)" size="small">委外加工</Button>
        </div>
      </div>
      <div v-else>已操作完成</div>
    </template>
    <template #expand_content="{ row }">
      <div class="expand-wrapper" style="width: 90%; margin: auto" :class="mt-2">
        <Table :columns="columns2" :data-source="row.stockVo.bomlist" rowKey="id" bordered>
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'action'">
              <span>
                <div>
                  <div class="mt-2">
                    <Button size="small" @click="addcg(record, 2)"
                      >采购申请</Button
                    >
                  </div>
                  <div class="mt-2">
                    <Button @click="markpc(record, 2)" size="small"
                      >排程计划</Button
                    >
                  </div>
                  <div class="mt-2">
                    <Button @click="addww(record, 2)" size="small"
                      >委外加工</Button
                    >
                  </div>
                </div>
              </span>
            </template>
          </template>
          <template #expandedRowRender="{ record }">
            <Table
              v-if="record.bomlist && record.bomlist.length > 0"
              :columns="columns2"
              :data-source="record.bomlist"
              :pagination="false"
              rowKey="id"
              bordered="false"
              class="m-5 bg-gray-100"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                  <span>
                    <div>
                      <div class="mt-2">
                        <Button size="small" @click="addcg(record, 2)"
                          >采购申请</Button
                        >
                      </div>
                      <div class="mt-2">
                        <Button @click="markpc(record, 2)" size="small"
                          >排程计划</Button
                        >
                      </div>
                      <div class="mt-2">
                        <Button @click="addww(record, 2)" size="small"
                          >委外加工</Button
                        >
                      </div>
                    </div>
                  </span>
                </template>
              </template>
            </Table>
          </template>
        </Table>
      </div>
    </template>
  </BasicTable>
  <Modal v-model:open="showpc" @ok="addpc">
    <div class="mt-5">
      <Form :model="formState">
        <FormItem label="标题">
          <Input v-model:value="formState.title" placeholder="标题" />
        </FormItem>
        <FormItem label="编码">
          <Input v-model:value="formState.sn" placeholder="sn" disabled />
        </FormItem>
        <FormItem label="是否流转">
          <RadioGroup
            v-model="formState.isturn"
            value="'0'"
            button-style="solid"
          >
            <RadioButton value="'0'">流转</RadioButton>
            <RadioButton value="'1'">默认</RadioButton>
          </RadioGroup>
        </FormItem>
        <FormItem label="面向类型">
          订单
          <Input v-model="formState.fromtype" value="1" hidden />
        </FormItem>
        <FormItem label="排产物料">
          <Input v-model:value="formState.proname" disabled />
        </FormItem>
        <FormItem label="是否加急">
          <RadioGroup v-model="formState.isturn" value="0" button-style="solid">
            <RadioButton value="0">流转</RadioButton>
            <RadioButton value="1">默认</RadioButton>
          </RadioGroup>
        </FormItem>
        <FormItem label="排产数量">
          <InputNumber v-model:value="formState.neednum" />
        </FormItem>
        <FormItem label="交工日期">
          <DatePicker v-model:value="formState.endtime" />
        </FormItem>
      </Form>
    </div>
  </Modal>
  <Modal v-model:open="showww" @ok="addww0">
    <div class="mt-5">
      <Form :model="formww">
        <FormItem label="标题">
          <Input v-model:value="formww.title" placeholder="标题" />
        </FormItem>
        <FormItem label="订单号">
          <Input v-model:value="formww.orders" placeholder="sn" disabled />
        </FormItem>
        <FormItem label="排产物料">
          <Input v-model:value="formww.stockname" disabled />
        </FormItem>
        <FormItem label="是否待料">
          <RadioGroup v-model:value="formww.status" button-style="solid">
            <RadioButton value="0">带料</RadioButton>
            <RadioButton value="1">不带</RadioButton>
          </RadioGroup>
        </FormItem>
        <FormItem label="委外客户">
          <InputSearch @search="handleScust" v-model:value="formww.custname" />
        </FormItem>
        <FormItem label="价格">
          <InputNumber v-model="formww.prices" value="1" />
        </FormItem>
        <FormItem label="委外数量">
          <InputNumber v-model:value="formww.counts" />
        </FormItem>
        <FormItem label="交工日期">
          <DatePicker v-model:value="formww.endtime" />
        </FormItem>
        <FormItem label="备注">
          <Input v-model:value="formww.remark" />
        </FormItem>
      </Form>
    </div>
  </Modal>
  <Modal v-model:open="showcg" @ok="addcg0">
    <div class="mt-5">
      <Form :model="formcg">
        <FormItem label="流转号">
          <Input v-model:value="formcg.sn" placeholder="标题" disabled />
        </FormItem>
        <FormItem label="排产物料">
          <Input v-model:value="formcg.stockname" disabled />
        </FormItem>
        <FormItem label="价格">
          <InputNumber v-model="formcg.prices" value="1" />
        </FormItem>
        <FormItem label="采购数量">
          <InputNumber v-model:value="formcg.count" />
        </FormItem>
        <FormItem label="备注">
          <Input v-model:value="formcg.remark" />
        </FormItem>
      </Form>
    </div>
  </Modal>
  <Modal v-model:open="showcustlist" width="100%" wrap-class-name="full-modal">
    <CustLists ref="custref" @child-event="handleChildEvent" />
  </Modal>
</template>
