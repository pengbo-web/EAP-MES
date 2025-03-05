import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import {
  getPopupContainer
} from '@vben/utils';
import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';
export const querySchema: FormSchemaGetter = () => [
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible:false
  },{
    title: '标识',
    field: 'remark',
  },{
    title: '班组长',
    field: 'upnames',
  },
  {
    title: '组员',
    field: 'stname',
  },
  
  {
    title: '是否禁用',
    field: 'status',
    slots: {
          default: ({ row }) => {
            // 可选从DictEnum中获取 DictEnum.DIYISNO 便于维护
            return renderDict(row.status, 'diyisno');
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
    label: '标识',
    fieldName: 'remark',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '班组长',
    fieldName: 'upids',
    component: 'Select',
    componentProps: {
      getPopupContainer,
      mode: 'multiple',
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'selectRequired',
  },
  {
    label: '组员',
    fieldName: 'useids',
    component: 'Select',
    componentProps: {
      getPopupContainer,
      mode: 'multiple',
      optionFilterProp: 'label',
      optionLabelProp: 'label',
    },
    rules: 'selectRequired',
  },
  {
    label: '是否禁用',
    fieldName: 'status',
    component: 'RadioGroup',
    componentProps: {
      options: getDictOptions('diyisno'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
  },
  
];
