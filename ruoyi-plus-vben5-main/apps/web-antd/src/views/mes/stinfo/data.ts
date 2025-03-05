import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'stockname',
    label: '物料',
  },
  {
    component: 'Input',
    fieldName: 'stocktypeid',
    label: '库位',
  },
  {
    component: 'Input',
    fieldName: 'counts',
    label: '数量',
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
    title: '物料',
    field: 'stock.tilte',
  },
  {
    title: '库位',
    field: 'stockType.sn',
  },
  {
    title: '数量',
    field: 'counts',
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default: 'status',
    },
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
    label: '编码',
    fieldName: 'id',
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
    label: '库位',
    fieldName: 'stocktypeid',
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
