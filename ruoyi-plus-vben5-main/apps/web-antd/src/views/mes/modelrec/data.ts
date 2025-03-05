import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'modelid',
    label: '磨具编号',
    
  },
  {
    component: 'Input',
    fieldName: 'wname',
    label: '使用地址',
  },
  {
    component: 'Select',
    componentProps: {
    },
    fieldName: 'type',
    label: '类型',
  },
  {
    component: 'Input',
    fieldName: 'cname',
    label: '外发名称',
  },
  {
    component: 'Input',
    fieldName: 'phone',
    label: '联系电话',
  },
  {
    component: 'Input',
    fieldName: 'address',
    label: '地址',
  },
  {
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    fieldName: 'endtime',
    label: '归还日期',
  },
  {
    component: 'Textarea',
    fieldName: 'remark1',
    label: '',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'status',
    label: '状态',
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
    title: '磨具编号',
    field: 'modelid',
  },
  {
    title: '使用地址',
    field: 'wname',
  },
  {
    title: '类型',
    field: 'type',
  },
  {
    title: '外发名称',
    field: 'cname',
  },
  {
    title: '联系电话',
    field: 'phone',
  },
  {
    title: '地址',
    field: 'address',
  },
  {
    title: '归还日期',
    field: 'endtime',
  },
  {
    title: '',
    field: 'remark1',
  },
  {
    title: '状态',
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
    label: '编号',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '磨具编号',
    fieldName: 'modelid',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '使用地址',
    fieldName: 'wname',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '外发名称',
    fieldName: 'cname',
    component: 'Input',
  },
  {
    label: '联系电话',
    fieldName: 'phone',
    component: 'Input',
  },
  {
    label: '地址',
    fieldName: 'address',
    component: 'Input',
  },
  {
    label: '归还日期',
    fieldName: 'endtime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
