import { useUserStore } from '@/store/modules/user';
import { intersection, isArray } from 'lodash-es';

export function usePermission() {
  const userStore = useUserStore();
  const adminPermissions = '*:*:*';

  function hasPermission(value?: string | string[], def = true): boolean {
    // 没有传参的话默认返回true
    if (!value) {
      return def;
    }

    const allCodeList = userStore.permissionList || [];

    // 管理员
    const isAdmin = allCodeList.filter((item) => item === adminPermissions).length;
    if (isAdmin > 0) {
      return true;
    }

    if (!isArray(value)) {
      const splits = ['||', '&&'];
      const splitName = splits.find((item) => value.includes(item));
      if (splitName) {
        const splitCodes = value.split(splitName);
        return splitName === splits[0]
          ? intersection(splitCodes, allCodeList).length > 0
          : intersection(splitCodes, allCodeList).length === splitCodes.length;
      }
      return allCodeList.includes(value);
    }
    return (intersection(value, allCodeList) as string[]).length > 0;
  }

  return { hasPermission };
}
