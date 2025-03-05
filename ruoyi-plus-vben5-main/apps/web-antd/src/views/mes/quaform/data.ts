import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'proname',
    label: '产品',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '批次',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '标题',
  },
  {
    component: 'Input',
    fieldName: 'resu',
    label: '结果',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '编码',
    field: 'id',
    visible: false,
  },
  {
    title: '产品',
    field: 'productVo.title',
  },
  {
    title: '批次',
    field: 'sn',
  },
  {
    title: '标题',
    field: 'title',
  },
  {
    title: '结果',
    field: 'resu',
  },
  {
    title: '是否入库',
    field: 'instock',
  },
  {
    title: '时间',
    field: 'createTime',
  },
  {
    title: '提交人',
    field: 'username',
  },
  {
    title: '状态',
    field: 'status',
    slots: { default: 'status' },
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
    label: '编码',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '产品',
    fieldName: 'proid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '批次',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '标题',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '结果',
    fieldName: 'resu',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '是否入库',
    fieldName: 'instock',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '排序',
    fieldName: 'sort',
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
    component: 'Textarea',
    rules: 'required',
  },
];
