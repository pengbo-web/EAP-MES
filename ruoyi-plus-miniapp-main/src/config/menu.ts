import { MenuProps } from '@/types/menu';
/**
 * 不存在图标  默认的图标
 */
export const defaultIcon = 'tmicon-new-releases';
/**
 * 这里要替换成TmUI的图标
 *
 */
export const menuReplacer: MenuProps[] = [
  {
    name: 'System',
    icon: 'tmicon-cog-fill',
  },
  {
    name: 'User',
    icon: 'tmicon-user-fill',
    props: { iconColor: 'orange', fontColor: 'orange' },
  },
  {
    name: 'Role',
    icon: 'tmicon-userplus-fill',
  },
  {
    name: 'Menu',
    icon: 'tn-icon-menu',
  },
  {
    name: 'Dept',
    icon: 'tn-icon-group-circle',
  },
  {
    name: 'Post',
    icon: 'tmicon-smile-fill',
    props: { iconColor: 'green', fontColor: 'green' },
  },
  {
    name: 'Dict',
    icon: 'tn-icon-book',
    props: { iconColor: 'orange', fontColor: 'orange' },
  },
  {
    name: 'Websocket',
    icon: 'tmicon-ios-cellular',
  },
  {
    name: 'Config',
    icon: 'tmicon-ios-cafe',
    props: { iconColor: 'green', fontColor: 'green' },
  },
  {
    name: 'Notice',
    icon: 'tmicon-ios-alarm',
    props: { iconColor: 'green', fontColor: 'green' },
  },
  {
    name: 'Log',
    icon: 'tmicon-ios-filing',
    props: { iconColor: 'green', fontColor: 'green' },
  },
  {
    name: 'Operlog',
    icon: 'tmicon-ios-filing',
    props: { iconColor: 'green', fontColor: 'green' },
  },
  {
    name: 'Logininfor',
    icon: 'tmicon-userplus-fill',
    props: { iconColor: 'green', fontColor: 'green' },
  },
  {
    name: 'Oss',
    icon: 'tmicon-folderopen-fill',
  },
  {
    name: 'Client',
    icon: 'tmicon-mobile-alt',
    props: { iconColor: 'orange', fontColor: 'orange' },
  },
  {
    name: 'Tenant',
    icon: 'tn-icon-company-fill',
  },
  {
    name: 'Tenant',
    icon: 'tn-icon-company-fill',
  },
  {
    name: 'TenantPackage',
    icon: 'tmicon-resource',
  },
  {
    name: 'Monitor',
    icon: 'tmicon-laptop',
  },
  {
    name: 'Online',
    icon: 'tn-icon-my-simple',
    props: { iconColor: 'green', fontColor: 'green' },
  },
  {
    name: 'Cache',
    icon: 'tn-icon-data',
    props: { iconColor: 'green', fontColor: 'green' },
  },
  {
    name: 'Admin',
    icon: 'tmicon-unlock-fill',
  },
  {
    name: 'Powerjob',
    icon: 'tmicon-lock-fill',
  },
  {
    name: 'Tool',
    icon: '',
  },
  {
    name: 'Build',
    icon: 'tmicon-plus-square-fill',
  },
  {
    name: 'Gen',
    icon: 'tmicon-minus-square',
  },
  {
    name: 'Https://gitee.com/dromara/RuoYi-Vue-Plus',
    icon: 'tmicon-lianjie',
  },
  {
    name: 'Link',
    icon: 'tmicon-lianjie',
  },
];

/**
 * 需要排除的菜单 根据name排除
 */
export const excludeMenuList = ['Tool', 'Gen', 'Build', 'Admin', 'Powerjob', 'Websocket'];
