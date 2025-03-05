import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'title',
    label: '厂商',
  },
  {
    component: 'Input',
    fieldName: 'process',
    label: '工艺',
  },
  {
    component: 'Input',
    fieldName: 'mobile',
    label: '联系方式',
  },
  {
    component: 'Input',
    fieldName: 'address',
    label: '地址',
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
    title: '厂商',
    field: 'title',
  },
  {
    title: '工艺',
    field: 'process',
  },
  {
    title: '联系方式',
    field: 'mobile',
  },
  {
    title: '地址',
    field: 'address',
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
    field: 'action',
    fixed: 'right',
    slots: { default: 'action' },
    title: '操作',
    width: 180,
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
    label: '厂商',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '工艺',
    fieldName: 'process',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '联系方式',
    fieldName: 'mobile',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '地址',
    fieldName: 'address',
    component: 'Input',
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
    defaultValue: '1',
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
