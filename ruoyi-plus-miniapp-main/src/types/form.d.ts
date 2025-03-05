/**
 * 用作定义表单规则  这样会有提示
 * 例如: const rules = reactive<FormRules<typeof XXX>>()
 * xxx就是ref/reactive对象  ref要用.value才能获取真正值的类型
 * 或者定义过类型 直接用类型 const rules = reactive<FormRules<YourType>>()
 */
export declare type FormRules<T> = {
  [key in keyof T]?: Tmui.tmFormRules[];
};
