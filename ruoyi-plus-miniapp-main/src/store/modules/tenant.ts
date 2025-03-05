import { defineStore } from 'pinia';
import { tenantList } from '@/api/auth';
import { TenantOption } from '@/api/auth/model';
import { store } from '@/store';

interface TenantState {
  /**
   * list
   */
  tenantList: TenantOption[];
  /**
   * 是否启用租户功能
   */
  tenantEnabled: boolean;
  /**
   * 是是否已经选中项目
   */
  checked: boolean;
  /**
   * 选中项目的label
   */
  checkedLabel: string;
  /**
   * 是否已经初始化过了
   */
  inited: boolean;
}

export const useTenantStore = defineStore('tenant', {
  state: (): TenantState => ({
    tenantList: [],
    tenantEnabled: true,
    checked: false,
    checkedLabel: '选择租户',
    inited: false,
  }),
  getters: {
    isChecked(state) {
      return state.checked;
    },
  },
  actions: {
    async initTenant() {
      if (this.inited) return;
      const { tenantEnabled, voList } = await tenantList();
      this.tenantEnabled = tenantEnabled;
      this.tenantList = voList;
      this.inited = true;
    },
    setCheckedLabel(label: string) {
      this.checkedLabel = label;
    },
    setChecked(checked: boolean) {
      this.checked = checked;
    },
  },
});

export function useTenantStoreWithOut() {
  return useTenantStore(store);
}
