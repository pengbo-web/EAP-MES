import type { Menu } from '@/api/auth/model';
import { PageEnum } from '@/enums/pageEnum';
import { language$t } from '@/tmui/tool/lib/language';
import { defaultIcon, menuReplacer, excludeMenuList } from '@/config/menu';
import { ProjectMenu } from '@/types/menu';

export function transformRoutes(routes: Menu[]): ProjectMenu[] {
  // 进行转换
  const menuList: ProjectMenu[] = [];
  routes.forEach((item) => {
    // 根目录菜单
    if (item.path === '/' && item.children && item.children.length === 1) {
      item = item.children[0];
    }
    const { path, name, meta, hidden, component } = item;
    // 图标替换
    let icon = '';
    let props;
    // 通过name匹配
    const temp = menuReplacer.find((item) => item.name === name);
    if (temp) {
      icon = temp.icon;
      if (temp.props) {
        props = temp.props;
      }
    } else {
      icon = defaultIcon;
    }
    // 其它组件参数
    const menu: ProjectMenu = {
      path,
      name,
      title: meta?.title || '未知',
      icon,
      hidden,
      component,
      props,
    };
    if (item.children && item.children.length) {
      menu.children = transformRoutes(item.children);
    }
    // 不添加的路由
    if (excludeMenuList.includes(name)) {
      return;
    }
    menuList.push(menu);
  });
  return menuList;
}

/**
 * 判断是否为可打开页面的菜单(不包括link类型的判断)
 * @param component menu的component属性
 */
export function isOpenableMenu(component: string) {
  // 这些是父组件  在app中用不到
  const rootComponents = ['Layout', 'ParentView'];
  return !rootComponents.includes(component);
}

/**
 * 判断是否为link类型的菜单
 * @param path menu的path属性
 */
export function isLinkMenu(path: string) {
  return path.startsWith('http');
}
/**
 * 根据menu的不同类型进行跳转
 * 本地页面 -> navigateTo
 * 外部链接 -> window.open || 微信 要做兼容性处理
 * 依旧是菜单 -> 跳转到菜单分发
 * @param menu
 */
export function menuDispatcher(menu: ProjectMenu): void {
  /**
   * 兼容性处理
   *
   * H5 window.open
   * APP 打开浏览器页面
   * 小程序 复制链接
   */
  if (isLinkMenu(menu.path)) {
    // #ifdef H5
    window.open(menu.path);
    // #endif
    // #ifdef APP
    plus.runtime.openURL(menu.path);
    // #endif
    // #ifdef MP
    uni.setClipboardData({
      data: menu.path,
      success() {
        uni.showToast({ title: '复制链接成功', icon: 'success' });
      },
    });
    // #endif
    return;
  }
  /**
   * 本地页面 直接跳转
   */
  if (isOpenableMenu(menu.component)) {
    // 跳转链接要加上pages
    const realUrl = '/pages/' + menu.component;
    uni.navigateTo({ url: realUrl }).catch((e) => {
      console.log(`跳转到[${realUrl}]失败 ${e.errMsg}`);
      uni.showToast({ title: language$t('message.jump.notfound'), icon: 'error' });
    });

    return;
  }
  /**
   * 多级菜单
   */
  if (menu.children && menu.children.length) {
    const data = JSON.stringify(menu);
    uni.navigateTo({ url: PageEnum.MENU_DISPATCHER_PAGE + '?data=' + data });
  }
}
