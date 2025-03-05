<script setup lang="ts">
import type { VbenFormProps } from '@vben/common-ui';

import type { VxeGridProps } from '#/adapter/vxe-table';
import type { Dept } from '#/api/system/dept/model';

import { nextTick,ref } from 'vue';

import { Page, useVbenDrawer } from '@vben/common-ui';
import { eachTree, getVxePopupContainer } from '@vben/utils';

import { Popconfirm, Space } from 'ant-design-vue';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { deptList, deptRemove } from '#/api/system/dept';

import { columns, querySchema } from './data';
import deptDrawer from './dept-drawer.vue';

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
        const resp = await deptList({
          ...formValues,
        });
        return { rows: resp };
      },
      // 默认请求接口后展开全部 不需要可以删除这段
      querySuccess: () => {
        // 默认展开 需要加上标记
        // eslint-disable-next-line no-use-before-define
        eachTree(tableApi.grid.getData(), (item) => (item.expand = true));
        nextTick(() => {
          setExpandOrCollapse(true);
        });
      },
    },
  },
  /**
   * 虚拟滚动  默认关闭
   */
  scrollY: {
    enabled: false,
    gt: 0,
  },
  rowConfig: {
    keyField: 'deptId',
  },
  treeConfig: {
    parentField: 'parentId',
    rowField: 'deptId',
    transform: true,
  },
  id: 'system-dept-index',
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
  gridEvents: {
    cellDblclick: (e) => {
      const { row = {} } = e;
      if (!row?.children) {
        return;
      }
      const isExpanded = row?.expand;
      tableApi.grid.setTreeExpand(row, !isExpanded);
      row.expand = !isExpanded;
    },
    // 需要监听使用箭头展开的情况 否则展开/折叠的数据不一致
    toggleTreeExpand: (e) => {
      const { row = {}, expanded } = e;
      row.expand = expanded;
    },
  },
});
const [DeptDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: deptDrawer,
});

function handleAdd() {
  drawerApi.setData({ update: false });
  drawerApi.open();
}

function handleSubAdd(row: Dept) {
  const { deptId } = row;
  drawerApi.setData({ id: deptId, update: false });
  drawerApi.open();
}


async function handleDelete(row: Dept) {
  await deptRemove(row.deptId);
  await tableApi.query();
}

/**
 * 全部展开/折叠
 * @param expand 是否展开
 */
function setExpandOrCollapse(expand: boolean) {
  eachTree(tableApi.grid.getData(), (item) => (item.expand = expand));
  tableApi.grid?.setAllTreeExpand(expand);
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
    <BasicTable table-title="部门列表" table-title-help="双击展开/收起子菜单">
      <template #toolbar-tools>
        <Space>
          <a-button @click="setExpandOrCollapse(false)">
            {{ $t('pages.common.collapse') }}
          </a-button>
          <a-button @click="setExpandOrCollapse(true)">
            {{ $t('pages.common.expand') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['system:dept:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
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
    <DeptDrawer @reload="tableApi.query()" />
  </Page>
</template>
