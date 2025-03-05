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
import { tmodelAdd, tmodelInfo, tmodelUpdate } from '#/api/mes/tmodel';
import {
  tmodelExport,
  tmodelList,
  tmodelRemove,
} from '#/api/mes/tmodel';
import type { TmodelForm } from '#/api/mes/tmodel/model';
import { commonDownloadExcel } from '#/utils/file/download';

import tmodelModal from './tmodel-modal.vue';
import { columns, querySchema } from './data';
import modelrecModal from '#/views/mes/modelrec/modelrec-modal.vue';
const [ModelrecModal, modalApi2] = useVbenModal({
  connectedComponent: modelrecModal,
});
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
     'nexttime',
     ['params[beginnexttime]', 'params[endnexttime]'],
     ['YYYY-MM-DD 00:00:00', 'YYYY-MM-DD 23:59:59'],
   ],
   [
     'otime',
     ['params[beginotime]', 'params[endotime]'],
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
        return await tmodelList({
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
  id: 'mes-tmodel-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [TmodelModal, modalApi] = useVbenModal({
  connectedComponent: tmodelModal,
});

function handleAdd() {
  modalApi.setData({});
  modalApi.open();
}

async function handleEdit(row: Required<TmodelForm>) {
  modalApi.setData({ id: row.id });
  modalApi.open();
}

async function handleDelete(row: Required<TmodelForm>) {
  await tmodelRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<TmodelForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await tmodelRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(tmodelExport, '模具管理数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
const showmodel=ref(false);
const datas=ref<any[]>([])
async function showmodels(row:any){
  
  let rec=await tmodelInfo(row);
  datas.value=rec.reclist;
  console.log(rec)
  showmodel.value=true;
}
function useadd(){
  modalApi2.open()
}
const columns3 = [
  {
    title: '使用位置',
    dataIndex: 'wname',
  },
  {
    title: '类型',
    className: 'column-money',
    dataIndex: 'type',
  },
  {
    title: '外发名称',
    dataIndex: 'cname',
  },
  {
    title: '联系电话',
    dataIndex: 'phone',
  },
  {
    title: '地址',
    dataIndex: 'address',
  },
  {
    title: '日期',
    dataIndex: 'endtime',
  },
  {
    title: '状态',
    dataIndex: 'status',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
];
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="模具管理列表">
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:tmodel:export']"
            @click="handleDownloadExcel"
          >
            {{ $t('pages.common.export') }}
          </a-button>
          <a-button
            :disabled="!vxeCheckboxChecked(tableApi)"
            danger
            type="primary" 
            v-access:code="['mes:tmodel:remove']" 
            @click="handleMultiDelete">
            {{ $t('pages.common.delete') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:tmodel:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #toadd="{row}">
        <ghost-button @click.stop="useadd()">
          添加记录
        </ghost-button>
      </template>
      <template #infos="{row}">
        <ghost-button @click.stop="showmodels(row.id)" danger>
          查看记录
        </ghost-button>
      </template>
      <template #action="{ row }">
        <Space>
          <ghost-button
            v-access:code="['mes:tmodel:edit']"
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
              v-access:code="['mes:tmodel:remove']"
              @click.stop=""
            >
              {{ $t('pages.common.delete') }}
            </ghost-button>
          </Popconfirm>
        </Space>
      </template>
    </BasicTable>
    <Drawer v-model:open="showmodel" width="50%" >
      <Table :columns="columns3" :data-source="datas">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'status'">
            <Tag v-if="record.status==1" color="cyan">已归还</Tag>
            <Tag v-else color="red">未归还</Tag>
          </template>
        </template>
      </Table>
    </Drawer>
    <TmodelModal @reload="tableApi.query()" />
    <ModelrecModal @reload="tableApi.query()" />
  </Page>
</template>
