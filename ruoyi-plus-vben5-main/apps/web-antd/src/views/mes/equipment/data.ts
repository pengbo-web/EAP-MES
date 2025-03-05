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
    label: '编码',
  },
  {
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.SYS_OPER_TYPE 便于维护
      options: getDictOptions('sys_oper_type'),
    },
    fieldName: 'typeId',
    label: '类别',
  },
  {
    component: 'Input',
    fieldName: 'brand',
    label: '品牌',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '标题',
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
    title: '编码',
    field: 'sn',
  },
  {
    title: '类别',
    field: 'typename',
  },
  {
    title: '品牌',
    field: 'brand',
  },
  {
    title: '保养次数',
    field: 'count',
  },
  {
    title: '标题',
    field: 'title',
  },
  {
    field: 'sst',
    title: '部门编号',
    width: 200,
    visible: false,
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.SYS_NORMAL_DISABLE 便于维护
        return renderDict(row.status, 'sys_normal_disable');
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
    label: '类别',
    fieldName: 'typeId',
    component: 'Select',
    componentProps: {
      getPopupContainer,
    },
    rules: 'selectRequired',
  },
  {
    label: '品牌',
    fieldName: 'brand',
    component: 'Input',
  },
  {
    label: '保养次数',
    fieldName: 'count',
    component: 'Input',
  },
  
  {
    label: '是否禁用',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.SYS_NORMAL_DISABLE 便于维护
      options: getDictOptions('sys_normal_disable'),
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
