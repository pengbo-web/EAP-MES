import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'title',
    label: '名称',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '编码',
  },
  {
    component: 'Input',
    fieldName: 'brand',
    label: '品牌',
  },
  
  {
    component: 'Input',
    fieldName: 'local',
    label: '位置',
  },
  {
    component: 'Input',
    fieldName: 'gk',
    label: '规格',
  },
  {
    component: 'Input',
    fieldName: 'descs',
    label: '描述',
  },
  {
    component: 'RangePicker',
    fieldName: 'otime',
    label: '保养周期',
  },
  {
    component: 'RangePicker',
    fieldName: 'nexttime',
    label: '下次周期',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible: false
  },
  {
    title: '操作',
    field: 'toadd',
    width: 100,
    slots:{default: 'toadd'}
  },
  {
    title: '查看',
    field: 'infos',
    width: 100,
    slots:{default: 'infos'}
  },
  {
    title: '名称',
    field: 'title',
  },
  {
    title: '编码',
    field: 'sn',
  },
  {
    title: '品牌',
    field: 'brand',
  },
  {
    title: '类别',
    field: 'otype',
  },
  {
    title: '当前库存',
    field: 'stocknum',
  },
  {
    title: '位置',
    field: 'local',
  },
  {
    title: '规格',
    field: 'gk',
  },
  {
    title: '描述',
    field: 'descs',
  },
  {
    title: '历史库存',
    field: 'stockrec',
  },
  {
    title: '报废库存',
    field: 'otstock',
  },
  {
    title: '使用中数量',
    field: 'usestock',
  },
  {
    title: '保养周期',
    field: 'otime',
  },
  {
    title: '下次周期',
    field: 'nexttime',
  },
  {
    title: '可用库存量',
    field: 'instock',
  },
  {
    title: '状态',
    field: 'status',
    visible:false,
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '已加工数量',
    field: 'markcount',

  },
  {
    title: '加工产品',
    field: 'proids',
    visible:false,
  },
  {
    title: '刀具名称',
    field: 'names',
    visible:false,
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
    label: '名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '编码',
    fieldName: 'sn',
    component: 'Input',
  },
  {
    label: '品牌',
    fieldName: 'brand',
    component: 'Input',
  },
  {
    label: '类别',
    fieldName: 'otype',
    component: 'Input',
  },
  {
    label: '当前库存',
    fieldName: 'stocknum',
    component: 'Input',
    defaultValue:0,
    rules: 'required',
  },
  {
    label: '位置',
    fieldName: 'local',
    component: 'Input',
  },
  {
    label: '规格',
    fieldName: 'gk',
    component: 'Input',
  },
  {
    label: '描述',
    fieldName: 'descs',
    component: 'Input',
  },
  {
    label: '保养周期',
    fieldName: 'otime',
    component: 'Input',
    renderComponentContent: () => ({
      suffix: () => '天',
    }),
    rules: 'required',
  },
  {
    label: '下次周期',
    fieldName: 'nexttime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
