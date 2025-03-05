import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'equMain',
    label: '关联主表',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '名称',
  },
  {
    component: 'Input',
    fieldName: 'standards',
    label: '标准',
  },
  {
    component: 'Input',
    fieldName: 'imgs',
    label: '图片',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.EQUSTATUS 便于维护
      options: getDictOptions('equstatus'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'status',
    label: '状态',
  },
  {
    component: 'Input',
    fieldName: 'upimg',
    label: '',
  },
  {
    component: 'Input',
    fieldName: 'rebak',
    label: '',
  },
  {
    component: 'Input',
    fieldName: 'equId',
    label: '设备id',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
  },
  {
    title: '关联主表',
    field: 'equMain',
  },
  {
    title: '名称',
    field: 'title',
  },
  {
    title: '标准',
    field: 'standards',
  },
  {
    title: '图片',
    field: 'imgs',
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.EQUSTATUS 便于维护
        return renderDict(row.status, 'equstatus');
      },
    },
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '',
    field: 'upimg',
  },
  {
    title: '',
    field: 'rebak',
  },
  {
    title: '设备id',
    field: 'equId',
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
    label: '关联主表',
    fieldName: 'equMain',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    rules: 'required',
  },
  {
    label: '名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '标准',
    fieldName: 'standards',
    component: 'Input',
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
    label: '状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.EQUSTATUS 便于维护
      options: getDictOptions('equstatus'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
