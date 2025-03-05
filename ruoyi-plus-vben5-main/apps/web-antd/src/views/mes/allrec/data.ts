import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'type',
    label: '类型',
  },
  {
    component: 'Input',
    fieldName: 'fromtype',
    label: '来源',
  },
  {
    component: 'Input',
    fieldName: 'counts',
    label: '数量',
  },
  {
    component: 'Input',
    fieldName: 'stockname',
    label: '物料',
  },
  {
    component: 'Input',
    fieldName: 'cname',
    label: '供应商',
  },
  {
    component: 'Input',
    fieldName: 'orderid',
    label: '订单号',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '编码',
    field: 'id',
    visible: false,
  },
  {
    title: '物料',
    field: 'stockVo.tilte',
  },
  {
    title: '类型',
    field: 'type',
  },
  {
    title: '来源',
    field: 'fromtype',
  },
  {
    title: '数量',
    field: 'counts',
  },
  {
    title: '单位',
    field: 'unitid',
    visible: false,
  },
  {
    title: '价格',
    field: 'prices',
  },
  
  {
    title: '供应商',
    field: 'cname',
  },
  {
    title: '角色状态',
    field: 'status',
    visible: false,
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '',
    field: 'remark2',
    visible: false,
  },
  {
    title: '库位',
    field: 'stocktype',
    visible: false,
  },
  {
    title: '订单号',
    field: 'orderid',
  },
  {
    field: 'action',
    fixed: 'right',
    slots: { default: 'action' },
    title: '操作',
    width: 180,
    visible: false,
  },
];

export const modalSchema: FormSchemaGetter = () => [
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
    label: '类型',
    fieldName: 'type',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
  },
  {
    label: '来源',
    fieldName: 'fromtype',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
  },
  {
    label: '数量',
    fieldName: 'counts',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '单位',
    fieldName: 'unitid',
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
    label: '物料',
    fieldName: 'stockid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '供应商',
    fieldName: 'cname',
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
    label: '',
    fieldName: 'remark2',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '库位',
    fieldName: 'stocktype',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
  },
  {
    label: '',
    fieldName: 'orderid',
    component: 'Input',
    rules: 'required',
  },
];
