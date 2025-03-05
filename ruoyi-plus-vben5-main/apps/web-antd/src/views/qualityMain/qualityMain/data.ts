import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'title',
    label: '质检名称',
  },
  {
    component: 'Input',
    fieldName: 'other',
    label: '别名',
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
    title: '打印',
    field: 'prints',
    slots: { default: 'prints' },
  },
  {
    title: '二维码',
    field: 'qprint',
    
    slots: { default: 'qprint' },
  },
  {
    title: '二维码',
    field: 'qprints',
    type: 'html',
    visible: false,
  },
  {
    title: '质检名称',
    field: 'title',
  },
  {
    title: '别名',
    field: 'other',
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.SYS_YES_NO 便于维护
        return renderDict(row.status, 'diyisno');
      },
    },
  },
  {
    title: '备注',
    field: 'remark',
  },
  { field: 'showall', title: '质检内容', width: 100, slots: { default: 'showall' } },
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
    label: '质检名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '别名',
    fieldName: 'other',
    component: 'Input',
  },
  {
    label: '状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.SYS_YES_NO 便于维护
      options: getDictOptions('sys_yes_no'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue: '0',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
