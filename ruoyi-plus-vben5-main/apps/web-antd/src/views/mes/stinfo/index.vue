<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space,Tag } from 'ant-design-vue';
import dayjs from 'dayjs';
import { stinfoAdd, stinfoInfo, stinfoUpdate } from '#/api/mes/stinfo';
import {   
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps 
} from '#/adapter/vxe-table';

import {
  stinfoExport,
  stinfoList,
  stinfoRemove,
} from '#/api/mes/stinfo';
import type { StinfoForm } from '#/api/mes/stinfo/model';
import { commonDownloadExcel } from '#/utils/file/download';

import stinfoDrawer from './stinfo-drawer.vue';
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
        return await stinfoList({
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
  id: 'mes-stinfo-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [StinfoDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: stinfoDrawer,
});

function handleAdd() {
  drawerApi.setData({});
  drawerApi.open();
}

async function handleEdit(row: any) {
  row.status='1'
  await stinfoUpdate(row);
  await tableApi.query();
}

async function handleDelete(row: any) {
  row.status='2'
  await stinfoUpdate(row);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<StinfoForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await stinfoRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(stinfoExport, '库位调拨数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="库位调拨列表">
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:stinfo:export']"
            @click="handleDownloadExcel"
          >
            {{ $t('pages.common.export') }}
          </a-button>
          <a-button
            :disabled="!vxeCheckboxChecked(tableApi)"
            danger
            type="primary" 
            v-access:code="['mes:stinfo:remove']" 
            @click="handleMultiDelete">
            {{ $t('pages.common.delete') }}
          </a-button>
          <!-- <a-button
            type="primary"
            v-access:code="['mes:stinfo:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button> -->
        </Space>
      </template>
      <template #status="{ row }">
        <Tag color="#2db7f5" v-if="row.status==0">待确认</Tag>
        <Tag color="#87d068" v-if="row.status==1">同意</Tag>
        <Tag color="#f50" v-if="row.status==2">拒绝</Tag>
      </template>
      <template #action="{ row }">
        <Space v-if="row.status==0">
          <ghost-button
            v-access:code="['mes:instock:edit']"
            @click.stop="handleEdit(row)"
          >
           同意
          </ghost-button>
          <Popconfirm
            :get-popup-container="getVxePopupContainer"
            placement="left"
            title="确认拒绝？"
            @confirm="handleDelete(row)"
          >
            <ghost-button
              danger
              v-access:code="['mes:instock:remove']"
              @click.stop=""
            >
             拒绝
            </ghost-button>
          </Popconfirm>
        </Space>
        <div v-else>无法操作</div>
      </template>
    </BasicTable>
    <StinfoDrawer @reload="tableApi.query()" />
  </Page>
</template>
