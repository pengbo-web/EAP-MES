import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'stockid',
    label: '',
  },
  {
    component: 'Input',
    fieldName: 'counts',
    label: '',
  },
  {
    component: 'Input',
    fieldName: 'mainid',
    label: '',
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
  {
    component: 'Input',
    fieldName: 'orderid',
    label: '订单号',
  },
  {
    component: 'Input',
    fieldName: 'remark2',
    label: '备注',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '',
    field: 'id',
  },
  {
    title: '',
    field: 'stockid',
  },
  {
    title: '',
    field: 'counts',
  },
  {
    title: '',
    field: 'mainid',
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
    title: '订单号',
    field: 'orderid',
  },
  {
    title: '备注',
    field: 'remark2',
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
    label: '',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '',
    fieldName: 'stockid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '',
    fieldName: 'counts',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '',
    fieldName: 'mainid',
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
  {
    label: '订单号',
    fieldName: 'orderid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '备注',
    fieldName: 'remark2',
    component: 'Input',
    rules: 'required',
  },
];
