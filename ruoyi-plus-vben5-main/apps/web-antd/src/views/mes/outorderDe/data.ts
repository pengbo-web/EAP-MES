import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'stockid',
    label: '物料',
  },
  {
    component: 'Input',
    fieldName: 'unid',
    label: '单位',
  },
  {
    component: 'Input',
    fieldName: 'prices',
    label: '单价',
  },
  {
    component: 'Input',
    fieldName: 'counts',
    label: '数量',
  },
  {
    component: 'Input',
    fieldName: 'allprice',
    label: '总价',
  },
  {
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    fieldName: 'ctime',
    label: '交付时间',
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
  {
    component: 'Input',
    fieldName: 'pcount',
    label: '已计划数量',
  },
  {
    component: 'Select',
    componentProps: {
    },
    fieldName: 'fromtype',
    label: '类型',
  },
  {
    component: 'Input',
    fieldName: 'remark2',
    label: '',
  },
  {
    component: 'Input',
    fieldName: 'orderid',
    label: '',
  },
  {
    component: 'Input',
    fieldName: 'mainid',
    label: '',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '编号',
    field: 'id',
  },
  {
    title: '物料',
    field: 'stockid',
  },
  {
    title: '单位',
    field: 'unid',
  },
  {
    title: '单价',
    field: 'prices',
  },
  {
    title: '数量',
    field: 'counts',
  },
  {
    title: '总价',
    field: 'allprice',
  },
  {
    title: '交付时间',
    field: 'ctime',
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
    title: '已计划数量',
    field: 'pcount',
  },
  {
    title: '类型',
    field: 'fromtype',
  },
  {
    title: '',
    field: 'remark2',
  },
  {
    title: '',
    field: 'orderid',
  },
  {
    title: '',
    field: 'mainid',
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
    label: '物料',
    fieldName: 'stockid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '单位',
    fieldName: 'unid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '单价',
    fieldName: 'prices',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '数量',
    fieldName: 'counts',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '总价',
    fieldName: 'allprice',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '交付时间',
    fieldName: 'ctime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    rules: 'required',
  },
  {
    label: '状态',
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
    label: '已计划数量',
    fieldName: 'pcount',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '类型',
    fieldName: 'fromtype',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
  },
  {
    label: '',
    fieldName: 'remark2',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '',
    fieldName: 'orderid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '',
    fieldName: 'mainid',
    component: 'Input',
    rules: 'required',
  },
];
