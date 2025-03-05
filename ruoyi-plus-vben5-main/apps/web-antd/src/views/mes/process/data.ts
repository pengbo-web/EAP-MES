import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import {
  getPopupContainer
} from '@vben/utils';
import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'sn',
    label: '编码',
  },
  {
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.PROTYPES 便于维护
      options: getDictOptions('protypes'),
    },
    fieldName: 'typeId',
    label: '类别',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '标题',
  },
  {
    component: 'Input',
    fieldName: 'userids',
    label: '用户',
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
    title: '编码',
    field: 'sn',
  },{
    title: '标题',
    field: 'title',
  },
  {
    title: '类别',
    field: 'typeId',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.PROTYPES 便于维护
        return renderDict(row.typeId, 'protypes');
      },
    },
  },
  {
    title: '标准工时(分钟)',
    field: 'refvalue',
  },
  {
    title: '关联用户',
    field: 'usernames',
  },
  {
    title: '计件',
    field: 'moneytype',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.PROTYPEOFM 便于维护
        return renderDict(row.moneytype, 'protypeofm');
      },
    },
  },
  {
    title: '单价',
    field: 'moneyprice',
  },
  {
    title: 'SAP',
    field: 'remark',
    type: 'html',
    showOverflow: true
  },
  {
    title: '是否激活',
    field: 'status',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.SYS_NORMAL_DISABLE 便于维护
        return renderDict(row.status, 'sys_normal_disable');
      },
    },
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
  },
  {
    label: '标题',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '类别',
    fieldName: 'typeId',
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.PROTYPES 便于维护
      options: getDictOptions('protypes'),
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'selectRequired',
  },
  {
    label: '标准工时',
    fieldName: 'refvalue',
    component: 'Input',
    renderComponentContent: () => ({
      suffix: () => '分钟',
    }),
    defaultValue: 0,
    rules: 'required',
  },
  {
    label: '计件',
    fieldName: 'moneytype',
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.PROTYPEOFM 便于维护
      options: getDictOptions('protypeofm'),
    },
    defaultValue: '0',
    rules: 'selectRequired',
  },
  {
    label: '单价',
    fieldName: 'moneyprice',
    component: 'Input',
    renderComponentContent: () => ({
      suffix: () => '¥',
    }),
    defaultValue: '0.00',
    help: '计时工艺请填写月薪资',
    rules: 'required',
  },
  {
    label: '关联用户',
    fieldName: 'userids',
    component: 'Select',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.PROTYPEOFM 便于维护
      getPopupContainer,
      mode: 'multiple',
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
  },
  
  {
    label: 'SAP',
    fieldName: 'remark',
    component: 'RichTextarea',
  },
  {
    label: '状态',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.SYS_NORMAL_DISABLE 便于维护
      options: getDictOptions('sys_normal_disable'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    defaultValue: '0',
    rules: 'selectRequired',
  },
];
