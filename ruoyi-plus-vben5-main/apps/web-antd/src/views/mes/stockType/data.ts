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
    fieldName: 'descs',
    label: '位置',
  },
  {
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    fieldName: 'looptime',
    label: '移动时间',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '货架编码',
  },
  {
    component: 'Input',
    fieldName: 'kwid',
    label: '库位信息',
  },
];

export const columns: VxeGridProps['columns'] = [
  {
    title: '主键',
    field: 'id',
   visible: false,
  },
  
  {
    title: '货架编码',
    field: 'sn',
    treeNode: true,
  },
  {
    title: '名称',
    field: 'title',
  },
  {
    title: '位置',
    field: 'descs',
  },
  {
    title: '最后移动时间',
    field: 'looptime',
  },
  {
    title: '移动次数',
    field: 'counts',
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '库位信息',
    field: 'kwid',
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
    label: '货架编码',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '上级',
    fieldName: 'pid',
    component: 'TreeSelect',
    rules: 'required',
  },
  {
    label: '位置',
    fieldName: 'descs',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '库位信息',
    fieldName: 'kwid',
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
  }
];
