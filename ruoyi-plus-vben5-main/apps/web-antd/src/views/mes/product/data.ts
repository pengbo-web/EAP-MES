import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';
import {
  getPopupContainer
} from '@vben/utils';
export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'sn',
    label: '编码',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '物料名称',
  },
  {
    component: 'Input',
    fieldName: 'protypeId',
    label: '类别',
  },
  {
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.MARKPRO 便于维护
      options: getDictOptions('markpro'),
    },
    fieldName: 'mustbe',
    label: '分类',
  },
  {
    component: 'Input',
    fieldName: 'colors',
    label: '颜色',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'seq', width: 100 ,treeNode: true,},
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
    title: '物料名称',
    field: 'title',
    width:180
  },
  {
    title: '使用单位',
    field: 'unit',
  },
  {
    title: '类别',
    field: 'typename',
  },
  {
    title: '组成件',
    field: 'pidname',
  },
  {
    title: '版本',
    field: 'ver',
  },
  {
    title: '重量',
    field: 'weight',
  },
  {
    title: '需求数量',
    field: 'needCount',
  },
  {
    title: 'BOM数量',
    field: 'downCount',
    slots:{default: 'downcount'},
  },
  {
    title: '图片',
    field: 'imgs',
    slots: { default: 'imgs' },
    minWidth: 50,
  },
  {
    title: '颜色',
    field: 'colors',
  },
  {
    title: '产能',
    field: 'stime',
  },
  {
    title: '工费',
    field: 'prices',
  },
  {
    title: '分类',
    field: 'mustbe',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.MARKPRO 便于维护
        return renderDict(row.mustbe, 'markpro');
      },
    },
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.SYS_YES_NO 便于维护
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
    label: '选择物料',
    fieldName: 'stocksname',
    component: 'Input',
    formItemClass: 'col-span-2',
    rules: 'required',
  },
  {
    label: '选择物料',
    fieldName: 'stocks',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    component: 'Input',
    formItemClass: 'col-span-2',
    rules: 'required',
  },

  {
    label: '编码',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '物料名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '使用单位',
    fieldName: 'unitId',
    component: 'Select',
    componentProps:{
      getPopupContainer,
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'required',
  },
  {
    label: '重量',
    fieldName: 'weight',
    component: 'Input',
    defaultValue:1,
    rules: 'required',
  },
  {
    label: '产能',
    fieldName: 'stime',
    component: 'InputNumber',
    componentProps: {
      addonAfter: '分',
    },
    defaultValue:1,
    rules: 'required',
  },
  {
    label: '工费',
    fieldName: 'prices',
    component: 'InputNumber',
    componentProps: {
      addonAfter: '元',
    },
    defaultValue:1,
    rules: 'required',
  },
  {
    label: '类别',
    fieldName: 'protypeId',
    component: 'Select',
    componentProps:{
      getPopupContainer,
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'required',
  },
  
  {
    label: '分类',
    fieldName: 'mustbe',
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.MARKPRO 便于维护
      options: getDictOptions('markpro'),
    },
    rules: 'selectRequired',
  },
  {
    label: '组成件',
    fieldName: 'pidname',
    component: 'Input',
    formItemClass: 'col-span-2',
    rules: 'required',
  },
  {
    label: '组成件',
    fieldName: 'pid',
    component: 'TreeSelect',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '需求数量',
    fieldName: 'needCount',
    component: 'Input',
    defaultValue:1,
    rules: 'required',
  },
  {
    label: '图片',
    fieldName: 'imgs',
    component: 'ImageUpload',
    componentProps: {
      // accept: ['jpg'], // 不支持type/*的写法 支持拓展名(不带.) 文件头(image/png这种)
      // maxNumber: 1, // 最大上传文件数 默认为1 为1会绑定为string而非string[]类型
      // resultField: 'url', // 上传成功后返回的字段名 默认url 可选['ossId', 'url', 'fileName']
    },
  },
  {
    label: '颜色',
    fieldName: 'colors',
    component: 'Input',
  },
  {
    label: '版本',
    fieldName: 'ver',
    component: 'Input',
    defaultValue:0,
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
  {
    label: '状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.SYS_YES_NO 便于维护
      options: getDictOptions('sys_normal_disable'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue:'0',
    rules: 'selectRequired',
  },
];
