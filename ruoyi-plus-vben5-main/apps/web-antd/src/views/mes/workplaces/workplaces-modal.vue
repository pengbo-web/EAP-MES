<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';
import { Modal, Popconfirm, Space, InputSearch } from 'ant-design-vue';
import { useVbenForm } from '#/adapter/form';
import {
  workplacesAdd,
  workplacesInfo,
  workplacesUpdate,
} from '#/api/mes/workplaces';

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
    },
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
    await getUpUserlist();
    if (isUpdate.value && id) {
      const record = await workplacesInfo(id);
      await formApi.setValues(record);
    }

    modalApi.modalLoading(false);
  },
});
import {
  workplaceExport,
  workplaceList,
  workplaceRemove,
} from '#/api/mes/workplace';
async function getUpUserlist(){
  const res = await workplaceList({pageNum: 1,
          pageSize: 300});
  const options = res.rows.map((item) => ({
    label: item.title,
    value: item.id,
  }));
  const placeholder = options.length > 0 ? '请选择' : '无用户列表';
  formApi.updateSchema([
    {
      fieldName:'workshopId',
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
    if (!isUpdate.value) {
      data.teams=tedatas.value.join(",");
      data.equIds=equdatas.value.join(",");
      data.processIds=procedatas.value.join(",");
    }
    await (isUpdate.value ? workplacesUpdate(data) : workplacesAdd(data));
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
const showte = ref(false);
import Teams from '#/views/system/dept/list.vue';
const tedatas = ref<any[]>([]);
const toref = ref<any>({});
async function gettermsinfo() {
  const record = toref.value.rowinfo;
  let names = '';
  record.forEach((element) => {
    names = names + element.deptName + ',';
    tedatas.value.push(element.deptId);
  });
  formApi.setFieldValue('teamsids', names);
  showte.value = false;
}
function openshowte() {
  showte.value = true;
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
  formApi.setFieldValue('equlists', names);
  showequ.value = false;
}
function openshowequ() {
  showequ.value = true;
}
const showproce = ref(false);
import Proces from '#/views/mes/process/list.vue';
const procedatas = ref<any[]>([]);
const proceref = ref<any>({});
async function getprocesinfo() {
  const record = proceref.value.rowinfo;
  let names = '';
  record.forEach((element) => {
    names = names + element.title + ',';
    procedatas.value.push(element.id);
  });
  formApi.setFieldValue('processnames', names);
  showproce.value = false;
}
function openshowproce() {
  showproce.value = true;
}
</script>

<template>
  <BasicModal :close-on-click-modal="false" :title="title" class="w-[550px]">
    <BasicForm> 
      <template #teamsids="slotProps">
        <InputSearch v-bind="slotProps" @search="openshowte" />
      </template>
      <template #equlists="slotProps">
        <InputSearch v-bind="slotProps" @search="openshowequ" />
      </template>
      <template #processnames="slotProps">
        <InputSearch v-bind="slotProps" @search="openshowproce" />
      </template>
    
    </BasicForm>
    <Modal
      v-model:open="showte"
      width="100%"
      wrap-class-name="full-modal"
      @ok="gettermsinfo"
    >
      <Teams ref="toref"></Teams>
    </Modal>
    <Modal
      v-model:open="showequ"
      width="100%"
      wrap-class-name="full-modal"
      @ok="getequsinfo"
    >
      <Equs ref="equref"></Equs>
    </Modal>
    <Modal
      v-model:open="showproce"
      width="100%"
      wrap-class-name="full-modal"
      @ok="getprocesinfo"
    >
      <Proces ref="proceref"></Proces>
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
