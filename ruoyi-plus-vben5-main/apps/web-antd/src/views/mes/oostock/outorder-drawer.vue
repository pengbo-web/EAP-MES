<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenDrawer } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';

import { useVbenForm } from '#/adapter/form';
import { outorderAdd, outorderInfo, outorderUpdate } from '#/api/mes/outorder';
import {
  InputSearch,
  Form,
  FormItem,
  Modal,
  InputNumber,
  Input,
  RadioGroup,
  Radio
} from 'ant-design-vue';
import { drawerSchema } from './data';
import {
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps,
} from '#/adapter/vxe-table';
import {
  outorderDeAdd,
  outorderDeInfo,
  outorderDeUpdate,
} from '#/api/mes/outorderDe';
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
      const record = await outorderInfo(id);
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
    if (!isUpdate.value) {
      let concatenatedDataArray = [];

      let totalprice = 0.0;
      await Promise.all(
        datas.value.map(async (item: any) => {
          let pricenew = (item.prices * item.count).toFixed(4);
          let par = {
            id: undefined,
            allprice: pricenew,
            fromtype: '单独委外',
            orderid: item.orderid,
            stockid: item.id,
            prices: item.amount2,
            counts: item.count,
            status:item.status,
            remark: item.remark,
          };
          let re = await outorderDeAdd(par);
          totalprice += Number(item.amount2);
          let dataArray = re;
          concatenatedDataArray.push(dataArray);
        }),
      );
      let concatenatedData = concatenatedDataArray.join(',');
      data.stockids = concatenatedData;
      data.prices = totalprice;
    }
    data.fromtype = '单独委外';
    await (isUpdate.value ? outorderUpdate(data) : outorderAdd(data));
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
import Stocks from '#/views/mes/stock/lists.vue';
import CustLists from '#/views/mes/custom/list.vue';
const toref = ref<any>({});
const datainfo = ref<any>({});
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
      field: 'orderid',
      title: '订单号',
      slots: { default: 'orderid' },
    },
    {
      align: 'center',
      field: 'status',
      title: '是否待料',
      slots: { default: 'status' },
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
const custref = ref<any>({});
const showcustlist = ref(false);
function handleScust() {
  showcustlist.value = true;
}
function handleChildEvent() {
  const record = custref.value.rowinfo;
  formApi.setFieldValue('custnames', record.title),
    formApi.setFieldValue('custid', record.id),
    (showcustlist.value = false);
}
function handlecreat() {
  const today = new Date();
  const year = today.getFullYear();
  const month = (today.getMonth() + 1).toString().padStart(2, '0');
  const day = today.getDate().toString().padStart(2, '0');
  const min = today.getMinutes() + Math.random().toFixed(4) * 10000;
  formApi.setFieldValue('sn', year + month + day + min);
}
const showtype = ref(false);
const stypeinfo = ref<any>({});
const typeref = ref<any>({});
import Stocktype from '#/views/mes/stockType/list.vue';
function hanldtype() {
  showtype.value = false;
  stypeinfo.value = typeref.value.rowinfo;
  formApi.setFieldValue('stocktypename', stypeinfo.value.title);
  formApi.setFieldValue('stocktype', stypeinfo.value.id);
}
function searchtype() {
  showtype.value = true;
}
</script>

<template>
  <BasicDrawer :close-on-click-modal="false" :title="title" class="w-[800px]">
    <BasicForm>
      <template #stocknames="slotProps">
        <InputSearch v-bind="slotProps" @search="handleSearch" />
      </template>
      <template #custnames="slotProps">
        <InputSearch v-bind="slotProps" @search="handleScust" />
      </template>
      <template #stocktypename="slotProps">
        <InputSearch v-bind="slotProps" @search="searchtype" />
      </template>
      <template #sn="slotProps">
        <InputSearch v-bind="slotProps" @search="handlecreat">
          <template #enterButton>
            <a-button>生成</a-button>
          </template>
        </InputSearch>
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
        <template #status="{ row }">
          <RadioGroup v-model:value="row.status" name="radioGroup">
            <Radio value="1">是</Radio>
            <Radio value="0">否</Radio>
          </RadioGroup>
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
