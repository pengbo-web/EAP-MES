<script setup lang="ts">
import { computed, ref ,reactive,defineComponent,onMounted} from 'vue';

import { useVbenDrawer } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';

import { useVbenForm } from '#/adapter/form';
import { productAdd, productInfo, productUpdate } from '#/api/mes/product';
import { InputSearch,Form,FormItem,Modal } from 'ant-design-vue';
import { drawerSchema } from './data';
import { protypeList } from '#/api/mes/protype';
import {
  unitmeasureList,
} from '#/api/mes/unitmeasure';
import {
  stockList,
} from '#/api/mes/stock';
const emit = defineEmits<{ reload: [] }>();

const isUpdate = ref(false);
const title = computed(() => {
  return isUpdate.value ? $t('pages.common.edit') : $t('pages.common.add');
});
import { columns as stockColumns } from '#/views/mes/stock/data';
onMounted(init);
async function init() {
  console.log(torefpro.value)
}
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
import  Stocks from '#/views/mes/stock/lists.vue'
import  Prolits from '#/views/mes/product/lists.vue'
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
      const record = await productInfo(id);
      formApi.setFieldValue('stocksname', record.stockname);
      await formApi.setValues(record);
    }
    await getProtypeList();
    await getUnitlist();
    drawerApi.drawerLoading(false);
  },
});
async function getProtypeList(){
  const res = await protypeList();
  const options = res.map((item) => ({
    label: item.title,
    value: item.id,
  }));
  const placeholder = options.length > 0 ? '请选择' : '无自定义分类';
  formApi.updateSchema([
    {
      fieldName:'protypeId',
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
      fieldName:'unitId',
      componentProps: {
        options,
        placeholder,
      }
    }
  ])
};


async function handleConfirm() {
  try {
    drawerApi.drawerLoading(true);
    const { valid } = await formApi.validate();
    if (!valid) {
      return;
    }
    // getValues获取为一个readonly的对象 需要修改必须先深拷贝一次
    const data = cloneDeep(await formApi.getValues());
    await (isUpdate.value ? productUpdate(data) : productAdd(data));
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
const formState: UnwrapRef<any> = reactive({
  name: '',
  delivery: false,
  type: [],
  resource: '',
  desc: '',
});
const open = ref<boolean>(false);
function handleSearch(){
  open.value = true;
}
const toref=ref<any>({});
async function getinfo(){
  const record=toref.value.rowinfo[0]
  await Promise.all([
        formApi.setFieldValue('stocks',record.id),
        formApi.setFieldValue('stocksname',record.tilte),
        formApi.setFieldValue('sn',record.sn),
        formApi.setFieldValue('title',record.tilte),
        formApi.setFieldValue('unitId',record.unitid),
      ]);
  open.value=false;
}
const proopen = ref<boolean>(false);
function proseach(){
  proopen.value = true;
}
function handleChildEvent(){
  const record=torefpro.value.rowinfo;
  formApi.setFieldValue('pidname',record.title),
  formApi.setFieldValue('pid',record.id),
  proopen.value = false;
}
const torefpro=ref<any>({});
async function getproinfo(){
  const record=torefpro.value.rowinfo[0]
  await Promise.all([
        formApi.setFieldValue('stocks',record.id),
        formApi.setFieldValue('stocksname',record.tilte),
        formApi.setFieldValue('sn',record.sn),
        formApi.setFieldValue('title',record.tilte),
        formApi.setFieldValue('unitId',record.unitid),
      ]);
      proopen.value=false;
}
</script>

<template>
  <BasicDrawer :close-on-click-modal="false" :title="title" class="w-[550px]">
    <BasicForm>
      <template #stocksname="slotProps">
        <InputSearch v-bind="slotProps" @search="handleSearch" />
      </template>
      <template #pidname="slotProps">
        <InputSearch v-bind="slotProps" @search="proseach" />
      </template>
    </BasicForm>
    <Modal
      v-model:open="open"
      width="100%"
      wrap-class-name="full-modal"
      @ok="getinfo"
    >
      <Stocks ref="toref" />
    </Modal>
    <Modal
      v-model:open="proopen"
      width="100%"
      wrap-class-name="full-modal"
      @ok="getproinfo"
    >
      <Prolits ref="torefpro" @child-event="handleChildEvent"/>
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
