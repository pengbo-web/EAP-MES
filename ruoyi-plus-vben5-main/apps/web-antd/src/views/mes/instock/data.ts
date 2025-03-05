import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
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
  {
    component: 'Input',
    fieldName: 'fromtype',
    label: '来源',
  },
  {
    component: 'RangePicker',
    fieldName: 'subtime',
    label: '确认时间',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible:false,
  },
  {
    title: '编码',
    field: 'stockVo.sn',
  },
  {
    title: '产品',
    field: 'proName',
  },
  {
    title: '数量',
    field: 'count',
  },
  {
    title: '来源',
    field: 'fromtype',
  },
  {
    title: '价格',
    field: 'nowprice',
  },
  {
    title: '总价',
    field: 'allprice',
  },
  {
    title: '领用人',
    field: 'username',
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default:'status'
    }
  },
  {
    title: '库位信息',
    field: 'kwname',
  },
  {
    title: '是否流转入库',
    field: 'st',
    visible:false,
  },
  {
    title: '确认时间',
    field: 'subtime',
  },
  {
    title: '库存信息',
    field: 'stockid',
    visible:false,
  },
  {
    title: '更新前库存',
    field: 'oldst',
  },
  {
    title: '库位信息',
    field: 'kwid',
    visible:false,
  },
  {
    title: '库位信息',
    field: 'kwsn',
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
    label: '来源',
    fieldName: 'fromtype',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
  },
  {
    label: '价格',
    fieldName: 'nowprice',
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
    label: '库位信息',
    fieldName: 'kwname',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '是否流转入库',
    fieldName: 'st',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '确认时间',
    fieldName: 'subtime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    rules: 'required',
  },
  {
    label: '库存信息',
    fieldName: 'stockid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '更新前库存',
    fieldName: 'oldst',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '库位信息',
    fieldName: 'kwid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '',
    fieldName: 'kwsn',
    component: 'Input',
    rules: 'required',
  },
];
