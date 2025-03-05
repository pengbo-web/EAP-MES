import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  
  {
    component: 'Input',
    fieldName: 'outname',
    label: '外协',
  },
  {
    component: 'Input',
    fieldName: 'proname',
    label: '产品',
  },
  {
    component: 'Input',
    fieldName: 'count',
    label: '数量',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible: false,
  },
  {
    title: '外协',
    field: 'outsourcingVo.title',
  },
  {
    title: '产品',
    field: 'productVo.title',
  },
  {
    title: '数量',
    field: 'count',
  },
  {
    title: '状态',
    field: 'status',
    slots: { default: 'status' },
  },
  {
    title: '开始时间',
    field: 'createTime',
  },
  {
    title: '结束时间',
    field: 'remark',
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
    label: '流转sn',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '流转id',
    fieldName: 'taskId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '外协',
    fieldName: 'outId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '产品',
    fieldName: 'proId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '数量',
    fieldName: 'count',
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
