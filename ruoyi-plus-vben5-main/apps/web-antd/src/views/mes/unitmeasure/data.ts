import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import {
  addFullName,
  cloneDeep,
  getPopupContainer,
  listToTree,
} from '@vben/utils';
import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'unitsn',
    label: '单位编码',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '名称',
  },
  {
    component: 'Input',
    fieldName: 'grow',
    label: '进率',
  }
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible: false,
  },
  {
    title: '单位编码',
    field: 'unitsn',
  },
  {
    title: '上级单位',
    field: 'upname',
  },
  {
    title: '名称',
    field: 'title',
  },
  {
    title: '进率',
    field: 'grow',
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
    title: '备注',
    field: 'remark',
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
    label: '单位编码',
    fieldName: 'unitsn',
    component: 'Input',
  },
  {
    label: '名称',
    fieldName: 'title',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '上级单位',
    fieldName: 'remark',
    component: 'Select',
    componentProps: {
      getPopupContainer
    }
  },
  {
    label: '进率',
    fieldName: 'grow',
    component: 'Input',
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
    defaultValue:'1',
    rules: 'selectRequired',
  },
];
