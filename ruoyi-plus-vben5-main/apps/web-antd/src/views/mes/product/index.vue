<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps,useVbenModal } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space,Avatar } from 'ant-design-vue';
import dayjs from 'dayjs';
const [ProductImpotModal, userImportModalApi] = useVbenModal({
  connectedComponent: ProductImportModal,
});
import {   
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps 
} from '#/adapter/vxe-table';
function handleImport() {
  userImportModalApi.open();
}
import {
  productExport,
  productList,
  productLists,
  productRemove,
} from '#/api/mes/product';

import type { ProductForm } from '#/api/mes/product/model';
import { commonDownloadExcel } from '#/utils/file/download';
import { useRouter } from 'vue-router';
import productDrawer from './product-drawer.vue';
import ProductImportModal from './product-import-modal.vue';
import Bomindex from './bomindex.vue';
import { columns, querySchema } from './data';
const router = useRouter();
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
        return await productLists({
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
  id: 'system-product-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [ProductDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: productDrawer,
});

function handleAdd() {
  drawerApi.setData({});
  drawerApi.open();
}

async function handleEdit(row: Required<ProductForm>) {
  drawerApi.setData({ id: row.id });
  drawerApi.open();
}

async function handleDelete(row: Required<ProductForm>) {
  await productRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<ProductForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await productRemove(ids);
      await tableApi.query();
    },
  });
}

function openlist() {
  // router.push({ path: '/mes/product/bomindex' });
}
const bomopen=ref(false);
const indexinfo=ref<any>({});
  const bominfo=ref<any>({});
async function bomopent(row:any){
  await Promise.all([ indexinfo.value=row]);
  bomopen.value=(true);

}
function handleDownloadExcel() {
  commonDownloadExcel(productExport, '产品管理数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="产品管理列表">
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:product:export']"
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
            v-access:code="['mes:product:remove']" 
            @click="handleMultiDelete">
            {{ $t('pages.common.delete') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:product:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #imgs="{row}">
        <Avatar  :src="row.avatar || ''" />
      </template>
      <template #downcount="{row}">
        <p class="text-blue-600 cursor-pointer" @click="bomopent(row)">Bom({{row.downcount-1}})</p>
      </template>
      <template #action="{ row }">
        <Space>
          <ghost-button
            v-access:code="['mes:product:edit']"
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
              v-access:code="['mes:product:remove']"
              @click.stop=""
            >
              {{ $t('pages.common.delete') }}
            </ghost-button>
          </Popconfirm>
        </Space>
      </template>
    </BasicTable>
    <ProductImpotModal @reload="tableApi.query()" />
    <ProductDrawer @reload="tableApi.query()" />
    <Modal
      v-model:open="bomopen"
      width="100%"
      wrap-class-name="full-modal"
    >
      <Bomindex ref="bominfo" :rowinfo="indexinfo" />
    </Modal>
  </Page>
</template>
