import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import { getDictOptions } from '#/utils/dict';
import {
  getPopupContainer
} from '@vben/utils';
export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'userId',
    label: '用户',
  },
  {
    component: 'Textarea',
    fieldName: 'menuids',
    label: '目录',
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
    title: '用户',
    field: 'idaname',
  },
  {
    title: '目录',
    field: 'rolename',
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
    label: '用户',
    fieldName: 'userId',
    component: 'Select',
    componentProps: {
      getPopupContainer,
      mode: 'multiple',
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'required',
  },
  {
    label: '目录',
    fieldName: 'menuids',
    component: 'Select',
    componentProps: {
      getPopupContainer,
      optionFilterProp: 'label',
      optionLabelProp: 'label',
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
