<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { nextTick,ref } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Popconfirm, Space,Button } from 'ant-design-vue';

import { useVbenVxeGrid, type VxeGridProps } from '#/adapter/vxe-table';
import { stockTypeList, stockTypeRemove } from '#/api/mes/stockType';
import type { StockTypeForm } from '#/api/mes/stockType/model';
import { columns, querySchema } from './data';
import stockTypeDrawer from './stockType-drawer.vue';

const formOptions: VbenFormProps = {
  commonConfig: {
    labelWidth: 80,
    componentProps: {
      allowClear: true,
    },
  },
  schema: querySchema(),
  wrapperClass: 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4',
};

const gridOptions: VxeGridProps = {
  columns,
  height: 'auto',
  keepSource: true,
  pagerConfig: {
    enabled: false,
  },
  proxyConfig: {
    ajax: {
      query: async (_, formValues = {}) => {
        const resp = await stockTypeList({pageNum: 1,pageSize:10,
          ...formValues,
        });
        return { rows: resp };
      },
      // 默认请求接口后展开全部 不需要可以删除这段
      querySuccess: () => {
        nextTick(() => {
          expandAll();
        });
      },
    },
  },
  rowConfig: {
    keyField: 'id',
  },
  /**
  * 虚拟滚动开关 默认关闭
  * 数据量小可以选择关闭
  * 如果遇到样式问题(空白、错位 滚动等)可以选择关闭虚拟滚动
  */
  scrollY: {
    enabled: false,
    gt: 0,
  },
  treeConfig: {
    parentField: 'pid',
    rowField: 'id',
    // 自动转换为tree 由vxe处理 无需手动转换
    transform: true,
  },
  // 表格全局唯一表示 保存列配置需要用到
  id: 'mes-stockType-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({ formOptions, gridOptions });
const [StockTypeDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: stockTypeDrawer,
});

function handleAdd() {
  drawerApi.setData({});
  drawerApi.open();
}


async function handleDelete(row: Required<StockTypeForm>) {
  await stockTypeRemove(row.id);
  await tableApi.query();
}

function expandAll() {
  tableApi.grid?.setAllTreeExpand(true);
}

function collapseAll() {
  tableApi.grid?.setAllTreeExpand(false);
}
const rowinfo = ref<any>({});
defineExpose({
  handleEdit,
  rowinfo,
});
const emit = defineEmits(['child-event']);
function handleEdit(row:any){
  rowinfo.value=row
  emit('child-event', { row });
 
}
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="库存分类列表">
      <template #toolbar-tools>
        <Space>
          <a-button @click="collapseAll">
            {{ $t('pages.common.collapse') }}
          </a-button>
          <a-button @click="expandAll">
            {{ $t('pages.common.expand') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:stockType:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #action="{ row }">
        <Button
            @click="handleEdit(row)"
            v-if="row.sst != 1"
          >
            选择
          </Button>
      </template>
    </BasicTable>
    <StockTypeDrawer @reload="tableApi.query()" />
  </Page>
</template>
