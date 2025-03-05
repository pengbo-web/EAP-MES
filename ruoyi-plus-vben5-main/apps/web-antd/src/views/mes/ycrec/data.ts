import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'sn',
    label: '流转',
  },
  {
    component: 'Input',
    fieldName: 'ycdesc',
    label: '描述',
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
    title: '流转',
    field: 'sn',
  },
  {
    title: '物料',
    field: 'productVo.title',
  },
  {
    title: '正常数量',
    field: 'count',
  },
  {
    title: '图片',
    field: 'imgs',
  },
  {
    title: '异常数量',
    field: 'yccount',
  },
  {
    title: '描述',
    field: 'ycdesc',
  },
  {
    title: '用户',
    field: 'username',
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
    visible: false,
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
    label: '流转',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '正常数量',
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
    label: '异常数量',
    fieldName: 'yccount',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '描述',
    fieldName: 'ycdesc',
    component: 'Input',
    rules: 'required',
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
];
