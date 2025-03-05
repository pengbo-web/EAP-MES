<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space,Tag,Button} from 'ant-design-vue';
import dayjs from 'dayjs';

import {   
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps 
} from '#/adapter/vxe-table';

import {
  orderExport,
  orderList,
  orderRemove,
} from '#/api/mes/order';
import type { OrderForm } from '#/api/mes/order/model';
import { commonDownloadExcel } from '#/utils/file/download';

import orderDrawer from './order-drawer.vue';
import { columns, querySchema } from './data';
import { orderAdd, orderInfo, orderUpdate,bequickapi } from '#/api/mes/order';
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
    height:120
  },
  // 表格全局唯一表示 保存列配置需要用到
  id: 'mes-order-index'
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

async function handleDelete(row: Required<OrderForm>) {
  await orderRemove(row.id);
  await tableApi.query();
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
  commonDownloadExcel(orderExport, '销售订单数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
async function changeCk(row:any){
  row.tsta=1
  await orderUpdate(row)
  await tableApi.query()
}
async function bequick(row:any){
  await bequickapi(row.id)
  await tableApi.query()
}
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="销售订单列表">
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:order:export']"
            @click="handleDownloadExcel"
          >
            {{ $t('pages.common.export') }}
          </a-button>
          <a-button
            :disabled="!vxeCheckboxChecked(tableApi)"
            danger
            type="primary" 
            v-access:code="['mes:order:remove']" 
            @click="handleMultiDelete">
            {{ $t('pages.common.delete') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:order:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #tsta="{ row }">
        <div v-if="row.status == '0'">
              <div class="block mt-2" v-if="row.tsta == 0">
                <div class="mt-2">
                  <Button type="primary" plain size="small" @click="changeCk(row)">出库申请</Button>
                </div>
                <div class="mt-2">
                  <Button type="dashed" danger size="small" @click="bequick(row)" v-if="row.istop==0">加急订单</Button>
                  <div v-else>已加急订单</div>
                </div>
                <div class="mt-2">
                  <Button class="bg-green-500 text-white"  plain size="small" >MRP计算</Button>
                </div>
              </div>
              <div class="block mt-2" v-if="row.tsta == 1">
                <Tag color="green" size="small"  class="text-blod">已提交</Tag>
                <Tag color="cyan" class="text-blod">未出库完成</Tag>
              </div>
              <div class="block mt-2" v-if="row.tsta == 3">
                <Tag color="blue" class="text-blod">已完成</Tag>
              </div>
            </div>
            <div class="block mt-2" v-else>
              <Tag color="purple" class="text-blod">已完成</Tag>
            </div>
      </template>
      <template #status="{ row }">
      <div v-if="row.status=='0'">未完成</div>
      <div v-else>已完成</div>
      </template>
      <template #action="{ row }">
        <Space>
          <ghost-button
            v-access:code="['mes:order:edit']"
            @click.stop="handleEdit(row)"
          >
            {{ $t('pages.common.edit') }}
          </ghost-button>
          <Popconfirm
            :get-popup-container="getVxePopupContainer"
            placement="left"
            title="确认删除？"
            @confirm="handleDelete(row)"
          >
            <ghost-button
              danger
              v-access:code="['mes:order:remove']"
              @click.stop=""
            >
              {{ $t('pages.common.delete') }}
            </ghost-button>
          </Popconfirm>
        </Space>
      </template>
    </BasicTable>
    <OrderDrawer @reload="tableApi.query()" />
  </Page>
</template>
