import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'sn',
    label: '物流号',
  },
  {
    component: 'Input',
    fieldName: 'cname',
    label: '名称',
  },
  {
    component: 'Input',
    fieldName: 'phone',
    label: '联系方式',
  },
  {
    component: 'Input',
    fieldName: 'conts',
    label: '信息',
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
    title: '物流号',
    field: 'sn',
  },
  {
    title: '名称',
    field: 'cname',
  },
  {
    title: '联系方式',
    field: 'phone',
  },
  {
    title: '信息',
    field: 'conts',
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
    label: '物流号',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '名称',
    fieldName: 'cname',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '联系方式',
    fieldName: 'phone',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '信息',
    fieldName: 'conts',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '角色状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diyisno'),
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
];
