import { useTmpiniaStore } from '@/tmui/tool/lib/tmpinia';

export function useTheme() {
  /**
   * 一堆的bug 不能用解构
   */
  const store = useTmpiniaStore();

  /**
   * 自动切换是否跟随系统主题
   */
  function autoDarkToggle() {
    const autoDark = store.tmuiConfig.autoDark;
    store.setTmAutoDark(!autoDark);
  }

  /**
   * 自动切换当前主题
   */
  function themeToggle() {
    const dark = store.tmStore.dark;
    store.setTmVuetifyDark(!dark);
  }

  function setAutoDark(autoDark: boolean) {
    store.setTmAutoDark(autoDark);
  }

  function setDark(dark: boolean) {
    store.setTmVuetifyDark(dark);
  }

  return {
    store,
    autoDarkToggle,
    themeToggle,
    setAutoDark,
    setDark,
  };
}
