<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenModal, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space,Drawer,Table,Tag } from 'ant-design-vue';
import dayjs from 'dayjs';

import {   
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps 
} from '#/adapter/vxe-table';

import {
  etoolExport,
  etoolList,
  etoolRemove,
} from '#/api/mes/etool';
import type { EtoolForm } from '#/api/mes/etool/model';
import { commonDownloadExcel } from '#/utils/file/download';

import etoolModal from './etool-modal.vue';
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
        return await etoolList({
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
  id: 'mes-etool-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [EtoolModal, modalApi] = useVbenModal({
  connectedComponent: etoolModal,
});

function handleAdd() {
  modalApi.setData({});
  modalApi.open();
}

async function handleEdit(row: Required<EtoolForm>) {
  modalApi.setData({ id: row.id });
  modalApi.open();
}

async function handleDelete(row: Required<EtoolForm>) {
  await etoolRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<EtoolForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await etoolRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(etoolExport, '工装夹具数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
import EtoolImportModal from './etool-import-modal.vue';
const [EtoolImporModal, userImportModalApi] = useVbenModal({
  connectedComponent: EtoolImportModal,
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
  let prec=await etoolInfo(row)
  reclist.value=prec.recVoList
}
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="工装夹具列表">
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:etool:export']"
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
            v-access:code="['mes:etool:remove']" 
            @click="handleMultiDelete">
            {{ $t('pages.common.delete') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:etool:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #infos="{ row }">
        <Button @click="openToolrec(row.id)">查看记录</Button>
      </template>
      <template #action="{ row }">
        <Space>
          <ghost-button
            v-access:code="['mes:etool:edit']"
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
              v-access:code="['mes:etool:remove']"
              @click.stop=""
            >
              {{ $t('pages.common.delete') }}
            </ghost-button>
          </Popconfirm>
        </Space>
      </template>
    </BasicTable>
    <EtoolImporModal @reload="tableApi.query()" />
    <EtoolModal @reload="tableApi.query()" />
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
  </Page>
</template>
