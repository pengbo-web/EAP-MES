import type { FormSchemaGetter } from '#/adapter/form';
import type { VxeGridProps } from '#/adapter/vxe-table';
import { renderDict } from '#/utils/render';

export const querySchema: FormSchemaGetter = () => [
  {
    component: 'Input',
    fieldName: 'username',
    label: '用户',
  },
  {
    component: 'Input',
    fieldName: 'proname',
    label: '产品',
  },
];

export const columns: VxeGridProps['columns'] = [
  { type: 'checkbox', width: 60 },
  {
    title: '主键',
    field: 'id',
    visible:false,
  },
  {
    title: '报工人',
    field: 'username',
  },
  {
    title: '报工物料',
    field: 'productVo.title',
  },
  {
    title: '工序',
    field: 'processVo.title',
  },
  {
    title: '计算方式',
    field: 'processVo.moneytype',
    slots: {
          default: ({ row }) => {
            if(null !== row.processVo){
              return renderDict(row.processVo.moneytype, 'protypeofm');
            }else{
              return '无';
            }
            // 可选从DictEnum中获取 DictEnum.SYS_YES_NO 便于维护
            
          },
        },
  },
  {
    title: '工费',
    field: 'processVo.moneyprice',
  },
  {
    title: '已完成数量',
    field: 'lastcount',
  },
  {
    title: '报工数量',
    field: 'count',
  },
  {
    title: '不良品',
    field: 'defective',
  },
  {
    title: '状态',
    field: 'status',
    slots:{default:'status'}
  },
  {
    title: '备注',
    field: 'createTime',
  },
  {
    field: 'action',
    fixed: 'right',
    slots: { default: 'action' },
    title: '操作',
    width: 180,
    visible:false,
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
    label: '用户',
    fieldName: 'userId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '任务id',
    fieldName: 'taskId',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '已完成数量',
    fieldName: 'lastcount',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '报工数量',
    fieldName: 'count',
    component: 'Input',
    rules: 'required',
  },
  {
    label: '不良品',
    fieldName: 'defective',
    component: 'Input',
    rules: 'required',
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
    label: '产品',
    fieldName: 'proid',
    component: 'Input',
    rules: 'required',
  },
];
