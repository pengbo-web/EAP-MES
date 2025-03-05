import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
 
  {
    component: 'Input',
    fieldName: 'title',
    label: '标题',
  },
  {
    component: 'Input',
    fieldName: 'conts',
    label: '异常内容',
  },
  {
    component: 'Input',
    fieldName: 'rest',
    label: '结果',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible:false
  },
  {
    title: '设备名称',
    field: 'equname',
  },
  {
    title: '点检类别',
    field: 'mainname',
  },
  {
    title: '标题',
    field: 'title',
  },
  {
    title: '异常内容',
    field: 'conts',
  },
  {
    title: '结果',
    field: 'rest',
  },
  {
    title: '提交人',
    field: 'username',
  },
  {
    title: '图片',
    field: 'remark',
    slots:{default:'remark'}
  },
  {
    title: '状态',
    field: 'status',
    slots:{default:'status'}
  },
  
  {
    title: '',
    field: 'equid',
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
    label: '主表关联',
    fieldName: 'equmian',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '列表关联',
    fieldName: 'listid',
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
    label: '异常内容',
    fieldName: 'conts',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '结果',
    fieldName: 'rest',
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
    label: '',
    fieldName: 'equid',
    component: 'Input',
    rules: 'required',
  },
];
