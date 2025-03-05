import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'workid',
    label: '工作id',
  },
  {
    component: 'Input',
    fieldName: 'orderid',
    label: '订单号',
  },
  {
    component: 'Input',
    fieldName: 'subtime',
    label: '交货日期',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '流转码',
  },
  {
    component: 'Input',
    fieldName: 'stockid',
    label: '物料码',
  },
  {
    component: 'Input',
    fieldName: 'banci',
    label: '版次',
  },
  {
    component: 'Input',
    fieldName: 'wul',
    label: '批次',
  },
  {
    component: 'Input',
    fieldName: 'counts',
    label: '数量',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'status',
    label: '角色状态',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '编号',
    field: 'id',
  },
  {
    title: '工作id',
    field: 'workid',
  },
  {
    title: '订单号',
    field: 'orderid',
  },
  {
    title: '交货日期',
    field: 'subtime',
  },
  {
    title: '流转码',
    field: 'sn',
  },
  {
    title: '物料码',
    field: 'stockid',
  },
  {
    title: '版次',
    field: 'banci',
  },
  {
    title: '批次',
    field: 'wul',
  },
  {
    title: '数量',
    field: 'counts',
  },
  {
    title: '角色状态',
    field: 'status',
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    field: 'action',
    fixed: 'right',
    slots: { default: 'action' },
    title: '操作',
    width: 180,
  },
];

export const modalSchema: FormSchemaGetter = () => [
  {
    label: '编号',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '工作id',
    fieldName: 'workid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '订单号',
    fieldName: 'orderid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '交货日期',
    fieldName: 'subtime',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '流转码',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '物料码',
    fieldName: 'stockid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '版次',
    fieldName: 'banci',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '批次',
    fieldName: 'wul',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '数量',
    fieldName: 'counts',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '角色状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      buttonStyle: 'solid',
      optionType: 'button',
    },
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
    rules: 'required',
  },
];
