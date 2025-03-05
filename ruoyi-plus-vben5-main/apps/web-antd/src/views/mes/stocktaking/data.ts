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
    fieldName: 'counts',
    label: '盘查数量',
  },
  {
    component: 'Input',
    fieldName: 'ycrec',
    label: '异常原因',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '编号',
    field: 'id',
    visible:false,
  },
  {
    title: '物料',
    field: 'stockVo.tilte',
  },
  {
    title: '库位',
    field: 'stypeinfo.sn',
  },
  {
    title: '盘查数量',
    field: 'counts',
  },
  {
    title: '异常数量',
    field: 'yccounts',
  },
  {
    title: '异常原因',
    field: 'ycrec',
  },
  {
    title: '结存量',
    field: 'jc',
  },
  {
    title: '状态',
    field: 'status',
    slots:{default: 'status'}
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
    label: '盘查数量',
    fieldName: 'counts',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '异常数量',
    fieldName: 'yccounts',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '异常原因',
    fieldName: 'ycrec',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '结存量',
    fieldName: 'jc',
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
