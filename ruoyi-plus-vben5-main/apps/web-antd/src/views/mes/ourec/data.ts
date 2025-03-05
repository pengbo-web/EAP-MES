import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'stockid',
    label: '物料',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '批次号',
  },
  {
    component: 'Input',
    fieldName: 'custId',
    label: '供应商',
  },
  {
    component: 'Input',
    fieldName: 'nprice',
    label: '进货价',
  },
  {
    component: 'Input',
    fieldName: 'stocktypeid',
    label: '入库货架',
  },
  {
    component: 'Input',
    fieldName: 'orderid',
    label: '订单号',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '',
    field: 'id',
    visible: false,
  },
  {
    title: '物料',
    field: 'stockname',
  },
  {
    title: '出货价',
    field: 'nprice',
  },
  {
    title: '入库货架',
    field: 'stypename',
  },
  {
    title: '数量',
    field: 'count',
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '订单号',
    field: 'orderid',
  },
  {
    title: '状态',
    field: 'status',
    slots:{default:'status'}
  },
  {
    title: '',
    field: 'remark2',
    visible: false,
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
    label: '',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },{
    label: '物料',
    fieldName: 'stockid',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '物料',
    fieldName: 'stockname',
    component: 'Input',
    formItemClass: 'col-span-2',
    rules: 'required',
  },
  {
    label: '批次号',
    fieldName: 'sn',
    component: 'Input',
    formItemClass: 'col-span-2',
  },
  {
    label: '入库货架',
    fieldName: 'stocktypeid',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '入库货架',
    fieldName: 'stocktypename',
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
    label: '状态',
    fieldName: 'status',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    defaultValue:0,
  },
  {
    label: '订单号',
    fieldName: 'orderid',
    component: 'Input',
  },
  
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
