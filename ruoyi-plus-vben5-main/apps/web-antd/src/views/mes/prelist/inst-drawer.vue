<script setup lang="ts">
import { computed, ref } from 'vue';

import { useVbenDrawer } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { cloneDeep } from '@vben/utils';
import {
  useVbenVxeGrid,
  vxeCheckboxChecked,
  type VxeGridProps,
} from '#/adapter/vxe-table';
import { useVbenForm } from '#/adapter/form';
import { instAdd, instInfo, instUpdate } from '#/api/mes/inst';
import { InputSearch, Modal, Table, InputNumber, Input,Select } from 'ant-design-vue';
import { drawerSchema } from './data';
import { inrecExport, inrecList, inrecRemove } from '#/api/mes/inrec';
import { inrecAdd, inrecInfo, inrecUpdate } from '#/api/mes/inrec';
import { getcustom } from '#/api/mes/stock';
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
const selectlist=ref<any[]>([])
const [BasicDrawer, drawerApi] = useVbenDrawer({
  fullscreenButton: false,
  onCancel: handleCancel,
  onConfirm: handleConfirm,
  onOpenChange: async (isOpen) => {
    if (!isOpen) {
      return null;
    }
    datainfo.value = [];
    drawerApi.drawerLoading(true);

    const { id } = drawerApi.getData() as { id?: number | string };
    isUpdate.value = !!id;

    if (isUpdate.value && id) {
      const record = await instInfo(id);
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
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');
    const hours = String(currentDate.getHours()).padStart(2, '0');
    const minutes = String(currentDate.getMinutes()).padStart(2, '0');
    const seconds = String(currentDate.getSeconds()).padStart(2, '0');
    const milliseconds = String(currentDate.getMilliseconds()).padStart(3, '0');
    if (!isUpdate.value) {
      data.sn = `${year}${month}${day}${hours}${minutes}${seconds}${milliseconds}`;
      let concatenatedDataArray = []; // 在循环外部定义一个数组来存储每次循环中的数据
      let totalprice = 0.00;
      await Promise.all(
        datainfo.value.map(async(item: any) => {
          let par = {
            id: undefined,
            custId: item.custid,
            stocktypeid: item.stockid,
            stockid: item.id,
            shuidian: '13',
            nprice: item.amount2,
            count: item.count,
            fromtype: '单独采购入库',
            remark: item.remark,
            status: '0',
          };
          let re=await inrecAdd(par);
          console.log(re)
          totalprice += Number(item.amount2)
          let dataArray = re;
          concatenatedDataArray.push(dataArray);
        }),
      );
      console.log(concatenatedDataArray)
      let concatenatedData = concatenatedDataArray.join(',');
      data.recids = concatenatedData;
      data.allprice = totalprice;
    }
    await (isUpdate.value ? instUpdate(data) : instAdd(data));
    emit('reload');
    await handleCancel();
  } catch (error) {
    console.error(error);
  } finally {
    drawerApi.drawerLoading(false);
  }
}
const datainfo = ref<any[]>([]);

async function handleCancel() {
  drawerApi.close();
  await formApi.resetForm();
}
import Stocks from '#/views/mes/stock/lists.vue';
const open = ref<boolean>(false);
const toref = ref<any>({});
function handleSearch() {
  open.value = true;
}

async function getinfo() {
  const record = toref.value.rowinfo;
  datainfo.value = record;
  // await Promise.all([
  //       formApi.setFieldValue('stocks',record.id),
  //       formApi.setFieldValue('stocksname',record.tilte),
  //       formApi.setFieldValue('sn',record.sn),
  //       formApi.setFieldValue('title',record.tilte),
  //       formApi.setFieldValue('unitId',record.unitid),
  //     ]);
  let names = '';
  record.forEach((element) => {
    names = names + element.tilte + ',';
    element.count = 0;
    element.amount2 = 0;
  });
  formApi.setFieldValue('stocksname', names);
  tableApi.query();
  open.value = false;
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
      field: 'custidscheck',
      title: '采购商',
      slots: { default: 'custidscheck' },
    },
    {
      align: 'center',
      field: 'custid',
      title: '采购商',
      visible: false,
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
        datainfo.value.map(async(item: any) => {
          let rec=await getcustom(item.id);
          console.log(rec)
          item.custidscheck=rec
        })
        return {
          rows: datainfo.value,
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
const onChange: InputNumberProps['onChange'] = (value) => {
  console.log('changed', value);
};
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
  <BasicDrawer :close-on-click-modal="false" :title="title" class="w-[750px]">
    <BasicForm>
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
        <template #custidscheck="{ row }">
          <Select v-model:value="row.custid"
            style=" width: 80px;"
            :options="row.custidscheck"
          />
        </template>
        <template #remark="{ row }">
          <Input v-model:value="row.remark"></Input>
        </template>
      </BasicTable>
      <template #stocksname="slotProps">
        <InputSearch v-bind="slotProps" @search="handleSearch" />
      </template>
      <template #stocktypename="slotProps">
        <InputSearch v-bind="slotProps" @search="searchtype" />
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
