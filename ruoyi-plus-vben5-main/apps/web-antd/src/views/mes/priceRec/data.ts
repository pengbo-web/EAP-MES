import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'proname',
    label: '商品',
  },
  {
    component: 'Input',
    fieldName: 'count',
    label: '当时数量',
  },
  {
    component: 'Input',
    fieldName: 'prices',
    label: '价格',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible:false
  },
  {
    title: '商品',
    field: 'proname',
  },
  {
    title: '当时数量',
    field: 'count',
  },
  {
    title: '价格',
    field: 'prices',
  },
  {
    title: '状态',
    field: 'status',
    visible:false
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
    visible:false
  },
];

export const modalSchema: FormSchemaGetter = () => [
  {
    label: '主键',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '商品',
    fieldName: 'proId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '当时数量',
    fieldName: 'count',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '价格',
    fieldName: 'prices',
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
