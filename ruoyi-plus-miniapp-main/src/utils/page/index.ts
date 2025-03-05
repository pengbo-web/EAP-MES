/**
 * 这里这要是用于在组件上跳转 比如@click="xxx"
 * 在script里直接解构const {navigateTo} = uni; 本地没有问题
 * 但是打包后会报错
 * @param url 跳转页面的地址
 */
export function navigateTo(url: string) {
  uni.navigateTo({ url });
}

/**
 * 获取当前页面的page信息 比如 pages/index/index
 * 注意是不带最前面的/的
 * @returns
 */
export function getCurrentPageUrl() {
  const pages = getCurrentPages();
  return pages[pages.length - 1].route;
}

/**
 * 打开外链 不同平台不同方式
 * @param url 外部链接
 */
export function openExternalLink(url: string) {
  // #ifdef H5
  window.open(url);
  // #endif
  // #ifdef APP
  plus.runtime.openURL(url);
  // #endif
  // #ifdef MP
  uni.setClipboardData({
    data: url,
    success() {
      uni.showToast({ title: '复制链接成功', icon: 'success' });
    },
  });
  // #endif
}
