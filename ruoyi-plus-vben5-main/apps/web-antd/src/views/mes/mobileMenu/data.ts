import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'title',
    label: '名称',
  },
  {
    component: 'Input',
    fieldName: 'type',
    label: '类别',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
  },
  {
    title: '图标',
    field: 'icoimg',
  },
  {
    title: '名称',
    field: 'title',
  },
  {
    title: '类别',
    field: 'type',
  },
  {
    title: '连接',
    field: 'urls',
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
    label: '主键',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '图标',
    fieldName: 'icoimg',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '类别',
    fieldName: 'type',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '连接',
    fieldName: 'urls',
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
