<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';
import {  useUserStore } from '@vben/stores';
import {
  Modal,
  Popconfirm,
  Space,
  Descriptions,
  DescriptionsItem,
  Button,
  Drawer,
  Row,
  InputNumber,
  Input,
  Divider,
  Col,
  message,
  Card,
} from 'ant-design-vue';
import dayjs from 'dayjs';

import {
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps,
} from '#/adapter/vxe-table';

import { orderExport, orderList, orderRemove } from '#/api/mes/order';
import type { OrderForm } from '#/api/mes/order/model';
import { commonDownloadExcel } from '#/utils/file/download';
import { saleoutAdd, saleoutInfo, saleoutUpdate } from '#/api/mes/saleout';
import {
  saleoutExport,
  saleoutList,
  saleoutrecList,
  saleoutRemove,
} from '#/api/mes/saleout';
import orderDrawer from './order-drawer.vue';
import { columns, querySchema } from './data';
import printJS from 'print-js';

const formOptions: VbenFormProps = {
  commonConfig: {
    labelWidth: 80,
    componentProps: {
      allowClear: true,
    },
  },
  schema: querySchema(),
  wrapperClass: 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4',
  // 处理区间选择器RangePicker时间格式 将一个字段映射为两个字段 搜索/导出会用到
  // 不需要直接删除
  fieldMappingTime: [
   [
     'otime',
     ['params[beginoTime]', 'params[endoTime]'],
     ['YYYY-MM-DD 00:00:00', 'YYYY-MM-DD 23:59:59'],
   ],
  ],
};

const gridOptions: VxeGridProps = {
  checkboxConfig: {
    // 高亮
    highlight: true,
    // 翻页时保留选中状态
    reserve: true,
    // 点击行选中
    // trigger: 'row',
  },
  columns,
  height: 'auto',
  keepSource: true,
  pagerConfig: {},
  proxyConfig: {
    ajax: {
      query: async ({ page }, formValues = {}) => {
        formValues.tsta = 0;
        return await orderList({
          pageNum: page.currentPage,
          pageSize: page.pageSize,
          ...formValues,
        });
      },
    },
  },
  rowConfig: {
    keyField: 'id',
  },
  // 表格全局唯一表示 保存列配置需要用到
  id: 'mes-order-index',
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [OrderDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: orderDrawer,
});

function handleAdd() {
  drawerApi.setData({});
  drawerApi.open();
}

async function handleEdit(row: Required<OrderForm>) {
  drawerApi.setData({ id: row.id });
  drawerApi.open();
}
const infolist = ref<any[]>([]);
const recopen = ref(false);
async function handleDelete(row: any) {
  // await orderRemove(row.id);
  // await tableApi.query();

  let res = await saleoutrecList({
    orderid: row.id,
  });
  infolist.value = res;
  pinfo.value = row;
  recopen.value = true;
  const use=useUserStore();
  useinfo.value=use.userInfo;
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<OrderForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await orderRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(
    orderExport,
    '销售订单数据',
    tableApi.formApi.form.values,
    {
      fieldMappingTime: formOptions.fieldMappingTime,
    },
  );
}
const open = ref(false);
const prolist = ref<any[]>([]);
const pinfo = ref<any>({});
function stockout(row: any) {
  open.value = true;
  row.orderList.map((item: any) => {
    item.count = 0;
    item.wuliu = '';
    item.remark = '';
  });
  prolist.value = JSON.parse(JSON.stringify(row.orderList));
  pinfo.value = row;
  const use=useUserStore();
  useinfo.value=use.userInfo;
}
function onClose() {
  open.value = false;
  recopen.value = false;
}
async function toprint(row:any){
  
  
    prolist.value=row;
    await new Promise((resolve) => setTimeout(resolve, 300));
    printEvent()
  
  
}
async function onSave() {
  let isValid = true;

  for (const item of prolist.value) {
    item.count = Number(item.count);

    if (item.count <= 0) {
      message.warning('数量不能为空或小于0');
      isValid = false;
      break;
    }
    if (item.count > Number(item.stockVo.nowstock)) {
      message.warning('数量不能大于库存');
      isValid = false;
      break;
    }
  }

  // 如果不满足条件，可以执行一些额外逻辑
  if (!isValid) {
    // message.warning('存在不满足条件的元素，操作未完成');
    return; // 提前退出函数
  }

  // 所有元素都满足条件时，执行 confimout.value = true
  confimout.value = true;
}
async function handleOk() {
  const uniqueCode = generateUniqueCode();
  for (const item of prolist.value) {
    let saout = {
      orderid: pinfo.value.id,
      stockid: item.stockid,
      counts: item.count,
      remark: item.remark,
      status: '1',
      sn: uniqueCode,
      wuliu: item.wuliu,
      custno: pinfo.value.custno,
    };
    await saleoutAdd(saout);
  }
  message.success('出库成功！');
  open.value = false;
  confimout.value = false;
  tableApi.query();
}
const generateUniqueCode = (): string => {
  const timestamp = new Date().getTime().toString(36); // 时间戳
  const random = Math.random().toString(36).substring(2, 10); // 随机数
  return `${timestamp}-${random}`;
};

