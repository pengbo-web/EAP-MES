import { defineStore } from 'pinia';
import { getRoutes } from '@/api/auth';
import { transformRoutes } from '@/utils/menu';
import type { ProjectMenu } from '@/types/menu';

export interface MenuState {
  menuList: ProjectMenu[];
}

export const useMenuStore = defineStore('miniapp-menu', {
  state: (): MenuState => ({
    menuList: [],
  }),
  actions: {
    async getMenuList() {
      const ret = await getRoutes();
      const transformList = transformRoutes(ret);
      this.menuList = transformList;
    },
  },
});
