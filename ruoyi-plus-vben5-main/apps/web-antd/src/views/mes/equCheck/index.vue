<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref } from 'vue';

import { getVxePopupContainer } from '@vben/utils';

import { Modal, Popconfirm, Space,Button,QRCode } from 'ant-design-vue';
import dayjs from 'dayjs';

import {   
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps 
} from '#/adapter/vxe-table';
import {
  Page,
  useVbenModal,
  type VbenFormProps,
  useVbenDrawer,
  VbenButton,
} from '@vben/common-ui';
import {
  equCheckExport,
  equCheckList,
  equCheckRemove,
} from '#/api/mes/equCheck';
import type { EquCheckForm } from '#/api/mes/equCheck/model';
import { commonDownloadExcel } from '#/utils/file/download';

import equCheckModal from './equCheck-modal.vue';
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
        return await equCheckList({
          pageNum: page.currentPage,
          pageSize: page.pageSize,
          ...formValues,
        });
      },
    },
  },
  rowConfig: {
    keyField: 'id',
    height:100
  },
  // 表格全局唯一表示 保存列配置需要用到
  id: 'mes-equCheck-index'
};

const [BasicTable, tableApi] = useVbenVxeGrid({
  formOptions,
  gridOptions,
});

const [EquCheckModal, modalApi] = useVbenModal({
  connectedComponent: equCheckModal,
});

function handleAdd() {
  modalApi.setData({});
  modalApi.open();
}

async function handleEdit(row: Required<EquCheckForm>) {
  modalApi.setData({ id: row.id });
  modalApi.open();
}

async function handleDelete(row: Required<EquCheckForm>) {
  await equCheckRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<EquCheckForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await equCheckRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(equCheckExport, '设备点检数据', tableApi.formApi.form.values, {
    fieldMappingTime: formOptions.fieldMappingTime,
  });
}
import listdrwer from './Listinfo.vue';
const [tenantDrawer, drawerApi] = useVbenDrawer({
  connectedComponent: listdrwer,
});
function selectSubList(row: any) {
  drawerApi.setData({ id: row.id });
  drawerApi.open();
}
const qrcodeCanvasRef = ref();
const printEvent = async (item: any) => {
  // 打印样式
  const printStyle = `
  .title {
    text-align: center;
  }
  .my-list-row {
    display: inline-block;
    width: 100%;
  }
  .my-list-col {
    float: left;
    width: 33.33%;
    height: 28px;
    line-height: 28px;
  }
  .my-top,
  .my-bottom {
    font-size: 12px;
  }
  .my-top {
    margin-bottom: 5px;
  }
  .my-bottom {
    margin-top: 30px;
    text-align: right;
  }
  `;

  // 打印顶部内容模板
  const topHtml =
    `
  <h1 class="title">` +
    item.title +
    `</h1>
  <div class="my-top">
    <div class="my-list-row">
      <div class="my-list-col">质检名称：` +
    item.title +
    `</div>
    </div>
  </div>
  `;

  // 打印底部内容模板
  const bottomHtml = `
  <div class="my-bottom">
    <div class="my-list-row">
      <div class="my-list-col"></div>
    </div>
  </div>
  `;

  const $grid = tableApi.grid;
  if ($grid) {
    const url = await qrcodeCanvasRef.value.toDataURL();
    item.prints=url
    // let canvasUrl = Qr.toDataURL('image/png')
    let conte = '<img src="' + url + '" />';
    $grid.print({
      sheetName: item.title + '设备点检',
      style: printStyle,
      data: [item],
      content: conte,
      beforePrintMethod({ html }) {
        console.log(html);
        return topHtml + html + bottomHtml;
      },
    });
  }
};
</script>

<template>
  <Page :auto-content-height="true">
    <BasicTable table-title="设备点检列表">
      <template #prints="{ row }">
        <p class="cursor-pointer text-blue-600" @click="printEvent(row)">
          打印
        </p>
      </template>
      <template #qprint="{ row }">
        <p class="m-2">
          <QRCode :value="row.id" :size="50" />
          <QRCode :value="row.id" ref="qrcodeCanvasRef" :size="200" hidden />
        </p>
      </template>
      <template #toolbar-tools>
        <Space>
          <a-button
            v-access:code="['mes:equCheck:export']"
            @click="handleDownloadExcel"
          >
            {{ $t('pages.common.export') }}
          </a-button>
          <a-button
            :disabled="!vxeCheckboxChecked(tableApi)"
            danger
            type="primary" 
            v-access:code="['mes:equCheck:remove']" 
            @click="handleMultiDelete">
            {{ $t('pages.common.delete') }}
          </a-button>
          <a-button
            type="primary"
            v-access:code="['mes:equCheck:add']"
            @click="handleAdd"
          >
            {{ $t('pages.common.add') }}
          </a-button>
        </Space>
      </template>
      <template #showinfo="{ row }">
        <Button @click="selectSubList(row)">查看检查项</Button>
      </template>
      <template #action="{ row }">
        <Space>
          <ghost-button
            v-access:code="['mes:equCheck:edit']"
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
              v-access:code="['mes:equCheck:remove']"
              @click.stop=""
            >
              {{ $t('pages.common.delete') }}
            </ghost-button>
          </Popconfirm>
        </Space>
      </template>
    </BasicTable>
    <tenantDrawer @reload="tableApi.query()" />
    <EquCheckModal @reload="tableApi.query()" />
  </Page>
</template>
