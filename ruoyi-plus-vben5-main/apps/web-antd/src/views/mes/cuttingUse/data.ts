import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'count',
    label: '数量',
  },
  {
    component: 'Input',
    fieldName: 'rebak',
    label: '原因',
  },
  {
    component: 'Input',
    fieldName: 'username',
    label: '用户',
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
    title: '主表',
    field: 'mesCuttingVo.title',
  },
  {
    title: '数量',
    field: 'count',
  },
  {
    title: '图片',
    field: 'imgs',
  },
  {
    title: '原因',
    field: 'rebak',
  },
  {
    title: '状态',
    field: 'type',
  },
  {
    title: '用户',
    field: 'username',
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default:'status',
    }
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '工作中',
    field: 'workId',
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
    label: '主表',
    fieldName: 'toolid',
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
    label: '图片',
    fieldName: 'imgs',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '原因',
    fieldName: 'rebak',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '状态',
    fieldName: 'type',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
  },
  {
    label: '用户',
    fieldName: 'userId',
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
    label: '工作中',
    fieldName: 'workId',
    component: 'Textarea',
    rules: 'required',
  },
];
