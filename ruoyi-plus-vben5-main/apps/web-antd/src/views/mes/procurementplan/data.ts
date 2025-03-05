import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'sn',
    label: '流转号',
  },
  {
    component: 'Input',
    fieldName: 'proname',
    label: '产品',
  },
  {
    component: 'Input',
    fieldName: 'count',
    label: '数量',
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
    title: '流转号',
    field: 'sn',
  },
  {
    title: '产品',
    field: 'product.title',
  },
  {
    title: '规格',
    field: 'stockVo.gk',
  },
  {
    title: '面积',
    field: 'mj',
  },
  {
    title: '数量',
    field: 'count',
  },
  {
    title: '单位',
    field: 'unitId',
    visible: false,
  },
  {
    title: '申请人',
    field: 'username',
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
    title: '单价',
    field: 'prices',
  },
  {
    title: '品类',
    field: 'fromtype',
  },
  {
    title: '时间',
    field: 'createTime',
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
    label: '产品',
    fieldName: 'proId',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    rules: 'required',
  },
  {
    label: '产品',
    fieldName: 'proname',
    component: 'Input',
    formItemClass: 'col-span-2',
    rules: 'required',
  },
  {
    label: '数量',
    fieldName: 'count',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '单价',
    fieldName: 'prices',
    component: 'Input',
    rules: 'required',
  },
  
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
