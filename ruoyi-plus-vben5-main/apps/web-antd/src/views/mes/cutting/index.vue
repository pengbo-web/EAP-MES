<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenModal, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space,Drawer,Table,Tag,Button } from 'ant-design-vue';
import dayjs from 'dayjs';

import {   
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps 
} from '#/adapter/vxe-table';

import {
  cuttingExport,
  cuttingList,
  cuttingRemove,
} from '#/api/mes/cutting';
import type { CuttingForm } from '#/api/mes/cutting/model';
import { commonDownloadExcel } from '#/utils/file/download';

import cuttingModal from './cutting-modal.vue';
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
  proxyConfig: {
    ajax: {
      query: async ({ page }, formValues = {}) => {
        return await cuttingList({
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
  id: 'mes-cutting-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [CuttingModal, modalApi] = useVbenModal({
  connectedComponent: cuttingModal,
});

function handleAdd() {
  modalApi.setData({});
  modalApi.open();
}

async function handleEdit(row: Required<CuttingForm>) {
  modalApi.setData({ id: row.id });
  modalApi.open();
}

async function handleDelete(row: Required<CuttingForm>) {
  await cuttingRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<CuttingForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await cuttingRemove(ids);
      await tableApi.query();
    },
  });
}
import { cuttingAdd, cuttingInfo, cuttingUpdate } from '#/api/mes/cutting';
function handleDownloadExcel() {
  commonDownloadExcel(cuttingExport, '刀具管理数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
import CuttingImportModal from './cutting-import-modal.vue';
const [CuttingImporModal, userImportModalApi] = useVbenModal({
  connectedComponent: CuttingImportModal,
});
function handleImport() {
  userImportModalApi.open();
}
const columns3 = [
  {
    title: '数量',
    dataIndex: 'count',
  },
  {
    title: '图片',
    className: 'column-money',
    dataIndex: 'imgs',
  },
  {
    title: '原因',
    dataIndex: 'rebak',
  },
  {
    title: '状态',
    dataIndex: 'type',
  },
  {
    title: '用户',
    dataIndex: 'username',
  },
  {
    title: '日期',
    dataIndex: 'createTime',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
];
const showtoolrec=ref(false)
const reclist=ref<any[]>([])
import { etoolAdd, etoolInfo, etoolUpdate } from '#/api/mes/etool';
async function openToolrec(row:any) {
  showtoolrec.value=true
  let prec=await cuttingInfo(row)
  reclist.value=prec.recVoList
}
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="刀具管理列表">
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:cutting:export']"
            @click="handleDownloadExcel"
          >
            {{ $t('pages.common.export') }}
          </a-button>
          <a-button
              @click="handleImport"
            >
              {{ $t('pages.common.import') }}
            </a-button>
          <a-button
            :disabled="!vxeCheckboxChecked(tableApi)"
            danger
            type="primary" 
            v-access:code="['mes:cutting:remove']" 
            @click="handleMultiDelete">
            {{ $t('pages.common.delete') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:cutting:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #infos="{ row }">
        <Button @click="openToolrec(row.id)">使用记录</Button>
      </template>
      <template #action="{ row }">
        <Space>
          <ghost-button
            v-access:code="['mes:cutting:edit']"
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
              v-access:code="['mes:cutting:remove']"
              @click.stop=""
            >
              {{ $t('pages.common.delete') }}
            </ghost-button>
          </Popconfirm>
        </Space>
      </template>
    </BasicTable>
    <Drawer v-model:open="showtoolrec" width="50%" >
      <Table :columns="columns3" :data-source="reclist">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'type'">
            <Tag v-if="record.status==1" color="cyan">报废</Tag>
            <Tag v-if="record.status==2" color="red">退换</Tag>
            <Tag v-if="record.status==3" color="blue">更换</Tag>
            <Tag v-if="record.status==0" color="green">领用中</Tag>
          </template>
        </template>
      </Table>
    </Drawer>
    <CuttingImporModal @reload="tableApi.query()" />
    <CuttingModal @reload="tableApi.query()" />
  </Page>
</template>
