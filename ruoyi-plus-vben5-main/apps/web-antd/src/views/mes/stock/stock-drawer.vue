<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenDrawer } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';

import { useVbenForm } from '#/adapter/form';
import { stockAdd, stockInfo, stockUpdate } from '#/api/mes/stock';

import { drawerSchema } from './data';
import {
  customList,
} from '#/api/mes/custom';
import {
  unitmeasureList,
} from '#/api/mes/unitmeasure';
import { stockTypeList } from '#/api/mes/stockType';
const emit = defineEmits<{ reload: [] }>();

const isUpdate = ref(false);
const title = computed(() => {
  return isUpdate.value ? $t('pages.common.edit') : $t('pages.common.add');
});

const [BasicForm, formApi] = useVbenForm({
  commonConfig: {
    // 默认占满两列
    formItemClass: 'col-span-2',
    // 默认label宽度 px
    labelWidth: 80,
    // 通用配置项 会影响到所有表单项
    componentProps: {
      class: 'w-full',
    }
  },
  schema: drawerSchema(),
  showDefaultActions: false,
  wrapperClass: 'grid-cols-2',
});

const [BasicDrawer, drawerApi] = useVbenDrawer({
  fullscreenButton: false,
  onCancel: handleCancel,
  onConfirm: handleConfirm,
  onOpenChange: async (isOpen) => {
    if (!isOpen) {
      return null;
    }
    drawerApi.drawerLoading(true);

    const { id } = drawerApi.getData() as { id?: number | string };
    isUpdate.value = !!id;

    if (isUpdate.value && id) {
      const record = await stockInfo(id);
      await formApi.setValues(record);
    }
    await getCustList();
    await getUnitlist();
    await getStockTypeList();
    drawerApi.drawerLoading(false);
  },
});
async function getCustList(){
  const res = await customList({ pageNum: 1, pageSize: 1000,type:'供应商' });
  const options = res.rows.map((item) => ({
    label: item.title,
    value: item.id,
  }));
  const placeholder = options.length > 0 ? '请选择' : '无客户列表';
  formApi.updateSchema([
    {
      fieldName:'custid',
      componentProps: {
          options,
          placeholder,
      }
    }
  ])
}
async function getUnitlist() {
  const res = await unitmeasureList({ pageNum: 1, pageSize: 1000 });
  const options = res.rows.map((item) => ({
    label: item.title,
    value: item.id,
  }));
  const placeholder = options.length > 0 ? '请选择' : '无计量单位列表';
  formApi.updateSchema([
    {
      fieldName:'unitid',
      componentProps: {
        options,
        placeholder,
      }
    }
  ])
}
async function getStockTypeList() {
  const res = await stockTypeList();
  const options = res.map((item) => ({
    label: item.sn,
    value: item.id,
  }));
  const placeholder = options.length > 0 ? '请选择' : '无库位列表';
  formApi.updateSchema([
    {
      fieldName:'stockid',
      componentProps: {
        options,
        placeholder,
      }
    }
  ])
}
async function handleConfirm() {
  try {
    drawerApi.drawerLoading(true);
    const { valid } = await formApi.validate();
    if (!valid) {
      return;
    }
    // getValues获取为一个readonly的对象 需要修改必须先深拷贝一次
    const data = cloneDeep(await formApi.getValues());
    console.log(data)
    if(data.custid){
      data.custid=data.custid.join(',');
    }
    
    await (isUpdate.value ? stockUpdate(data) : stockAdd(data));
    emit('reload');
    await handleCancel();
  } catch (error) {
    console.error(error);
  } finally {
    drawerApi.drawerLoading(false);
  }
}

async function handleCancel() {
  drawerApi.close();
  await formApi.resetForm();
}
</script>

<template>
  <BasicDrawer :close-on-click-modal="false" :title="title" class="w-[550px]">
    <BasicForm />
  </BasicDrawer>
</template>
