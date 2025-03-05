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
    fieldName: 'tilte',
    label: '名称',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.STOCKTYPE 便于维护
      options: getDictOptions('stocktype'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'type',
    label: '类别',
  },
  {
    component: 'Select',
    componentProps: {
    },
    fieldName: 'otype',
    label: '品类',
  },
  {
    component: 'Input',
    fieldName: 'iid',
    label: '图号',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '库存编码',
  },
  {
    component: 'Input',
    fieldName: 'custid',
    label: '供应商',
  },
  {
    component: 'Input',
    fieldName: 'gk',
    label: '规格',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'sales',
    label: '收否售卖',
  },
];

export const columns: VxeGridProps['columns'] = [
  {
    title: '主键',
    field: 'id',
    visible: false,
  },
  {
    title: '名称',
    field: 'tilte',
  },{
    title: '名称',
    field: 'sst',
    visible: false,
  },
  {
    title: '类别',
    field: 'type',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.STOCKTYPE 便于维护
        return renderDict(row.type, 'stocktype');
      },
    },
  },
  {
    title: '品类',
    field: 'otype',
  },
  {
    title: '图号',
    field: 'iid',
  },
  {
    title: '库存编码',
    field: 'sn',
  },
  {
    title: '规格',
    field: 'gk',
  },  {
    title: '单位',
    field: 'unitname',
  },
  {
    title: '当前库存',
    field: 'nowstock',
  },
  {
    title: '报警',
    field: 'warnnum',
  },
  {
    title: '供应商',
    field: 'custname',
  },
  {
    title: '库位',
    field: 'stockname',
  },
  {
    title: '进货价',
    field: 'pricein',
    
  },
  {
    title: '销售价',
    field: 'pricesale',
  },
  {
    title: '收否售卖',
    field: 'sales',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
        return renderDict(row.sales, 'diyisno');
      },
    },
  },
  {
    title: '历史库位 ',
    field: 'kwrec',
  },
  {
    field: 'action',
    fixed: 'right',
    slots: { default: 'action' },
    title: '操作',
    width: 120,
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
    label: '编码',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '名称',
    fieldName: 'tilte',
    component: 'Input',
    rules: 'required',
  },
  
  {
    label: '类别',
    fieldName: 'type',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.STOCKTYPE 便于维护
      options: getDictOptions('stocktype'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    rules: 'selectRequired',
  },
  {
    label: '品类',
    fieldName: 'otype',
    component: 'Input',
    help:'此品类关乎MRP计算，可填写为：委外、成品、原材料',
    rules: 'required',
  },
  {
    label: '图号',
    fieldName: 'iid',
    component: 'Input',
  },
  {
    label: '单位',
    fieldName: 'unitid',
    component: 'Select',
    componentProps: {
      getPopupContainer,
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'required',
  },
  {
    label: '规格',
    fieldName: 'gk',
    component: 'Input',
  },{
    label: '供应商',
    fieldName: 'custid',
    component: 'Select',
    componentProps: {
      getPopupContainer,
      mode: 'multiple',
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'required',
  },{
    label: '库位',
    fieldName: 'stockid',
    component: 'Select',
    componentProps: {
      getPopupContainer,
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
  },
  {
    label: '当前库存',
    fieldName: 'nowstock',
    component: 'Input',
    defaultValue: 0,
    rules: 'required',
  },{
    label: '报警数量',
    fieldName: 'warnnum',
    component: 'InputNumber',
    defaultValue: 0,
    rules: 'required',
  },
  {
    label: '进货价',
    fieldName: 'pricein',
    component: 'Input',
    renderComponentContent: () => ({
      suffix: () => '¥',
    }),
    defaultValue: 0,
    rules: 'required',
  },
  {
    label: '销售价',
    fieldName: 'pricesale',
    component: 'Input',
    renderComponentContent: () => ({
      suffix: () => '¥',
    }),
    defaultValue: 0,
    rules: 'required',
  },
  {
    label: '收否售卖',
    fieldName: 'sales',
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
];
