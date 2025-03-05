import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'workshopId',
    label: '工作站',
  },
  {
    component: 'Input',
    fieldName: 'proId',
    label: '物料',
  },
  {
    component: 'Input',
    fieldName: 'zpcount',
    label: '需组装数量',
  },
  {
    component: 'Input',
    fieldName: 'sort',
    label: '排序',
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
    fieldName: 'mustbe',
    label: '是否关键工序',
  },
  {
    component: 'Input',
    fieldName: 'istruec',
    label: '是否工艺流转',
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
    title: '班组',
    field: 'workshopname',
  },
  {
    title: '物料',
    field: 'proname',
  },
  {
    title: '需组装数量',
    field: 'zpcount',
  },
  {
    title: '排序',
    field: 'sort',
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
    title: '是否关键工序',
    field: 'mustbe',
  },
  {
    title: '是否工艺流转',
    field: 'istruec',
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
    label: '工作站',
    fieldName: 'workshopId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '物料',
    fieldName: 'proId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '需组装数量',
    fieldName: 'zpcount',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '排序',
    fieldName: 'sort',
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
    label: '是否关键工序',
    fieldName: 'mustbe',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '是否工艺流转',
    fieldName: 'istruec',
    component: 'Input',
    rules: 'required',
  },
];
