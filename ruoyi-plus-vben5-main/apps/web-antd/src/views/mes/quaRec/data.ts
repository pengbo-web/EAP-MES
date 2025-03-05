import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  
  {
    component: 'Input',
    fieldName: 'sn',
    label: '流转号',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '标题',
  },
  {
    component: 'Input',
    fieldName: 'valuest',
    label: '值',
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
    title: '质检人',
    field: 'createByName',
  },
  {
    title: '被质检人',
    field: 'taskinfo.username',
  },
  {
    title: '质检类型',
    field: 'quaname',
  },
  {
    title: '流转号',
    field: 'sn',
  },
  {
    title: '标题',
    field: 'title',
  },
  {
    title: '订单名称',
    field: 'taskinfo.workVo.title',
  },
  {
    title: '值',
    field: 'values',
  },
  {
    title: '结果',
    field: 'res',
  },
  {
    title: '结果',
    field: 'status',
    slots: { default: 'status' },
  },
  {
    title: '备注',
    field: 'createTime',
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
    label: '质检主键',
    fieldName: 'quaMian',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '流转号',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '标题',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '任务列表',
    fieldName: 'taskId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '值',
    fieldName: 'valuest',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '结果',
    fieldName: 'res',
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
];
