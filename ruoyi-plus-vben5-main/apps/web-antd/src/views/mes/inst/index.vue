<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space,Drawer,Table } from 'ant-design-vue';
import dayjs from 'dayjs';

import {   
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps 
} from '#/adapter/vxe-table';

import {
  instExport,
  instList,
  instRemove,
} from '#/api/mes/inst';
import type { InstForm } from '#/api/mes/inst/model';
import { commonDownloadExcel } from '#/utils/file/download';

import instDrawer from './inst-drawer.vue';
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
        console.log('formValues', formValues);
        formValues.ispri=0;
        return await instList({
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
  id: 'mes-inst-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [InstDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: instDrawer,
});

function handleAdd() {
  drawerApi.setData({});
  drawerApi.open();
}

async function handleEdit(row: Required<InstForm>) {
  drawerApi.setData({ id: row.id });
  drawerApi.open();
}

async function handleDelete(row: Required<InstForm>) {
  await instRemove(row.id);
  await tableApi.query();
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
    dataIndex: 'count',
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
const listopen=ref(false);
const datainfo=ref<any[]>([]);
function showlist(row:any){
  listopen.value=true;
  datainfo.value=[];
  row.inrecVo.map((item:any)=>{
    let params={
    title:item.stockVo.tilte,
    sn:item.stockVo.sn,
    gk:item.stockVo.gk,
    remark:item.remark,
    orderid:item.orderid,
    id:item.stockVo.id,
    stockid:item.stockVo.id,
    instid:row.id,
    instsn:row.sn,
    count:item.count
  }
  console.log(params)
  datainfo.value.push(params)
  })
  
}
function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<InstForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await instRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(instExport, '其他入库数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="其他入库列表">
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:inst:export']"
            @click="handleDownloadExcel"
          >
            {{ $t('pages.common.export') }}
          </a-button>
          <a-button
            :disabled="!vxeCheckboxChecked(tableApi)"
            danger
            type="primary" 
            v-access:code="['mes:inst:remove']" 
            @click="handleMultiDelete">
            {{ $t('pages.common.delete') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:inst:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #action="{ row }">
        <Space>
          <ghost-button
            v-access:code="['mes:inst:edit']"
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
              v-access:code="['mes:inst:remove']"
              @click.stop=""
            >
              {{ $t('pages.common.delete') }}
            </ghost-button>
          </Popconfirm>
        </Space>
      </template>
      <template #infos="{ row }">
        <vxe-button mode="text" status="primary" @click="showlist(row)" class="text-blue-600 cursor-pointer">关联物料</vxe-button>
      </template>
    </BasicTable>
    <InstDrawer @reload="tableApi.query()"/>
    <Drawer v-model:open="listopen"  width="50%" >
      <Table :columns="columns3" :data-source="datainfo">
        </Table>
    </Drawer>
  </Page>
</template>