function deleprolit(row: any) {
  const index = prolist.value.findIndex((item) => item.id === row.id);
  prolist.value.splice(index, 1);
}
const confimout = ref(false);
const useinfo=ref<any>({});
const printEvent = () => {
 
  printJS({
    printable: 'printView',
    type: 'html',
    style:
      '.print-css { font-size: 10px; width: 100%;} input{border:none;} .ccen{width: 100%;margin:auto;text-align:center;padding-top:10px;font-size:16px;} .yk-table{font-size: 7px;}',
    margin: 1,
    header: useinfo.value.realName,
    headerStyle: 'font-size:20px; text-align: center;',
  });
};
// const printEvent = async (item: any) => {
//   // 打印样式
//   const printStyle = `
//   .title {
//     text-align: center;
//   }
//   .my-list-row {
//     display: inline-block;
//     width: 100%;
//   }
//   .my-list-col {
//     float: left;
//     width: 33.33%;
//     height: 28px;
//     line-height: 28px;
//   }
//   .my-top,
//   .my-bottom {
//     font-size: 12px;
//   }
//   .my-top {
//     margin-bottom: 5px;
//   }
//   .my-bottom {
//     margin-top: 30px;
//     text-align: right;
//   }
//   `;

//   // 打印顶部内容模板
// const topHtml = `
//         <h1 class="title">出货单据</h1>
//         <div class="my-top">
//           <div class="my-list-row">
//             <div class="my-list-col">商品名称：vxe-table</div>
//             <div class="my-list-col">发货单号：X2665847132654</div>
//             <div class="my-list-col">发货日期：2020-09-20</div>
//           </div>
//           <div class="my-list-row">
//             <div class="my-list-col">收货姓名：小徐</div>
//             <div class="my-list-col">收货地址：火星第七区18号001</div>
//             <div class="my-list-col">联系电话：10086</div>
//           </div>
//         </div>
//         `;
// // 打印底部内容模板
// const bottomHtml = `
//         <div class="my-bottom">
//           <div class="my-list-row">
//             <div class="my-list-col"></div>
//             <div class="my-list-col">创建人：小徐</div>
//             <div class="my-list-col">创建日期：2020-09-20</div>
//           </div>
//         </div>
//         `;

