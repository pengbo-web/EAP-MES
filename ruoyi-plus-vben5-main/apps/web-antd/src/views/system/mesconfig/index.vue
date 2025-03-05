<script setup lang="ts">
import type { Recordable } from '@vben/types';

import { ref, onMounted } from 'vue';

import { Page, useVbenDrawer, type VbenFormProps } from '@vben/common-ui';
import { getVxePopupContainer } from '@vben/utils';

import {
  Modal,
  Popconfirm,
  Space,
  Form,
  FormItem,
  Input,
  Row,
  Col,
  Button
} from 'ant-design-vue';
import dayjs from 'dayjs';

import {
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps,
} from '#/adapter/vxe-table';

import {
  mesconfigExport,
  mesconfigList,
  mesconfigRemove,
} from '#/api/system/mesconfig';
import type { MesconfigForm } from '#/api/system/mesconfig/model';
import { commonDownloadExcel } from '#/utils/file/download';

import mesconfigDrawer from './mesconfig-drawer.vue';
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
import {
  mesconfigAdd,
  mesconfigInfo,
  mesconfigUpdate,
} from '#/api/system/mesconfig';
const mesconfig = ref({});
onMounted(async () => {
  let rec = await mesconfigInfo(2);
  console.log(rec);
  mesconfig.value = rec;
});

function handleAdd() {
  mesconfigUpdate({ id: 2, ...mesconfig.value });
  
}

async function handleEdit(row: Required<MesconfigForm>) {
  drawerApi.setData({ id: row.id });
  drawerApi.open();
}

async function handleDelete(row: Required<MesconfigForm>) {
  await mesconfigRemove(row.id);
  await tableApi.query();
}

function handleMultiDelete() {
  const rows = tableApi.grid.getCheckboxRecords();
  const ids = rows.map((row: Required<MesconfigForm>) => row.id);
  Modal.confirm({
    title: '提示',
    okType: 'danger',
    content: `确认删除选中的${ids.length}条记录吗？`,
    onOk: async () => {
      await mesconfigRemove(ids);
      await tableApi.query();
    },
  });
}

function handleDownloadExcel() {
  commonDownloadExcel(
    mesconfigExport,
    'MES配置数据',
    tableApi.formApi.form.values,
    {
      fieldMappingTime: formOptions.fieldMappingTime,
    },
  );
}
</script>

<template>
  <div class="h-screen bg-white">
    <div class="grid grid-cols-2 gap-4 p-11">
      <div>一天工作时间（分钟）：<Input v-model:value="mesconfig.times" /></div>
      <div>早班检查时间：<Input v-model:value="mesconfig.chetime" /></div>
    </div>
    <div class="fixed bottom-0 right-0 p-4 ">
      <Button type="primary" @click="handleAdd">修改</Button>
      </div>
  </div>
</template>
