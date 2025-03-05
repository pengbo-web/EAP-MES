import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'title',
    label: '名称',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '编码',
  },
  {
    component: 'Input',
    fieldName: 'brand',
    label: '品牌',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.EQUUSESTATUS 便于维护
      options: getDictOptions('equusestatus'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'otype',
    label: '类别',
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
    title: '编码',
    field: 'sn',
  },
  {
    title: '品牌',
    field: 'brand',
  },
  {
    title: '类别',
    field: 'otype',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.EQUUSESTATUS 便于维护
        return renderDict(row.otype, 'equusestatus');
      },
    },
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
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
        return renderDict(row.status, 'diyisno');
      },
    },
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '查看',
    field: 'infos',
    slots: { default: 'infos' },
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
    label: '编码',
    fieldName: 'sn',
    component: 'Input',
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
      // 可选从DictEnum中获取 DictEnum.EQUUSESTATUS 便于维护
      options: getDictOptions('equusestatus'),
      buttonStyle: 'solid',
      optionType: 'button',
    },

    rules: 'selectRequired',
  },
  {
    label: '当前库存',
    fieldName: 'stocknum',
    component: 'Input',
    defaultValue:0,
  },
  {
    label: '历史库存',
    fieldName: 'stockrec',
    component: 'Input',
    defaultValue:0,
  },
  {
    label: '报废库存',
    fieldName: 'otstock',
    component: 'Input',
    defaultValue:0,
    rules: 'required',
  },
  {
    label: '使用中数量',
    fieldName: 'usestock',
    component: 'Input',
    defaultValue:0,
    rules: 'required',
  },
  {
    label: '保养周期',
    fieldName: 'otime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
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
  },
  {
    label: '可用库存量',
    fieldName: 'instock',
    component: 'Input',
    defaultValue:0,
    rules: 'required',
  },
  {
    label: '状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue:'1',
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
