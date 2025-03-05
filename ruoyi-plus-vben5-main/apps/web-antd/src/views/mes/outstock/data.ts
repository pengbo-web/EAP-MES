import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'proname',
    label: '物料',
  },
  {
    component: 'Input',
    fieldName: 'fromtype',
    label: '来源',
  },
  {
    component: 'Input',
    fieldName: 'count',
    label: '数量',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '流转码',
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
    title: '物料',
    field: 'stockVo.tilte',
  },
  {
    title: '出库单位',
    field: 'uniname',
  },
  {
    title: '来源',
    field: 'fromtype',
  },
  {
    title: '数量',
    field: 'count',
  },
  {
    title: '流转码',
    field: 'sn',
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default: 'status',
    },
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '上次结存',
    field: 'laststock',
  },
  {
    title: '领用人',
    field: 'username',
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
    label: '物料',
    fieldName: 'proId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '出库单位',
    fieldName: 'unitId',
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
    label: '数量',
    fieldName: 'count',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '流转码',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '任务关联',
    fieldName: 'taskId',
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
  {
    label: '上次结存',
    fieldName: 'laststock',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '',
    fieldName: 'remark1',
    component: 'Input',
    rules: 'required',
  },
];
