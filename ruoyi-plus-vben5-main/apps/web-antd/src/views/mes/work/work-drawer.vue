<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenDrawer } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';

import { useVbenForm } from '#/adapter/form';
import { workAdd, workInfo, workUpdate } from '#/api/mes/work';
import { InputSearch,Form,FormItem,Modal } from 'ant-design-vue';
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
      const record = await workInfo(id);
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
    await (isUpdate.value ? workUpdate(data) : workAdd(data));
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
import  Prolits from '#/views/mes/product/lists.vue'
const proopen = ref<boolean>(false);
  const torefpro=ref<any>({});
function proseach(){
  proopen.value = true;
}
function handleChildEvent(){
  const record=torefpro.value.rowinfo;
  formApi.setFieldValue('proname',record.title),
  formApi.setFieldValue('proId',record.id),
  proopen.value = false;
}
</script>

<template>
  <BasicDrawer :close-on-click-modal="false" :title="title" class="w-[550px]">
    <BasicForm >
      <template #proname="slotProps">
        <InputSearch v-bind="slotProps" @search="proseach" />
      </template>
    </BasicForm>
    <Modal
      v-model:open="proopen"
      width="100%"
      wrap-class-name="full-modal"
      
    >
      <Prolits ref="torefpro" @child-event="handleChildEvent"/>
    </Modal>
  </BasicDrawer>
</template>
