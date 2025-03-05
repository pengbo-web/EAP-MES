import { useTabBarWithOut } from '@/store/modules/tabbar';

/**
 * 跳转对应的tabbar 然后设置对应的index
 */
uni.addInterceptor('switchTab', {
  invoke(data) {
    const tabBarStore = useTabBarWithOut();
    // 这里获取的是完整的 /page/xxx
    const { url } = data;
    const currentIndex = tabBarStore.tabBarList.findIndex((item) => item.url === url);
    // 重复点击 不操作
    if (currentIndex === tabBarStore.activeIndex) {
      return;
    }
    if (currentIndex !== -1) {
      console.log(`switchTab拦截器: ${url} 设置index: ${currentIndex}`);
      tabBarStore.setActiveIndex(currentIndex);
    }
  },
});
