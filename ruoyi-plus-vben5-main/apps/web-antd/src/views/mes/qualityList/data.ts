import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'qualitymainId',
    label: '名称',
  },
  {
    component: 'Input',
    fieldName: 'standards',
    label: '标准',
  },
  {
    component: 'Input',
    fieldName: 'refvalue',
    label: '公差',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'status',
    label: '角色状态',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
  },
  {
    title: '名称',
    field: 'qualitymainId',
  },
  {
    title: '标准',
    field: 'standards',
  },
  {
    title: '公差',
    field: 'refvalue',
  },
  {
    title: '角色状态',
    field: 'status',
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
    label: '名称',
    fieldName: 'qualitymainId',
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
    label: '公差',
    fieldName: 'refvalue',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '角色状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      buttonStyle: 'solid',
      optionType: 'button',
    },
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'RichTextarea',
    componentProps: {
      // options: {
      //  readyonly: false, // 是否只读
      // }
      // width: '100%', // 宽度
      // showImageUpload: true, // 是否显示图片上传
      // height: 400 // 高度 默认400
    },
    rules: 'required',
  },
];
