<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';

import { useVbenForm } from '#/adapter/form';
import { teamsAdd, teamsInfo, teamsUpdate } from '#/api/mes/teams';

import { modalSchema } from './data';
import {
  userList,
} from '#/api/system/user';
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
  schema: modalSchema(),
  showDefaultActions: false,
  wrapperClass: 'grid-cols-2',
});
async function getUpUserlist(){
  const res = await userList({pageNum: 1,
          pageSize: 300});
  const options = res.rows.map((item) => ({
    label: item.nickName,
    value: item.userId,
  }));
  const placeholder = options.length > 0 ? '请选择' : '无用户列表';
  formApi.updateSchema([
    {
      fieldName:'upids',
      componentProps: {
          options,
          placeholder,
      }
    },{
      fieldName:'useids',
      componentProps: {
          options,
          placeholder,
      }
    }
  ])
}
const [BasicModal, modalApi] = useVbenModal({
  fullscreenButton: false,
  onCancel: handleCancel,
  onConfirm: handleConfirm,
  onOpenChange: async (isOpen) => {
    if (!isOpen) {
      return null;
    }
    modalApi.modalLoading(true);

    const { id } = modalApi.getData() as { id?: number | string };
    isUpdate.value = !!id;
    await getUpUserlist();
    if (isUpdate.value && id) {
      const record = await teamsInfo(id);
      await Promise.all([
        formApi.setValues(record),
        formApi.setFieldValue('upids',record.upidsList),
        formApi.setFieldValue('useids',record.useidsList)
      ]);
     
    }
   
    modalApi.modalLoading(false);
  },
});

async function handleConfirm() {
  try {
    modalApi.modalLoading(true);
    const { valid } = await formApi.validate();
    if (!valid) {
      return;
    }
    // getValues获取为一个readonly的对象 需要修改必须先深拷贝一次
    const data = cloneDeep(await formApi.getValues());
    data.upids=data.upids.join(",");
    data.useids=data.useids.join(",");
    await (isUpdate.value ? teamsUpdate(data) : teamsAdd(data));
    emit('reload');
    await handleCancel();
  } catch (error) {
    console.error(error);
  } finally {
    modalApi.modalLoading(false);
  }
}

async function handleCancel() {
  modalApi.close();
  await formApi.resetForm();
}
</script>

<template>
  <BasicModal :close-on-click-modal="false" :title="title" class="w-[550px]">
    <BasicForm />
  </BasicModal>
  
</template>
