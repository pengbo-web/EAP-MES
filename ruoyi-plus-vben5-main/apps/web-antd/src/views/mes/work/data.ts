import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'sn',
    label: '编码',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '标题',
  },
  {
    component: 'Input',
    fieldName: 'stockname',
    label: '产品关联',
  },
  {
    component: 'RangePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    fieldName: 'endtime',
    label: '需求日期',
  },
  {
    component: 'Input',
    fieldName: 'orderid',
    label: '关联订单号',
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
    field: 'sn',
  },
  {
    title: '标题',
    field: 'title',
  },
  {
    title: '面向类型',
    field: 'fromtype',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
        return renderDict(row.fromtype, 'diy_mark');
      },
    },
  },
  {
    title: '物料',
    field: 'proinfo.title',
  },
  {
    title: '所需数量',
    field: 'neednum',
  },
  {
    title: '当前数量',
    field: 'currnum',
  },
  {
    title: '所有工序',
    field: 'processcount',
  },
  {
    title: '当前工序',
    field: 'currprocess',
  },
  {
    title: '质检次数',
    field: 'checkcount',
  },
  {
    title: '是否加急',
    field: 'istop',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
        return renderDict(row.istop, 'diyisno');
      },
    },
  },
  {
    title: '需求日期',
    field: 'endtime',
  },
  {
    title: '当前工序',
    field: 'currprocess',
  },
  {
    title: '完成工序',
    field: 'okprocess',
  },
  {
    title: '关联订单号',
    field: 'orderid',
  },
  {
    field: 'action',
    fixed: 'right',
    slots: { default: 'action' },
    title: '操作',
    width: 180,
    visible:false,
  },
];

export const drawerSchema: FormSchemaGetter = () => [
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
    label: '编码',
    fieldName: 'sn',
    component: 'Input',
  },
  {
    label: '标题',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '面向类型',
    fieldName: 'fromtype',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diy_mark'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue:'0',
    rules: 'selectRequired',
  },
  {
    label: '产品关联1',
    fieldName: 'proId',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },{
    label: '产品关联',
    fieldName: 'proname',
    component: 'Input',
    formItemClass: 'col-span-2',
    rules: 'required',
  },
  {
    label: '是否流转',
    fieldName: 'isturn',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('isturnon'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue:'0',
    rules: 'required',
  },
  
  {
    label: '所需数量',
    fieldName: 'neednum',
    component: 'Input',
    defaultValue:1,
    rules: 'required',
  },
  {
    label: '是否加急',
    fieldName: 'istop',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue:'0',
    rules: 'selectRequired',
  },
  {
    label: '是否MRP',
    fieldName: 'ismrp',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue:'0',
    rules: 'required',
  },
  {
    label: '需求日期',
    fieldName: 'endtime',
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
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    defaultValue:'0'
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
