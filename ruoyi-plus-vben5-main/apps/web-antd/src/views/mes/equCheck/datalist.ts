import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import type { TableProps } from 'ant-design-vue';
import { getDictOptions } from '#/utils/dict';
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
    label: '状态',
  },
];
export const columns1: TableProps['columns'] = [
  {
    title: '检验名称',
    dataIndex: 'title',
    width: '30%',
  },
  {
    title: '标准值',
    dataIndex: 'standards',
  },
  {
    title: '区域图',
    dataIndex: 'upimg',
  },
  {
    title: '备注',
    dataIndex: 'remark',
    filterSearch: (input, filter) =>
      (filter.value as string).indexOf(input) > -1,
  },
  {
    title: '编辑',
    dataIndex: 'operation',
    width: '15%',
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
    label: '公差',
    fieldName: 'refvalue',
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
    defaultValue: '1',
    
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Input',
    componentProps: {
      // options: {
      //  readyonly: false, // 是否只读
      // }
      // width: '100%', // 宽度
      // showImageUpload: true, // 是否显示图片上传
      // height: 400 // 高度 默认400
    },
  },
];
