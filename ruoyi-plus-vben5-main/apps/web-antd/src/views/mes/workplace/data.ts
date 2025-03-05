import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import {
  getPopupContainer
} from '@vben/utils';
import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'sn',
    label: '车间编码',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '车间名称',
  },
  {
    component: 'Input',
    fieldName: 'area',
    label: '面积',
  },
  {
    component: 'Input',
    fieldName: 'curator',
    label: '负责人',
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
    title: '车间编码',
    field: 'sn',
  },
  {
    title: '车间名称',
    field: 'title',
  },
  {
    title: '面积',
    field: 'area',
  },
  {
    title: '负责人',
    field: 'usename',
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
    label: '车间编码',
    fieldName: 'sn',
    component: 'Input',
  },
  {
    label: '车间名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '面积',
    fieldName: 'area',
    component: 'Input',
  },
  {
    label: '负责人',
    fieldName: 'curator',
    component: 'Select',
    componentProps: {
      getPopupContainer,
    },
    rules: 'required',
  },
  {
    label: '是否禁用',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue: '0',
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
