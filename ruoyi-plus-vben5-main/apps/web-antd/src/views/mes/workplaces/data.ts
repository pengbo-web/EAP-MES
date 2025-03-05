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
    component: 'Input',
    fieldName: 'title',
    label: '标题',
  },
  {
    component: 'Input',
    fieldName: 'workshopId',
    label: '车间ID',
  },
  {
    component: 'Input',
    fieldName: 'equIds',
    label: '设备',
  },
  {
    component: 'Input',
    fieldName: 'teams',
    label: '班组',
  },
  {
    component: 'Input',
    fieldName: 'tongsIds',
    label: '夹具',
  },
  {
    component: 'Input',
    fieldName: 'processIds',
    label: '工序',
  },
  {
    component: 'Input',
    fieldName: 'counts',
    label: '数量',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.SYS_NORMAL_DISABLE 便于维护
      options: getDictOptions('sys_normal_disable'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'status',
    label: '角色状态',
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
  },
  {
    title: '标题',
    field: 'title',
  },
  {
    title: '车间',
    field: 'workshopName',
  },
  {
    title: '设备',
    field: 'equIdsarray',
  },
  {
    title: '班组',
    field: 'teamnames',
  },
  {
    title: '夹具',
    field: 'tongames',
  },
  {
    title: '工序',
    field: 'processIdsarray',
  },
  {
    title: '状态',
    field: 'status',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.SYS_NORMAL_DISABLE 便于维护
        return renderDict(row.status, 'sys_normal_disable');
      },
    },
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
    label: '车间',
    fieldName: 'workshopId',
    component: 'Select',
    componentProps: {
      getPopupContainer,
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'selectRequired',
  },
  {
    label: '设备',
    fieldName: 'equIds',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '设备',
    fieldName: 'equlists',
    component: 'Input',
    formItemClass: 'col-span-2',
    rules: 'required',
  },

  {
    label: '班组',
    fieldName: 'teamsids',
    formItemClass: 'col-span-2',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '班组',
    fieldName: 'teams',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    component: 'Input',
  },
  {
    label: '夹具',
    fieldName: 'tongsIds',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '工序',
    fieldName: 'processIds',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
  },
  {
    label: '工序',
    fieldName: 'processnames',
    component: 'Input',
    formItemClass: 'col-span-2',
    rules: 'required',
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
    defaultValue:'0',
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
