<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';
import { Modal, Popconfirm, Space, InputSearch } from 'ant-design-vue';
import { useVbenForm } from '#/adapter/form';
import { equCheckAdd, equCheckInfo, equCheckUpdate } from '#/api/mes/equCheck';

import { modalSchema } from './data';

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
      const record = await equCheckInfo(id);
      await formApi.setValues(record);
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
    data.equIds=equdatas.value.join(",");
    data.equIdsarray=equdatas.value;
    await (isUpdate.value ? equCheckUpdate(data) : equCheckAdd(data));
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
const showequ = ref(false);
import Equs from '#/views/mes/equipment/lists.vue';
const equdatas = ref<any[]>([]);
const equref = ref<any>({});
async function getequsinfo() {
  const record = equref.value.rowinfo;
  let names = '';
  record.forEach((element) => {
    names = names + element.title + ',';
    equdatas.value.push(element.id);
  });
  formApi.setFieldValue('equIdsname', names);
  showequ.value = false;
}
function openshowequ() {
  showequ.value = true;
}
</script>

<template>
  <BasicModal :close-on-click-modal="false" :title="title" class="w-[550px]">
    <BasicForm >
      <template #equIdsname="slotProps">
        <InputSearch v-bind="slotProps" @search="openshowequ" />
      </template>
    </BasicForm>
    <Modal
      v-model:open="showequ"
      width="100%"
      wrap-class-name="full-modal"
      @ok="getequsinfo"
    >
      <Equs ref="equref"></Equs>
    </Modal>
  </BasicModal>
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
