/**
 * 组件要用的其他的
 */
export interface ComponentProps {
  iconColor: string;
  fontColor: string;
}

export interface MenuProps {
  name: string;
  icon: string;
  props?: ComponentProps;
}

export interface ProjectMenu {
  /**
   * 路径
   */
  path: string;
  /**
   * menu的name
   */
  name: string;
  /**
   * menu的图标
   */
  icon?: string;
  /**
   * menu的标题
   */
  title: string;
  /**
   * 是否隐藏
   */
  hidden: boolean;
  /**
   * 这里是真正的对应本地地址 system/user/index
   */
  component: string;
  /**
   * 子菜单
   */
  children?: ProjectMenu[];
  /**
   * 其他的属性 需要自行本地添加
   */
  props?: ComponentProps;
}
