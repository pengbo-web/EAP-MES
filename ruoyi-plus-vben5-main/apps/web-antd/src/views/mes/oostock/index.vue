<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import {
  Modal,
  Popconfirm,
  Space,
  Button,
  Drawer,
  Table,
} from 'ant-design-vue';
import dayjs from 'dayjs';

import {
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps,
} from '#/adapter/vxe-table';

import {
  outorderExport,
  outorderList,
  outorderRemove,
} from '#/api/mes/outorder';
import type { OutorderForm } from '#/api/mes/outorder/model';
import { commonDownloadExcel } from '#/utils/file/download';
import {
  outorderDeAdd,
  outorderDeInfo,
  outorderDeUpdate,
} from '#/api/mes/outorderDe';
import { outorderInfo } from '#/api/mes/outorder';
import outorderDrawer from './outorder-drawer.vue';
import { columns, querySchema } from './data';

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
      'subtime',
      ['params[beginSubtime]', 'params[endSubtime]'],
      ['YYYY-MM-DD 00:00:00', 'YYYY-MM-DD 23:59:59'],
    ],
    [
      'outtime',
      ['params[beginOuttime]', 'params[endOuttime]'],
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
        return await outorderList({
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
  id: 'mes-outorder-index',
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [OutorderDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: outorderDrawer,
});

function handleAdd() {
  drawerApi.setData({});
  drawerApi.open();
}

async function handleEdit(row: Required<OutorderForm>) {
  drawerApi.setData({ id: row.id });
  drawerApi.open();
}

async function handleDelete(row: Required<OutorderForm>) {
  await outorderRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<OutorderForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await outorderRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(
    outorderExport,
    '委外订单数据',
    tableApi.formApi.form.values,
    {
      fieldMappingTime: formOptions.fieldMappingTime,
    },
  );
}
const showorder = ref(false);
const datainfo = ref<any[]>([]);
async function glorder(id: number) {
  const record = await outorderInfo(id);
  datainfo.value = record.devo;
  datainfo.value.forEach((item) => {
    item.title = item.stockVo.tilte;
    item.gk = item.stockVo.gk;
    item.sn = item.stockVo.sn;
  });
  showorder.value = true;
}
const columns3 = [
  {
    title: '名称',
    dataIndex: 'title',
  },
  {
    title: '编码',
    className: 'column-money',
    dataIndex: 'sn',
  },
  {
    title: '规格',
    dataIndex: 'gk',
  },
  {
    title: '数量',
    dataIndex: 'counts',
  },
  {
    title: '订单号',
    dataIndex: 'orderid',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
];
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="委外订单列表">
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:outorder:export']"
            @click="handleDownloadExcel"
          >
            {{ $t('pages.common.export') }}
          </a-button>
          <a-button
            :disabled="!vxeCheckboxChecked(tableApi)"
            danger
            type="primary"
            v-access:code="['mes:outorder:remove']"
            @click="handleMultiDelete"
          >
            {{ $t('pages.common.delete') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:outorder:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #action="{ row }">
        <Button @click="glorder(row.id)">关联物料</Button>
        <!-- <Space>
          <ghost-button
            v-access:code="['mes:outorder:edit']"
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
              v-access:code="['mes:outorder:remove']"
              @click.stop=""
            >
              {{ $t('pages.common.delete') }}
            </ghost-button>
          </Popconfirm>
        </Space> -->
      </template>
    </BasicTable>
    <OutorderDrawer @reload="tableApi.query()"  width="600"/>
    <Drawer v-model:open="showorder" width="60%">
      <Table :columns="columns3" :data-source="datainfo"> </Table>
    </Drawer>
  </Page>
</template>
