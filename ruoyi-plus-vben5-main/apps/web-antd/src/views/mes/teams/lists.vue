<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenModal, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space } from 'ant-design-vue';
import dayjs from 'dayjs';

import {   
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps 
} from '#/adapter/vxe-table';

import {
  teamsExport,
  teamsList,
  teamsRemove,
} from '#/api/mes/teams';
import type { TeamsForm } from '#/api/mes/teams/model';
import { commonDownloadExcel } from '#/utils/file/download';

import teamsModal from './teams-modal.vue';
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
        return await teamsList({
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
  id: 'mes-teams-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [TeamsModal, modalApi] = useVbenModal({
  connectedComponent: teamsModal,
});

function handleAdd() {
  modalApi.setData({});
  modalApi.open();
}

async function handleDelete(row: Required<TeamsForm>) {
  await teamsRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<TeamsForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await teamsRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(teamsExport, '班组管理数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
const rowinfo = ref<any[]>([]);
defineExpose({
  handleEdit,
  rowinfo,
});
async function handleEdit(row: Required<StockForm>) {
  row.sst = 1;
  rowinfo.value.push(row);
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
    <BasicTable table-title="班组管理列表">
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
    <TeamsModal @reload="tableApi.query()" />
  </Page>
</template>
