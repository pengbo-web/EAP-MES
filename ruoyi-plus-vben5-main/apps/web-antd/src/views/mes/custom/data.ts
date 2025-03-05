import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.CUSTOMTYPE 便于维护
      options: getDictOptions('customtype'),
    },
    fieldName: 'type',
    label: '类型',
  },
  {
    component: 'Input',
    fieldName: 'descp',
    label: '简称',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '全称',
  },
  {
    component: 'Input',
    fieldName: 'phone',
    label: '联系方式',
  },
  {
    component: 'Input',
    fieldName: 'address',
    label: '收货地址',
  },
  {
    component: 'Input',
    fieldName: 'address1',
    label: '邮寄地址',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '编码',
    field: 'id',
    visible: false,
  },
  {
    title: '类型',
    field: 'type',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.CUSTOMTYPE 便于维护
        return renderDict(row.type, 'customtype');
      },
    },
  },
  {
    title: '全称',
    field: 'title',
  },
  {
    title: '简称',
    field: 'descp',
  },
  {
    title: '联系人',
    field: 'cname',
  },
  {
    title: '联系方式',
    field: 'phone',
  },
  {
    title: '银行账户',
    field: 'cardnum',
  },  
  {
    title: '社会编码',
    field: 'shnum',
  },
  {
    title: '收货地址',
    field: 'address',
  },
  {
    title: '邮寄地址',
    field: 'address1',
  },{
    title: '备注',
    field: 'remark',
  },
  {
    title: '是否禁用',
    field: 'status',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
        return renderDict(row.status, 'diyisno');
      },
    },
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
    label: '编码',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '全称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '简称',
    fieldName: 'descp',
    component: 'Input',
  },
  {
    label: '类型',
    fieldName: 'type',
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.CUSTOMTYPE 便于维护
      options: getDictOptions('customtype'),
    },
    rules: 'selectRequired',
  },
  {
    label: '联系人',
    fieldName: 'cname',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '联系方式',
    fieldName: 'phone',
    component: 'Input',
  },
  {
    label: '银行账户',
    fieldName: 'cardnum',
    component: 'Input',
  },
  
  
  {
    label: '社会编码',
    fieldName: 'shnum',
    component: 'Input',
  },
  {
    label: '收货地址',
    fieldName: 'address',
    component: 'Input',
  },
  {
    label: '邮寄地址',
    fieldName: 'address1',
    component: 'Input',
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
