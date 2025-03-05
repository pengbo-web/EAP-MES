import { defineStore } from 'pinia';
import { store } from '@/store';

const tabBarList = [
  {
    activeColor: 'light-blue',
    openType: 'switchTab',
    text: '$tabbar.index',
    icon: 'tmicon-home',
    url: '/pages/index/index',
  },
  {
    activeColor: 'light-blue',
    openType: 'switchTab',
    text: '$tabbar.usercenter',
    icon: 'tmicon-account',
    url: '/pages/user/index',
  },
];

export interface TabBarState {
  activeIndex: number;
  tabBarList: any[];
}

export const useTabBarStore = defineStore('miniapp-tab-bar', {
  state: (): TabBarState => ({
    activeIndex: 0,
    tabBarList,
  }),
  actions: {
    setActiveIndex(index: number) {
      this.activeIndex = index;
    },
  },
});

export function useTabBarWithOut() {
  return useTabBarStore(store);
}
