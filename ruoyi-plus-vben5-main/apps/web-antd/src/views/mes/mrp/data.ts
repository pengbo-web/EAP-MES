import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'custname',
    label: '客户',
  },
  {
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    fieldName: 'subtime',
    label: '交货时间',
  },
  {
    component: 'Input',
    fieldName: 'custno',
    label: '客户订单号',
  },
  {
    component: 'Input',
    fieldName: 'stockname',
    label: '物料信息',
  },
  {
    component: 'RangePicker',
    fieldName: 'otime',
    label: '订单时间',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible:false
  },
  {
    title: '操作',
    field: 'tsta',
    slots:{default: 'tsta'}
  },
  {
    title: '税点',
    field: 'shui',
    visible:false
  },
  {
    title: '客户',
    field: 'custname',
  },
  {
    title: '交货时间',
    field: 'subtime',
  },
  {
    title: '客户订单号',
    field: 'custno',
  },
  {
    title: '付款周期',
    field: 'cycle',
  },
  {
    title: '发票管理',
    field: 'fp',
  },
  {
    title: '订单时间',
    field: 'otime',
  },
  {
    title: '其他文件',
    field: 'otfile',
  },
  {
    title: '文件',
    field: 'ctfile',
  },
  {
    title: '状态',
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
    visible:false
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
    label: '税点',
    fieldName: 'shui',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '客户',
    fieldName: 'custid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '交货时间',
    fieldName: 'subtime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    rules: 'required',
  },
  {
    label: '客户订单号',
    fieldName: 'custno',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '付款周期',
    fieldName: 'cycle',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '物料信息',
    fieldName: 'stids',
    component: 'Textarea',
    rules: 'required',
  },
  {
    label: '发票管理',
    fieldName: 'fp',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '订单时间',
    fieldName: 'otime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    rules: 'required',
  },
  {
    label: '是否mrp',
    fieldName: 'ismrp',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '其他文件',
    fieldName: 'otfile',
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
    rules: 'required',
  },
  {
    label: '文件',
    fieldName: 'ctfile',
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
  {
    label: '状态2',
    fieldName: 'tsta',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '打印数量',
    fieldName: 'pricount',
    component: 'Input',
    rules: 'required',
  },
];
