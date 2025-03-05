import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import { getDictOptions } from '#/utils/dict';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'title',
    label: '名称',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '图号',
  },
  {
    component: 'Input',
    fieldName: 'brand',
    label: '品牌',
  },
  {
    component: 'Select',
    componentProps: {
      options: getDictOptions('cuttingtype'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'otype',
    label: '类别',
  },
  {
    component: 'Input',
    fieldName: 'stocknum',
    label: '当前库存',
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
    title: '名称',
    field: 'title',
  },
  {
    title: '图号',
    field: 'sn',
  },
  {
    title: '品牌',
    field: 'brand',
  },
  {
    title: '类别',
    field: 'otype',
  },
  {
    title: '当前库存',
    field: 'stocknum',
  },
  {
    title: '历史库存',
    field: 'stockrec',
  },
  {
    title: '报废库存',
    field: 'otstock',
  },
  {
    title: '使用中数量',
    field: 'usestock',
  },
  {
    title: '保养周期',
    field: 'otime',
  },
  {
    title: '下次周期',
    field: 'nexttime',
  },
  {
    title: '可用库存量',
    field: 'instock',
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
    title: '已加工数量',
    field: 'markcount',
  },
  {
    title: '加工产品',
    field: 'proids',
  },
  
  {
    title: '查看',
    field: 'infos',
    slots: {default: 'infos'}
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
    label: '名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '图号',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '品牌',
    fieldName: 'brand',
    component: 'Input',
  },
  {
    label: '类别',
    fieldName: 'otype',
    component: 'RadioGroup',
    componentProps: {
      options: getDictOptions('cuttingtype'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue: '1',
    rules: 'selectRequired',
  },
  {
    label: '当前库存',
    fieldName: 'stocknum',
    component: 'Input',
    defaultValue:0,
    rules: 'required',
  },
  {
    label: '历史库存',
    fieldName: 'stockrec',
    component: 'Input',
    defaultValue:0,
    rules: 'required',
  },
  {
    label: '保养周期',
    fieldName: 'otime',
    component: 'Input',
    renderComponentContent: () => ({
      suffix: () => '天',
    }),
    rules: 'required',
  },
  {
    label: '下次周期',
    fieldName: 'nexttime',
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
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue: '1',
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
