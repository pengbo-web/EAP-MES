import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';


export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'sn',
    label: '流转批次号',
  },
  {
    component: 'Input',
    fieldName: 'proname',
    label: '产品',
  },
  {
    component: 'Input',
    fieldName: 'username',
    label: '用户',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: 'id',
    field: 'id',
    visible: false,
  },
  {
    title: '流转批次号',
    field: 'sn',
  },
  {
    title: '产品',
    field: 'proname',
  },
  {
    title: '工作站',
    field: 'workplaceId',
  },
  {
    title: '用户',
    field: 'username',
  },
  {
    title: '所需数量',
    field: 'needcount',
  },
  {
    title: '生产数量',
    field: 'markcount',
  },
  {
    title: '完成工序',
    field: 'okprocess',
  },
  {
    title: '报废数量',
    field: 'scrapcount',
  },
  {
    title: '质检次数',
    field: 'isCheck',
  },
  {
    title: '流程id',
    field: 'currproid',
    visible: false,
  },
  {
    title: '当前流程',
    field: 'currprocess',
  },
  {
    title: '所有工序',
    field: 'allprocess',
  },
  {
    title: '返工数量',
    field: 'redoingcount',
  },
  {
    title: '返工id',
    field: 'redoingid',
  },
  {
    title: '是否加急',
    field: 'isTop',
  },
  {
    title: '主表id',
    field: 'workname',
  },
  {
    title: '类型',
    field: 'type',
    visible: false,
  },
  {
    title: '排序',
    field: 'sort',
    visible: false,
  },
  {
    title: '状态',
    field: 'status',
    visible: false,
  },
  {
    title: '备注',
    field: 'remark',
  },
  {
    title: '是否关键工序',
    field: 'mustbe',
  },
  {
    title: '是否够量',
    field: 'enough',
  },
  {
    title: '需出库数量',
    field: 'stocknum',
  },
  {
    title: '出库数量',
    field: 'outstocknum',
  },
  {
    title: '质检次数',
    field: 'checknum',
  },
  {
    title: '是否通过质检',
    field: 'ischecknew',
  },
  {
    title: '流转类型',
    field: 'worktype',
  },
  {
    title: '备注',
    field: 'remark2',
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

export const modalSchema: FormSchemaGetter = () => [
  {
    label: 'id',
    fieldName: 'id',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '流转批次号',
    fieldName: 'sn',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '产品',
    fieldName: 'proId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '工作站',
    fieldName: 'workplaceId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '用户',
    fieldName: 'userId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '所需数量',
    fieldName: 'needcount',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '生产数量',
    fieldName: 'markcount',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '完成工序',
    fieldName: 'okprocess',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '报废数量',
    fieldName: 'scrapcount',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '质检次数',
    fieldName: 'isCheck',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '流程id',
    fieldName: 'currproid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '当前流程',
    fieldName: 'currprocess',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '所有工序',
    fieldName: 'allprocess',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '返工数量',
    fieldName: 'redoingcount',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '返工id',
    fieldName: 'redoingid',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '是否加急',
    fieldName: 'isTop',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '主表id',
    fieldName: 'workId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '类型',
    fieldName: 'type',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
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
  {
    label: '是否关键工序',
    fieldName: 'mustbe',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '是否够量',
    fieldName: 'enough',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '需出库数量',
    fieldName: 'stocknum',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '出库数量',
    fieldName: 'outstocknum',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '质检次数',
    fieldName: 'checknum',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '是否通过质检',
    fieldName: 'ischecknew',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '流转类型',
    fieldName: 'worktype',
    component: 'Select',
    componentProps: {
    },
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark2',
    component: 'Input',
    rules: 'required',
  },
];
