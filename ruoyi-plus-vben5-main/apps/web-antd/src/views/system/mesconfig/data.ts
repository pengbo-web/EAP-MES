import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'times',
    label: '工作时间',
  },
  {
    component: 'Input',
    fieldName: 'chetime',
    label: '检查时间',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '编号',
    field: 'id',
  },
  {
    title: '工作时间',
    field: 'times',
  },
  {
    title: '检查时间',
    field: 'chetime',
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
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '工作时间',
    fieldName: 'times',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '检查时间',
    fieldName: 'chetime',
    component: 'Input',
    rules: 'required',
  },
];
