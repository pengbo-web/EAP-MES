import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import { getDictOptions } from '#/utils/dict';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'recids',
    label: '采购列表',
  },
  {
    component: 'Input',
    fieldName: 'sn',
    label: '批次号',
  },
  {
    component: 'Select',
    componentProps: {
    },
    fieldName: 'stocktype',
    label: '入库位置',
  },
  {
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    fieldName: 'ctime',
    label: '采购时间',
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
    title: '采购列表',
    field: 'stockname',
  },
  {
    title: '批次号',
    field: 'sn',
  },
  { title: '查看', width: 200, slots: { default: 'infos' } },
  {
    title: '入库位置',
    field: 'stocktype',
  },
  {
    title: '总价',
    field: 'allprice',
  },
  {
    title: '税点',
    field: 'SHUI',
  },
  {
    title: '文件',
    field: 'files',
  },
  {
    title: '提交人',
    field: 'username',
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
    title: '采购时间',
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
    label: '采购列表',
    fieldName: 'recids',
    component: 'Textarea',
    rules: 'required',
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
    label: '订单号',
    fieldName: 'orderid',
    component: 'Input',
  },
  {
    label: '批次号',
    fieldName: 'sn',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    rules: 'required',
  },
  {
    label: '入库位置',
    fieldName: 'stocktype',
    component: 'Select',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '出库库位',
    fieldName: 'stocktypename',
    component: 'Select',
    formItemClass: 'col-span-2',
  },
  {
    label: '总价',
    fieldName: 'allprice',
    component: 'Input',
  },
  {
    label: '结算方式',
    fieldName: 'MET',
    component: 'Input',
  },
  {
    label: '税点',
    fieldName: 'SHUI',
    component: 'Input',
  },
  {
    label: '文件',
    fieldName: 'files',
    component: 'FileUpload',
    /**
     * 注意这里获取为数组 需要自行定义回显/提交
     * 文件上传还在demo阶段 可能有重大改动!
     */
    componentProps: {
      // accept: ['xlsx'], // 不支持type/*的写法 建议使用拓展名(不带.)
      // maxNumber: 1, // 最大上传文件数
      // resultField: 'url', // 上传成功后返回的字段名 默认url 可选['ossId', 'url', 'fileName']
    },
  },
  {
    label: '采购时间',
    fieldName: 'ctime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    rules: 'required',
  },
  {
    label: '是否采购入库',
    fieldName: 'ispri',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    defaultValue:0,
    rules: 'required',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
  {
    label: '备注1',
    fieldName: 'remark2',
    component: 'Input',
  },
  {
    label: '状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue: '1',
    rules: 'selectRequired',
  },
];
