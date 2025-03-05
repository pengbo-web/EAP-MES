import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';

import { getDictOptions } from '#/utils/dict';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'RadioGroup',
    componentProps: {
      options: getDictOptions('equmaintype'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'maintype',
    label: '主类别',
  },
  {
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.EQUCHECK 便于维护
      options: getDictOptions('equcheck'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    fieldName: 'types',
    label: '类别',
  },
  {
    component: 'Input',
    fieldName: 'title',
    label: '标题',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title:'打印',
    field:'prints',
    slots: {
      default: 'prints',
    }
  },
  {
    title:'打印',
    field:'qprint',
    slots: {
      default: 'qprint',
    }
  },
  {
    title: '主键',
    field: 'id',
    visible: false,
  },
  {
    title: '主类别',
    field: 'maintype',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.EQUCHECK 便于维护
        return renderDict(row.maintype, 'equmaintype');
      },
    },
  },
  {
    title: '类别',
    field: 'types',
    slots: {
      default: ({ row }) => {
        // 可选从DictEnum中获取 DictEnum.EQUCHECK 便于维护
        return renderDict(row.types, 'equcheck');
      },
    },
  },
  {
    title: '关联设备',
    field: 'equIdnames',
  },
  {
    title: '标题',
    field: 'title',
  },
  {
    title: '查看',
    field: 'showinfo',
    slots: {
      default: 'showinfo',
    }

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
    label: '主类别',
    fieldName: 'maintype',
    component: 'RadioGroup',
    componentProps: {
      options: getDictOptions('equmaintype'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    rules: 'selectRequired',
  },
  {
    label: '类别',
    fieldName: 'types',
    component: 'RadioGroup',
    componentProps: {
      // 可选从DictEnum中获取 DictEnum.EQUCHECK 便于维护
      options: getDictOptions('equcheck'),
      buttonStyle: 'solid',
      optionType: 'button',
    },
    rules: 'selectRequired',
  },
  {
    label: '关联设备',
    fieldName: 'equIds',
    component: 'Input',
    dependencies: {
      show: () => false,
      triggerFields: [''],
    },
    rules: 'required',
  },
  {
    label: '关联设备',
    fieldName: 'equIdsname',
    component: 'Input',
    formItemClass:'col-span-2',
    rules: 'required',
  },
  {
    label: '标题',
    fieldName: 'title',
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
    defaultValue:'1',
    rules: 'selectRequired',
  },
  {
    label: '备注',
    fieldName: 'remark',
    component: 'Textarea',
  },
];
