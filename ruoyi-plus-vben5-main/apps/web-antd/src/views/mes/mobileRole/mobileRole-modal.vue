<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';

import { useVbenForm } from '#/adapter/form';
import { mobileRoleAdd, mobileRoleInfo, mobileRoleUpdate } from '#/api/mes/mobileRole';

import { modalSchema } from './data';
import {
  userList,
} from '#/api/system/user';
import {
  mURList,
} from '#/api/mes/mUR';
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

    if (isUpdate.value && id) {
      const record = await mobileRoleInfo(id);
      await formApi.setValues(record);
    }
    await getUpUserlist();
    await getmURList();
    modalApi.modalLoading(false);
  },
});
async function getmURList(){
  const res = await mURList({pageNum: 1,
          pageSize: 300});
  const options = res.rows.map((item) => ({
    label: item.title,
    value: item.id,
  }));
  const placeholder = options.length > 0 ? '请选择' : '无目录';
  formApi.updateSchema([
    {
      fieldName:'menuids',
      componentProps: {
          options,
          placeholder,
      }
    }
  ])
}
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
      fieldName:'userId',
      componentProps: {
          options,
          placeholder,
      }
    }
  ])
}
async function handleConfirm() {
  try {
    modalApi.modalLoading(true);
    const { valid } = await formApi.validate();
    if (!valid) {
      return;
    }
    // getValues获取为一个readonly的对象 需要修改必须先深拷贝一次
    const data = cloneDeep(await formApi.getValues());
    data.userIdarray=data.userId
    data.userId=null;
    await (isUpdate.value ? mobileRoleUpdate(data) : mobileRoleAdd(data));
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