//     // let canvasUrl = Qr.toDataURL('image/png')
//     console.log(item);
//     print({
//       sheetName: item.title + '质检',
//       style: printStyle,
//       data: [item],
//       beforePrintMethod({ html }) {
//         console.log(html);
//         return topHtml + html + bottomHtml;
//       },
//     });
// };
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="销售订单列表">
      <template #expand_content="{ row }">
        <div v-for="(item, index) in row.orderList">
          <Descriptions
            :label-col="{ span: 2 }"
            class="m-4"
            :column="2"
            size="small"
            bordered
          >
            <DescriptionsItem label="需求物料名称"
              ><p class="text-rose-800">
                {{ item.stockVo.tilte }}
              </p></DescriptionsItem
            >
            <DescriptionsItem label="规格"
              ><p class="text-rose-800">
                {{ item.stockVo.gk }}
              </p></DescriptionsItem
            >
            <DescriptionsItem label="库存数量">{{
              item.stockVo.nowstock
            }}</DescriptionsItem>
            <DescriptionsItem label="需求数量">{{
              item.counts
            }}</DescriptionsItem>
            <DescriptionsItem label="已出库数量" :span="2">{{
              item.ckcounts
            }}</DescriptionsItem>
          </Descriptions>
        </div>
      </template>
      <template #toolbar-tools>
        <Space>
          <Button
            v-access:code="['mes:order:export']"
            @click="handleDownloadExcel"
          >
            {{ $t('pages.common.export') }}
          </Button>
          <Button
            :disabled="!vxeCheckboxChecked(tableApi)"
            danger
            type="primary"
            v-access:code="['mes:order:remove']"
            @click="handleMultiDelete"
          >
            {{ $t('pages.common.delete') }}
          </Button>
          <!-- <Button
            type="primary"
            v-access:code="['mes:order:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </Button> -->
        </Space>
      </template>
      <template #sm="{ row }">
        <div v-if="row.tsta == 1">
          <Button type="dashed" size="small" danger @click="stockout(row)"
            >执行出库</Button
          >
          <!-- <span
            class="inline-flex items-center rounded-md bg-yellow-50 px-2 py-1 text-xs font-medium text-yellow-800 ring-1 ring-inset ring-yellow-600/20"
            >未完成，点开详情操作</span
          > -->
        </div>
      </template>
      <template #action="{ row }">
            <Button
              danger
              v-access:code="['mes:order:remove']"
              @click="handleDelete(row)"
            >
              已出库记录
            </Button>
      </template>
    </BasicTable>
    <OrderDrawer @reload="tableApi.query()" />
    <Drawer
      title="关闭"
      :width="720"
      :open="recopen"
      :body-style="{ paddingBottom: '80px' }"
      :footer-style="{ textAlign: 'right' }"
      @close="onClose"
    >
      <div v-for="(item, index) in infolist">
        <Card class="mt-2">
          <template #title>批次号：{{index}}</template>
          <template #extra><p class="text-blue-500 cursor-pointer" @click="toprint(item)">补打单据</p></template>
          <p v-for="(item, index) in item">
            <p class="text-black">物料名称：{{item.stockVo.tilte}}</p>
            <p>数量：{{ item.counts }}</p>
            <p>备注：{{ item.remark }}</p>
            <p>物流信息：{{ item.wuliu }}</p>
            <p>时间:{{item.createTime}}</p>
            <Divider/>
          </p>
        </Card>
      </div>
    </Drawer>
    <Drawer
      title="关闭"
      :width="720"
      :open="open"
      :body-style="{ paddingBottom: '80px' }"
      :footer-style="{ textAlign: 'right' }"
      @close="onClose"
    >
      <Descriptions
        :title="item.stockVo.tilte"
        class="m-2"
        bordered
        v-for="(item, index) in prolist"
      >
        <DescriptionsItem label="物料名称">{{
          item.stockVo.tilte
        }}</DescriptionsItem>
        <DescriptionsItem label="规格">{{ item.stockVo.gk }}</DescriptionsItem>
        <DescriptionsItem label="编码">{{ item.stockVo.sn }}</DescriptionsItem>
        <DescriptionsItem label="已出库数量" :span="3"> 123 </DescriptionsItem>
        <DescriptionsItem label="出库数量" :span="3">
          <InputNumber
            v-model:value="item.count"
            style="width: 200px"
            :min="1"
            :max="100000"
            :step="0.0001"
            string-mode
          />
        </DescriptionsItem>
        <DescriptionsItem label="出库价格" :span="3">
          <InputNumber
            v-model:value="item.prices"
            style="width: 200px"
            :min="1"
            :max="100000"
            :step="0.0001"
            string-mode
          />
        </DescriptionsItem>
        <DescriptionsItem label="物流信息" :span="3">
          <Input
            v-model:value="item.wuliu"
            style="width: 400px"
            :min="1"
            :max="100000"
            :step="0.0001"
            string-mode
          />
        </DescriptionsItem>
        <DescriptionsItem label="操作" :span="3">
          <Button type="dashed" danger @click="deleprolit(item)">去除</Button>
        </DescriptionsItem>
      </Descriptions>
      <Divider />
      <Row class="item-center" align="top">
        <Col :span="6">
          <Button @click="printEvent">打印</Button>
        </Col>
        <Col :span="6">
          <Button type="primary" @click="onSave">出库</Button>
        </Col>
      </Row>
    </Drawer>
    <div class="hidden">
      <div
        id="printView"
        style="width: 100%; padding-right: 5px; padding-left: 5px"
      >
        <table
          class="table"
          border="0pm"
          cellspacing="0"
          align="left"
          width="100%"
          style="
            font-family: SimSun;
            font-size: 12px;
            word-break: break-all;
            word-wrap: break-word;
            table-layout: fixed;
          "
        >
          <tr>
            <td style="font-size: 12px" align="center" colspan="3">
              地址：{{useinfo.address}}
            </td>
          </tr>
          <tr>
            <td
              style="font-size: 12px; line-height: 0px"
              align="center"
              colspan="3"
            >
              电话：{{useinfo.phonenumber}}
            </td>
          </tr>
          <tr>
            <td align="left"></td>
            <td
              style="font-size: 15px; font-weight: bolder"
              align="right"
              colspan="2"
            >
              单据编号：{{ pinfo.id }}
            </td>
          </tr>
          <tr style="line-height: 0px">
            <td align="left">购货单位:{{ pinfo.custname }}</td>
            <td align="left"></td>
            <!-- <td style="padding:5px;" align="left">制单人：{{ ww }}</td> -->
          </tr>
          <tr>
            <td align="left">
              地址:{{ pinfo.customVo ? pinfo.customVo.address1 : '' }}
            </td>
            <td align="left">
              联系人:{{ pinfo.customVo ? pinfo.customVo.cname : '' }}
            </td>
            <td align="left" v-if="pinfo.createTime">
              出库时间:{{ pinfo.createTime }}
            </td>
            <td align="left" v-else>
              出库时间:{{ new Date().toLocaleDateString() }}
            </td>
          </tr>
        </table>
        <table
          class="yk-table"
          border="1pm"
          cellspacing="0"
          align="center"
          width="95%"
          style="
            font-family: SimSun;
            font-size: 10px;
            word-break: break-all;
            word-wrap: break-word;
            table-layout: fixed;
          "
        >
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
          <tr v-for="(item, index) in prolist" align="center" class="yk-table">
            <td>{{ index }}</td>
            <td>{{ item.stockVo.tilte }}</td>
            <td>{{ item.stockVo.sn }}</td>
            <td>{{ item.stockVo.gk }}</td>
            <td>{{ pinfo.custno }}</td>
            <td>{{ item.stockVo.unitname }}</td>
            <td>
              {{ item.count }}
            </td>
            <td>
              {{ item.stockVo.price }}
            </td>
            <td>
              {{ item.count * item.stockVo.price }}
            </td>
            <td>
              {{ item.stockVo.remark }}
            </td>
          </tr>
          <tr align="center">
            5
            <td colspan="2">合计：</td>
            <td colspan="5"><span align="left">整</span></td>
            <!-- <td>{{ adAmount }}</td>
            <td /> -->
          </tr>
        </table>
        <div style="width: 98%; margin: auto; text-align: center" class="ccen">
          客户签收并盖章：_____________ 送货：____________
        </div>
      </div>
    </div>
    <Modal v-model:open="confimout" title="出库确认" @ok="handleOk">
      <p>请确认已打单！</p>
      <p>确认提交出库？</p>
    </Modal>
  </Page>
</template>
