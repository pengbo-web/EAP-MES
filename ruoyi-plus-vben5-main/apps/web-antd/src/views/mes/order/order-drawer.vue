<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenDrawer } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';

import { useVbenForm } from '#/adapter/form';
import { orderAdd, orderInfo, orderUpdate } from '#/api/mes/order';

import { drawerSchema } from './data';
import {
  InputSearch,
  Form,
  FormItem,
  Modal,
  InputNumber,
  Input,
} from 'ant-design-vue';
import {
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps,
} from '#/adapter/vxe-table';
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
      const record = await orderInfo(id);
      await formApi.setValues(record);
    }

    drawerApi.drawerLoading(false);
  },
});
import { orderDeAdd, orderDeInfo, orderDeUpdate } from '#/api/mes/orderDe';
async function handleConfirm() {
  try {
    drawerApi.drawerLoading(true);
    const { valid } = await formApi.validate();
    if (!valid) {
      return;
    }
    // getValues获取为一个readonly的对象 需要修改必须先深拷贝一次
    const data = cloneDeep(await formApi.getValues());
    if (!isUpdate.value) {
      let concatenatedDataArray = [];

      let totalprice = 0.0;
      await Promise.all(
        datas.value.map(async (item: any) => {
          let pricenew = (item.prices * item.count).toFixed(4);
          let par = {
            id: undefined,
            allprice: pricenew,
            stockid: item.id,
            prices: item.amount2,
            counts: item.count,
            remark: item.remark,
          };
          let re = await orderDeAdd(par);
          totalprice += Number(item.amount2);
          let dataArray = re;
          concatenatedDataArray.push(dataArray);
        }),
      );
      let concatenatedData = concatenatedDataArray.join(',');
      data.stids = concatenatedData;
    }
    await (isUpdate.value ? orderUpdate(data) : orderAdd(data));
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
const custref = ref<any>({});
const showcustlist = ref(false);
import CustLists from '#/views/mes/custom/list.vue';
function handleScust() {
  showcustlist.value = true;
}
function handleChildEvent() {
  const record = custref.value.rowinfo;
  formApi.setFieldValue('custname', record.title);
  formApi.setFieldValue('custid', record.id);
  showcustlist.value = false;
}
import Stocks from '#/views/mes/stock/lists.vue';
const toref = ref<any>({});
const datas = ref<any[]>([]);
async function getinfo() {
  const record = toref.value.rowinfo;
  let names = '';
  record.forEach((element) => {
    names = names + element.tilte + ',';
    element.count = 0;
    element.amount2 = 0;
    element.prices = 0;
    element.orderid = '';
  });

  datas.value = record;
  console.log(datas.value);
  formApi.setFieldValue('stocknames', names);
  open.value = false;
  tableApi.query();
}
const open = ref<boolean>(false);
function handleSearch() {
  open.value = true;
}
const gridOptions: VxeGridProps = {
  checkboxConfig: {
    // 高亮
    highlight: true,
    // 翻页时保留选中状态
    reserve: true,
  },
  columns: [
    {
      field: 'tilte',
      title: '名称',
    },
    {
      align: 'center',
      field: 'sn',
      title: '规格',
    },
    {
      align: 'center',
      field: 'count',
      title: '数量',
      slots: { default: 'count' },
    },
    {
      align: 'center',
      field: 'amount2',
      title: '价格',
      slots: { default: 'amount2' },
    },

    {
      align: 'center',
      field: 'remark',
      title: '备注',
      slots: { default: 'remark' },
    },
  ],
  height: 220,
  keepSource: true,
  pagerConfig: {
    enabled: false,
  },
  toolbarConfig: {
    custom: false,
    zoom: false,
    refresh: false,
  },
  proxyConfig: {
    ajax: {
      query: async () => {
        // datainfo.value.map(async(item: any) => {
        //   let rec=await getcustom(item.id);
        //   console.log(rec)
        //   item.custidscheck=rec
        // })
        return {
          rows: datas.value,
        };
      },
    },
  },
  rowConfig: {
    isCurrent: false,
    keyField: 'id',
  },
  id: 'profile-bind-table',
};
const [BasicTable, tableApi] = useVbenVxeGrid({
  gridOptions,
});
</script>

<template>
  <BasicDrawer :close-on-click-modal="false" :title="title" class="w-[550px]">
    <BasicForm >
      <template #stocknames="slotProps">
        <InputSearch v-bind="slotProps" @search="handleSearch" />
      </template>
      <template #custname="slotProps">
        <InputSearch v-bind="slotProps" @search="handleScust" />
      </template>
      <BasicTable class="col-span-2">
        <template #count="{ row }">
          <InputNumber v-model:value="row.count"></InputNumber>
        </template>
        <template #amount2="{ row }">
          <InputNumber
            v-model:value="row.amount2"
            min="0"
            max="100000000000"
            step="0.0001"
            onChange="{onChange}"
            stringMode
          ></InputNumber>
        </template>
        <template #orderid="{ row }">
          <Input v-model:value="row.orderid"></Input>
        </template>
        <template #remark="{ row }">
          <Input v-model:value="row.remark"></Input>
        </template>
      </BasicTable>
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
      v-model:open="showcustlist"
      width="100%"
      wrap-class-name="full-modal"
    >
      <CustLists ref="custref" @child-event="handleChildEvent" />
    </Modal>
  </BasicDrawer>
</template>
