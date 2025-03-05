<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref, defineExpose } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space } from 'ant-design-vue';
import dayjs from 'dayjs';
import {
  SmileTwoTone,
  HeartTwoTone,
  CheckCircleTwoTone,
  CheckOutlined,
} from '@ant-design/icons-vue';
import {
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps,
} from '#/adapter/vxe-table';

import { stockExport, stockList, stockRemove } from '#/api/mes/stock';
import type { StockForm } from '#/api/mes/stock/model';
import { commonDownloadExcel } from '#/utils/file/download';

import stockDrawer from './stock-drawer.vue';
import { columns, querySchema } from './datasea';
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
  // fieldMappingTime: [
  //  [
  //    'createTime',
  //    ['params[beginTime]', 'params[endTime]'],
  //    ['YYYY-MM-DD 00:00:00', 'YYYY-MM-DD 23:59:59'],
  //  ],
  // ],
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
  cellStyle({ row, column }) {
    if (column.field === 'sst') {
      console.log(row);
      if (row.sst >= '1') {
        return {
          backgroundColor: '#187',
        };
      } else if (row.age === 26) {
        return {
          backgroundColor: '#2db7f5',
        };
      }
    }
  },
  proxyConfig: {
    ajax: {
      query: async ({ page }, formValues = {}) => {
        return await stockList({
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
  id: 'mes-stock-index',
};
const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});
const rowinfo = ref<any[]>([]);
defineExpose({
  handleEdit,
  rowinfo,
});
const [StockDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: stockDrawer,
});

function handleAdd() {
  drawerApi.setData({});
  drawerApi.open();
}

async function handleEdit(row: Required<StockForm>) {
  row.sst = 1;
  rowinfo.value.push(row);
}

async function handleDelete(row: Required<StockForm>) {
  await stockRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<StockForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await stockRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(
    stockExport,
    '仓库信息数据',
    tableApi.formApi.form.values,
    {
      fieldMappingTime: formOptions.fieldMappingTime,
    },
  );
}
function deletes(row:any){
  const index=rowinfo.value.findIndex(item => item === row);
  console.log(index)
  row.sst=0
  rowinfo.value.splice(index,1)
}
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="仓库信息列表">
     
      <template #action="{ row }">
        <Space>
          <ghost-button
            v-access:code="['mes:stock:edit']"
            @click.stop="handleEdit(row)"
            v-if="row.sst != 1"
          >
            选择
          </ghost-button>
          <view v-if="row.sst == 1">
            <CheckOutlined style="color: #52c41a" />
          </view>
          <view v-if="row.sst == 1">
            <ghost-button
            danger
              @click.stop="deletes(row)"
            >
              删除
            </ghost-button>
          </view>
        </Space>
      </template>
    </BasicTable>
    <StockDrawer @reload="tableApi.query()" />
  </Page>
</template>
