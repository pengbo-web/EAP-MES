import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'stockid',
    label: '物料',
  },
  {
    component: 'Input',
    fieldName: 'locationId',
    label: '库位ID',
  },
  {
    component: 'Input',
    fieldName: 'quantity',
    label: '操作数量',
  },
  {
    component: 'Input',
    fieldName: 'operation',
    label: '操作类型',
  },
  {
    component: 'Input',
    fieldName: 'batchNo',
    label: '业务批次号',
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
    visible: false,
  },
  {
    title: '',
    field: 'sn',
    visible: false,
  },
  {
    title: '物料',
    field: 'stockid',
  },
  {
    title: '库位ID',
    field: 'locationId',
  },
  {
    title: '操作数量',
    field: 'quantity',
  },
  {
    title: '操作类型',
    field: 'operation',
  },
  {
    title: '业务批次号',
    field: 'batchNo',
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

export const drawerSchema: FormSchemaGetter = () => [
  {
    label: '编号',
    fieldName: 'id',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '',
    fieldName: 'sn',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '物料',
    fieldName: 'stockid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '库位ID',
    fieldName: 'locationId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '操作数量',
    fieldName: 'quantity',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '操作类型',
    fieldName: 'operation',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '业务批次号',
    fieldName: 'batchNo',
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
