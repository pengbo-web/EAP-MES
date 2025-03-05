import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

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
    fieldName: 'shuidian',
    label: '税点',
  },
  {
    component: 'Input',
    fieldName: 'met',
    label: '结算方式',
  },
  {
    component: 'Input',
    fieldName: 'count',
    label: '数量',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'status',
    label: '状态',
  },
  {
    component: 'Select',
    componentProps: {
    },
    fieldName: 'fromtype',
    label: '类型',
  },
  {
    component: 'Input',
    fieldName: 'remark2',
    label: '备注2',
  },
  {
    component: 'Input',
    fieldName: 'orderid',
    label: '订单',
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
    field: 'stockVo.tilte',
  },
  {
    title: '批次号',
    field: 'sn',
  },
  {
    title: '供应商',
    field: 'custId',
  },
  {
    title: '进货价',
    field: 'nprice',
  },
  {
    title: '入库货架',
    field: 'stocktypeid',
  },
  {
    title: '税点',
    field: 'shuidian',
  },
  {
    title: '结算方式',
    field: 'met',
  },
  {
    title: '数量',
    field: 'count',
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
        return renderDict(row.status, 'diyisno');
      },
    },
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '类型',
    field: 'fromtype',
  },
  {
    title: '备注2',
    field: 'remark2',
  },
  {
    title: '订单',
    field: 'orderid',
  },
  {
    field: 'action',
    fixed: 'right',
    slots: { default: 'action' },
    title: '操作',
    width: 180,
    visible: false,
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
  },
  {
    label: '物料',
    fieldName: 'stockid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '批次号',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '供应商',
    fieldName: 'custId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '进货价',
    fieldName: 'nprice',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '入库货架',
    fieldName: 'stocktypeid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '税点',
    fieldName: 'shuidian',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '结算方式',
    fieldName: 'met',
    component: 'Input',
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
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diyisno'),
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
  {
    label: '类型',
    fieldName: 'fromtype',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
  },
  {
    label: '备注2',
    fieldName: 'remark2',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '订单',
    fieldName: 'orderid',
    component: 'Input',
    rules: 'required',
  },
];
