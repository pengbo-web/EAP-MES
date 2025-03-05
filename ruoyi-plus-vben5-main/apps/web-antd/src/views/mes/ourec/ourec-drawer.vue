<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenDrawer } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';

import { useVbenForm } from '#/adapter/form';
import { ourecAdd, ourecInfo, ourecUpdate } from '#/api/mes/ourec';
import {
  InputSearch,
  Form,
  FormItem,
  Modal,
  InputNumber,
  Input,
} from 'ant-design-vue';
import { drawerSchema } from './data';

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
    },
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
      const record = await ourecInfo(id);
      await formApi.setValues(record);
    }

    drawerApi.drawerLoading(false);
  },
});

async function handleConfirm() {
  try {
    drawerApi.drawerLoading(true);
    const { valid } = await formApi.validate();
    if (!valid) {
      return;
    }
    // getValues获取为一个readonly的对象 需要修改必须先深拷贝一次
    const data = cloneDeep(await formApi.getValues());
    await (isUpdate.value ? ourecUpdate(data) : ourecAdd(data));
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
const open = ref<boolean>(false);
function handleSearch() {
  open.value = true;
}
import Stocks from '#/views/mes/stock/listsc.vue';
import Stocktype from '#/views/mes/stockType/list.vue';
const custref = ref<any>({});
function handleChildEvent() {
  const record = custref.value.rowinfo;
  const today = new Date();
  const year = today.getFullYear();
  const month = (today.getMonth() + 1).toString().padStart(2, '0');
  const day = today.getDate().toString().padStart(2, '0');
  const min = today.getMinutes() + Math.random().toFixed(4) * 10000;
  formApi.setFieldValue('sn', year + month + day + min);
  formApi.setFieldValue('stockname', record.tilte),
    formApi.setFieldValue('stockid', record.id),
    (open.value = false);
}
const showtype = ref(false);
const typeref = ref<any>({});
const typeinfo = ref<any>({});
function hanldtype() {
  showtype.value = false;
  typeinfo.value = typeref.value.rowinfo;
  formApi.setFieldValue('stocktypename', typeinfo.value.title);
  formApi.setFieldValue('stocktypeid', typeinfo.value.id);
}
function searchtype() {
  showtype.value = true;
}
</script>

<template>
  <BasicDrawer :close-on-click-modal="false" :title="title" class="w-[550px]">
    <BasicForm>
      <template #stockname="slotProps">
        <InputSearch v-bind="slotProps" @search="handleSearch" />
      </template>
      <template #stocktypename="slotProps">
        <InputSearch v-bind="slotProps" @search="searchtype" />
      </template>
    </BasicForm>
    <Modal v-model:open="open" width="100%" wrap-class-name="full-modal">
      <Stocks ref="custref" @child-event="handleChildEvent" />
    </Modal>
    <Modal v-model:open="showtype" width="100%" wrap-class-name="full-modal">
      <Stocktype ref="typeref" @child-event="hanldtype" />
    </Modal>
  </BasicDrawer>
</template>
<style lang="less">
.full-modal {
  .ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
  }
  .ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh);
  }
  .ant-modal-body {
    flex: 1;
  }
}
</style>
