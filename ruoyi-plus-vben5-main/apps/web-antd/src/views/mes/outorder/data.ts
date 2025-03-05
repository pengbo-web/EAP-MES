import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'title',
    label: '委外名称',
  },
  {
    component: 'Input',
    fieldName: 'custname',
    label: '委外商家',
  },
  {
    component: 'Input',
    fieldName: 'orders',
    label: '订单号',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '外部编号',
  },
  {
    component: 'RangePicker',
    fieldName: 'subtime',
    label: '完成日期',
  },
  {
    component: 'RangePicker',
    fieldName: 'outtime',
    label: '出库时间',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '编码',
    field: 'id',
    visible:false,
  },
  {
    title: '委外名称',
    field: 'title',
  },
  {
    title: '委外商家',
    field: 'cname',
  },
  {
    title: '订单号',
    field: 'orders',
  },
  {
    title: '外部编号',
    field: 'sn',
  },
  {
    title: '委外物料清单',
    field: 'stonames',
  },
  {
    title: '出库库位',
    field: 'stocktype',
  },
  {
    title: '价格',
    field: 'prices',
  },
  {
    title: '要求完成日期',
    field: 'subtime',
  },
  {
    title: '出库时间',
    field: 'outtime',
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '入库物料',
    field: 'stoid',
    visible:false,
  },
  {
    title: '入库数量',
    field: 'counts',
    visible:false,
  },
  {
    title: '类型',
    field: 'fromtype',
  },
  {
    title: '',
    field: 'remark2',
    visible:false,
  },
  {
    field: 'showinfo',
    fixed: 'right',
    slots: { default: 'showinfo' },
    title: '查看',
    width: 120,
  },
  {
    field: 'inlist',
    fixed: 'right',
    slots: { default: 'inlist' },
    title: '查看',
    width: 120,
  },
  {
    field: 'action',
    fixed: 'right',
    slots: { default: 'action' },
    title: '操作',
    width: 180,
  },
];

export const drawerSchema: FormSchemaGetter = () => [
  {
    label: '编码',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '委外物料清单',
    fieldName: 'stockids',
    component: 'Textarea',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '入库物料',
    fieldName: 'stocknames',
    component: 'Textarea',
    formItemClass: 'col-span-2',
    rules: 'required',
  },
  {
    label: '入库库位',
    fieldName: 'stocktype',
    component: 'Select',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '入库库位',
    fieldName: 'stocktypename',
    component: 'Select',
    formItemClass: 'col-span-2',
  },
];
